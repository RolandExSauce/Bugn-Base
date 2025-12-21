package com.bugnbass.backend.model;

import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.model.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Entity representing a customer's order.
 * Contains details about order items, status, payment, and delivery.
 */
@Table(name = "orders")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    /**
     * Unique identifier for the order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique order number.
     */
    @Column(unique = true)
    private String orderNumber;

    /**
     * User who placed the order.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Total price of the order.
     */
    @Column(nullable = false)
    private Integer totalOrderPrice;

    /**
     * List of order items included in this order.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * Date when the order was placed.
     */
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate orderedDate;

    /**
     * Expected delivery date for the order.
     */
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate deliveryDate;

    /**
     * Current status of the order (e.g., RECEIVED, CANCELED).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    /**
     * Shipping address for the order.
     */
    @Column(nullable = false)
    private String shippingAddress;

    /**
     * Payment method chosen for the order.
     */
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    /**
     * Full name of the recipient for delivery purposes.
     */
    @Column(name = "delivery_fullname")
    private String deliveryFullname;

    /**
     * Postcode for delivery.
     */
    @Column(name = "delivery_postcode")
    private Integer deliveryPostcode;
}
