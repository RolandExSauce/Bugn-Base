package com.bugnbass.backend.model;
import com.bugnbass.backend.model._interface.IBaseUser;
import com.bugnbass.backend.model.enums.UserRole;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User implements IBaseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_USER;

    @Column
    private String phone;

    @Column(nullable = false)
    private Integer postcode;

    @Column
    private String address;

    @Column(nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public void encodePw(String plainPw, PasswordEncoder encoder){
        this.password = encoder.encode(plainPw);
    }
}
