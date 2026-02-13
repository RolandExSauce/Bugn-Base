package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.auth.AuthResponse;
import com.bugnbass.backend.dto.auth.LoginDto;
import com.bugnbass.backend.dto.auth.RegisterDto;
import com.bugnbass.backend.security.AuthTokenFilter;
import com.bugnbass.backend.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
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
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean AuthService authService;

    // -------- LOGIN --------

    @Test
    void login_validBody_returns200_andPassesDtoToService() throws Exception {
        LoginDto loginDto = new LoginDto("max@test.com", "secret");

        when(authService.handleLogin(any(LoginDto.class)))
                .thenReturn(mock(AuthResponse.class));

        mockMvc.perform(post("/bugnbass/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk());

        ArgumentCaptor<LoginDto> captor = ArgumentCaptor.forClass(LoginDto.class);
        verify(authService, times(1)).handleLogin(captor.capture());
        verifyNoMoreInteractions(authService);

        assertThat(captor.getValue().email()).isEqualTo("max@test.com");
        assertThat(captor.getValue().password()).isEqualTo("secret");
    }

    @Test
    void login_malformedJson_returns400_andDoesNotCallService() throws Exception {
        mockMvc.perform(post("/bugnbass/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }

    // -------- REGISTER --------

    @Test
    void register_validBody_returns200_andPassesDtoToService() throws Exception {
        RegisterDto registerDto =
                new RegisterDto("Max", "Mustermann", "max@test.com", "secret");

        when(authService.handleRegister(any(RegisterDto.class)))
                .thenReturn(mock(AuthResponse.class));

        mockMvc.perform(post("/bugnbass/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        ArgumentCaptor<RegisterDto> captor = ArgumentCaptor.forClass(RegisterDto.class);
        verify(authService, times(1)).handleRegister(captor.capture());
        verifyNoMoreInteractions(authService);

        RegisterDto passed = captor.getValue();
        assertThat(passed.firstname()).isEqualTo("Max");
        assertThat(passed.lastname()).isEqualTo("Mustermann");
        assertThat(passed.email()).isEqualTo("max@test.com");
        assertThat(passed.password()).isEqualTo("secret");
    }

    @Test
    void register_malformedJson_returns400_andDoesNotCallService() throws Exception {
        mockMvc.perform(post("/bugnbass/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }
}
