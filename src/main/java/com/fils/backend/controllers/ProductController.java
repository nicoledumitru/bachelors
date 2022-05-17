package com.fils.backend.controllers;

import com.fils.backend.domain.ImageModel;
import com.fils.backend.domain.Product;
import com.fils.backend.domain.ProductType;
import com.fils.backend.domain.User;
//import com.fils.backend.repositories.ProductTypeRepository;
import com.fils.backend.repositories.ProductTypeRepository;
import com.fils.backend.security.JwtUtil;
import com.fils.backend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    ProductTypeRepository categoryRepository;

    @Autowired
    private EmailTokenService emailVerificationTokenService;



    @PostMapping("")
    public ResponseEntity<Product> createProduct(@RequestBody Product product, @RequestHeader("Authorization") String auth) {
        try {
            String jwtToken = auth.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            Optional<User> userByUsername = userService.getUserByUsername(username);

            if(userByUsername.isPresent() && userByUsername.get().getRoles().contains("ROLE_ADMIN") && product!=null) {
                Product p = new Product();
                p.setName(product.getName());
                p.setType(product.getType());
                p.setDescription(product.getDescription());
                p.setPrice(product.getPrice());
                p.setStock(product.getStock());
                p.setImageUrl(product.getImageUrl());
                p.setUser(userByUsername.get());

                productService.saveProduct(p);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //cod pt cazul in care produsul nou are acelasi nume cu un alt produs al aceluiasi furnizor
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProductById(@PathVariable("id") Long id) {
        Product product = productService.getProductById(id);
        productService.deleteProduct(product);
        return ResponseEntity.ok("DELETED: " + product);
    }

    @GetMapping("/sort/{categoryId}")
    public ResponseEntity<List<Product>> sortProductsByType(@PathVariable Long categoryId){
//        List<Product> allProducts = productService.getProducts();
//        List<Product> sortedProducts = new ArrayList<>();
//        for (Product product: allProducts) {
//            if(product.getType().equals(type)){
//                sortedProducts.add(product);
//            }
//        }
//        if(!sortedProducts.isEmpty()) {
//            return new ResponseEntity<>(sortedProducts, HttpStatus.OK);
//        } else return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        List<Product> sortedProducts = productService.getProductsByCategory(categoryId);
        if(!sortedProducts.isEmpty()) {
            return new ResponseEntity<>(sortedProducts, HttpStatus.OK);
        } else return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    //Daca dau numele ca parametru imi face cautare in produse (bara de search) si le afiseaza, altfel le afiseaza pe toate
    @GetMapping("")
    public ResponseEntity<List<Product>> getProducts(@RequestParam(required = false) String name) {
        try {
            List<Product> products = new ArrayList<>();

            if (name == null)
                productService.getProducts().forEach(products::add);
            else
                productService.getProductsByName(name).forEach(products::add);

            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id){
        try{
            Product product = productService.getProductById(id);
//            product.setTotalRating(productService.computeRating(reviewService.getByProductId(id)));
            return new ResponseEntity<>(product,HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/product-category")
    public ResponseEntity<List<ProductType>> getAllCategories(){
        return new ResponseEntity<>(categoryRepository.findAll(),HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity updateProduct(@RequestBody Product product, @RequestHeader("Authorization") String auth){
        try {
            String jwtToken = auth.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            Optional<User> userByUsername = userService.getUserByUsername(username);

            if (userByUsername.isPresent()) {
                Product dbProduct = productService.getProductById(product.getId());
                if (dbProduct!=null) {
                    if (dbProduct.getUser().getId().equals(userByUsername.get().getId())) {
                        // USER CAN ONLY EDIT PRICE and STOCK
                        dbProduct.setPrice(product.getPrice());
                        dbProduct.setStock(product.getStock());
                        productService.saveProduct(dbProduct);
                        //PRIMESTE MAIL CINE IL ARE IN WISHLIST
                        emailVerificationTokenService.sendNewsletter(userByUsername.get(), product);
                        return ResponseEntity.ok().body("Product updated successfully");
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("ERROR: MissMatch Product-UserId with UserId (NOT_FOUND)");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Product Couldn't be found !");
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("ERROR: MissMatch JWT TOKEN with User (NOT_FOUND)");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("ERROR: EmailService failed !!");
        }
    }

//    @GetMapping("/recommendations")
//    public ResponseEntity<List<Product>> getProductRecommendations(@RequestHeader("Authorization") String auth){
//        try {
//            String jwtToken = auth.substring(7);
//            String username = jwtUtil.extractUsername(jwtToken);
//            Optional<User> userByUsername = userService.getUserByUsername(username);
//
//            if (userByUsername.isPresent() && userByUsername.get().getRoles().contains("ROLE_ADMIN")) {
//
//            }
//        }catch (Exception e){
//
//        }
//    }

}
