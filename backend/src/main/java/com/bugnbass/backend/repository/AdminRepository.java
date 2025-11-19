package com.bugnbass.backend.repository;
import com.bugnbass.backend.model.Admin;
import com.bugnbass.backend.model.Product;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByUsername(String username);
}
