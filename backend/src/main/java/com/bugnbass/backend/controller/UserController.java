package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.auth.UserDto;
import com.bugnbass.backend.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing the authenticated user's profile.
 *
 * <p>Provides endpoints for updating the user's own profile. Admin users
 * can also update any user's profile by providing the target user's ID.</p>
 *
 * <p>All endpoints are secured and accessible only to authenticated users
 * with ROLE_USER or ROLE_ADMIN authority.</p>
 */
@RestController
@RequestMapping("/bugnbass/api/user/profile")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public class UserController {

    /** Service handling user-related business logic. */
    private final UserService userService;

    /**
     * Constructs the UserController with the required UserService.
     *
     * @param userService the UserService instance
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Updates the authenticated user's profile or allows an admin to update
     * another user's profile.
     *
     * <p>Only firstname, lastname, phone, address, postcode, and email
     * can be updated. Regular users can only update their own profile, while
     * admins can update any user's profile by specifying the user's ID in the payload.</p>
     *
     * @param userDto the {@link UserDto} containing the fields to update
     * @return the updated {@link UserDto}
     */
    @PatchMapping
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }
}
