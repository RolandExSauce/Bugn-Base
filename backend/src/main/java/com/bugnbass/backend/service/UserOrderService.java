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
 * <p>Allows authenticated users to create orders, view their own orders,
 * cancel eligible orders, and return delivered orders.
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
        order.setDeliveryFullname(dto.deliveryFullname());
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
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        User user = getAuthenticatedUser();

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

        return orderRepo.findByUser(user)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    /**
     * Cancels an order if allowed.
     */
    public OrderStatus cancelOrder(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        User user = getAuthenticatedUser();

        if (!order.getUser().equals(user)) {
            throw new RuntimeException("You can only cancel your own orders");
        }

        if (order.getOrderStatus() == OrderStatus.CANCELED) {
            throw new RuntimeException("Order is already canceled");
        }

        if (order.getOrderStatus() == OrderStatus.SHIPPING
                || order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Delivered or shipped orders cannot be canceled");
        }

        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepo.save(order);

        return OrderStatus.CANCELED;
    }

    /**
     * Returns a delivered order.
     */
    public OrderStatus returnOrder(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        User user = getAuthenticatedUser();

        if (!order.getUser().equals(user)) {
            throw new RuntimeException("You can only return your own orders");
        }

        if (order.getOrderStatus() != OrderStatus.DELIVERED) {
            throw new RuntimeException("Only delivered orders can be returned");
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
