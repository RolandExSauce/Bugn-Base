package com.bugnbass.backend.repository;

import com.bugnbass.backend.model.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing Product entities. Provides CRUD operations and methods for finding
 * products by ID and name.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  /**
   * Finds an active product by its ID.
   *
   * @param id the product ID
   * @return an Optional containing the product if found and active, or empty if not
   */
  Optional<Product> findByIdAndActiveTrue(Long id);

  /**
   * Finds a product by its name.
   *
   * @param productName the name of the product
   * @return an Optional containing the product if found, or empty if not
   */
  Optional<Product> findByName(String productName);
}
