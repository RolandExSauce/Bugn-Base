package com.bugnbass.backend.repository;
import com.bugnbass.backend.model.Image;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    List<Image> findAllByProduct_ProductId(String productId);
}
