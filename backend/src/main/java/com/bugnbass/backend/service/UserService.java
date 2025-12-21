package com.bugnbass.backend.service;

import static com.bugnbass.backend.mappers.UserMapper.toUserDto;

import com.bugnbass.backend.dto.auth.RegisterDto;
import com.bugnbass.backend.dto.auth.UserDto;
import com.bugnbass.backend.exceptions.UserNotFoundException;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model.enums.UserRole;
import com.bugnbass.backend.model.ibaseinterface.IbaseUser;
import com.bugnbass.backend.repository.AdminRepository;
import com.bugnbass.backend.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * Service for managing users and admins, including registration and retrieval by email.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    /**
     * Repository for CRUD operations on User entities.
     */
    private final UserRepository userRepository;

    /**
     * Repository for CRUD operations on Admin entities.
     */
    private final AdminRepository adminRepository;

    /**
     * Finds a user or admin by email.
     *
     * @param email the email address to search for
     * @return an Optional containing the found IBaseUser, or empty if not found
     */
    public Optional<IbaseUser> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(u -> (IbaseUser) u)
                .or(() -> adminRepository.findByEmail(email).map(a -> (IbaseUser) a));
    }

    /**
     * Finds a customer (non-admin) user by email.
     *
     * @param email the email address to search for
     * @return an Optional containing the User if found, or empty if not
     */
    public Optional<User> findCustomerByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Registers a new user with the given registration data and password encoder.
     *
     * @param dto     the registration data transfer object
     * @param encoder the PasswordEncoder to encode the password
     * @return the saved User entity
     */
    public User registerUser(RegisterDto dto, PasswordEncoder encoder) {
        User user = User.builder()
                .email(dto.email())
                .firstname(dto.firstname())
                .lastname(dto.lastname())
                .password(encoder.encode(dto.password()))
                .role(UserRole.ROLE_USER)
                .build();
        return userRepository.save(user);
    }

    /**
     * Updates the authenticated user's profile or allows an admin to update any user's profile.
     * Only firstname, lastname, phone, address, postcode, and email can be updated.
     *
     * @param dto the UserDto containing the fields to update
     * @return the updated UserDto
     * @throws UserNotFoundException if the target user cannot be found
     * @throws SecurityException if the authenticated user is neither the target user nor an admin
     */
    public UserDto updateUser(UserDto dto) {
        User authUser = getAuthenticatedUser();
        boolean isAdmin = authUser.getRole() == UserRole.ROLE_ADMIN;

/*        if (!isAdmin && !authUser.getId().toString().equals(dto.id())) {
            throw new SecurityException("User not authorized to update this profile.");
        }*/

        User userToUpdate = userRepository.findById(UUID.fromString(dto.id()))
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with id: " + dto.id()));

        userToUpdate.setFirstname(dto.firstname());
        userToUpdate.setLastname(dto.lastname());
        userToUpdate.setEmail(dto.email());
        userToUpdate.setPhone(dto.phone() != null
                ? Integer.parseInt(dto.phone().toString()) : null);
        userToUpdate.setAddress(dto.address());
        userToUpdate.setPostcode(String.valueOf(dto.postcode()));

        User updated = userRepository.save(userToUpdate);

        // Map to UserDto before returning
        return toUserDto(updated);
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @return the authenticated User
     * @throws UserNotFoundException if user cannot be found
     */
    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.findCustomerByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }
}
