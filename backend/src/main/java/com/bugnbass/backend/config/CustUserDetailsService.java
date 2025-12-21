package com.bugnbass.backend.config;

import com.bugnbass.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of Spring Security's {@link UserDetailsService}.
 * Loads user details by email for authentication purposes.
 */
@Service
@RequiredArgsConstructor
public class CustUserDetailsService implements UserDetailsService {

  /** Service for accessing user data. */
  private final UserService userService;

  /**
   * Loads a user by their email address.
   *
   * @param email the email of the user to load
   * @return the user details if found
   * @throws UsernameNotFoundException if no user with the given email exists
   */
  @Override
  public UserDetails loadUserByUsername(String email) {
    return userService.findByEmail(email)
        .orElseThrow(() ->
            new UsernameNotFoundException("User not found with email: " + email)
        );
  }
}
