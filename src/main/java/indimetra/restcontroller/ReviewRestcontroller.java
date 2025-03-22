package indimetra.restcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import indimetra.modelo.entity.Review;
import jakarta.validation.Valid;

public class ReviewRestcontroller {


    //HAY QUE VER EL TEMA DEL RATING
    // @PostMapping
    // public ResponseEntity<Review> create(@RequestBody @Valid ReviewRequestDto reviewDto) {
    //     Review review = modelMapper.map(reviewDto, Review.class);

    //     Review newReview = reviewService.create(review);

    //     reviewService.actualizarRatingCortometraje(review.getCortometraje().getId());

    //     return ResponseEntity.status(201).body(newReview);
    // }

}
