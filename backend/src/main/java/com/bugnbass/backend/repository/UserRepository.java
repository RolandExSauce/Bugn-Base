package com.bugnbass.backend.repository;

import com.bugnbass.backend.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing User entities. Provides CRUD operations and methods for finding users by
 * email.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

  /**
   * Finds a user by their email.
   *
   * @param email the user's email
   * @return an Optional containing the User if found, or empty if not
   */
  Optional<User> findByEmail(String email);
}
