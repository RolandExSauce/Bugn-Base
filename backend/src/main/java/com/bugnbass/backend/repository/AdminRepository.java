package com.bugnbass.backend.repository;

import com.bugnbass.backend.model.Admin;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing Admin entities. Provides methods for CRUD operations and finding admins
 * by email.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

  /**
   * Finds an admin by email.
   *
   * @param email the admin's email
   * @return an Optional containing the Admin if found, or empty if not found
   */
  Optional<Admin> findByEmail(String email);
}
