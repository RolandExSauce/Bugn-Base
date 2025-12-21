package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.auth.AuthResponse;
import com.bugnbass.backend.dto.auth.LoginDto;
import com.bugnbass.backend.dto.auth.RegisterDto;
import com.bugnbass.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication operations such as login and registration.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * Service handling authentication logic.
     */
    private final AuthService authService;

    /**
     * Constructs the AuthController with the required AuthService.
     *
     * @param authService the AuthService instance
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Authenticates a user with login credentials.
     *
     * @param loginDto the login data transfer object containing username and password
     * @return AuthResponse containing authentication details such as JWT token
     */
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginDto loginDto) {
        return authService.handleLogin(loginDto);
    }

    /**
     * Registers a new user.
     *
     * @param registerDto the registration data transfer object containing user details
     * @return AuthResponse containing authentication details such as JWT token
     */
    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterDto registerDto) {
        return authService.handleRegister(registerDto);
    }
}
