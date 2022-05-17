package com.fils.backend.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name="reviews")
@Entity
@Data
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="rating")
    private int rating;

    private String text;

//    @Column(name = "local_date_time", columnDefinition = "TIMESTAMP")
//    private LocalDateTime localDateTime;

    public Review(int rating){
        this.rating = rating;
    }
}
