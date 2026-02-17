package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.CreateReviewRequest;
import com.bugnbass.backend.dto.ReviewResponseDto;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.Review;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.repository.OrderRepository;
import com.bugnbass.backend.repository.ProductRepository;
import com.bugnbass.backend.repository.ReviewRepository;
import com.bugnbass.backend.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling review-related business logic.
 * Provides functionality for retrieving reviews and creating new reviews
 * with validation based on user orders.
 */
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    /**
     * Retrieves all reviews for a given product.
     *
     * @param productId the product identifier
     * @return a list of ReviewResponseDto objects
     */
    public List<ReviewResponseDto> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProductId(productId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Creates a new review for a product.
     * A review can only be created if the user has an order containing the product
     * with a delivered or returned status, and if the user has not already reviewed the product.
     *
     * @param req the review creation request
     * @return the created review as a DTO
     * @throws RuntimeException if the user is not allowed to review the product
     */
    public ReviewResponseDto createReview(CreateReviewRequest req) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        Product product = productRepository.findById(req.productId())
                .orElseThrow();

        boolean hasDeliveredOrderWithProduct =
                orderRepository.existsByUserAndOrderStatusInAndOrderItemsProductId(
                        user,
                        List.of(OrderStatus.DELIVERED, OrderStatus.RETURNED),
                        product.getId()
                );

        if (!hasDeliveredOrderWithProduct) {
            throw new RuntimeException("You can only review products from delivered orders");
        }

        if (reviewRepository.existsByProductIdAndUserId(product.getId(), user.getId())) {
            throw new RuntimeException("User already reviewed this product");
        }

        Review review = Review.builder()
                .product(product)
                .user(user)
                .rating(req.rating())
                .comment(req.comment())
                .build();

        reviewRepository.save(review);

        return toDto(review);
    }

    /**
     * Maps a Review entity to a ReviewResponseDto.
     *
     * @param review the review entity
     * @return the mapped DTO
     */
    private ReviewResponseDto toDto(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getUser().getFirstname(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}
