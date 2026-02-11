package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.auth.AuthResponse;
import com.bugnbass.backend.dto.auth.LoginDto;
import com.bugnbass.backend.dto.auth.RegisterDto;
import com.bugnbass.backend.security.AuthTokenFilter;
import com.bugnbass.backend.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = AuthTokenFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false) // kein Security/CSRF Stress
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthService authService;

    // -------- LOGIN --------

    @Test
    void login_validBody_returns200_andCallsService() throws Exception {

        LoginDto loginDto = new LoginDto("max@test.com", "secret");

        when(authService.handleLogin(any()))
                .thenReturn(mock(AuthResponse.class));

        mockMvc.perform(post("/bugnbass/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(authService).handleLogin(any(LoginDto.class));
        verifyNoMoreInteractions(authService);
    }

    @Test
    void login_invalidJson_returns400_andDoesNotCallService() throws Exception {
        mockMvc.perform(post("/bugnbass/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{")) // kaputtes JSON
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }
    // -------- REGISTER --------

    @Test
    void register_validBody_returns200_andCallsService() throws Exception {

        RegisterDto registerDto =
                new RegisterDto("Max", "Mustermann", "max@test.com", "secret");

        when(authService.handleRegister(any()))
                .thenReturn(mock(AuthResponse.class));

        mockMvc.perform(post("/bugnbass/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(authService).handleRegister(any(RegisterDto.class));
        verifyNoMoreInteractions(authService);
    }


    @Test
    void register_invalidJson_returns400_andDoesNotCallService() throws Exception {
        mockMvc.perform(post("/bugnbass/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{")) // kaputtes JSON
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }
}
