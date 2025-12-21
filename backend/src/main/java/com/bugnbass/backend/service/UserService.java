package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.auth.RegisterDto;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model.enums.UserRole;
import com.bugnbass.backend.model.ibaseinterface.IbaseUser;
import com.bugnbass.backend.repository.AdminRepository;
import com.bugnbass.backend.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
}
