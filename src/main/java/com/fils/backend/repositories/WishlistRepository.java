package com.fils.backend.repositories;

import com.fils.backend.domain.Product;
import com.fils.backend.domain.User;
import com.fils.backend.domain.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistItem, Integer> {
    List<WishlistItem> findByUser(User user);
    WishlistItem findByUserAndProduct(User user, Product product);
    void deleteByIdAndUser(int id, User user);
}
