package com.bugnbass.backend.dto;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.model.enums.StockStatus;
import java.util.List;


public record ProductResponseDTO(
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
        List<ImageDTO> images
) {
    public static ProductResponseDTO fromEntity(com.bugnbass.backend.model.Product product) {
        List<ImageDTO> imageDTOs = product.getImages().stream()
                .map(ImageDTO::fromEntity)
                .toList();

        return new ProductResponseDTO(
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
                imageDTOs
        );
    }
}