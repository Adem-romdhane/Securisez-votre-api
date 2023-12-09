package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements GenericService<Rating, Integer> {

    private final RatingRepository ratingRepository;

    @Override
    public Rating add(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getAll() {
        return ratingRepository.findAll();
    }

    @Override
    public Rating update(Integer id, Rating rating) {
        return ratingRepository.findById(id)
                .map(r -> {
                    r.setMoodysRating(r.getMoodysRating());
                    r.setSandPRating(r.getSandPRating());
                    r.setFitchRating(r.getFitchRating());
                    r.setOrderNumber(r.getOrderNumber());
                    return ratingRepository.save(r);
                }).orElseThrow(()-> new RuntimeException("Rating not founded"));
    }

    @Override
    public String deleteById(Integer id) {
        ratingRepository.deleteById(id);
        return "rating deleted";
    }
}
