package com.bugnbass.backend.service;
import com.bugnbass.backend.config.JwtUtil;
import com.bugnbass.backend.dto.auth.AuthResponse;
import com.bugnbass.backend.dto.auth.LoginDTO;
import com.bugnbass.backend.dto.auth.RegisterDTO;
import com.bugnbass.backend.dto.auth.UserDTO;
import com.bugnbass.backend.model.Admin;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model._interface.IBaseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse handleLogin(LoginDTO loginDTO) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.email(),
                        loginDTO.password()
                )
        );

        IBaseUser user = (IBaseUser) auth.getPrincipal();
        return buildResponse(user);
    }

    public AuthResponse handleRegister(RegisterDTO dto) {
        User newUser = userService.registerUser(dto, passwordEncoder);
        return buildResponse(newUser);
    }

    //build dto auth reponse
    private AuthResponse buildResponse(IBaseUser user) {
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
        }

        else if (user instanceof Admin) {
            createdAt = Instant.now();
        }

        UserDTO userDTO = new UserDTO(
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
                userDTO,
                jwtUtil.generateToken(user.getEmail(), user.getRole())
        );
    }
}

