package com.fils.backend.services;

import com.fils.backend.domain.CartItem;
import com.fils.backend.domain.Order;
import com.fils.backend.domain.User;
import com.fils.backend.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public void saveOrder(Order order){orderRepository.save(order);}
    public List<Order> getOrdersByUser(User user){return orderRepository.findAllByUser(user);}
//    public Order getOrderByUserAndCartItemList(User user, List<CartItem> list){ return orderRepository.findOrderByUserAndCartItemList(user, list);}

    public String generateOrderTrackingNumber() {

        // generate a random UUID number (UUID version-4)
        return UUID.randomUUID().toString();
    }
}
