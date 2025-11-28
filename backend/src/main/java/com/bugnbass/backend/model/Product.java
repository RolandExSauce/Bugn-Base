package com.bugnbass.backend.model;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.model.enums.StockStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "product_id", updatable = false)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private String description;

    private Double price;

    @Column(name = "shipping_cost")
    private int shippingCost;

    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_status")
    private StockStatus stockStatus;

    @Column(name = "shipping_time")
    private Integer shippingTime;

    private Boolean active;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Image> images = new ArrayList<>();
}
