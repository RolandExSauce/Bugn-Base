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
    String email;

    @Column
    String username;

    @Column(nullable = false)
    String password;

    @Enumerated(EnumType.STRING)
    UserRole role = UserRole.ROLE_USER;


    @Column(nullable = false, unique = true)
    private String fullName; // For customer users, use the full name for profile picture


    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String stateAndDistrict; // state and district of customer, e.g. Vienna, 1220

    @Column(nullable = false)
    private String shippingAddress; //Can be updated when making order


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
