package com.bugnbass.backend.model;

import com.bugnbass.backend.model._interface.IBaseUser;
import com.bugnbass.backend.model.enums.UserRole;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User implements IBaseUser {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column private String firstname;

  @Column private String lastname;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  private UserRole role = UserRole.ROLE_USER;

  @Column private Integer phone;

  @Column private String postcode;

  @Column private String address;

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

  public String getPhoneNumber() {
    return this.phone + "";
  }
}
