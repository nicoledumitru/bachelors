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
public class Product{

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

    double totalRating;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public Product(Long id, String name, ProductType type, String description, double price, String imageUrl, int stock, double totalRating, User user) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.totalRating = totalRating;
        this.user = user;
    }
}
