package com.bugnbass.backend.repository;

import com.bugnbass.backend.model.Review;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for performing CRUD operations and custom queries on Review entities.
 */
public interface ReviewRepository extends JpaRepository<Review, String> {

    /**
     * Retrieves all reviews associated with a specific product.
     *
     * @param productId the product identifier
     * @return a list of reviews for the given product
     */
    List<Review> findByProductId(Long productId);

    /**
     * Checks whether a review exists for a given product and user.
     *
     * @param productId the product identifier
     * @param userId the user identifier
     * @return true if a review exists, otherwise false
     */
    boolean existsByProductIdAndUserId(Long productId, UUID userId);

    /**
     * Calculates the average rating for a specific product.
     *
     * @param productId the product identifier
     * @return the average rating or null if no reviews exist
     */
    @Query(
            """
                SELECT AVG(r.rating)
                FROM Review r
                WHERE r.product.id = :productId
            """)
    Double getAverageRating(Long productId);

    /**
     * Counts the total number of reviews for a specific product.
     *
     * @param productId the product identifier
     * @return the number of reviews
     */
    @Query(
            """
              select count(r)
              from Review r
              where r.product.id = :productId
            """)
    long getReviewCount(Long productId);

    /**
     * Retrieves rating statistics (average rating and review count) for multiple products.
     *
     * @param productIds the collection of product identifiers
     * @return a list of Object arrays containing:
     *         [productId (Long), averageRating (Double), reviewCount (Long)]
     */
    @Query(
            """
                select r.product.id, avg(r.rating), count(r)
                from Review r
                where r.product.id in :productIds
                group by r.product.id
            """)
    java.util.List<Object[]> findRatingStatsByProductIds(
            @Param("productIds") Collection<Long> productIds);
}
