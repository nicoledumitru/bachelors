package com.fils.backend.services;

import com.fils.backend.domain.Product;
import com.fils.backend.domain.Review;
import com.fils.backend.domain.User;
import com.fils.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getProducts(){return productRepository.findAll();}
    public Product getProductById(Long id){return productRepository.findById(id).get();}

    public List<Product> getProductsByCategory(Long categoryId){return productRepository.findAllByTypeId(categoryId);}
    public void deleteProduct(Product product){
        productRepository.delete(product);
    }
    public List<Product> getProductsByName(String name){ return productRepository.findByNameContaining(name);}
    public void saveProduct(Product product){productRepository.save(product);}
    public List<Product> getAllByUser(User user){ return productRepository.findAllByUser(user);}
    public double computeRating(List<Review> reviews){
        int sum = 0;
        for(Review review : reviews){
            sum = sum+review.getRating();
        }
        double totalRating = (double)sum/reviews.size();
        return totalRating;
    }
    public List<Product> threeBestRanked(List<Product> productList){
        List<Product> bestRanked = new ArrayList<>();
//        List<Double> allRatings = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            for (int j = i + 1; j < productList.size(); j++) {
                Product temp;
                if (productList.get(j).getTotalRating() > productList.get(i).getTotalRating()) {
                    temp = productList.get(i);
                    productList.set(i,productList.get(j));
                    productList.set(j, temp);
                }
            }
        }
//        for(Product product: productList) {
//             allRatings.add(product.getTotalRating());
//        }
//        List<Product> orderedDesc =
//        Collections.sort(allRatings, Collections.reverseOrder());
//        Collections.sort(productList, (p1, p2) -> p1.getTotalRating() - p2.getTotalRating());
        bestRanked.add(productList.get(0));
        bestRanked.add(productList.get(1));
        bestRanked.add(productList.get(2));
        bestRanked.add(productList.get(3));
        return bestRanked;
    }
}
