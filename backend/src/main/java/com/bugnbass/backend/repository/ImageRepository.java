package com.bugnbass.backend.repository;

import com.bugnbass.backend.model.Image;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing Image entities. Provides methods for CRUD operations and retrieving
 * images by product ID.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {

  /**
   * Finds all images associated with a specific product ID.
   *
   * @param productId the product ID
   * @return a list of images for the given product
   */
  List<Image> findAllByProduct_Id(Long productId);
}
