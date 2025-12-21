package com.bugnbass.backend.service;

import com.bugnbass.backend.config.JwtUtil;
import com.bugnbass.backend.dto.auth.AuthResponse;
import com.bugnbass.backend.dto.auth.LoginDto;
import com.bugnbass.backend.dto.auth.RegisterDto;
import com.bugnbass.backend.dto.auth.UserDto;
import com.bugnbass.backend.model.Admin;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model.ibaseinterface.IbaseUser;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service handling authentication operations such as login and user registration.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * Authentication manager for validating user credentials.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Utility for generating JWT tokens.
     */
    private final JwtUtil jwtUtil;

    /**
     * Service for user-related operations.
     */
    private final UserService userService;

    /**
     * Password encoder for hashing user passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Handles user login and generates authentication response.
     *
     * @param loginDto the login data transfer object containing email and password
     * @return AuthResponse containing user details and JWT token
     */
    public AuthResponse handleLogin(LoginDto loginDto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.email(),
                        loginDto.password()
                )
        );

        IbaseUser user = (IbaseUser) auth.getPrincipal();
        return buildResponse(user);
    }

    /**
     * Handles user registration and generates authentication response.
     *
     * @param dto the registration data transfer object containing user information
     * @return AuthResponse containing user details and JWT token
     */
    public AuthResponse handleRegister(RegisterDto dto) {
        User newUser = userService.registerUser(dto, passwordEncoder);
        return buildResponse(newUser);
    }

    /**
     * Builds the AuthResponse object containing user information and JWT token.
     *
     * @param user the authenticated or newly registered user
     * @return AuthResponse containing user details and JWT token
     */
    private AuthResponse buildResponse(IbaseUser user) {
        String id = "";
        String firstname = "";
        String lastname = "";
        Integer phone = null;
        String address = null;
        String postcode = null;
        boolean active = true;
        Instant createdAt = Instant.now();

        if (user instanceof User u) {
            id = u.getId().toString();
            firstname = u.getFirstname();
            lastname = u.getLastname();
            phone = u.getPhone();
            address = u.getAddress();
            postcode = u.getPostcode();
            active = u.isActive();
            createdAt = u.getCreatedAt();
        } else if (user instanceof Admin) {
            createdAt = Instant.now();
        }

        UserDto userDto = new UserDto(
                id,
                firstname,
                lastname,
                phone,
                address,
                postcode,
                user.getEmail(),
                active,
                createdAt,
                user.getRole().name()
        );

        return new AuthResponse(
                userDto,
                jwtUtil.generateToken(user.getEmail(), user.getRole())
        );
    }
}
