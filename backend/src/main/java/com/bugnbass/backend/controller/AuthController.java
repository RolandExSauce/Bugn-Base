package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.auth.AuthResponse;
import com.bugnbass.backend.dto.auth.LoginDTO;
import com.bugnbass.backend.dto.auth.RegisterDTO;
import com.bugnbass.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication-related operations. Provides endpoints for user login and
 * registration.
 */
@RestController
@RequestMapping("/bugnbass/auth")
public class AuthController {

  private final AuthService authService;

  /**
   * Constructs an AuthController with the given AuthService.
   *
   * @param authService the authentication service
   */
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  /**
   * Authenticates a user with the provided login credentials.
   *
   * @param loginDTO the login data (username/email and password)
   * @return an AuthResponse containing authentication details (e.g., JWT)
   */
  @PostMapping("/login")
  public AuthResponse login(@Valid @RequestBody LoginDTO loginDTO) {
    return authService.handleLogin(loginDTO);
  }

  /**
   * Registers a new user with the provided registration data.
   *
   * @param registerDTO the registration data (username, email, password, etc.)
   * @return an AuthResponse containing authentication details for the new user
   */
  @PostMapping("/register")
  public AuthResponse regsiter(@Valid @RequestBody RegisterDTO registerDTO) {
    System.out.println("dto: " + registerDTO);
    return authService.handleRegister(registerDTO);
  }
}
