package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.auth.UserDto;
import com.bugnbass.backend.exceptions.GlobalExceptionHandler;
import com.bugnbass.backend.security.AuthTokenFilter;
import com.bugnbass.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = AuthTokenFilter.class
        )
)
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean UserService userService;

    // -------------------- TESTS --------------------

    @Test
    void updateUser_withoutAuth_returns4xx_andDoesNotCallService() throws Exception {

        UserDto request = sampleUserDto();

        mockMvc.perform(patch("/bugnbass/api/user/profile")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(userService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateUser_withUser_returns200_andJsonBody() throws Exception {

        UserDto request = sampleUserDto();
        UserDto updated = sampleUpdatedUserDto();

        when(userService.updateUser(any())).thenReturn(updated);

        mockMvc.perform(patch("/bugnbass/api/user/profile")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.firstname").value("Max"))
                .andExpect(jsonPath("$.lastname").value("Mustermann"))
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));

        ArgumentCaptor<UserDto> captor = ArgumentCaptor.forClass(UserDto.class);
        verify(userService).updateUser(captor.capture());

        assertThat(captor.getValue().firstname()).isEqualTo("Max");

        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser_withAdmin_returns200() throws Exception {

        UserDto request = sampleUserDto();

        when(userService.updateUser(any())).thenReturn(request);

        mockMvc.perform(patch("/bugnbass/api/user/profile")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userService).updateUser(any());
        verifyNoMoreInteractions(userService);
    }

    // -------------------- HELPERS --------------------

    private static UserDto sampleUserDto() {
        return new UserDto(
                "1",
                "Max",
                "Mustermann",
                123456,
                "Street 1",
                "1234",
                "max@example.com",
                true,
                Instant.parse("2026-02-11T10:15:30Z"),
                "ROLE_USER"
        );
    }

    private static UserDto sampleUpdatedUserDto() {
        return new UserDto(
                "1",
                "Max",
                "Mustermann",
                123456,
                "Street 1",
                "1234",
                "new@example.com",
                true,
                Instant.parse("2026-02-11T10:15:30Z"),
                "ROLE_USER"
        );
    }
}
