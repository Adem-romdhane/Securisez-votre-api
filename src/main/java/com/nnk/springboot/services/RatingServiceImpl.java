package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
                    r.setMoodysRating(rating.getMoodysRating());
                    r.setSandPRating(rating.getSandPRating());
                    r.setFitchRating(rating.getFitchRating());
                    r.setOrderNumber(rating.getOrderNumber());
                    return ratingRepository.save(r);
                }).orElseThrow(() -> new RuntimeException("Rating not founded"));
    }

    @Override
    public String deleteById(Integer id) {
        ratingRepository.deleteById(id);
        return "rating deleted";
    }

    @Override
    public Rating findById(Integer id) {
        Optional<Rating> optionalRating = ratingRepository.findById(id);
        Rating rating = null;
        if (optionalRating.isPresent()) {
            rating = optionalRating.get();
        }else {
            throw new RuntimeException("rating not founded :" + id);
        }
        return rating;
    }
}
