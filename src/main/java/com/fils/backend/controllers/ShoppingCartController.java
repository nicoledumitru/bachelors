package com.fils.backend.controllers;

import com.fils.backend.domain.CartItem;
import com.fils.backend.domain.Product;
import com.fils.backend.domain.Test;
import com.fils.backend.domain.User;
import com.fils.backend.repositories.CartItemRepository;
import com.fils.backend.repositories.ProductRepository;
import com.fils.backend.security.JwtUtil;
import com.fils.backend.services.ShoppingCartServices;
import com.fils.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cart")
@Component
public class ShoppingCartController {
    @Autowired
    private ShoppingCartServices cartServices;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("")
    public ResponseEntity showShoppingCart(@RequestHeader("Authorization") String auth){
        //try {
            String jwtToken = auth.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            Optional<User> userByUsername = userService.getUserByUsername(username);
            if(userByUsername.isPresent()){
//                userByUsername.get().setPassword(null);
                return ResponseEntity.status(HttpStatus.OK).body(cartServices.listCartItems(userByUsername.get()));
            } else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You should log in first");
            }
    }

    @PostMapping("/add")
    public ResponseEntity addProductToCart(@RequestBody Test pid,
                                   @RequestHeader("Authorization") String auth){
//        , @RequestParam Integer qty
        int addedQty;
        int quantity= 1;
        try {
            String jwtToken = auth.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            Optional<User> userByUsername = userService.getUserByUsername(username);

            if (userByUsername.isPresent()) {
//                int addedQty =
//                cartServices.addProduct(pid, userByUsername.get());
//                cartServices.addProduct(pid);

//                addedQty+

                Product product = productRepository.findById(pid.pid).get();
                CartItem cartItem = cartItemRepository.findByUserAndProduct(userByUsername.get(),product);
                if(product.getStock()>0) {
                    if (cartItem != null) {
                        addedQty = cartItem.getQuantity() + quantity;
                        cartItem.setQuantity(addedQty);
                    } else {
                        cartItem = new CartItem();
                        cartItem.setQuantity(quantity);
                        cartItem.setUser(userByUsername.get());
                        cartItem.setProduct(product);
                    }
                }
                else{ return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("The product is out of stock");}
                product.setStock(product.getStock()-1);
                cartItemRepository.save(cartItem);
                productRepository.save(product);
                return ResponseEntity.status(HttpStatus.OK).body("Item added to your cart");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: MissMatch JWT TOKEN with User (NOT_FOUND)");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Couldn't parse current user jwt to get user");
        }
    }
}
