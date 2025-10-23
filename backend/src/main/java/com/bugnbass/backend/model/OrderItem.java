package com.bugnbass.backend.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;


@Table(name = "order_items")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Each orderItem is part of one Order, but an order can have many orderItems
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // One orderItem is only associated with one product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    //Number of units of the product that customer has ordered and the price
    @Column(nullable = false)
    private int quantity;
    private BigDecimal price;
};
