package com.fils.backend.services;

import com.fils.backend.domain.Review;
import com.fils.backend.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    public List<Review> getReviews(){return reviewRepository.findAll();}
    public Optional<Review> getById(Long id){return reviewRepository.findById(id);}
    public List<Review> getByUserId(Long id){return reviewRepository.findByUserId(id);}
    public void saveReview(Review review){ reviewRepository.save(review);}
    public List<Review> getByProductId(Long productID){ return reviewRepository.findByProductId(productID);}
}
