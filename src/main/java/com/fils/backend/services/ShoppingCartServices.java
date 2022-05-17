package com.fils.backend.services;

import com.fils.backend.domain.CartItem;
import com.fils.backend.domain.Product;
import com.fils.backend.domain.User;
import com.fils.backend.repositories.CartItemRepository;
import com.fils.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServices {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<CartItem> listCartItems(User user){
        return cartItemRepository.findByUser(user);
    }

    public CartItem saveItem(CartItem cartItem){ return cartItemRepository.save(cartItem);}

//    public CartItem getItemByID(int id){
//        return cartItemRepository.getById(id);
//    }
//
//    public void removeCartItem(CartItem cartItem){
//        cartItemRepository.delete(cartItem);
//    }

}
