package com.bugnbass.backend.model;

import com.bugnbass.backend.model.enums.UserRole;
import com.bugnbass.backend.model.ibaseinterface.IbaseUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;


/**
 * Entity representing an administrator user in the system.
 * Implements {@link IbaseUser} for Spring Security integration.
 */
@Entity
@Table(name = "admins")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Admin implements IbaseUser {

    /**
     * Unique identifier for the admin.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Email address of the admin, must be unique.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Password for authentication.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Role of the user, defaulted to ROLE_ADMIN.
     */
    @Enumerated(EnumType.STRING)
    UserRole role = UserRole.ROLE_ADMIN;

    /**
     * Returns the authorities granted to the admin.
     * Currently, admins have no granted authorities beyond their role.
     *
     * @return a collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Returns the password used to authenticate the admin.
     *
     * @return the admin's password
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Returns the username used to authenticate the admin.
     * In this case, it is the email address.
     *
     * @return the admin's email
     */
    @Override
    public String getUsername() {
        return this.email;
    }
}
