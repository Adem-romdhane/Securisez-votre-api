package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rating")
public class Rating {
    // TODO: Map columns in data table RATING with corresponding java fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    Integer ratingId;
    @Column(name = "moodys_rating")
    String moodysRating;
    @Column(name = "sand_prating")
    String sandPRating;
    @Column(name = "fitch_rating")
    String fitchRating;
    @Column(name = "order_number")
    Integer orderNumber;
}
