package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.OrderDto;
import com.bugnbass.backend.dto.OrderItemDto;
import com.bugnbass.backend.exceptions.GlobalExceptionHandler;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.model.enums.PaymentMethod;
import com.bugnbass.backend.security.AuthTokenFilter;
import com.bugnbass.backend.service.OrderService;
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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.test.context.support.WithMockUser;

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

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean OrderService orderService;

    @Test
    void createOrder_withoutAuth_returns401or403_andDoesNotCallService() throws Exception {
        // csrf dazu, damit nicht CSRF der Grund für 403 ist
        OrderDto dto = validOrderDto();

        mockMvc.perform(post("/bugnbass/api/user/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(orderService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void createOrder_withUser_returns201_andReturnsStatus_andPassesDtoToService() throws Exception {
        when(orderService.createOrder(any())).thenReturn(OrderStatus.RECEIVED);

        OrderDto dto = validOrderDto();

        mockMvc.perform(post("/bugnbass/api/user/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("\"RECEIVED\""));

        ArgumentCaptor<OrderDto> captor = ArgumentCaptor.forClass(OrderDto.class);
        verify(orderService).createOrder(captor.capture());
        assertThat(captor.getValue().shippingAddress()).isEqualTo(dto.shippingAddress());
        assertThat(captor.getValue().orderItems()).hasSize(1);

        verifyNoMoreInteractions(orderService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getOrderById_withUser_returns200_andJson() throws Exception {
        OrderDto dto = validOrderDto();
        when(orderService.getOrderById(5L)).thenReturn(dto);

        mockMvc.perform(get("/bugnbass/api/user/orders/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderItems").isArray())
                .andExpect(jsonPath("$.orderItems.length()").value(1))
                .andExpect(jsonPath("$.deliveryFullname").value(dto.deliveryFullname()))
                .andExpect(jsonPath("$.deliveryPostcode").value(dto.deliveryPostcode()));

        verify(orderService).getOrderById(5L);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getOrdersForCustomer_withUser_returns200_andArray() throws Exception {
        when(orderService.getOrdersByCustomer()).thenReturn(List.of(validOrderDto()));

        mockMvc.perform(get("/bugnbass/api/user/orders/customer"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(orderService).getOrdersByCustomer();
        verifyNoMoreInteractions(orderService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void cancelOrder_withUser_returns200_andStatus() throws Exception {
        when(orderService.cancelOrder(7L)).thenReturn(OrderStatus.CANCELED);

        mockMvc.perform(patch("/bugnbass/api/user/orders/cancel/{id}", 7)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("\"CANCELED\""));

        verify(orderService).cancelOrder(7L);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void returnOrder_withUser_returns200_andStatus() throws Exception {
        when(orderService.returnOrder(9L)).thenReturn(OrderStatus.RETURNED);

        mockMvc.perform(patch("/bugnbass/api/user/orders/{id}/return", 9)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("\"RETURNED\""));

        verify(orderService).returnOrder(9L);
        verifyNoMoreInteractions(orderService);
    }

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
