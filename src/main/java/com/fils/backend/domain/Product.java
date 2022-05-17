package com.fils.backend.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.*;

@Table(name="products")
@Entity
@Data
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    @NotNull(message = "Product name is required.")
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ProductType type;

    private String description;

    @Column(name="price")
    private double price;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="stock")
    private int stock;

    private double totalRating;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

//  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//  @JoinColumn(name="id")
//  private OrderLineItem orderLineItem;

//    public Product(String name, String description, double price, String imageUrl, ProductType type, int stock) {
//        this.name = name;
//        this.type = type;
//        this.description = description;
//        this.price = price;
//        this.stock = stock;
//        this.imageUrl = imageUrl;
//    }
}
