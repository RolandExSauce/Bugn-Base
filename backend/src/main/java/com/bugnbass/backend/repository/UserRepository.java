package com.bugnbass.backend.repository;

import com.bugnbass.backend.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on {@link User} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard methods for saving, updating,
 * deleting, and retrieving users. Also includes custom queries for retrieving users
 * by unique identifiers such as UUID or email.</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Finds a user by their UUID identifier.
     *
     * @param uuid the UUID of the user
     * @return an {@link Optional} containing the User if found, or empty if not found
     */
    Optional<User> findById(UUID uuid);

    /**
     * Finds a user by their email address.
     *
     * @param email the email address to search for
     * @return an {@link Optional} containing the User if found, or empty if not found
     */
    Optional<User> findByEmail(String email);
}
