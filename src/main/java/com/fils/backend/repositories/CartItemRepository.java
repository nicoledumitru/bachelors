package com.fils.backend.repositories;

import com.fils.backend.domain.CartItem;
import com.fils.backend.domain.Product;
import com.fils.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    List<CartItem> findByUser(User user);
    CartItem getById(int id);
    CartItem findByUserAndProduct(User user, Product product);
    CartItem findByProduct(Product product);
    @Transactional
    void deleteAllByUser(User user);
    void deleteCartItemsByUser(User user);
}
