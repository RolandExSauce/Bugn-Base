package com.bugnbass.backend.dto;

import com.bugnbass.backend.model.enums.ProductCategory;
import java.util.List;

/**
 * DTO representing filters for querying products. Supports filtering by name, category, price
 * range, brands, and pagination.
 *
 * @param name the product name to filter by (optional)
 * @param category the product category to filter by (optional)
 * @param priceMin the minimum price to filter by (optional)
 * @param priceMax the maximum price to filter by (optional)
 * @param brand a list of brands to filter by (optional)
 * @param pageNumber the page number for pagination (optional)
 * @param pageSize the number of items per page for pagination (optional)
 */
public record ProductFilter(
    String name,
    ProductCategory category,
    Integer priceMin,
    Integer priceMax,
    List<String> brand,
    Integer pageNumber,
    Integer pageSize) {}
