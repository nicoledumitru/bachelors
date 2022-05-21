package com.fils.backend.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

@Table(name="orders")
@Entity
@Data
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_items")
    List<Product> productsFromCart;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

//    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Column(name = "price")
    private double totalPrice;

    @Column(name="order_tracking_number")
    private String orderTrackingNumber;

    @Column(name = "local_date_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime localDateTime;
}
