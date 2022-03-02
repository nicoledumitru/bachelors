package com.fils.backend.repositories;

import com.fils.backend.domain.CartItem;
import com.fils.backend.domain.Product;
import com.fils.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    List<CartItem> findByUser(User user);
    CartItem getById(int id);
//    void deleteByUserAndProduct(int userId, int cartItemId);
    CartItem findByUserAndProduct(User user, Product product);
}
