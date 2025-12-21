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
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;


/**
 * Entity representing a standard user/customer in the system.
 * Implements {@link IbaseUser} for Spring Security integration.
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User implements IbaseUser {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Email address of the user, must be unique.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * First name of the user.
     */
    @Column
    private String firstname;

    /**
     * Last name of the user.
     */
    @Column
    private String lastname;

    /**
     * Password for authentication.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Role of the user, defaulted to ROLE_USER.
     */
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_USER;

    /**
     * Phone number of the user.
     */
    @Column
    private Integer phone;

    /**
     * Postcode of the user.
     */
    @Column
    private String postcode;

    /**
     * Address of the user.
     */
    @Column
    private String address;

    /**
     * Indicates whether the user is active.
     */
    @Column(nullable = false)
    private boolean active = true;

    /**
     * Timestamp indicating when the user was created.
     */
    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    /**
     * Returns the authorities granted to the user.
     * Currently, users have no granted authorities beyond their role.
     *
     * @return a collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return the user's password
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Returns the username used to authenticate the user.
     * In this case, it is the email address.
     *
     * @return the user's email
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Returns the user's phone number as a String.
     *
     * @return phone number in String format
     */
    public String getPhoneNumber() {
        return this.phone + "";
    }
}
