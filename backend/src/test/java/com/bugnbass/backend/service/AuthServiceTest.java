package com.bugnbass.backend.service;

import com.bugnbass.backend.config.JwtUtil;
import com.bugnbass.backend.dto.auth.AuthResponse;
import com.bugnbass.backend.dto.auth.LoginDto;
import com.bugnbass.backend.dto.auth.RegisterDto;
import com.bugnbass.backend.model.Admin;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model.ibaseinterface.IbaseUser;
import com.bugnbass.backend.model.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock AuthenticationManager authenticationManager;
    @Mock JwtUtil jwtUtil;
    @Mock UserService userService;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks AuthService authService;

    private LoginDto loginDto;
    private RegisterDto registerDto;

    @BeforeEach
    void setup() {
        loginDto = new LoginDto("max@test.com", "secret");
        registerDto = new RegisterDto("Max", "Mustermann", "max@test.com", "secret");
    }

    @Test
    void handleLogin_returnsAuthResponse_whenCredentialsValid_userPrincipal() {
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        Instant createdAt = Instant.parse("2024-01-01T10:00:00Z");

        User user = mock(User.class);

        when(user.getId()).thenReturn(userId);
        when(user.getFirstname()).thenReturn("Max");
        when(user.getLastname()).thenReturn("Mustermann");
        when(user.getPhone()).thenReturn(123456);
        when(user.getAddress()).thenReturn("Street 1");
        when(user.getPostcode()).thenReturn("1010");
        when(user.isActive()).thenReturn(true);
        when(user.getCreatedAt()).thenReturn(createdAt);
        when(user.getEmail()).thenReturn("max@test.com");

        // ✅ FIX: Role direkt stubben (UserRole ist Enum)
        UserRole roleValue = UserRole.class.getEnumConstants()[0];
        when(user.getRole()).thenReturn(roleValue);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn((IbaseUser) user);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);

        when(jwtUtil.generateToken(eq("max@test.com"), eq(roleValue)))
                .thenReturn("jwt-token");

        AuthResponse response = authService.handleLogin(loginDto);

        assertThat(response.accessToken()).isEqualTo("jwt-token");
        assertThat(response.user().id()).isEqualTo(userId.toString());
        assertThat(response.user().role()).isEqualTo(roleValue.name());
    }


    @Test
    void handleLogin_throws_whenAuthenticationFails() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("bad"));

        assertThatThrownBy(() -> authService.handleLogin(loginDto))
                .isInstanceOf(BadCredentialsException.class);

        verify(authenticationManager).authenticate(any());
        verifyNoInteractions(jwtUtil, userService, passwordEncoder);
    }

    @Test
    void handleRegister_returnsAuthResponse_whenRegisterSuccessful() {
        // Arrange
        UUID userId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        Instant createdAt = Instant.parse("2024-02-02T10:00:00Z");

        User newUser = mock(User.class);
        when(newUser.getId()).thenReturn(userId);
        when(newUser.getFirstname()).thenReturn("Max");
        when(newUser.getLastname()).thenReturn("Mustermann");
        when(newUser.getPhone()).thenReturn(null);
        when(newUser.getAddress()).thenReturn(null);
        when(newUser.getPostcode()).thenReturn("0");
        when(newUser.isActive()).thenReturn(true);
        when(newUser.getCreatedAt()).thenReturn(createdAt);
        when(newUser.getEmail()).thenReturn("max@test.com");

        UserRole roleValue = UserRole.class.getEnumConstants()[0];
        when(newUser.getRole()).thenReturn(roleValue);

        when(userService.registerUser(eq(registerDto), eq(passwordEncoder)))
                .thenReturn(newUser);

        when(jwtUtil.generateToken(eq("max@test.com"), eq(roleValue)))
                .thenReturn("jwt-token");

        // Act
        AuthResponse response = authService.handleRegister(registerDto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("jwt-token");
        assertThat(response.user()).isNotNull();
        assertThat(response.user().id()).isEqualTo(userId.toString());
        assertThat(response.user().email()).isEqualTo("max@test.com");
        assertThat(response.user().createdAt()).isEqualTo(createdAt);
        assertThat(response.user().role()).isEqualTo(roleValue.name());

        verify(userService).registerUser(eq(registerDto), eq(passwordEncoder));
        verify(jwtUtil).generateToken(eq("max@test.com"), eq(roleValue));
        verifyNoInteractions(authenticationManager);
        verifyNoMoreInteractions(userService, jwtUtil);
    }

    @Test
    void handleRegister_throws_whenUserServiceThrows() {
        when(userService.registerUser(eq(registerDto), eq(passwordEncoder)))
                .thenThrow(new RuntimeException("boom"));

        assertThatThrownBy(() -> authService.handleRegister(registerDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("boom");

        verify(userService).registerUser(eq(registerDto), eq(passwordEncoder));
        verifyNoInteractions(authenticationManager, jwtUtil);
    }

    @Test
    void handleLogin_adminPrincipal_buildsResponseWithDefaultsButEmailAndToken() {
        // Arrange
        Admin admin = mock(Admin.class);
        when(admin.getEmail()).thenReturn("admin@test.com");

        UserRole roleValue = UserRole.class.getEnumConstants()[0];
        when(admin.getRole()).thenReturn(roleValue);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn((IbaseUser) admin);

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtUtil.generateToken(eq("admin@test.com"), eq(roleValue)))
                .thenReturn("admin-jwt");

        // Act
        AuthResponse response = authService.handleLogin(new LoginDto("admin@test.com", "pw"));

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("admin-jwt");
        assertThat(response.user()).isNotNull();
        assertThat(response.user().email()).isEqualTo("admin@test.com");

        // Admin-Fall: defaults
        assertThat(response.user().id()).isEqualTo("");
        assertThat(response.user().firstname()).isEqualTo("");
        assertThat(response.user().lastname()).isEqualTo("");
        assertThat(response.user().active()).isTrue();
        assertThat(response.user().createdAt()).isNotNull();
        assertThat(response.user().role()).isEqualTo(roleValue.name());

        verify(authenticationManager).authenticate(any());
        verify(jwtUtil).generateToken(eq("admin@test.com"), eq(roleValue));
        verifyNoInteractions(userService, passwordEncoder);
    }
}
