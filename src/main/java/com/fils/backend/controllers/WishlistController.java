package com.fils.backend.controllers;

import com.fils.backend.domain.*;
import com.fils.backend.repositories.ProductRepository;
import com.fils.backend.repositories.WishlistRepository;
import com.fils.backend.security.JwtUtil;
import com.fils.backend.services.UserService;
import com.fils.backend.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/wishlist")
@Component
public class WishlistController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @Autowired
    WishlistService wishlistService;

    @Autowired
    ProductRepository productRepository;

    @GetMapping("")
    public ResponseEntity getWishlist(@RequestHeader("Authorization") String auth) {
        String jwtToken = auth.substring(7);
        String username = jwtUtil.extractUsername(jwtToken);
        Optional<User> userByUsername = userService.getUserByUsername(username);
        if (userByUsername.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(wishlistService.getWishlistItems(userByUsername.get()));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You should log in first");
        }
    }

    @PostMapping("/add")
    public ResponseEntity addProductToWishlist(@RequestBody Test pid,
                                           @RequestHeader("Authorization") String auth){
        try {
            String jwtToken = auth.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            Optional<User> userByUsername = userService.getUserByUsername(username);

            if (userByUsername.isPresent()) {

                Product product = productRepository.findById(pid.pid).get();
                WishlistItem wli = wishlistService.getWLIByUserAndProduct(userByUsername.get(), product);

                if(wli!=null){
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Product already in wishlist");
                }
                else {
                    wishlistService.addProductToWishlist(product, userByUsername.get());
                    return ResponseEntity.status(HttpStatus.OK).body("Item added to your wishlist");
                }


            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: MissMatch JWT TOKEN with User (NOT_FOUND)");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Couldn't parse current user jwt to get user");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteFromWishlistById(@PathVariable("id") Long productId,
                                                 @RequestHeader("Authorization") String auth){
        try {
            String jwtToken = auth.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            Optional<User> userByUsername = userService.getUserByUsername(username);

            if (userByUsername.isPresent()) {
                Product product = productRepository.findById(productId).get();
                wishlistService.deleteFromWishlist(product, userByUsername.get());
                return ResponseEntity.status(HttpStatus.OK).body("Item removed from wishlist");

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: MissMatch JWT TOKEN with User (NOT_FOUND)");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Couldn't parse current user jwt to get user");
        }
    }
}
