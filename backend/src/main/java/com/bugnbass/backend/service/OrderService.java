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
    }

    @Transactional
    public OrderStatus createOrder(OrderDTO orderDTO) {

        // Find customer by email (new field name)
        var user = userService
                .findByEmail(orderDTO.customerEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // ❗ NO USER INFO UPDATE HERE — this DTO no longer handles updates.
        // The shipping info below belongs only to the ORDER, not the user profile.

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setTotalOrderPrice(BigDecimal.valueOf(orderDTO.totalOrderPrice()));
        order.setDeliveryDate(LocalDate.now().plusWeeks(2));
        order.setOrderedDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.RECEIVED);

        // Set shipping information inside Order (if your Order entity supports it)
        order.setShippingPhone(orderDTO.customerPhoneNumber());
        order.setShippingStateAndDistrict(orderDTO.shippingStateAndDistrict());
        order.setShippingAddress(orderDTO.shippingAddress());

        Order savedParent = order;

        // Map order items
        List<OrderItem> orderItems = orderDTO.orderItems().stream()
                .map(dto -> {
                    Product product = productService.getProduct(dto.productId());

                    OrderItem item = new OrderItem();
                    item.setProduct(product);
                    item.setQuantity(dto.quantity());
                    item.setPrice(BigDecimal.valueOf(dto.price()));
                    item.setOrder(savedParent);
                    return item;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        // Save order + items
        orderRepo.save(order);
        orderItemRepo.saveAll(orderItems);

        return order.getOrderStatus();
    }
}
