package com.bugnbass.backend.repository;

import com.bugnbass.backend.model.Admin;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for CRUD operations on Admin entities.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {

    /**
     * Finds an admin by email.
     *
     * @param email the email address to search for
     * @return an Optional containing the Admin if found, or empty if not
     */
    Optional<Admin> findByEmail(String email);
}
