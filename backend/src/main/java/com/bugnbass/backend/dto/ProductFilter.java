package com.bugnbass.backend.dto;

import com.bugnbass.backend.model.enums.ProductCategory;
import java.util.List;

/**
 * DTO used for filtering products in search or listing operations.
 *
 * @param name       optional product name to filter by (partial match)
 * @param category   optional product category to filter by
 * @param priceMin   optional minimum price filter
 * @param priceMax   optional maximum price filter
 * @param brand      optional list of brands to filter by
 * @param pageNumber optional page number for pagination (0-based)
 * @param pageSize   optional page size for pagination
 */
public record ProductFilter(
    String name,
    ProductCategory category,
    Integer priceMin,
    Integer priceMax,
    List<String> brand,
    Integer pageNumber,
    Integer pageSize
) {
}
