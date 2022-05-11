package com.fils.backend.services;

import com.fils.backend.domain.Product;
import com.fils.backend.domain.Review;
import com.fils.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getProducts(){return productRepository.findAll();}
    public Product getProductById(Long id){return productRepository.findById(id).get();}

    public List<Product> getProductsByCategory(Long categoryId){return productRepository.findByTypeId(categoryId);}
    public void deleteProduct(Product product){
        productRepository.delete(product);
    }
    public List<Product> getProductsByName(String name){ return productRepository.findByNameContaining(name);}
    public void saveProduct(Product product){productRepository.save(product);}
    public double computeRating(List<Review> reviews){
        int sum = 0;
        for(Review review : reviews){
            sum = sum+review.getRating();
        }
        double totalRating = sum/reviews.size();
        return totalRating;
    }
}
