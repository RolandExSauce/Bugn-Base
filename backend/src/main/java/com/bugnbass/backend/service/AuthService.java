package com.bugnbass.backend.service;

import com.bugnbass.backend.config.JwtUtil;
import com.bugnbass.backend.dto.AuthResponse;
import com.bugnbass.backend.dto.LoginDTO;
import com.bugnbass.backend.dto.RegisterDTO;
import com.bugnbass.backend.model.Admin;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model._interface.IBaseUser;
import com.bugnbass.backend.model.enums.UserRole;
import com.bugnbass.backend.repository.AdminRepository;
import com.bugnbass.backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

//auth service methods
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            AdminRepository adminRepository,
            UserRepository userRepository, PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    };

    public AuthResponse handleLogin(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.username(),
                        loginDTO.password()
                )
        );
        UserDetails userDetails = (IBaseUser) authentication.getPrincipal();
        String username = userDetails.getUsername();
        IBaseUser baseUser = userRepository.findByUsername(username)
                .map(u -> (IBaseUser) u)
                .orElseGet(() -> adminRepository.findByUsername(username)
                        .map(a -> (IBaseUser) a)
                        .orElseThrow(() ->
                                new UsernameNotFoundException("User not found with email: " + username)
                        )
                );
        return buildAuthResponse(baseUser);
    };


    public AuthResponse handleRegister(RegisterDTO registerDTO) {

        UserRole userRole = registerDTO.role();

        if(userRole.equals(UserRole.ROLE_ADMIN)){
            Admin newAdmin = new Admin();
            newAdmin.setEmail(registerDTO.email());
            newAdmin.setUsername(registerDTO.username());

            newAdmin.setPassword(passwordEncoder.encode(registerDTO.password()));
            return buildAuthResponse(newAdmin);
        }
        else {
            User newUser = new User();
            newUser.setEmail(registerDTO.email());
            newUser.setUsername(registerDTO.username());

            newUser.encodePw(registerDTO.password(), passwordEncoder);
            return buildAuthResponse(newUser);
        }
    };

    public AuthResponse buildAuthResponse(IBaseUser baseUser) {
        return new AuthResponse(
                baseUser.getEmail(),
                baseUser.getUsername(),
                jwtUtil.generateToken(baseUser.getUsername())
        );
    }

};
