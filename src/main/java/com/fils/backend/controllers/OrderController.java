package com.fils.backend.controllers;

import com.fils.backend.domain.CartItem;
import com.fils.backend.domain.Order;
import com.fils.backend.domain.Product;
import com.fils.backend.domain.User;
import com.fils.backend.security.JwtUtil;
import com.fils.backend.services.OrderService;
import com.fils.backend.services.ShoppingCartServices;
import com.fils.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @Autowired
    ShoppingCartServices cartService;

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

    @PostMapping("/new")
    public ResponseEntity placeNewOrder(@RequestHeader("Authorization") String auth){
        try {
            String jwtToken = auth.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            Optional<User> userByUsername = userService.getUserByUsername(username);

            List<CartItem> items = cartService.listCartItems(userByUsername.get());
            List<Product> productList = new ArrayList<>();
            for (CartItem item: items) {
                productList.add(item.getProduct());
            }

            double totalPrice=0;
            for(CartItem item : items){
                totalPrice = item.getProduct().getPrice() * item.getQuantity();
            }

            if(userByUsername.isPresent()){
                Order o = new Order();
                o.setUser(userByUsername.get());
                o.setProductsFromCart(productList);
                o.setTotalPrice(Double.parseDouble(df.format(totalPrice)));
                o.setOrderTrackingNumber(orderService.generateOrderTrackingNumber());
                orderService.saveOrder(o);
                return ResponseEntity.status(HttpStatus.OK).body("The order is placed, thank you");
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: MissMatch JWT TOKEN with User (NOT_FOUND)");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Couldn't parse current user jwt to get user");
        }
    }
}
