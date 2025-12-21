package com.bugnbass.backend.repository;

import com.bugnbass.backend.model.User;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for CRUD operations on User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findById(UUID uuid);

    /**
     * Finds a user by their email address.
     *
     * @param email the email address to search for
     * @return an Optional containing the User if found, or empty if not
     */
    Optional<User> findByEmail(String email);
}
