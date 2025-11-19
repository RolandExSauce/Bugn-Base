package com.bugnbass.backend.config;
import com.bugnbass.backend.model.Admin;
import com.bugnbass.backend.model.enums.UserRole;
import com.bugnbass.backend.repository.AdminRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
@RequiredArgsConstructor
public class AdminConfig {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.emails}")
    private List<String> adminEmails;

    @Value("${admin.default-password}")
    private String defaultPassword;

    @PostConstruct
    public void initAdmins() {
        for (String email : adminEmails) {
            adminRepository.findByEmail(email).orElseGet(() -> {
                Admin admin = Admin.builder()
                        .id(System.currentTimeMillis()) //maybe use UUID ? 
                        .email(email)
                        .password(passwordEncoder.encode(defaultPassword))
                        .role(UserRole.ROLE_ADMIN)
                        .build();
                return adminRepository.save(admin);
            });
        }
    }
}
