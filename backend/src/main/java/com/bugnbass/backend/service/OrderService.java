package com.bugnbass.backend.service;


import com.bugnbass.backend.dto.OrderDTO;
import com.bugnbass.backend.dto.OrderItemDTO;
import com.bugnbass.backend.exceptions.UserNotFoundException;
import com.bugnbass.backend.model.Order;
import com.bugnbass.backend.model.OrderItem;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model._interface.IBaseUser;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.model.enums.UserRole;
import com.bugnbass.backend.repository.OrderItemRepository;
import com.bugnbass.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final ProductService productService;
    private final UserService userService;

    private OrderDTO mapToDTO(Order order) {
        return new OrderDTO(
                order.getUser().getEmail(),
                order.getUser().getPhoneNumber(),
                "",
                order.getShippingAddress(),
                order.getTotalOrderPrice().doubleValue(),
                order.getOrderItems().stream().map(item ->
                        new OrderItemDTO(
                                String.valueOf(item.getProduct().getId()),
                                item.getQuantity(),
                                item.getPrice().doubleValue()
                        )
                ).toList()
        );
    }


    public OrderStatus createOrder(OrderDTO dto) {

        User user = userService.findCustomerByEmail(dto.customerEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + dto.customerEmail()));

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.RECEIVED);
        order.setOrderedDate(LocalDate.now());
        order.setDeliveryDate(LocalDate.now().plusWeeks(2));
        order.setShippingAddress(dto.shippingAddress());
        order.setOrderNumber(generateOrderNumber());

        List<OrderItem> items = dto.orderItems().stream().map(itemDTO -> {

            Product product = productService.getProduct(itemDTO.productId());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDTO.quantity());
            item.setPrice(BigDecimal.valueOf(product.getPrice()));

            return item;
        }).toList();

        order.setOrderItems(items);

        BigDecimal totalPrice = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalOrderPrice(totalPrice);

        orderRepo.save(order);
        orderItemRepo.saveAll(items);

        return order.getOrderStatus();
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis();
    }

    public OrderDTO getOrderById(Long id, String requesterEmail) {

        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getUser().getEmail().equals(requesterEmail)) {
            return mapToDTO(order);
        }

        IBaseUser baseUser = userService.findByEmail(requesterEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (baseUser.getRole() == UserRole.ROLE_ADMIN) {
            return mapToDTO(order);
        }


        throw new RuntimeException("Access denied");
    }

    public List<OrderDTO> getOrdersByCustomer(String email) {

        User user = userService.findCustomerByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));

        List<Order> orders = orderRepo.findByUser(user);

        return orders.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<OrderDTO> getAllOrders() {

        List<Order> orders = orderRepo.findAll();

        return orders.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public OrderStatus updateOrderStatus(Long id, OrderStatus newStatus) {

        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(newStatus);
        orderRepo.save(order);

        return newStatus;
    }

    public OrderStatus cancelOrder(Long id, String email) {

        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You can only cancel your own orders");
        }

        if (order.getOrderStatus() == OrderStatus.CANCELED) {
            throw new RuntimeException("Order is already canceled");
        }

        if (order.getOrderStatus() == OrderStatus.SHIPPING ||
                order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Delivered or shipped orders cannot be canceled");
        }

        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepo.save(order);

        return OrderStatus.CANCELED;
    }

    public void deleteOrder(Long id) {

        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        orderRepo.delete(order);
    }






}
