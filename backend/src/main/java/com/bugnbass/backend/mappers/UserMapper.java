package com.bugnbass.backend.mappers;

import com.bugnbass.backend.dto.auth.UserDto;
import com.bugnbass.backend.model.User;

/**
 * Mapper class for converting {@link User} entities to {@link UserDto} objects.
 *
 * <p>This class provides methods to safely transform User domain models into
 * data transfer objects suitable for API responses, exposing only the necessary
 * fields for clients.</p>
 */
public class UserMapper {

    /**
     * Converts a {@link User} entity to a {@link UserDto}.
     *
     * <p>Maps all relevant profile and account information including:
     * id, firstname, lastname, phone, address, postcode, email, active status,
     * creation timestamp, and role.</p>
     *
     * @param user the User entity to convert
     * @return a UserDto containing the user's public profile information,
     *         or {@code null} if the input user is {@code null}
     */
    public static UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserDto(
                user.getId().toString(),
                user.getFirstname(),
                user.getLastname(),
                user.getPhone() != null ? user.getPhone() : null,
                user.getAddress(),
                user.getPostcode() != null ? user.getPostcode() : "0",
                user.getEmail(),
                user.isActive(),
                user.getCreatedAt(),
                user.getRole().toString()
        );
    }
}
