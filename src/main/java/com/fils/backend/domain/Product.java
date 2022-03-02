package com.fils.backend.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    @Column
    private ProductType type;

    private String description;

    @Column(name="price")
    private double price;

    @Column(name="imageUrl")
    private String imageURL;

//  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//  @JoinColumn(name="id")
//  private OrderLineItem orderLineItem;

    public Product(String name, ProductType type, String description, double price) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.price = price;
    }
}
