package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.OrderDto;
import com.bugnbass.backend.dto.OrderItemDto;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.model.enums.PaymentMethod;
import com.bugnbass.backend.security.AuthTokenFilter;
import com.bugnbass.backend.service.AdminOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AdminOrderController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = AuthTokenFilter.class
        )
)
@AutoConfigureMockMvc
@Import(AdminOrderControllerTest.MethodSecurityTestConfig.class)
class AdminOrderControllerTest {

    @TestConfiguration
    @EnableMethodSecurity(prePostEnabled = true)
    static class MethodSecurityTestConfig {
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AdminOrderService adminOrderService;

    // ---------- GET ----------

    @Test
    void getAllOrders_withoutAuth_returns4xx_andDoesNotCallService() throws Exception {
        mockMvc.perform(get("/bugnbass/api/admin/orders"))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(adminOrderService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllOrders_withUserRole_returns403_andDoesNotCallService() throws Exception {
        mockMvc.perform(get("/bugnbass/api/admin/orders"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminOrderService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllOrders_withAdmin_returns200_andArray() throws Exception {
        when(adminOrderService.getAllOrders()).thenReturn(List.of(validOrderDto()));

        mockMvc.perform(get("/bugnbass/api/admin/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].orderNumber").value("ORD-123"))
                .andExpect(jsonPath("$[0].totalOrderPrice").value(1000))
                .andExpect(jsonPath("$[0].orderStatus").value("RECEIVED"))
                .andExpect(jsonPath("$[0].deliveryPostcode").value(1234));

        verify(adminOrderService).getAllOrders();
        verifyNoMoreInteractions(adminOrderService);
    }

    // ---------- PATCH UPDATE ----------

    @Test
    void updateOrder_withoutAuth_returns4xx_andDoesNotCallService() throws Exception {
        OrderDto dto = validOrderDto();

        mockMvc.perform(patch("/bugnbass/api/admin/orders/update")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(adminOrderService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateOrder_withUserRole_returns403_andDoesNotCallService() throws Exception {
        OrderDto dto = validOrderDto();

        mockMvc.perform(patch("/bugnbass/api/admin/orders/update")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminOrderService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateOrder_withAdmin_returns200_andPassesDtoToService() throws Exception {
        OrderDto dto = validOrderDto();

        when(adminOrderService.updateOrder(any(OrderDto.class))).thenReturn(dto);

        mockMvc.perform(patch("/bugnbass/api/admin/orders/update")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber").value("ORD-123"))
                .andExpect(jsonPath("$.orderStatus").value("RECEIVED"));

        ArgumentCaptor<OrderDto> captor = ArgumentCaptor.forClass(OrderDto.class);
        verify(adminOrderService).updateOrder(captor.capture());

        assertThat(captor.getValue().orderItems()).hasSize(1);

        verifyNoMoreInteractions(adminOrderService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateOrder_withoutCsrf_returns403_andDoesNotCallService() throws Exception {
        mockMvc.perform(patch("/bugnbass/api/admin/orders/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validOrderDto())))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminOrderService);
    }

    // ---------- DELETE ----------

    @Test
    void deleteOrder_withoutAuth_returns4xx_andDoesNotCallService() throws Exception {
        mockMvc.perform(delete("/bugnbass/api/admin/orders/delete/{id}", 7L)
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(adminOrderService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteOrder_withUserRole_returns403_andDoesNotCallService() throws Exception {
        mockMvc.perform(delete("/bugnbass/api/admin/orders/delete/{id}", 7L)
                        .with(csrf()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminOrderService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteOrder_withAdmin_returns204_andCallsService() throws Exception {
        mockMvc.perform(delete("/bugnbass/api/admin/orders/delete/{id}", 7L)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(adminOrderService).deleteOrder(7L);
        verifyNoMoreInteractions(adminOrderService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteOrder_withoutCsrf_returns403_andDoesNotCallService() throws Exception {
        mockMvc.perform(delete("/bugnbass/api/admin/orders/delete/{id}", 7L))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminOrderService);
    }

    // ---------- HELPER ----------

    private static OrderDto validOrderDto() {
        return new OrderDto(
                1L,
                "ORD-123",
                1000,
                List.of(new OrderItemDto(1L, "Piano", 2, 500)),
                LocalDate.of(2026, 2, 11),
                LocalDate.of(2026, 2, 15),
                OrderStatus.RECEIVED,
                "Test Street 1",
                PaymentMethod.PAYPAL,
                1234,
                "Max",
                "Mustermann"
        );
    }
}
