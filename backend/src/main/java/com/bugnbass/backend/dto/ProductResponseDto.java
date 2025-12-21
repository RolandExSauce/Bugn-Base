package com.bugnbass.backend.dto;

import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.model.enums.StockStatus;
import java.util.List;

/**
 * DTO representing a product in responses to the client.
 *
 * @param id           unique identifier of the product
 * @param name         name of the product
 * @param category     category of the product
 * @param description  product description
 * @param price        product price
 * @param shippingCost shipping cost for the product
 * @param brand        brand of the product
 * @param stockStatus  current stock status
 * @param shippingTime estimated shipping time
 * @param active       whether the product is active
 * @param images       list of associated images
 */
public record ProductResponseDto(
        Long id,
        String name,
        ProductCategory category,
        String description,
        Integer price,
        int shippingCost,
        String brand,
        StockStatus stockStatus,
        Integer shippingTime,
        Boolean active,
        List<ImageDto> images
) {
    /**
     * Converts a Product entity to a ProductResponseDTO.
     *
     * @param product the Product entity
     * @return the ProductResponseDTO
     */
    public static ProductResponseDto fromEntity(com.bugnbass.backend.model.Product product) {
        List<ImageDto> imageDtos = product.getImages().stream()
                .map(ImageDto::fromEntity)
                .toList();

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getDescription(),
                product.getPrice(),
                product.getShippingCost(),
                product.getBrand(),
                product.getStockStatus(),
                product.getShippingTime(),
                product.getActive(),
                imageDtos
        );
    }
}
