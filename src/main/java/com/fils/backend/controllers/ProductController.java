package com.fils.backend.controllers;

import com.fils.backend.domain.ImageModel;
import com.fils.backend.domain.Product;
import com.fils.backend.domain.ProductType;
import com.fils.backend.domain.User;
import com.fils.backend.repositories.ProductTypeRepository;
import com.fils.backend.security.JwtUtil;
import com.fils.backend.services.ImageModelService;
import com.fils.backend.services.ProductService;
import com.fils.backend.services.ReviewService;
import com.fils.backend.services.UserService;
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
    ProductTypeRepository categoryRepository;

    @Autowired
    ReviewService reviewService;


    @PostMapping("")
    public ResponseEntity<Product> createProduct(@RequestBody Product product, @RequestHeader("Authorization") String auth) {
        try {
            String jwtToken = auth.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            Optional<User> userByUsername = userService.getUserByUsername(username);
            Set<Product> listOfExistedProducts = product.getType().getProducts();

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
//                listOfExistedProducts.add(p);
//                productService.saveProduct(new Product(product.getName(), product.getType(), product.getDescription(), product.getPrice()));
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
            product.setTotalRating(productService.computeRating(reviewService.getByProductId(id)));
            return new ResponseEntity<>(product,HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/product-category")
//    public ResponseEntity<List<ProductType>> getAllCategories(){
//        List<ProductType> getAllCategories = new ArrayList<>();
//        getAllCategories = categoryRepository.findAll();
//        return new ResponseEntity<>(categoryRepository.findAll(),HttpStatus.OK);
//    }

    @GetMapping("/sort")
    public ResponseEntity<List<Product>> sortProductsByType(@RequestParam Long categoryId){
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

//    @PostMapping("/upload")
//    public ResponseEntity uploadImage(@RequestParam("myFile") MultipartFile file) {
//        try{
//            imageModelService.uploadImage(file);
//            return ResponseEntity.status(HttpStatus.OK).body("Image uploaded");
//        } catch (Exception e) {
//            String message = "Could not upload the file: " + file.getOriginalFilename() + "!";
//            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
//        }
//    }
}
