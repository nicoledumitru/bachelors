package com.fils.backend.services;

import com.fils.backend.domain.Product;
import com.fils.backend.domain.User;
import com.fils.backend.domain.WishlistItem;
import com.fils.backend.repositories.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WishlistService {

    @Autowired
    WishlistRepository wishlistRepository;

    public WishlistItem getWLIByUserAndProduct(User user, Product product){
        return  wishlistRepository.findByUserAndProduct(user, product);
    }

    public List<WishlistItem> getWishlistItems(User user){
        return wishlistRepository.findByUser(user);
    }

    public void addProductToWishlist(Product product, User user) {
        WishlistItem wli= new WishlistItem();
            wli.setProduct(product);
            wli.setUser(user);

        wishlistRepository.save(wli);
    }

    public void deleteFromWishlist(Product product, User user){
        WishlistItem wli = wishlistRepository.findByUserAndProduct(user,product);
        System.out.println(wli);
        wishlistRepository.delete(wli);
    }
}
