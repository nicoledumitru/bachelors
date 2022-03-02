package com.fils.backend.controllers;

import com.fils.backend.domain.Product;
import com.fils.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("")
    public List<Product> getProducts(){
        return productService.getProducts();
    }

    @DeleteMapping("/deletebyid")
    public ResponseEntity deleteProductById(@RequestParam Long id) {
        Product product = productService.getProductById(id);
        productService.deleteProduct(product);
        return ResponseEntity.ok("DELETED: " + product);
    }
}
