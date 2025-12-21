package com.bugnbass.backend.model.ibaseinterface;

import com.bugnbass.backend.model.enums.UserRole;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Base interface for user entities in the system.
 * Extends Spring Security's {@link UserDetails} to integrate authentication and authorization.
 */
public interface IbaseUser extends UserDetails {

    /**
     * Returns the email of the user.
     *
     * @return user's email
     */
    String getEmail();

    /**
     * Returns the role of the user.
     *
     * @return user's role
     */
    UserRole getRole();
}
