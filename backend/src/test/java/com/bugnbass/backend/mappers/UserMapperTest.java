package com.bugnbass.backend.mappers;

import com.bugnbass.backend.dto.auth.UserDto;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model.enums.UserRole;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class UserMapperTest {

    @Test
    void toUserDto_mapsAllFieldsCorrectly() {

        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.parse("2026-02-11T10:15:30Z");

        User user = new User();
        user.setId(id);
        user.setFirstname("Max");
        user.setLastname("Mustermann");
        user.setPhone(123456);
        user.setAddress("Street 1");
        user.setPostcode("1234");
        user.setEmail("max@example.com");
        user.setActive(true);
        user.setCreatedAt(createdAt);
        user.setRole(UserRole.ROLE_USER);

        UserDto dto = UserMapper.toUserDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(id.toString());
        assertThat(dto.firstname()).isEqualTo("Max");
        assertThat(dto.lastname()).isEqualTo("Mustermann");
        assertThat(dto.phone()).isEqualTo(123456);
        assertThat(dto.address()).isEqualTo("Street 1");
        assertThat(dto.postcode()).isEqualTo("1234");
        assertThat(dto.email()).isEqualTo("max@example.com");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(createdAt);
        assertThat(dto.role()).isEqualTo("ROLE_USER");
    }

    @Test
    void toUserDto_returnsNull_whenUserIsNull() {

        UserDto dto = UserMapper.toUserDto(null);

        assertThat(dto).isNull();
    }

    @Test
    void toUserDto_handlesNullPhoneAndPostcode() {

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstname("Max");
        user.setLastname("Mustermann");
        user.setPhone(null);
        user.setAddress("Street 1");
        user.setPostcode(null);
        user.setEmail("max@example.com");
        user.setActive(true);
        user.setCreatedAt(Instant.now());
        user.setRole(UserRole.ROLE_ADMIN);

        UserDto dto = UserMapper.toUserDto(user);

        assertThat(dto.phone()).isNull();
        assertThat(dto.postcode()).isEqualTo("0"); // dein Default-Wert
        assertThat(dto.role()).isEqualTo("ROLE_ADMIN");
    }
}
