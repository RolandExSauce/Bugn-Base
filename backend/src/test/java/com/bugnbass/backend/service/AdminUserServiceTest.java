package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.AdminUpdateUserDto;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model.enums.UserRole;
import com.bugnbass.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AdminUserService adminUserService;

    @Test
    void getUsers_returnsAllUsers() {
        User u1 = new User();
        User u2 = new User();

        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<User> result = adminUserService.getUsers();

        assertThat(result).containsExactly(u1, u2);

        verify(userRepository).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getUserById_returnsUser_whenFound() {
        UUID id = UUID.randomUUID();
        User user = new User();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = adminUserService.getUserById(id.toString());

        assertThat(result).isSameAs(user);

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getUserById_throwsBadRequest_whenUuidInvalid() {
        assertThatThrownBy(() -> adminUserService.getUserById("not-a-uuid"))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException rse = (ResponseStatusException) ex;
                    assertThat(rse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertThat(rse.getReason()).contains("Invalid UUID");
                });

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getUserById_throwsEntityNotFound_whenMissing() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminUserService.getUserById(id.toString()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUser_updatesFields_andSaves() {
        UUID id = UUID.randomUUID();

        User existing = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        AdminUpdateUserDto dto = new AdminUpdateUserDto(
                "Max",
                "Mustermann",
                123456,
                "Street 1",
                "1010",
                "max@test.com",
                true,
                UserRole.ROLE_USER
        );

        User result = adminUserService.updateUser(id.toString(), dto);

        assertThat(result).isSameAs(existing);

        assertThat(existing.getFirstname()).isEqualTo("Max");
        assertThat(existing.getLastname()).isEqualTo("Mustermann");
        assertThat(existing.getPhone()).isEqualTo(123456);
        assertThat(existing.getAddress()).isEqualTo("Street 1");
        assertThat(existing.getPostcode()).isEqualTo("1010");
        assertThat(existing.getEmail()).isEqualTo("max@test.com");
        assertThat(existing.isActive()).isTrue();
        assertThat(existing.getRole()).isEqualTo(UserRole.ROLE_USER);

        verify(userRepository).findById(id);
        verify(userRepository).save(existing);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUser_throwsBadRequest_whenUuidInvalid() {
        AdminUpdateUserDto dto = new AdminUpdateUserDto(
                "Max",
                "Mustermann",
                123,
                "Street 1",
                "1010",
                "max@test.com",
                true,
                UserRole.ROLE_USER
        );

        assertThatThrownBy(() -> adminUserService.updateUser("not-a-uuid", dto))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException rse = (ResponseStatusException) ex;
                    assertThat(rse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertThat(rse.getReason()).contains("Invalid UUID");
                });

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUser_throwsEntityNotFound_whenMissing() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        AdminUpdateUserDto dto = new AdminUpdateUserDto(
                "Max",
                "Mustermann",
                123,
                "Street 1",
                "1010",
                "max@test.com",
                true,
                UserRole.ROLE_USER
        );

        assertThatThrownBy(() -> adminUserService.updateUser(id.toString(), dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUser_setsActiveFalse_andSaves() {
        UUID id = UUID.randomUUID();

        User existing = new User();
        existing.setActive(true);

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));

        adminUserService.deleteUser(id.toString());

        assertThat(existing.isActive()).isFalse();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue()).isSameAs(existing);

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUser_throwsBadRequest_whenUuidInvalid() {
        assertThatThrownBy(() -> adminUserService.deleteUser("not-a-uuid"))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException rse = (ResponseStatusException) ex;
                    assertThat(rse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertThat(rse.getReason()).contains("Invalid UUID");
                });

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUser_throwsEntityNotFound_whenMissing() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminUserService.deleteUser(id.toString()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }
}
