package com.bugnbass.backend.model;
import com.bugnbass.backend.model._interface.IBaseUser;
import com.bugnbass.backend.model.enums.UserRole;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collection;
import java.util.List;
import lombok.*;


@Entity
@Table(name = "admins")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Admin implements IBaseUser {

    @Id
    private Long id;

    String email;
    String username;
    String password;

    @Enumerated(EnumType.STRING)
    UserRole role = UserRole.ROLE_ADMIN;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    public void encodePw(String plainPw, PasswordEncoder encoder){
        this.password = encoder.encode(plainPw);
    };
};


