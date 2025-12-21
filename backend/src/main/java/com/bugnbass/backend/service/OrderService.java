package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.OrderDto;
import com.bugnbass.backend.exceptions.UserNotFoundException;
import com.bugnbass.backend.mappers.OrderMapper;
import com.bugnbass.backend.model.Order;
import com.bugnbass.backend.model.OrderItem;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.model.enums.UserRole;
import com.bugnbass.backend.repository.OrderItemRepository;
import com.bugnbass.backend.repository.OrderRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service for managing orders, including creation, retrieval, update, cancellation, and deletion.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    /**
     * Repository for CRUD operations on Order entities.
     */
    private final OrderRepository orderRepo;

    /**
     * Repository for CRUD operations on OrderItem entities.
     */
    private final OrderItemRepository orderItemRepo;

    /**
     * Service for product-related operations.
     */
    private final ProductService productService;

    /**
     * Service for user-related operations.
     */
    private final UserService userService;

    /**
     * Mapper to convert between Order entities and OrderDTOs.
     */
    private final OrderMapper orderMapper;

    /**
     * Creates a new order for the authenticated user.
     *
     * @param dto the OrderDTO containing order details
     * @return the status of the created order
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
     * Generates a unique order number based on current time.
     *
     * @return the generated order number
     */
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis();
    }

    /**
     * Retrieves an order by its ID, ensuring the authenticated user or admin has access.
     *
     * @param id the order ID
     * @return the OrderDTO representing the order
     */
    public OrderDto getOrderById(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        User user = getAuthenticatedUserOrAdmin();

        if (!order.getUser().equals(user) && user.getRole() != UserRole.ROLE_ADMIN) {
            throw new RuntimeException("Access denied");
        }

        return orderMapper.toDto(order);
    }

    /**
     * Retrieves orders for the authenticated customer.
     *
     * @return a list of OrderDTOs for the user
     */
    public List<OrderDto> getOrdersByCustomer() {
        User user = getAuthenticatedUser();
        List<Order> orders = orderRepo.findByUser(user);
        return orders.stream().map(orderMapper::toDto).toList();
    }

    /**
     * Cancels an order by its ID.
     *
     * @param id the order ID
     * @return the updated OrderStatus (CANCELED)
     */
    public OrderStatus cancelOrder(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        User user = getAuthenticatedUserOrAdmin();

        if (!order.getUser().equals(user) && user.getRole() != UserRole.ROLE_ADMIN) {
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
     * Marks a delivered order as returned.
     *
     * @param id the order ID
     * @return the updated OrderStatus (RETURNED)
     */
    public OrderStatus returnOrder(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        User user = getAuthenticatedUserOrAdmin();

        if (!order.getUser().equals(user) && user.getRole() != UserRole.ROLE_ADMIN) {
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
     * Retrieves the currently authenticated user.
     *
     * @return the authenticated User
     * @throws UserNotFoundException if user cannot be found
     */
    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findCustomerByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }

    /**
     * Retrieves all orders in the system.
     *
     * @return a list of OrderDTOs for all orders
     */
    public List<OrderDto> getAllOrders() {
        return orderRepo.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    /**
     * Updates an order. Currently, only the order status can be updated.
     *
     * @param orderDto the OrderDTO containing updated information
     * @return the updated OrderDTO
     */
    public OrderDto updateOrder(OrderDto orderDto) {
        Order order = orderRepo.findById(orderDto.id())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(orderDto.orderStatus());
        orderRepo.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Deletes an order by its ID.
     *
     * @param id the order ID
     */
    public void deleteOrder(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        orderRepo.delete(order);
    }

    /**
     * Retrieves the authenticated user or admin.
     * Admin check is handled in the controller layer.
     *
     * @return the authenticated User
     */
    private User getAuthenticatedUserOrAdmin() {
        return getAuthenticatedUser(); // Admins are already checked in controller
    }
}
