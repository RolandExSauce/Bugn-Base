package com.bugnbass.backend.mappers;

import com.bugnbass.backend.dto.auth.UserDto;
import com.bugnbass.backend.model.User;

public class UserMapper {

    /**
     * Converts a User entity to a UserDto.
     *
     * @param user the User entity to convert
     * @return a UserDto containing the user's public profile information
     */
    public static UserDto toUserDto(User user) {
        if (user == null) return null;

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
