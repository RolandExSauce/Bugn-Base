package com.bugnbass.backend.repository;

import com.bugnbass.backend.model.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for CRUD operations on Product entities.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds an active product by its ID.
     *
     * @param id the product ID
     * @return an Optional containing the Product if found and active, or empty if not
     */
    Optional<Product> findByIdAndActiveTrue(Long id);

    /**
     * Finds a product by its name.
     *
     * @param productName the name of the product
     * @return an Optional containing the Product if found, or empty if not
     */
    Optional<Product> findByName(String productName);
}
