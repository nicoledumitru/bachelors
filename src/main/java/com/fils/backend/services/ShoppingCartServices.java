package com.fils.backend.services;

import com.fils.backend.domain.CartItem;
import com.fils.backend.domain.Product;
import com.fils.backend.domain.Test;
import com.fils.backend.domain.User;
import com.fils.backend.repositories.CartItemRepository;
import com.fils.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
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

    public void removeCartItemsByUser(User user){
        cartItemRepository.deleteAllByUser(user);
    }
    public void addToCart(Test id, User user){
        int addedQty;
        int quantity= 1;
        Product product = productRepository.findById(id.pid).get();
        CartItem cartItem = cartItemRepository.findByUserAndProduct(user,product);
        if(product.getStock()>0) {
            if (cartItem != null) {
                addedQty = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(addedQty);
            } else {
                cartItem = new CartItem();
                cartItem.setQuantity(quantity);
                cartItem.setUser(user);
                cartItem.setProduct(product);
            }
        }
        else{ return;}
        product.setStock(product.getStock()-1);
        cartItemRepository.save(cartItem);
        productRepository.save(product);
    }
}
