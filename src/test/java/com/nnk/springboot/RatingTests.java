package com.nnk.springboot;


import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class RatingTests {

    @Autowired
    private RatingRepository ratingRepository;

    @Test
    public void ratingTest() {
        Rating rating = Rating.builder()
                .moodysRating("Moodys Rating")
                .fitchRating("Fitch Rating")
                .sandPRating("Sand PRating")
                .orderNumber(10)
                .build();


        // Save
        rating = ratingRepository.save(rating);
        assertNotNull(rating.getRatingId());
        assertEquals(10, (int) rating.getOrderNumber());

        // Update
        rating.setOrderNumber(20);
        rating = ratingRepository.save(rating);
        assertEquals(rating.getOrderNumber(), 20, 20);

        // Find
        List<Rating> listResult = ratingRepository.findAll();
        assertFalse(listResult.isEmpty());

        // Delete
        Integer id = rating.getRatingId();
        ratingRepository.delete(rating);
        Optional<Rating> ratingList = ratingRepository.findById(id);
        assertFalse(ratingList.isPresent());
    }
}
