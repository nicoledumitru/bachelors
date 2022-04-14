package com.fils.backend.services;

import com.fils.backend.domain.Product;
import com.fils.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getProducts(){return productRepository.findAll();}
    public Product getProductById(Long id){return productRepository.getById(id);}
    public void deleteProduct(Product product){
        productRepository.delete(product);
    }
    public List<Product> getProductsByName(String name){ return productRepository.findByNameContaining(name);}
    public void saveProduct(Product product){productRepository.save(product);}
}
