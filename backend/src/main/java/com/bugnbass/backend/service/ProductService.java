package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.ProductFilter;
import com.bugnbass.backend.dto.ProductResponseDto;
import com.bugnbass.backend.exceptions.ProductNotFoundException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.repository.ProductRepository;
import com.bugnbass.backend.repository.ReviewRepository;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Service;

/**
 * Service for publicly accessible product operations, including retrieval and filtering.
 */
@Service
public class ProductService {

    private static final int DEFAULT_PAGE_SIZE = 25;
    private static final int DEFAULT_PAGE_NUMBER = 0;

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    /**
     * Creates a new ProductService with the required repositories.
     *
     * @param productRepository the product repository
     * @param reviewRepository the review repository
     */
    public ProductService(ProductRepository productRepository, ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    /**
     * Retrieves a single active product by its identifier.
     *
     * @param id the product identifier
     * @return the Product entity
     * @throws ProductNotFoundException if the product does not exist or is not active
     */
    public Product getProduct(Long id) {
        return productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    /**
     * Retrieves a list of products matching the given filter criteria.
     * Supports filtering by name, category, price range, brand, minimum stars, and pagination.
     *
     * @param filter the product filter parameters
     * @return a list of filtered ProductResponseDto objects
     */
    public List<ProductResponseDto> getProducts(ProductFilter filter) {

        String nameQuery = normalize(filter.name());
        Integer priceMin = filter.priceMin();
        Integer priceMax = filter.priceMax();
        Integer starsMin = filter.stars();

        int pageSize = sanitizePageSize(filter.pageSize());
        int pageNumber = sanitizePageNumber(filter.pageNumber());
        long offset = (long) pageNumber * pageSize;

        List<String> brands = filter.brand();

        // 1) Produkte filtern (ohne Stars zuerst)
        List<Product> filtered = productRepository.findAll().stream()
                .filter(p -> Boolean.TRUE.equals(p.getActive()))
                .filter(p -> nameQuery == null
                        || (p.getName() != null && normalize(p.getName()).contains(nameQuery)))
                .filter(p -> filter.category() == null
                        || Objects.equals(p.getCategory(), filter.category()))
                .filter(p -> priceMin == null
                        || (p.getPrice() != null && p.getPrice() >= priceMin))
                .filter(p -> priceMax == null
                        || (p.getPrice() != null && p.getPrice() <= priceMax))
                .filter(p -> matchesBrand(brands, p.getBrand()))
                .sorted(Comparator.comparing(Product::getId, Comparator.nullsLast(Long::compareTo)))
                .toList();

        // 2) Ratings für alle gefilterten Produkte in EINEM Query holen (Batch)
        List<Long> productIds = filtered.stream()
                .map(Product::getId)
                .filter(Objects::nonNull)
                .toList();

        Map<Long, RatingStats> statsByProductId = loadRatingStats(productIds);

        // 3) Stars-Filter anwenden (min avg rating)
        if (starsMin != null) {
            filtered = filtered.stream()
                    .filter(p -> {
                        RatingStats s = statsByProductId.get(p.getId());
                        double avg = (s == null || s.avg == null) ? 0.0 : s.avg;
                        return avg >= starsMin;
                    })
                    .toList();
        }

        // 4) Paging + Mapping (inkl. avg + count)
        return filtered.stream()
                .skip(offset)
                .limit(pageSize)
                .map(p -> {
                    RatingStats s = statsByProductId.get(p.getId());
                    double avg = (s == null || s.avg == null) ? 0.0 : s.avg;
                    long cnt = (s == null) ? 0L : s.count;
                    return ProductResponseDto.fromEntity(p, avg, cnt);
                })
                .toList();
    }

    // -------------------- helpers --------------------

    /**
     * Loads rating statistics (average rating and count) for a batch of product IDs.
     *
     * @param productIds the product identifiers
     * @return a map keyed by product ID containing rating statistics
     */
    private Map<Long, RatingStats> loadRatingStats(List<Long> productIds) {
        Map<Long, RatingStats> map = new HashMap<>();
        if (productIds == null || productIds.isEmpty()) {
            return map;
        }

        // rows: [productId, avgRating(Double), count(Long)]
        List<Object[]> rows = reviewRepository.findRatingStatsByProductIds(productIds);

        for (Object[] row : rows) {
            Long productId = (Long) row[0];
            Double avg = (Double) row[1];
            Long count = (Long) row[2];

            map.put(productId, new RatingStats(avg, count != null ? count : 0L));
        }
        return map;
    }

    /**
     * Ensures the page size is valid and falls back to a default value if necessary.
     *
     * @param pageSize the requested page size
     * @return a sanitized page size
     */
    private static int sanitizePageSize(Integer pageSize) {
        if (pageSize == null) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.max(1, pageSize);
    }

    /**
     * Ensures the page number is valid and falls back to a default value if necessary.
     *
     * @param pageNumber the requested page number
     * @return a sanitized page number
     */
    private static int sanitizePageNumber(Integer pageNumber) {
        if (pageNumber == null) {
            return DEFAULT_PAGE_NUMBER;
        }
        return Math.max(0, pageNumber);
    }

    /**
     * Normalizes a string for case-insensitive comparisons by trimming and converting to lowercase.
     *
     * @param s the input string
     * @return the normalized string or null if empty
     */
    private static String normalize(String s) {
        if (s == null) {
            return null;
        }
        String trimmed = s.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return trimmed.toLowerCase();
    }

    /**
     * Checks whether a product brand matches any of the requested brands.
     *
     * @param requestedBrands the list of requested brands
     * @param productBrand the product brand
     * @return true if the brand matches or no brand filter is applied
     */
    private static boolean matchesBrand(List<String> requestedBrands, String productBrand) {
        if (requestedBrands == null || requestedBrands.isEmpty()) {
            return true;
        }
        if (productBrand == null || productBrand.isBlank()) {
            return false;
        }

        String pb = productBrand.trim();
        return requestedBrands.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(b -> !b.isEmpty())
                .anyMatch(b -> b.equalsIgnoreCase(pb));
    }

    /**
     * Internal value object holding rating statistics for a product.
     */
    private static final class RatingStats {
        final Double avg;
        final long count;

        RatingStats(Double avg, long count) {
            this.avg = avg;
            this.count = count;
        }
    }
}
