package com.bugnbass.backend.config;

import com.bugnbass.backend.model.Admin;
import com.bugnbass.backend.model.enums.UserRole;
import com.bugnbass.backend.repository.AdminRepository;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * Configuration component for initializing default admin users in the system.
 * Checks if admins with predefined emails exist, and if not, creates them with a default password.
 */
@Component
@RequiredArgsConstructor
public class AdminConfig {

  /** Repository for accessing Admin entities. */
  private final AdminRepository adminRepository;

  /** Password encoder used to hash admin passwords. */
  private final PasswordEncoder passwordEncoder;

  /** List of default admin email addresses to initialize. */
  private final List<String> adminEmails =
      new ArrayList<>(Arrays.asList(
          "admin1@example.com",
          "admin2@example.com"
      ));

  /** Default password for newly created admin users, injected from configuration. */
  @Value("${admin.default-password}")
  private String defaultPassword;

  /**
   * Initializes default admin accounts after the application starts.
   * If an admin with a given email does not exist, it is created with the default password.
   */
  @PostConstruct
  public void initAdmins() {
    for (String email : adminEmails) {
      adminRepository.findByEmail(email).orElseGet(() -> {
        Admin admin = Admin.builder()
            .email(email)
            .password(passwordEncoder.encode(defaultPassword))
            .role(UserRole.ROLE_ADMIN)
            .build();
        return adminRepository.save(admin);
      });
    }
  }
}
