package com.bugnbass.backend.dto;

import jakarta.validation.constraints.*;

public record ProductDTO(
        @NotNull
        @Size(max = 50, message = "Der Name ist zu lang.")
        String name,
        @NotNull
        String category,
        @Size(max = 500, message = "Die Beschreibung ist zu lang.")
        String description,
        @Positive(message = "Der Preis muss positiv sein")
        Integer price,
        @Positive(message = "Die Versandkosten müssen positiv sein")
        Integer shippingCost,
        @Size(max = 25, message = "Der Hersteller ist zu lang.")
        String brand,
        Boolean stockStatus,
        @Min(1)
        @Max(5)
        Integer shippingTime,
        Boolean active
) {}
