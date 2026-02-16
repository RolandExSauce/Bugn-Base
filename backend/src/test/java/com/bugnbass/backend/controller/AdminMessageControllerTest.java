package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.MessageResponse;
import com.bugnbass.backend.dto.ReplyMessageRequest;
import com.bugnbass.backend.model.enums.MessageStatus;
import com.bugnbass.backend.security.AuthTokenFilter;
import com.bugnbass.backend.service.AdminMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AdminMessageController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = AuthTokenFilter.class
        )
)
@AutoConfigureMockMvc
@Import(AdminMessageControllerTest.MethodSecurityTestConfig.class)
class AdminMessageControllerTest {

    @TestConfiguration
    @EnableMethodSecurity(prePostEnabled = true)
    static class MethodSecurityTestConfig {
        // aktiviert @PreAuthorize in WebMvcTest, ohne MVC zu zerschießen
    }

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean AdminMessageService adminMessageService;

    // ---------- GET /bugnbass/api/admin/messages ----------

    @Test
    void getAll_withoutAuth_returns4xx_andDoesNotCallService() throws Exception {
        mockMvc.perform(get("/bugnbass/api/admin/messages"))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(adminMessageService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAll_withUserRole_returns403_andDoesNotCallService() throws Exception {
        mockMvc.perform(get("/bugnbass/api/admin/messages"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminMessageService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAll_withAdmin_returns200_andArray() throws Exception {
        when(adminMessageService.getAll()).thenReturn(List.of(openMessage()));

        mockMvc.perform(get("/bugnbass/api/admin/messages"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Max Mustermann"))
                .andExpect(jsonPath("$[0].email").value("max@example.com"))
                .andExpect(jsonPath("$[0].messageStatus").value("OPEN"));

        verify(adminMessageService).getAll();
        verifyNoMoreInteractions(adminMessageService);
    }

    // ---------- POST /bugnbass/api/admin/messages/{id}/send-reply ----------

    @Test
    void sendReply_withoutAuth_returns4xx_andDoesNotCallService() throws Exception {
        UUID id = UUID.randomUUID();
        ReplyMessageRequest req = new ReplyMessageRequest("Danke!");

        mockMvc.perform(post("/bugnbass/api/admin/messages/{id}/send-reply", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(adminMessageService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void sendReply_withUserRole_returns403_andDoesNotCallService() throws Exception {
        UUID id = UUID.randomUUID();
        ReplyMessageRequest req = new ReplyMessageRequest("Danke!");

        mockMvc.perform(post("/bugnbass/api/admin/messages/{id}/send-reply", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminMessageService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void sendReply_withAdmin_returns200_andPassesIdAndReplyToService() throws Exception {
        UUID id = UUID.randomUUID();
        ReplyMessageRequest req = new ReplyMessageRequest("Wir kümmern uns darum.");

        when(adminMessageService.sendReply(id, req.reply()))
                .thenReturn(repliedMessage());

        mockMvc.perform(post("/bugnbass/api/admin/messages/{id}/send-reply", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.adminReply").value("Vielen Dank für deine Nachricht."))
                .andExpect(jsonPath("$.messageStatus").value("REPLIED"));

        ArgumentCaptor<String> replyCaptor = ArgumentCaptor.forClass(String.class);
        verify(adminMessageService).sendReply(eq(id), replyCaptor.capture());
        assertThat(replyCaptor.getValue()).isEqualTo(req.reply());

        verifyNoMoreInteractions(adminMessageService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void sendReply_withoutCsrf_returns403_andDoesNotCallService() throws Exception {
        UUID id = UUID.randomUUID();
        ReplyMessageRequest req = new ReplyMessageRequest("Test");

        mockMvc.perform(post("/bugnbass/api/admin/messages/{id}/send-reply", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminMessageService);
    }

    // -------------------- HELPERS --------------------

    private static MessageResponse openMessage() {
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        return new MessageResponse(
                UUID.randomUUID(),
                "Max Mustermann",
                "max@example.com",
                "Frage zum Produkt",
                "Hallo, ich habe eine Frage.",
                created,
                null,
                null,
                MessageStatus.OPEN,
                null
        );
    }

    private static MessageResponse repliedMessage() {
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime replied = LocalDateTime.now();
        return new MessageResponse(
                UUID.randomUUID(),
                "Max Mustermann",
                "max@example.com",
                "Frage zum Produkt",
                "Hallo, ich habe eine Frage.",
                created,
                "Vielen Dank für deine Nachricht.",
                replied,
                MessageStatus.REPLIED,
                null
        );
    }
}
