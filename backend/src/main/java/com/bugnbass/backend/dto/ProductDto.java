package com.bugnbass.backend.dto;

import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.model.enums.StockStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating or updating a product.
 *
 * @param name         the name of the product (max 50 characters, required)
 * @param category     the category of the product (must be PIANOS, GUITARS, or VIOLINS, required)
 * @param description  the description of the product (max 500 characters)
 * @param price        the price of the product (must be positive)
 * @param shippingCost the shipping cost for the product (must be positive)
 * @param brand        the brand of the product (max 25 characters)
 * @param stockStatus  the stock status of the product (IN_STOCK, OUT_OF_STOCK, LOW_STOCK)
 * @param shippingTime estimated shipping time in days (1-5)
 * @param active       whether the product is active or inactive
 */
public record ProductDto(

    @NotNull
    @Size(max = 50, message = "Der Name ist zu lang.")
    String name,

    @NotNull
    @Pattern(regexp = "PIANOS|GUITARS|VIOLINS", message = "Invalid Product Category")
    ProductCategory category,

    @Size(max = 500, message = "Die Beschreibung ist zu lang.")
    String description,

    @Positive(message = "Der Preis muss positiv sein")
    Integer price,

    @Positive(message = "Die Versandkosten müssen positiv sein")
    Integer shippingCost,

    @Size(max = 25, message = "Der Hersteller ist zu lang.")
    String brand,

    @Pattern(regexp = "IN_STOCK|OUT_OF_STOCK|LOW_STOCK", message = "Invalid Product Category")
    StockStatus stockStatus,

    @Min(1)
    @Max(5)
    Integer shippingTime,

    Boolean active
) {
}
