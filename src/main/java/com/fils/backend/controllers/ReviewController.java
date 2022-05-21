package com.fils.backend.controllers;

import com.fils.backend.domain.*;
import com.fils.backend.security.JwtUtil;
import com.fils.backend.services.OrderService;
import com.fils.backend.services.ProductService;
import com.fils.backend.services.ReviewService;
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
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    JwtUtil jwtUtil;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @PostMapping("/add")
    public ResponseEntity addReview(@RequestBody Review review, @RequestHeader("Authorization") String auth){
        try{
            String jwtToken = auth.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            Optional<User> userByUsername = userService.getUserByUsername(username);
            List<Order> ordersByUser = orderService.getOrdersByUser(userByUsername.get());
            List<Product> productList = new ArrayList<>();

            Product productById = productService.getProductById(review.getProduct().getId());
            for(Order o: ordersByUser) {
                for (Product p: o.getProductsFromCart()) {
                    productList.add(p);
                }
            }

            //daca acest user are o comanda anterioara care include produsul la care vrea sa faca review.
            System.out.println(productList);
            if(userByUsername.isPresent()){
                for(int i=0;i<=productList.size();i++){
                    if(productList.get(i).getId()==review.getProduct().getId()){
                        review.setUser(userByUsername.get());
                        reviewService.saveReview(review);
                        break;
                    }
                }
                double totalRatingForProduct = productService.computeRating(reviewService.getByProductId(review.getProduct().getId()));
                productById.setTotalRating(Double.parseDouble(df.format(totalRatingForProduct)));
                productService.saveProduct(productById);
                return ResponseEntity.ok("Review successfully added");
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You didn't order this product to leave a review");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Couldn't parse current user jwt to get user");
        }
    }

    @GetMapping("")
    public ResponseEntity getReviews(@RequestBody Test pid){
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getByProductId(pid.pid));
    }

}
