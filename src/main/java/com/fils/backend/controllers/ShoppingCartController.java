package com.fils.backend.controllers;

import com.fils.backend.domain.User;
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
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//        User u = userService.getCurrentUser(auth);
//        return ResponseEntity.status(HttpStatus.OK).body(cartServices.listCartItems(u));
    }

    @PostMapping("/add")
    public ResponseEntity addProductToCart(@RequestParam Long pid, @RequestParam Integer qty,
                                   @RequestHeader("Authorization") String auth){

        try {
            String jwtToken = auth.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            Optional<User> userByUsername = userService.getUserByUsername(username);

            if (userByUsername.isPresent()) {
//                User u = userService.getCurrentUser(auth);
//                userByUsername.get().setPassword(null);
                int addedQty = cartServices.addProduct(pid, qty, userByUsername.get());

                return ResponseEntity.status(HttpStatus.OK).body(addedQty+" item(s) were added to your cart");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: MissMatch JWT TOKEN with User (NOT_FOUND)");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Couldn't parse current user jwt to get user");
        }
    }
}
