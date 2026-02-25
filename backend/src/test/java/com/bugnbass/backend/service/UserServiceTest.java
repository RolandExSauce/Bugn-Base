package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.auth.RegisterDto;
import com.bugnbass.backend.dto.auth.UserDto;
import com.bugnbass.backend.exceptions.UserNotFoundException;
import com.bugnbass.backend.model.Admin;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model.enums.UserRole;
import com.bugnbass.backend.model.ibaseinterface.IbaseUser;
import com.bugnbass.backend.repository.AdminRepository;
import com.bugnbass.backend.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock AdminRepository adminRepository;

    @InjectMocks UserService userService;

    private final String authEmail = "auth@test.com";

    @BeforeEach
    void setUpSecurityContext() {
        SecurityContextHolder.getContext()
                .setAuthentication(new TestingAuthenticationToken(authEmail, "pw"));
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    // -------------------- findByEmail --------------------

    @Test
    void findByEmail_returnsUser_whenUserExists() {
        User user = mock(User.class);
        when(userRepository.findByEmail("u@test.com")).thenReturn(Optional.of(user));

        Optional<IbaseUser> result = userService.findByEmail("u@test.com");

        assertThat(result).containsSame(user);

        verify(userRepository).findByEmail("u@test.com");
        verifyNoInteractions(adminRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findByEmail_returnsAdmin_whenUserMissingButAdminExists() {
        when(userRepository.findByEmail("a@test.com")).thenReturn(Optional.empty());

        Admin admin = mock(Admin.class);
        when(adminRepository.findByEmail("a@test.com")).thenReturn(Optional.of(admin));

        Optional<IbaseUser> result = userService.findByEmail("a@test.com");

        assertThat(result).containsSame(admin);

        verify(userRepository).findByEmail("a@test.com");
        verify(adminRepository).findByEmail("a@test.com");
        verifyNoMoreInteractions(userRepository, adminRepository);
    }

    @Test
    void findByEmail_returnsEmpty_whenNeitherUserNorAdminExists() {
        when(userRepository.findByEmail("x@test.com")).thenReturn(Optional.empty());
        when(adminRepository.findByEmail("x@test.com")).thenReturn(Optional.empty());

        Optional<IbaseUser> result = userService.findByEmail("x@test.com");

        assertThat(result).isEmpty();

        verify(userRepository).findByEmail("x@test.com");
        verify(adminRepository).findByEmail("x@test.com");
        verifyNoMoreInteractions(userRepository, adminRepository);
    }

    // -------------------- findCustomerByEmail --------------------

    @Test
    void findCustomerByEmail_returnsUser_whenExists() {
        User user = mock(User.class);
        when(userRepository.findByEmail("c@test.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findCustomerByEmail("c@test.com");

        assertThat(result).containsSame(user);

        verify(userRepository).findByEmail("c@test.com");
        verifyNoInteractions(adminRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findCustomerByEmail_returnsEmpty_whenMissing() {
        when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.findCustomerByEmail("missing@test.com");

        assertThat(result).isEmpty();

        verify(userRepository).findByEmail("missing@test.com");
        verifyNoInteractions(adminRepository);
        verifyNoMoreInteractions(userRepository);
    }

    // -------------------- registerUser --------------------

    @Test
    void registerUser_encodesPassword_setsRoleAndSaves() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode("pw")).thenReturn("ENC(pw)");

        RegisterDto dto = new RegisterDto("Max", "Mustermann", "max@test.com", "pw");

        when(userRepository.findByEmail("max@test.com")).thenReturn(Optional.empty());

        User saved = mock(User.class);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        User result = userService.registerUser(dto, encoder);

        assertThat(result).isSameAs(saved);

        verify(encoder).encode("pw");
        verify(userRepository).findByEmail("max@test.com");
        verify(userRepository).save(captor.capture());

        User toSave = captor.getValue();
        assertThat(toSave.getEmail()).isEqualTo("max@test.com");
        assertThat(toSave.getFirstname()).isEqualTo("Max");
        assertThat(toSave.getLastname()).isEqualTo("Mustermann");
        assertThat(toSave.getPassword()).isEqualTo("ENC(pw)");
        assertThat(toSave.getRole()).isEqualTo(UserRole.ROLE_USER);

        verifyNoInteractions(adminRepository);
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(encoder);
    }


    @Test
    void registerUser_propagatesException_whenEncoderFails_andDoesNotSave() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode(anyString())).thenThrow(new RuntimeException("encoder boom"));

        RegisterDto dto = new RegisterDto("Max", "Mustermann", "max@test.com", "pw");

        when(userRepository.findByEmail("max@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.registerUser(dto, encoder))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("encoder boom");

        verify(userRepository).findByEmail("max@test.com");
        verify(encoder).encode("pw");

        verify(userRepository, never()).save(any(User.class));

        verifyNoInteractions(adminRepository);
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(encoder);
    }


    // -------------------- updateUser --------------------

    @Test
    void updateUser_updatesFields_savesAndReturnsDto_fromMapper() {
        // auth user existiert
        User authUser = mock(User.class);
        when(authUser.getRole()).thenReturn(UserRole.ROLE_USER);
        when(userRepository.findByEmail(authEmail)).thenReturn(Optional.of(authUser));

        UUID targetId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        User userToUpdate = mock(User.class);
        when(userRepository.findById(targetId)).thenReturn(Optional.of(userToUpdate));

        User updated = mock(User.class);
        when(userRepository.save(userToUpdate)).thenReturn(updated);

        when(updated.getId()).thenReturn(targetId);
        when(updated.getFirstname()).thenReturn("NewFirst");
        when(updated.getLastname()).thenReturn("NewLast");
        when(updated.getPhone()).thenReturn(123);
        when(updated.getAddress()).thenReturn("New Street");
        when(updated.getPostcode()).thenReturn("1010");
        when(updated.getEmail()).thenReturn("new@test.com");
        when(updated.isActive()).thenReturn(true);
        when(updated.getCreatedAt()).thenReturn(Instant.parse("2024-01-01T00:00:00Z"));
        when(updated.getRole()).thenReturn(UserRole.ROLE_USER);

        UserDto input = new UserDto(
                targetId.toString(),
                "NewFirst",
                "NewLast",
                123,
                "New Street",
                "1010",
                "new@test.com",
                true,
                Instant.parse("2020-01-01T00:00:00Z"),
                "ROLE_USER"
        );

        UserDto result = userService.updateUser(input);

        verify(userToUpdate).setFirstname("NewFirst");
        verify(userToUpdate).setLastname("NewLast");
        verify(userToUpdate).setEmail("new@test.com");
        verify(userToUpdate).setPhone(123);
        verify(userToUpdate).setAddress("New Street");
        verify(userToUpdate).setPostcode("1010");
        verify(userRepository).save(userToUpdate);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(targetId.toString());
        assertThat(result.firstname()).isEqualTo("NewFirst");
        assertThat(result.lastname()).isEqualTo("NewLast");
        assertThat(result.phone()).isEqualTo(123);
        assertThat(result.address()).isEqualTo("New Street");
        assertThat(result.postcode()).isEqualTo("1010");
        assertThat(result.email()).isEqualTo("new@test.com");
        assertThat(result.role()).isEqualTo(UserRole.ROLE_USER.toString());

        verify(userRepository).findByEmail(authEmail);
        verify(userRepository).findById(targetId);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(adminRepository);
    }

    @Test
    void updateUser_setsPhoneNull_whenDtoPhoneNull() {
        User authUser = mock(User.class);
        when(userRepository.findByEmail(authEmail)).thenReturn(Optional.of(authUser));

        UUID targetId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        User userToUpdate = mock(User.class);
        when(userRepository.findById(targetId)).thenReturn(Optional.of(userToUpdate));

        User updated = mock(User.class);
        when(userRepository.save(userToUpdate)).thenReturn(updated);

        when(updated.getId()).thenReturn(targetId);
        when(updated.getRole()).thenReturn(UserRole.ROLE_USER);

        UserDto input = new UserDto(
                targetId.toString(),
                "A",
                "B",
                null,
                "Street",
                "1010",
                "a@test.com",
                true,
                Instant.now(),
                "ROLE_USER"
        );

        userService.updateUser(input);

        verify(userToUpdate).setPhone(null);
        verify(userRepository).save(userToUpdate);

        verify(userRepository).findByEmail(authEmail);
        verify(userRepository).findById(targetId);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(adminRepository);
    }

    @Test
    void updateUser_throwsUserNotFound_whenTargetUserMissing() {
        User authUser = mock(User.class);
        when(userRepository.findByEmail(authEmail)).thenReturn(Optional.of(authUser));

        UUID targetId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        when(userRepository.findById(targetId)).thenReturn(Optional.empty());

        UserDto input = new UserDto(
                targetId.toString(),
                "A", "B", null, null, "1010", "x@test.com",
                true, Instant.now(), "ROLE_USER"
        );

        assertThatThrownBy(() -> userService.updateUser(input))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(targetId.toString());

        verify(userRepository).findByEmail(authEmail);
        verify(userRepository).findById(targetId);
        verify(userRepository, never()).save(any());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(adminRepository);
    }

    @Test
    void updateUser_throwsUserNotFound_whenAuthenticatedUserMissing() {
        when(userRepository.findByEmail(authEmail)).thenReturn(Optional.empty());

        UUID targetId = UUID.fromString("33333333-3333-3333-3333-333333333333");

        UserDto input = new UserDto(
                targetId.toString(),
                "A", "B", null, null, "1010", "x@test.com",
                true, Instant.now(), "ROLE_USER"
        );

        assertThatThrownBy(() -> userService.updateUser(input))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(authEmail);

        verify(userRepository).findByEmail(authEmail);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(adminRepository);
    }

    @Test
    void updateUser_throws_whenDtoIdIsInvalidUuid() {
        User authUser = mock(User.class);
        when(userRepository.findByEmail(authEmail)).thenReturn(Optional.of(authUser));

        UserDto input = new UserDto(
                "not-a-uuid",
                "A", "B", null, null, "1010", "x@test.com",
                true, Instant.now(), "ROLE_USER"
        );

        assertThatThrownBy(() -> userService.updateUser(input))
                .isInstanceOf(IllegalArgumentException.class);

        verify(userRepository).findByEmail(authEmail);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(adminRepository);
    }
}
