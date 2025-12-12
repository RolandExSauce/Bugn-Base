package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.auth.RegisterDTO;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model._interface.IBaseUser;
import com.bugnbass.backend.model.enums.UserRole;
import com.bugnbass.backend.repository.AdminRepository;
import com.bugnbass.backend.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for managing users and administrators. Provides methods for finding users by email and
 * registering new users.
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final AdminRepository adminRepository;

  /**
   * Finds a user or admin by email.
   *
   * @param email the email to search
   * @return an Optional containing the found IBaseUser, or empty if not found
   */
  public Optional<IBaseUser> findByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .map(u -> (IBaseUser) u)
        .or(() -> adminRepository.findByEmail(email).map(a -> (IBaseUser) a));
  }

  /**
   * Finds a customer (user) by email.
   *
   * @param email the email to search
   * @return an Optional containing the found User, or empty if not found
   */
  public Optional<User> findCustomerByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  /**
   * Registers a new user with the provided registration data.
   *
   * @param dto the registration data
   * @param encoder the password encoder
   * @return the saved User entity
   */
  public User registerUser(RegisterDTO dto, PasswordEncoder encoder) {
    User user =
        User.builder()
            .email(dto.email())
            .firstname(dto.firstname())
            .lastname(dto.lastname())
            .password(encoder.encode(dto.password()))
            .role(UserRole.ROLE_USER)
            .build();
    return userRepository.save(user);
  }
}
