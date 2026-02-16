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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    // -------------------- LOGIN --------------------

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
        when(user.getRole()).thenReturn(UserRole.ROLE_USER);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn((IbaseUser) user);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);

        when(jwtUtil.generateToken("max@test.com", UserRole.ROLE_USER))
                .thenReturn("jwt-token");

        AuthResponse response = authService.handleLogin(loginDto);

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("jwt-token");
        assertThat(response.user()).isNotNull();
        assertThat(response.user().id()).isEqualTo(userId.toString());
        assertThat(response.user().firstname()).isEqualTo("Max");
        assertThat(response.user().lastname()).isEqualTo("Mustermann");
        assertThat(response.user().phone()).isEqualTo(123456);
        assertThat(response.user().address()).isEqualTo("Street 1");
        assertThat(response.user().postcode()).isEqualTo("1010");
        assertThat(response.user().email()).isEqualTo("max@test.com");
        assertThat(response.user().active()).isTrue();
        assertThat(response.user().createdAt()).isEqualTo(createdAt);
        assertThat(response.user().role()).isEqualTo("ROLE_USER");

        ArgumentCaptor<UsernamePasswordAuthenticationToken> tokenCaptor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        verify(authenticationManager).authenticate(tokenCaptor.capture());
        UsernamePasswordAuthenticationToken token = tokenCaptor.getValue();
        assertThat(token.getPrincipal()).isEqualTo("max@test.com");
        assertThat(token.getCredentials()).isEqualTo("secret");

        verify(jwtUtil).generateToken("max@test.com", UserRole.ROLE_USER);
        verifyNoInteractions(userService, passwordEncoder);
        verifyNoMoreInteractions(authenticationManager, jwtUtil);
    }

    @Test
    void handleLogin_throws_whenAuthenticationFails() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("bad"));

        assertThatThrownBy(() -> authService.handleLogin(loginDto))
                .isInstanceOf(BadCredentialsException.class);

        verify(authenticationManager).authenticate(any());
        verifyNoInteractions(jwtUtil, userService, passwordEncoder);
        verifyNoMoreInteractions(authenticationManager);
    }

    @Test
    void handleLogin_adminPrincipal_buildsResponseWithDefaultsButEmailAndToken() {

        Admin admin = mock(Admin.class);
        when(admin.getEmail()).thenReturn("admin@test.com");
        when(admin.getRole()).thenReturn(UserRole.ROLE_ADMIN);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn((IbaseUser) admin);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);

        when(jwtUtil.generateToken("admin@test.com", UserRole.ROLE_ADMIN))
                .thenReturn("admin-jwt");

        AuthResponse response = authService.handleLogin(new LoginDto("admin@test.com", "pw"));

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("admin-jwt");
        assertThat(response.user()).isNotNull();
        assertThat(response.user().email()).isEqualTo("admin@test.com");
        assertThat(response.user().role()).isEqualTo("ROLE_ADMIN");

        assertThat(response.user().id()).isEqualTo("");
        assertThat(response.user().firstname()).isEqualTo("");
        assertThat(response.user().lastname()).isEqualTo("");
        assertThat(response.user().active()).isTrue();
        assertThat(response.user().createdAt()).isNotNull();

        ArgumentCaptor<UsernamePasswordAuthenticationToken> tokenCaptor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        verify(authenticationManager).authenticate(tokenCaptor.capture());
        UsernamePasswordAuthenticationToken token = tokenCaptor.getValue();
        assertThat(token.getPrincipal()).isEqualTo("admin@test.com");
        assertThat(token.getCredentials()).isEqualTo("pw");

        verify(jwtUtil).generateToken("admin@test.com", UserRole.ROLE_ADMIN);
        verifyNoInteractions(userService, passwordEncoder);
        verifyNoMoreInteractions(authenticationManager, jwtUtil);
    }

    // -------------------- REGISTER --------------------

    @Test
    void handleRegister_returnsAuthResponse_whenRegisterSuccessful() {

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
        when(newUser.getRole()).thenReturn(UserRole.ROLE_USER);

        when(userService.registerUser(registerDto, passwordEncoder))
                .thenReturn(newUser);

        when(jwtUtil.generateToken("max@test.com", UserRole.ROLE_USER))
                .thenReturn("jwt-token");

        AuthResponse response = authService.handleRegister(registerDto);

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("jwt-token");
        assertThat(response.user()).isNotNull();
        assertThat(response.user().id()).isEqualTo(userId.toString());
        assertThat(response.user().email()).isEqualTo("max@test.com");
        assertThat(response.user().createdAt()).isEqualTo(createdAt);
        assertThat(response.user().role()).isEqualTo("ROLE_USER");

        verify(userService).registerUser(registerDto, passwordEncoder);
        verify(jwtUtil).generateToken("max@test.com", UserRole.ROLE_USER);
        verifyNoInteractions(authenticationManager);
        verifyNoMoreInteractions(userService, jwtUtil);
        verifyNoMoreInteractions(passwordEncoder); // keine direkten calls erwartet
    }

    @Test
    void handleRegister_throws_whenUserServiceThrows() {
        when(userService.registerUser(registerDto, passwordEncoder))
                .thenThrow(new RuntimeException("boom"));

        assertThatThrownBy(() -> authService.handleRegister(registerDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("boom");

        verify(userService).registerUser(registerDto, passwordEncoder);
        verifyNoInteractions(authenticationManager, jwtUtil);
        verifyNoMoreInteractions(userService);
    }
}
