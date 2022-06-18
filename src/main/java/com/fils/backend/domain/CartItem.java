package com.fils.backend.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name= "cart_items")
@Data
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private int quantity;

    public CartItem(int id, Product product, User user, int quantity) {
        this.id = id;
        this.product = product;
        this.user = user;
        this.quantity = quantity;
    }
}
