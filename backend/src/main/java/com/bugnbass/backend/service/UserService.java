package com.bugnbass.backend.service;
import com.bugnbass.backend.dto.auth.RegisterDTO;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model._interface.IBaseUser;
import com.bugnbass.backend.model.enums.UserRole;
import com.bugnbass.backend.repository.UserRepository;
import com.bugnbass.backend.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    
    public Optional<IBaseUser> findByEmail(String email) {
    return userRepository.findByEmail(email)
            .map(u -> (IBaseUser) u)
            .or(() -> adminRepository.findByEmail(email).map(a -> (IBaseUser) a));
    }


    public User registerUser(RegisterDTO dto, PasswordEncoder encoder) {
       User user = User.builder()
        .email(dto.email())
        .firstname(dto.firstName())
        .lastname(dto.lastName())
        .password(encoder.encode(dto.password()))
        .role(UserRole.ROLE_USER)
        .build();


        return userRepository.save(user);
    }
}

