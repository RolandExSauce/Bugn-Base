package com.bugnbass.backend.model;

import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.model.enums.StockStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Entity representing a product in the store.
 * Contains information such as category, price, brand, stock, and associated images.
 */
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Product {

    /**
     * Unique identifier for the product.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", updatable = false)
    private Long id;

    /**
     * Name of the product.
     */
    private String name;

    /**
     * Category of the product.
     */
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    /**
     * Description of the product.
     */
    private String description;

    /**
     * Price of the product.
     */
    private Integer price;

    /**
     * Shipping cost for the product.
     */
    @Column(name = "shipping_cost")
    private int shippingCost;

    /**
     * Brand of the product.
     */
    private String brand;

    /**
     * Stock status of the product (e.g., IN_STOCK, OUT_OF_STOCK).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "stock_status")
    private StockStatus stockStatus;

    /**
     * Estimated shipping time in days.
     */
    @Column(name = "shipping_time")
    private Integer shippingTime;

    /**
     * Indicates whether the product is active and available for purchase.
     */
    private Boolean active;

    /**
     * List of images associated with the product.
     */
    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Image> images = new ArrayList<>();
}
