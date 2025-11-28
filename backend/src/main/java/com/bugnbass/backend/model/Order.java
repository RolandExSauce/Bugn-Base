package com.bugnbass.backend.model;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.model.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "orders")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String orderNumber;

    //user can have multiple orders
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal totalOrderPrice; //store total price of order

    // Order can have multiple order items, orphanRemoval = true means if parent has no reference remove child entity
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate orderedDate; //Date when product was ordered

    // For the date that the order will be delivered, we'll just go with date now + 2 weeks
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate deliveryDate;

    //Track status of order, e.g. RECEIVED when order is created or CANCELED if customer chooses to cancel the order
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
};

