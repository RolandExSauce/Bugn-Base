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

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    };

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginDTO loginDTO) {
        return authService.handleLogin(loginDTO);
    };

    @PostMapping("/register")
    public AuthResponse regsiter(@Valid @RequestBody RegisterDTO registerDTO) {
        return authService.handleRegister(registerDTO);
    }
}
