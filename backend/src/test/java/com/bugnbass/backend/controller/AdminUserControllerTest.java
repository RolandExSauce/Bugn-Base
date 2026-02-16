package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.AdminUpdateUserDto;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model.enums.UserRole;
import com.bugnbass.backend.security.AuthTokenFilter;
import com.bugnbass.backend.service.AdminUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AdminUserController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = AuthTokenFilter.class
        )
)
@AutoConfigureMockMvc
@Import(AdminUserControllerTest.MethodSecurityTestConfig.class)
class AdminUserControllerTest {

    @TestConfiguration
    @EnableMethodSecurity(prePostEnabled = true)
    static class MethodSecurityTestConfig {}

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean AdminUserService adminUserService;

    // ---------- GET /bugnbass/api/admin/users ----------

    @Test
    void getUsers_withoutAuth_returns4xx_andDoesNotCallService() throws Exception {
        mockMvc.perform(get("/bugnbass/api/admin/users"))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(adminUserService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getUsers_withUserRole_returns403_andDoesNotCallService() throws Exception {
        mockMvc.perform(get("/bugnbass/api/admin/users"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminUserService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUsers_withAdmin_returns200_andArray() throws Exception {
        User u = validUser();
        when(adminUserService.getUsers()).thenReturn(List.of(u));

        mockMvc.perform(get("/bugnbass/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(u.getId().toString()))
                .andExpect(jsonPath("$[0].email").value(u.getEmail()))
                .andExpect(jsonPath("$[0].firstname").value(u.getFirstname()))
                .andExpect(jsonPath("$[0].lastname").value(u.getLastname()))
                .andExpect(jsonPath("$[0].role").value(u.getRole().name()))
                .andExpect(jsonPath("$[0].active").value(u.isActive()))
                .andExpect(jsonPath("$[0].postcode").value(u.getPostcode()))
                .andExpect(jsonPath("$[0].address").value(u.getAddress()))
                .andExpect(jsonPath("$[0].phone").value(u.getPhone()));

        verify(adminUserService).getUsers();
        verifyNoMoreInteractions(adminUserService);
    }

    // ---------- GET /bugnbass/api/admin/users/{id} ----------

    @Test
    void getUserById_withoutAuth_returns4xx_andDoesNotCallService() throws Exception {
        mockMvc.perform(get("/bugnbass/api/admin/users/{id}", "user-123"))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(adminUserService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getUserById_withUserRole_returns403_andDoesNotCallService() throws Exception {
        mockMvc.perform(get("/bugnbass/api/admin/users/{id}", "user-123"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminUserService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserById_withAdmin_returns200_andCallsService_andReturnsJson() throws Exception {
        String id = UUID.randomUUID().toString();
        User u = validUser();

        when(adminUserService.getUserById(id)).thenReturn(u);

        mockMvc.perform(get("/bugnbass/api/admin/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(u.getId().toString()))
                .andExpect(jsonPath("$.email").value(u.getEmail()))
                .andExpect(jsonPath("$.role").value(u.getRole().name()))
                .andExpect(jsonPath("$.active").value(u.isActive()));

        verify(adminUserService).getUserById(id);
        verifyNoMoreInteractions(adminUserService);
    }

    // ---------- PUT /bugnbass/api/admin/users/{id} ----------

    @Test
    void updateUser_withoutAuth_returns4xx_andDoesNotCallService() throws Exception {
        String id = UUID.randomUUID().toString();
        AdminUpdateUserDto dto = validUpdateDto();

        mockMvc.perform(put("/bugnbass/api/admin/users/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(adminUserService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateUser_withUserRole_returns403_andDoesNotCallService() throws Exception {
        String id = UUID.randomUUID().toString();
        AdminUpdateUserDto dto = validUpdateDto();

        mockMvc.perform(put("/bugnbass/api/admin/users/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminUserService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser_withAdmin_returns200_andPassesIdAndDtoToService_andReturnsUpdatedUser() throws Exception {
        String id = UUID.randomUUID().toString();
        AdminUpdateUserDto dto = validUpdateDto();

        User updated = validUser();
        updated.setFirstname(dto.firstname());
        updated.setLastname(dto.lastname());
        updated.setEmail(dto.email());
        updated.setPhone(dto.phone());
        updated.setAddress(dto.address());
        updated.setPostcode(dto.postcode());
        updated.setActive(dto.active());
        updated.setRole(dto.role());

        when(adminUserService.updateUser(eq(id), any(AdminUpdateUserDto.class))).thenReturn(updated);

        mockMvc.perform(put("/bugnbass/api/admin/users/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstname").value(dto.firstname()))
                .andExpect(jsonPath("$.lastname").value(dto.lastname()))
                .andExpect(jsonPath("$.email").value(dto.email()))
                .andExpect(jsonPath("$.phone").value(dto.phone()))
                .andExpect(jsonPath("$.address").value(dto.address()))
                .andExpect(jsonPath("$.postcode").value(dto.postcode()))
                .andExpect(jsonPath("$.active").value(dto.active()))
                .andExpect(jsonPath("$.role").value(dto.role().name()));

        ArgumentCaptor<AdminUpdateUserDto> captor = ArgumentCaptor.forClass(AdminUpdateUserDto.class);
        verify(adminUserService).updateUser(eq(id), captor.capture());

        AdminUpdateUserDto passed = captor.getValue();
        assertThat(passed.firstname()).isEqualTo(dto.firstname());
        assertThat(passed.lastname()).isEqualTo(dto.lastname());
        assertThat(passed.email()).isEqualTo(dto.email());
        assertThat(passed.active()).isEqualTo(dto.active());
        assertThat(passed.role()).isEqualTo(dto.role());

        verifyNoMoreInteractions(adminUserService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser_withAdmin_invalidDto_returns400_andDoesNotCallService() throws Exception {
        String id = UUID.randomUUID().toString();

        // invalid: firstname blank, email invalid, active null, role null, postcode null
        AdminUpdateUserDto invalid = new AdminUpdateUserDto(
                "   ",
                "Mustermann",
                null,
                null,
                null,
                "not-an-email",
                null,
                null
        );

        mockMvc.perform(put("/bugnbass/api/admin/users/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(adminUserService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser_withoutCsrf_returns403_andDoesNotCallService() throws Exception {
        String id = UUID.randomUUID().toString();
        AdminUpdateUserDto dto = validUpdateDto();

        mockMvc.perform(put("/bugnbass/api/admin/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminUserService);
    }

    // ---------- DELETE /bugnbass/api/admin/users/{id} ----------

    @Test
    void deleteUser_withoutAuth_returns4xx_andDoesNotCallService() throws Exception {
        mockMvc.perform(delete("/bugnbass/api/admin/users/{id}", UUID.randomUUID().toString())
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(adminUserService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteUser_withUserRole_returns403_andDoesNotCallService() throws Exception {
        mockMvc.perform(delete("/bugnbass/api/admin/users/{id}", UUID.randomUUID().toString())
                        .with(csrf()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminUserService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_withAdmin_returns204_andCallsService() throws Exception {
        String id = UUID.randomUUID().toString();

        mockMvc.perform(delete("/bugnbass/api/admin/users/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(adminUserService).deleteUser(id);
        verifyNoMoreInteractions(adminUserService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_withoutCsrf_returns403_andDoesNotCallService() throws Exception {
        mockMvc.perform(delete("/bugnbass/api/admin/users/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminUserService);
    }

    // -------------------- HELPERS --------------------

    private static AdminUpdateUserDto validUpdateDto() {
        // Achtung: dein UserRole Enum scheint ROLE_USER / ROLE_ADMIN zu haben (siehe User default).
        // Ich verwende hier ROLE_USER.
        return new AdminUpdateUserDto(
                "Max",
                "Mustermann",
                123456789,
                "Test Street 1",
                "1234",
                "max@example.com",
                true,
                UserRole.ROLE_USER
        );
    }

    private static User validUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .email("max@example.com")
                .firstname("Max")
                .lastname("Mustermann")
                .password("secret") // nicht null wegen @Column(nullable=false)
                .role(UserRole.ROLE_USER)
                .phone(123456789)
                .postcode("1234")
                .address("Test Street 1")
                .active(true)
                .createdAt(Instant.now())
                .build();
    }
}
