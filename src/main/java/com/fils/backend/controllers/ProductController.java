package com.fils.backend.controllers;

import com.fils.backend.domain.Product;
import com.fils.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            productService.saveProduct(new Product(product.getName(), product.getType(), product.getDescription(), product.getPrice()));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
}
