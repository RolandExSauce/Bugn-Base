package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.OrderDto;
import com.bugnbass.backend.dto.OrderItemDto;
import com.bugnbass.backend.exceptions.GlobalExceptionHandler;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.model.enums.PaymentMethod;
import com.bugnbass.backend.security.AuthTokenFilter;
import com.bugnbass.backend.service.UserOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserOrderController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = AuthTokenFilter.class
        )
)
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
class UserOrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserOrderService userOrderService;

    // ---------- POST /bugnbass/api/user/orders ----------

    @Test
    void createOrder_withoutAuth_returns4xx_andDoesNotCallService() throws Exception {
        OrderDto dto = validOrderDto();

        mockMvc.perform(post("/bugnbass/api/user/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(userOrderService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void createOrder_withUser_returns201_andReturnsStatus_andPassesDtoToService() throws Exception {
        when(userOrderService.createOrder(any(OrderDto.class))).thenReturn(OrderStatus.RECEIVED);

        OrderDto dto = validOrderDto();

        mockMvc.perform(post("/bugnbass/api/user/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("\"RECEIVED\""));

        ArgumentCaptor<OrderDto> captor = ArgumentCaptor.forClass(OrderDto.class);
        verify(userOrderService).createOrder(captor.capture());

        assertThat(captor.getValue().shippingAddress()).isEqualTo(dto.shippingAddress());
        assertThat(captor.getValue().orderItems()).hasSize(1);

        verifyNoMoreInteractions(userOrderService);
    }

    // ---------- GET /bugnbass/api/user/orders/{id} ----------

    @Test
    void getOrderById_withoutAuth_returns4xx_andDoesNotCallService() throws Exception {
        mockMvc.perform(get("/bugnbass/api/user/orders/{id}", 5))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(userOrderService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getOrderById_withUser_returns200_andJson() throws Exception {
        OrderDto dto = validOrderDto();
        when(userOrderService.getOrderById(5L)).thenReturn(dto);

        mockMvc.perform(get("/bugnbass/api/user/orders/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderItems").isArray())
                .andExpect(jsonPath("$.orderItems.length()").value(1))
                .andExpect(jsonPath("$.deliveryFullname").value(dto.deliveryFullname()))
                .andExpect(jsonPath("$.deliveryPostcode").value(dto.deliveryPostcode()));

        verify(userOrderService).getOrderById(5L);
        verifyNoMoreInteractions(userOrderService);
    }

    // ---------- GET /bugnbass/api/user/orders/customer ----------

    @Test
    void getOrdersForCustomer_withoutAuth_returns4xx_andDoesNotCallService() throws Exception {
        mockMvc.perform(get("/bugnbass/api/user/orders/customer"))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(userOrderService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getOrdersForCustomer_withUser_returns200_andArray() throws Exception {
        when(userOrderService.getOrdersByCustomer()).thenReturn(List.of(validOrderDto()));

        mockMvc.perform(get("/bugnbass/api/user/orders/customer"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(userOrderService).getOrdersByCustomer();
        verifyNoMoreInteractions(userOrderService);
    }

    // ---------- PATCH /bugnbass/api/user/orders/cancel/{id} ----------

    @Test
    void cancelOrder_withoutAuth_returns4xx_andDoesNotCallService() throws Exception {
        mockMvc.perform(patch("/bugnbass/api/user/orders/cancel/{id}", 7)
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(userOrderService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void cancelOrder_withUser_returns200_andStatus() throws Exception {
        when(userOrderService.cancelOrder(7L)).thenReturn(OrderStatus.CANCELED);

        mockMvc.perform(patch("/bugnbass/api/user/orders/cancel/{id}", 7)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("\"CANCELED\""));

        verify(userOrderService).cancelOrder(7L);
        verifyNoMoreInteractions(userOrderService);
    }

    // ---------- PATCH /bugnbass/api/user/orders/{id}/return ----------

    @Test
    void returnOrder_withoutAuth_returns4xx_andDoesNotCallService() throws Exception {
        mockMvc.perform(patch("/bugnbass/api/user/orders/{id}/return", 9)
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(userOrderService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void returnOrder_withUser_returns200_andStatus() throws Exception {
        when(userOrderService.returnOrder(9L)).thenReturn(OrderStatus.RETURNED);

        mockMvc.perform(patch("/bugnbass/api/user/orders/{id}/return", 9)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("\"RETURNED\""));

        verify(userOrderService).returnOrder(9L);
        verifyNoMoreInteractions(userOrderService);
    }

    // -------------------- HELPERS --------------------

    private static OrderDto validOrderDto() {
        return new OrderDto(
                null,
                "ORD-123",
                1000,
                List.of(new OrderItemDto(1L, 2, 500)),
                LocalDate.of(2026, 2, 11),
                LocalDate.of(2026, 2, 15),
                OrderStatus.RECEIVED,
                "Test Street 1",
                PaymentMethod.PAYPAL,
                "Max Mustermann",
                1234
        );
    }
}
