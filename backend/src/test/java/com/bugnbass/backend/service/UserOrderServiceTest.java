package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.OrderDto;
import com.bugnbass.backend.dto.OrderItemDto;
import com.bugnbass.backend.exceptions.UserNotFoundException;
import com.bugnbass.backend.mappers.OrderMapper;
import com.bugnbass.backend.model.Order;
import com.bugnbass.backend.model.OrderItem;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.model.enums.PaymentMethod;
import com.bugnbass.backend.repository.OrderItemRepository;
import com.bugnbass.backend.repository.OrderRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserOrderServiceTest {

    @Mock
    OrderRepository orderRepo;

    @Mock
    OrderItemRepository orderItemRepo;

    @Mock
    ProductService productService;

    @Mock
    UserService userService;

    @Mock
    OrderMapper orderMapper;

    @InjectMocks
    UserOrderService orderService;

    private final String email = "user@test.com";

    @BeforeEach
    void setUpSecurityContext() {
        SecurityContextHolder.getContext()
                .setAuthentication(new TestingAuthenticationToken(email, "pw"));
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    // -------------------- createOrder --------------------

    @Test
    void createOrder_createsOrder_savesOrderAndItems_returnsReceived() {
        LocalDate today = LocalDate.now();

        User user = mock(User.class);
        when(userService.findCustomerByEmail(email)).thenReturn(Optional.of(user));

        OrderItemDto i1 = new OrderItemDto(10L, 2, 0);
        OrderItemDto i2 = new OrderItemDto(11L, 1, 0);

        OrderDto dto = new OrderDto(
                null, null, null,
                List.of(i1, i2),
                null, null, null,
                "My Street 1",
                PaymentMethod.PAYPAL,
                "Max Mustermann",
                1010
        );

        Product p1 = mock(Product.class);
        when(p1.getPrice()).thenReturn(100);
        Product p2 = mock(Product.class);
        when(p2.getPrice()).thenReturn(50);

        when(productService.getProduct(10L)).thenReturn(p1);
        when(productService.getProduct(11L)).thenReturn(p2);

        OrderStatus status = orderService.createOrder(dto);

        assertThat(status).isEqualTo(OrderStatus.RECEIVED);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepo).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();

        assertThat(savedOrder.getUser()).isSameAs(user);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.RECEIVED);
        assertThat(savedOrder.getShippingAddress()).isEqualTo("My Street 1");
        assertThat(savedOrder.getDeliveryFullname()).isEqualTo("Max Mustermann");
        assertThat(savedOrder.getDeliveryPostcode()).isEqualTo(1010);
        assertThat(savedOrder.getPaymentMethod()).isEqualTo(PaymentMethod.PAYPAL);
        assertThat(savedOrder.getOrderedDate()).isEqualTo(today);
        assertThat(savedOrder.getDeliveryDate()).isEqualTo(today.plusWeeks(2));
        assertThat(savedOrder.getTotalOrderPrice()).isEqualTo(250);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<OrderItem>> itemsCaptor = ArgumentCaptor.forClass((Class) List.class);
        verify(orderItemRepo).saveAll(itemsCaptor.capture());
        List<OrderItem> items = itemsCaptor.getValue();

        assertThat(items).hasSize(2);

        assertThat(items.get(0).getOrder()).isSameAs(savedOrder);
        assertThat(items.get(0).getProduct()).isSameAs(p1);
        assertThat(items.get(0).getQuantity()).isEqualTo(2);
        assertThat(items.get(0).getPrice()).isEqualTo(100);

        assertThat(items.get(1).getOrder()).isSameAs(savedOrder);
        assertThat(items.get(1).getProduct()).isSameAs(p2);
        assertThat(items.get(1).getQuantity()).isEqualTo(1);
        assertThat(items.get(1).getPrice()).isEqualTo(50);

        verifyNoInteractions(orderMapper);
        verifyNoMoreInteractions(orderRepo, orderItemRepo, productService, userService);
    }

    @Test
    void createOrder_throwsUserNotFound_whenNoAuthenticatedUser() {
        when(userService.findCustomerByEmail(email)).thenReturn(Optional.empty());

        OrderDto dto = new OrderDto(
                null, null, null,
                List.of(),
                null, null, null,
                "addr",
                PaymentMethod.PAYPAL,
                "Name",
                1010
        );

        assertThatThrownBy(() -> orderService.createOrder(dto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(email);

        verify(userService).findCustomerByEmail(email);
        verifyNoInteractions(orderRepo, orderItemRepo, productService, orderMapper);
        verifyNoMoreInteractions(userService);
    }

    // -------------------- getOrderById --------------------

    @Test
    void getOrderById_returnsDto_whenOwner() {
        User user = mock(User.class);
        when(userService.findCustomerByEmail(email)).thenReturn(Optional.of(user));

        Order order = mock(Order.class);
        when(orderRepo.findById(5L)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(user);

        OrderDto mapped = mock(OrderDto.class);
        when(orderMapper.toDto(order)).thenReturn(mapped);

        OrderDto result = orderService.getOrderById(5L);

        assertThat(result).isSameAs(mapped);

        verify(orderRepo).findById(5L);
        verify(userService).findCustomerByEmail(email);
        verify(orderMapper).toDto(order);

        verifyNoMoreInteractions(orderRepo, userService, orderMapper);
        verifyNoInteractions(orderItemRepo, productService);
    }

    @Test
    void getOrderById_throws_whenOrderNotFound() {
        when(orderRepo.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderById(5L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Order not found");

        verify(orderRepo).findById(5L);
        verifyNoMoreInteractions(orderRepo);
        verifyNoInteractions(userService, orderMapper, orderItemRepo, productService);
    }

    @Test
    void getOrderById_throwsAccessDenied_whenNotOwner() {
        User authUser = mock(User.class);
        when(userService.findCustomerByEmail(email)).thenReturn(Optional.of(authUser));

        User owner = mock(User.class);

        Order order = mock(Order.class);
        when(orderRepo.findById(5L)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(owner);

        assertThatThrownBy(() -> orderService.getOrderById(5L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Access denied");

        verify(orderRepo).findById(5L);
        verify(userService).findCustomerByEmail(email);
        verifyNoInteractions(orderMapper);
        verifyNoMoreInteractions(orderRepo, userService);
    }

    // -------------------- getOrdersByCustomer --------------------

    @Test
    void getOrdersByCustomer_returnsMappedList() {
        User user = mock(User.class);
        when(userService.findCustomerByEmail(email)).thenReturn(Optional.of(user));

        Order o1 = mock(Order.class);
        Order o2 = mock(Order.class);
        when(orderRepo.findByUser(user)).thenReturn(List.of(o1, o2));

        OrderDto d1 = mock(OrderDto.class);
        OrderDto d2 = mock(OrderDto.class);
        when(orderMapper.toDto(o1)).thenReturn(d1);
        when(orderMapper.toDto(o2)).thenReturn(d2);

        List<OrderDto> result = orderService.getOrdersByCustomer();

        assertThat(result).containsExactly(d1, d2);

        verify(userService).findCustomerByEmail(email);
        verify(orderRepo).findByUser(user);
        verify(orderMapper).toDto(o1);
        verify(orderMapper).toDto(o2);

        verifyNoMoreInteractions(userService, orderRepo, orderMapper);
        verifyNoInteractions(orderItemRepo, productService);
    }

    @Test
    void getOrdersByCustomer_throwsUserNotFound_whenNoAuthenticatedUser() {
        when(userService.findCustomerByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrdersByCustomer())
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(email);

        verify(userService).findCustomerByEmail(email);
        verifyNoInteractions(orderRepo, orderMapper, orderItemRepo, productService);
        verifyNoMoreInteractions(userService);
    }

    // -------------------- cancelOrder --------------------

    @Test
    void cancelOrder_setsCanceled_whenOwnerAndValidState() {
        User user = mock(User.class);
        when(userService.findCustomerByEmail(email)).thenReturn(Optional.of(user));

        Order order = mock(Order.class);
        when(orderRepo.findById(7L)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(user);
        when(order.getOrderStatus()).thenReturn(OrderStatus.RECEIVED);

        OrderStatus result = orderService.cancelOrder(7L);

        assertThat(result).isEqualTo(OrderStatus.CANCELED);
        verify(order).setOrderStatus(OrderStatus.CANCELED);
        verify(orderRepo).save(order);

        verify(orderRepo).findById(7L);
        verify(userService).findCustomerByEmail(email);
        verifyNoMoreInteractions(orderRepo, userService);
        verifyNoInteractions(orderMapper, orderItemRepo, productService);
    }

    @Test
    void cancelOrder_throws_whenOrderNotFound() {
        when(orderRepo.findById(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.cancelOrder(7L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Order not found");

        verify(orderRepo).findById(7L);
        verifyNoMoreInteractions(orderRepo);
        verifyNoInteractions(userService, orderMapper, orderItemRepo, productService);
    }

    @Test
    void cancelOrder_throws_whenNotOwner() {
        User user = mock(User.class);
        when(userService.findCustomerByEmail(email)).thenReturn(Optional.of(user));

        User owner = mock(User.class);

        Order order = mock(Order.class);
        when(orderRepo.findById(7L)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(owner);

        assertThatThrownBy(() -> orderService.cancelOrder(7L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("only cancel your own orders");

        verify(orderRepo).findById(7L);
        verify(userService).findCustomerByEmail(email);
        verifyNoMoreInteractions(orderRepo, userService);
        verifyNoInteractions(orderMapper, orderItemRepo, productService);
    }

    @Test
    void cancelOrder_throws_whenAlreadyCanceled() {
        User user = mock(User.class);
        when(userService.findCustomerByEmail(email)).thenReturn(Optional.of(user));

        Order order = mock(Order.class);
        when(orderRepo.findById(7L)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(user);
        when(order.getOrderStatus()).thenReturn(OrderStatus.CANCELED);

        assertThatThrownBy(() -> orderService.cancelOrder(7L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("already canceled");

        verify(orderRepo).findById(7L);
        verify(userService).findCustomerByEmail(email);
        verify(orderRepo, never()).save(any());
        verifyNoMoreInteractions(orderRepo, userService);
    }

    @Test
    void cancelOrder_throws_whenShipping() {
        User user = mock(User.class);
        when(userService.findCustomerByEmail(email)).thenReturn(Optional.of(user));

        Order order = mock(Order.class);
        when(orderRepo.findById(7L)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(user);
        when(order.getOrderStatus()).thenReturn(OrderStatus.SHIPPING);

        assertThatThrownBy(() -> orderService.cancelOrder(7L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("cannot be canceled");

        verify(orderRepo).findById(7L);
        verify(userService).findCustomerByEmail(email);
        verify(orderRepo, never()).save(any());
        verifyNoMoreInteractions(orderRepo, userService);
    }

    @Test
    void cancelOrder_throws_whenDelivered() {
        User user = mock(User.class);
        when(userService.findCustomerByEmail(email)).thenReturn(Optional.of(user));

        Order order = mock(Order.class);
        when(orderRepo.findById(7L)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(user);
        when(order.getOrderStatus()).thenReturn(OrderStatus.DELIVERED);

        assertThatThrownBy(() -> orderService.cancelOrder(7L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("cannot be canceled");

        verify(orderRepo).findById(7L);
        verify(userService).findCustomerByEmail(email);
        verify(orderRepo, never()).save(any());
        verifyNoMoreInteractions(orderRepo, userService);
    }

    // -------------------- returnOrder --------------------

    @Test
    void returnOrder_setsReturned_onlyIfDelivered() {
        User user = mock(User.class);
        when(userService.findCustomerByEmail(email)).thenReturn(Optional.of(user));

        Order order = mock(Order.class);
        when(orderRepo.findById(9L)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(user);
        when(order.getOrderStatus()).thenReturn(OrderStatus.DELIVERED);

        OrderStatus result = orderService.returnOrder(9L);

        assertThat(result).isEqualTo(OrderStatus.RETURNED);
        verify(order).setOrderStatus(OrderStatus.RETURNED);
        verify(orderRepo).save(order);

        verify(orderRepo).findById(9L);
        verify(userService).findCustomerByEmail(email);
        verifyNoMoreInteractions(orderRepo, userService);
        verifyNoInteractions(orderMapper, orderItemRepo, productService);
    }

    @Test
    void returnOrder_throws_whenNotDelivered() {
        User user = mock(User.class);
        when(userService.findCustomerByEmail(email)).thenReturn(Optional.of(user));

        Order order = mock(Order.class);
        when(orderRepo.findById(9L)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(user);
        when(order.getOrderStatus()).thenReturn(OrderStatus.RECEIVED);

        assertThatThrownBy(() -> orderService.returnOrder(9L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Only delivered orders can be returned");

        verify(orderRepo).findById(9L);
        verify(userService).findCustomerByEmail(email);
        verify(orderRepo, never()).save(any());
        verifyNoMoreInteractions(orderRepo, userService);
    }

    @Test
    void returnOrder_throws_whenOrderNotFound() {
        when(orderRepo.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.returnOrder(9L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Order not found");

        verify(orderRepo).findById(9L);
        verifyNoMoreInteractions(orderRepo);
        verifyNoInteractions(userService, orderMapper, orderItemRepo, productService);
    }

    @Test
    void returnOrder_throws_whenNotOwner() {
        User user = mock(User.class);
        when(userService.findCustomerByEmail(email)).thenReturn(Optional.of(user));

        User owner = mock(User.class);

        Order order = mock(Order.class);
        when(orderRepo.findById(9L)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(owner);

        assertThatThrownBy(() -> orderService.returnOrder(9L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("only return your own orders");

        verify(orderRepo).findById(9L);
        verify(userService).findCustomerByEmail(email);
        verifyNoMoreInteractions(orderRepo, userService);
        verifyNoInteractions(orderMapper, orderItemRepo, productService);
    }
}
