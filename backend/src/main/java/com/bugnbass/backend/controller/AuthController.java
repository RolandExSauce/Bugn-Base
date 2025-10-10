package com.bugnbass.backend.controller;
import com.bugnbass.backend.dto.AuthResponse;
import com.bugnbass.backend.dto.LoginDTO;
import com.bugnbass.backend.dto.RegisterDTO;
import com.bugnbass.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bugnbass/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    };

    @PostMapping
    public AuthResponse login(@Valid @RequestBody LoginDTO loginDTO) {

        return authService.login(loginDTO);
    };

    @PostMapping
    public AuthResponse regsiter(@Valid @RequestBody RegisterDTO registerDTO) {

        return authService.register(registerDTO);
    };

}
