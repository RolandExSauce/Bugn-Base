package com.bugnbass.backend.service;
import com.bugnbass.backend.dto.OrderDTO;
import com.bugnbass.backend.exceptions.UserNotFoundException;
import com.bugnbass.backend.model.Order;
import com.bugnbass.backend.model.OrderItem;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.repository.OrderItemRepository;
import com.bugnbass.backend.repository.OrderRepository;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class OrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final ProductService productService;
    private final UserService userService;

    public OrderService(
            OrderRepository orderRepo,
            OrderItemRepository orderItemRepo,
            ProductService productService,
            UserService userService
    ) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.productService = productService;
        this.userService = userService;
    };

    @Transactional
    public OrderStatus createOrder(OrderDTO orderDTO) {

        // Check if user exists by email
        Optional<User> userOptional = userService.findUserByEmail(orderDTO.userEmailToMakeOrderFor());

        //Extract the user from Optional first
        User user = userOptional.orElseThrow(() ->
                new IllegalArgumentException("User not found")
        );

        boolean userDataUpdated = false;

        // Check if the email has been updated
        if (!user.getEmail().equals(orderDTO.updatedOrNotUserEmail())) {
            user.setEmail(orderDTO.updatedOrNotUserEmail());
            userDataUpdated = true;
        }

        // Check if the phone number has been updated
        if (!user.getPhoneNumber().equals(orderDTO.updatedOrNotUserPhoneNumber())) {
            user.setPhoneNumber(orderDTO.updatedOrNotUserPhoneNumber());
            userDataUpdated = true;
        }

        // Check if the state and district have been updated
        if (!user.getStateAndDistrict().equals(orderDTO.updatedOrNotStateAndDistrict())) {
            user.setStateAndDistrict(orderDTO.updatedOrNotStateAndDistrict());
            userDataUpdated = true;
        }

        // Check if the shipping address has been updated
        if (!user.getShippingAddress().equals(orderDTO.updatedOrNotShippingAddress())) {
            user.setShippingAddress(orderDTO.updatedOrNotShippingAddress());
            userDataUpdated = true;
        }

        // Save the updated customer details if any changes were made
        if (userDataUpdated) {
            userService.updateCustomer(user); // Fixed variable name from 'customer' to 'user'
        }

        // Create order
        Order order = new Order();

        order.setUser(user);
        order.setUser(user);
        order.setTotalOrderPrice(BigDecimal.valueOf(orderDTO.totalOrderPrice()));
        order.setDeliveryDate(LocalDate.now().plusWeeks(2));
        order.setOrderedDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.RECEIVED);

        Order finalOrder = order;
        List<OrderItem> orderItems = orderDTO.orderItems().stream().map(dto -> {
            Product product = productService.getProduct(dto.productId());

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(dto.quantity());
            orderItem.setPrice(BigDecimal.valueOf(dto.price()));
            orderItem.setOrder(finalOrder);
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order = orderRepo.save(order);
        orderItemRepo.saveAll(orderItems);

        return order.getOrderStatus();
    };

};
