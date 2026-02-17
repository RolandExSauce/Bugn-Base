package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.OrderDto;
import com.bugnbass.backend.exceptions.UserNotFoundException;
import com.bugnbass.backend.mappers.OrderMapper;
import com.bugnbass.backend.model.Order;
import com.bugnbass.backend.model.OrderItem;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.repository.OrderItemRepository;
import com.bugnbass.backend.repository.OrderRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for customer-facing order operations.
 *
 * <p>Business rules:
 * <ul>
 *   <li>If status is RECEIVED: user can cancel -> status becomes CANCELED</li>
 *   <li>If status is SHIPPING: user cannot perform actions (no cancel, no return)</li>
 *   <li>If status is DELIVERED: user can return -> status becomes RETURNED</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserOrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final ProductService productService;
    private final UserService userService;
    private final OrderMapper orderMapper;

    /**
     * Creates a new order for the authenticated user.
     */
    public OrderStatus createOrder(OrderDto dto) {
        User user = getAuthenticatedUser();

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.RECEIVED);
        order.setOrderedDate(LocalDate.now());
        order.setDeliveryDate(LocalDate.now().plusWeeks(2));
        order.setShippingAddress(dto.shippingAddress());
        order.setOrderNumber(generateOrderNumber());
        order.setDeliveryPostcode(dto.deliveryPostcode());
        order.setPaymentMethod(dto.paymentMethod());

        List<OrderItem> items = dto.orderItems().stream().map(itemDTO -> {
            Product product = productService.getProduct(itemDTO.productId());
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDTO.quantity());
            item.setPrice(product.getPrice());
            return item;
        }).toList();

        order.setOrderItems(items);

        int totalPrice = items.stream()
                .mapToInt(i -> i.getPrice() * i.getQuantity())
                .sum();
        order.setTotalOrderPrice(totalPrice);

        orderRepo.save(order);
        orderItemRepo.saveAll(items);

        return order.getOrderStatus();
    }

    /**
     * Retrieves a specific order belonging to the authenticated user.
     */
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        User user = getAuthenticatedUser();

        Order order = orderRepo.findByIdWithItemsAndProducts(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().equals(user)) {
            throw new RuntimeException("Access denied");
        }

        return orderMapper.toDto(order);
    }


    /**
     * Retrieves all orders of the authenticated user.
     */
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByCustomer() {
        User user = getAuthenticatedUser();

        return orderRepo.findByUserWithItemsAndProducts(user)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    /**
     * Cancels an order.
     *
     * <p>Allowed only when the current status is RECEIVED.
     * After cancel, status becomes CANCELED.
     */
    public OrderStatus cancelOrder(Long id) {
        User user = getAuthenticatedUser();

        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().equals(user)) {
            throw new RuntimeException("You can only cancel your own orders");
        }

        if (order.getOrderStatus() != OrderStatus.RECEIVED) {
            throw new RuntimeException(
                    "Order cannot be canceled in status: " + order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepo.save(order);
        return OrderStatus.CANCELED;
    }

    /**
     * Returns an order.
     *
     * <p>Allowed only when the current status is DELIVERED.
     * After return, status becomes RETURNED.
     */
    public OrderStatus returnOrder(Long id) {
        User user = getAuthenticatedUser();

        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().equals(user)) {
            throw new RuntimeException("You can only return your own orders");
        }

        if (order.getOrderStatus() != OrderStatus.DELIVERED) {
            throw new RuntimeException(
                    "Order cannot be returned in status: " + order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.RETURNED);
        orderRepo.save(order);
        return OrderStatus.RETURNED;
    }

    /**
     * Generates a unique order number.
     */
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis();
    }

    /**
     * Resolves the authenticated user.
     */
    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userService.findCustomerByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }
}
