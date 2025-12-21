package com.bugnbass.backend.repository;

import com.bugnbass.backend.model.Image;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for CRUD operations on Image entities.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {

    /**
     * Retrieves all images associated with a specific product ID.
     *
     * @param productId the ID of the product
     * @return a list of Image entities linked to the product
     */
    List<Image> findAllByProductId(Long productId);
}
