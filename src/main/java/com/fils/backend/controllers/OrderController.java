package com.fils.backend.controllers;

import com.fils.backend.domain.CartItem;
import com.fils.backend.domain.Order;
import com.fils.backend.domain.Product;
import com.fils.backend.domain.User;
import com.fils.backend.repositories.OrderRepository;
import com.fils.backend.security.JwtUtil;
import com.fils.backend.services.EmailTokenService;
import com.fils.backend.services.OrderService;
import com.fils.backend.services.ShoppingCartServices;
import com.fils.backend.services.UserService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @Autowired
    ShoppingCartServices cartService;

    @Autowired
    EmailTokenService emailService;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @GetMapping("")
    public ResponseEntity getAllOrders(@RequestHeader("Authorization") String auth){
        String jwtToken = auth.substring(7);
        String username = jwtUtil.extractUsername(jwtToken);
        Optional<User> userByUsername = userService.getUserByUsername(username);
        if(userByUsername.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrdersByUser(userByUsername.get()));
        } else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You should log in first");
        }
    }

    @GetMapping("/buyers")
    public ResponseEntity getNumberBuyers(@RequestHeader("Authorization") String auth) {
        String jwtToken = auth.substring(7);
        String username = jwtUtil.extractUsername(jwtToken);
        Optional<User> userByUsername = userService.getUserByUsername(username);

        //get entire list of orders
        List<Order> allOrders = orderRepository.findAll();

        //create product list with all products ever bought
        List<Product> allProductsFromAllOrders = new ArrayList<>();

        int sum=0; //number of all customers that ordered from this seller

        if(userByUsername.isPresent() && userByUsername.get().getRoles().contains("ROLE_ADMIN")){
            for (Order o : allOrders){
                for(Product p : o.getProductsFromCart()){
                    allProductsFromAllOrders.add(p);
                }
            }
            for(Product product : allProductsFromAllOrders){
                if(product.getUser() == userByUsername.get()){
                    sum ++;
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(sum);
        } else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("ADMIN account is required");
        }
    }

    @PostMapping("/new")
    public ResponseEntity placeNewOrder(@RequestHeader("Authorization") String auth){
        try {
            String jwtToken = auth.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            Optional<User> userByUsername = userService.getUserByUsername(username);

            //get customer's shopping cart
            List<CartItem> items = cartService.listCartItems(userByUsername.get());

            //create productLst which contains all products from shopping cart
            List<Product> productList = new ArrayList<>();
            for (CartItem item: items) {
                productList.add(item.getProduct());
            }

            double totalPrice=0;
            for(CartItem item : items){
                //compute total price for order
                totalPrice += item.getProduct().getPrice() * item.getQuantity();
            }

            if(userByUsername.isPresent()){
                Order o = new Order();
                o.setUser(userByUsername.get());
                o.setProductsFromCart(productList);
                o.setTotalPrice(Double.parseDouble(df.format(totalPrice)));
                o.setOrderTrackingNumber(orderService.generateOrderTrackingNumber());
                o.setLocalDateTime(LocalDateTime.now());
                orderService.saveOrder(o);
                emailService.sendOrderConfirmation(userByUsername.get(), o);
                cartService.removeCartItemsByUser(userByUsername.get());
                return ResponseEntity.status(HttpStatus.OK).body("The order is placed, thank you");
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: MissMatch JWT TOKEN with User (NOT_FOUND)");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Couldn't parse current user jwt to get user");
        }
    }
}
