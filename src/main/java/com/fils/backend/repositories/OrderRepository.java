package com.fils.backend.repositories;

import com.fils.backend.domain.CartItem;
import com.fils.backend.domain.Order;
import com.fils.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);
//    Order findOrderByUserAndCartItemList(User user, List<CartItem> list);
}
