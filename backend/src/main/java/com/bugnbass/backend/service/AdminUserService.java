package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.AdminUpdateUserDto;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service responsible for administrative user operations such as retrieval,
 * update, and deactivation (soft delete).
 */
@Service
public class AdminUserService {

    private final UserRepository userRepository;

    /**
     * Constructs the AdminUserService with required dependencies.
     *
     * @param userRepository repository for user persistence
     */
    public AdminUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users.
     *
     * @return list of all {@link User} entities
     */
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by UUID.
     *
     * @param id the user identifier as string (UUID)
     * @return the {@link User} entity
     * @throws EntityNotFoundException if user does not exist
     */
    @Transactional(readOnly = true)
    public User getUserById(String id) {
        UUID uuid = parseUuidOrThrow(id);

        return userRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    /**
     * Updates user profile and administrative properties.
     *
     * @param id  the user identifier as string (UUID)
     * @param dto the update data transfer object
     * @return the updated {@link User}
     */
    @Transactional
    public User updateUser(String id, AdminUpdateUserDto dto) {
        UUID uuid = parseUuidOrThrow(id);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

        user.setFirstname(dto.firstname());
        user.setLastname(dto.lastname());
        user.setPhone(dto.phone());
        user.setAddress(dto.address());
        user.setPostcode(dto.postcode());
        user.setEmail(dto.email());
        user.setActive(dto.active());
        user.setRole(dto.role());

        return userRepository.save(user);
    }

    /**
     * Deactivates (soft deletes) a user account.
     *
     * @param id the user identifier as string (UUID)
     */
    @Transactional
    public void deleteUser(String id) {
        UUID uuid = parseUuidOrThrow(id);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

        user.setActive(false);
        userRepository.save(user);
    }

    /**
     * Parses a UUID from string or throws a {@link ResponseStatusException}
     * with HTTP 400 if the format is invalid.
     *
     * @param id the UUID string
     * @return parsed {@link UUID}
     */
    private UUID parseUuidOrThrow(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID: " + id);
        }
    }
}
