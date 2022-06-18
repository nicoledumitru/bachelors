package com.fils.backend;

import com.fils.backend.domain.CartItem;
import com.fils.backend.domain.Product;
import com.fils.backend.domain.ProductType;
import com.fils.backend.domain.User;
import com.fils.backend.repositories.ProductRepository;
import com.fils.backend.services.ShoppingCartServices;
import com.fils.backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingCartServices cartServices;

    @Mock
    private ProductRepository productRepository;

    @Test
    void contextLoads() {
    }

    private User user;
    private CartItem cartItem;
    private Product product;
    private com.fils.backend.domain.Test productId;
    @BeforeEach
    void setUp(){
        user = new User("nicoledumitru", "nicole.dumitru30@gmail.com", "nicole");
        productId = new com.fils.backend.domain.Test(Long.valueOf(7));
    }
    @Test
    void login(){
        Optional<User> savedUser = userService.getUserByUsername(user.getUsername());
        assertThat(savedUser).isNotNull();
    }
    
//    @Test
//    void addToCart(){
//        cartServices.addToCart(productId,user);
//        assertEquals(1, cartServices.listCartItems(user).size());
//    }

}
