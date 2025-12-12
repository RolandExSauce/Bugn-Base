package com.bugnbass.backend.dto;

import com.bugnbass.backend.model.enums.StockStatus;
import jakarta.validation.constraints.*;

/**
 * DTO representing a product for creation or update operations. Includes validation constraints for
 * fields such as name, category, price, shipping, and stock status.
 *
 * @param name the product name, required, max 50 characters
 * @param category the product category, required
 * @param description the product description, max 500 characters
 * @param price the product price, must be positive
 * @param shippingCost the shipping cost, must be positive
 * @param brand the product brand, max 25 characters
 * @param stockStatus the stock status
 * @param shippingTime the shipping time in days, between 1 and 5
 * @param active whether the product is active
 */
public record ProductDTO(
    @NotNull @Size(max = 50, message = "Der Name ist zu lang.") String name,
    @NotNull String category,
    @Size(max = 500, message = "Die Beschreibung ist zu lang.") String description,
    @Positive(message = "Der Preis muss positiv sein") Double price,
    @Positive(message = "Die Versandkosten müssen positiv sein") Integer shippingCost,
    @Size(max = 25, message = "Der Hersteller ist zu lang.") String brand,
    StockStatus stockStatus,
    @Min(1) @Max(5) Integer shippingTime,
    Boolean active) {}
