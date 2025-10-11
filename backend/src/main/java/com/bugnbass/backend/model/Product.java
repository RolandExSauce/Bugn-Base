package com.bugnbass.backend.model;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity(name = "Product")
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {

    @Id
    @Column(
            name= "product_id",
            updatable = false
    )
    private String productId = UUID.randomUUID().toString();

    private String name;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private String description;

    private Integer price;

    @Column(
            name = "shipping_cost"
    )
    private int shippingCost;

    private String brand;

    @Column(
            name = "stock_status"
    )
    private Boolean inStock;

    @Column(
            name = "shipping_time"
    )
    private Integer shippingTime;

    private Boolean active;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Image> images = new ArrayList<>();
}
