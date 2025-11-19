package com.bugnbass.backend.repository;
import com.bugnbass.backend.model.Product;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByProductIdAndActiveTrue(String id);
    Optional<Product> findProductByName(String productName);
}
