package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.CreateReviewRequest;
import com.bugnbass.backend.dto.ReviewResponseDto;
import com.bugnbass.backend.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing product reviews.
 * Provides endpoints for retrieving reviews by product and creating new reviews.
 */
@RestController
@RequestMapping("/bugnbass/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Retrieves all reviews for a specific product.
     *
     * @param productId the product identifier
     * @return a list of reviews for the product
     */
    @GetMapping("/product/{productId}")
    public List<ReviewResponseDto> getByProduct(
            @PathVariable("productId") Long productId
    ) {
        return reviewService.getReviewsByProduct(productId);
    }

    /**
     * Creates a new review for a product.
     * Only accessible to authenticated users with the USER role.
     *
     * @param req the review creation request
     * @return the created review
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ReviewResponseDto create(
            @Valid @RequestBody CreateReviewRequest req
    ) {
        return reviewService.createReview(req);
    }
}
