package com.bugnbass.backend.dto;
import com.bugnbass.backend.model.enums.ProductCategory;
import java.util.List;

public record ProductFilter (
        String name,
        ProductCategory category,
        Double priceMin,
        Double priceMax,
        List<String> brand,
        Integer pageNumber,
        Integer pageSize
) {}

