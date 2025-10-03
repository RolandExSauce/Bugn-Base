package com.bugnbass.backend.model;
import com.bugnbass.backend.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "User")
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;
    String email;

    @Enumerated(EnumType.STRING)
    UserRole role = UserRole.ROLE_USER;
};
