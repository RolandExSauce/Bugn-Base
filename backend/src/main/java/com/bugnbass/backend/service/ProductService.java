package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.ProductFilter;
import com.bugnbass.backend.dto.ProductResponseDto;
import com.bugnbass.backend.exceptions.ProductNotFoundException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service for publicly accessible product operations, including retrieval and filtering.
 */
@Service
public class ProductService {

    private static final int DEFAULT_PAGE_SIZE = 25;
    private static final int DEFAULT_PAGE_NUMBER = 0;

    /**
     * Repository for CRUD operations on Product entities.
     */
    private final ProductRepository productRepository;

    /**
     * Constructs the ProductService with required ProductRepository.
     *
     * @param productRepository the ProductRepository instance
     */
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieves a single active product by its ID.
     *
     * @param id the product ID
     * @return the Product object
     * @throws ProductNotFoundException if the product does not exist or is inactive
     */
    public Product getProduct(Long id) {
        return productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    /**
     * Retrieves a list of products matching the given filter criteria.
     * Supports filtering by name, category, price range, brand, and pagination.
     *
     * @param filter the ProductFilter containing filtering and pagination parameters
     * @return a list of ProductResponseDTO objects matching the filter
     */
    public List<ProductResponseDto> getProducts(ProductFilter filter) {

        List<Product> allProducts = productRepository.findAll().stream()
                .filter(Product::getActive)
                .toList();

        return allProducts.stream()
                .filter(p -> filter.name() == null
                        || p.getName().toLowerCase().contains(filter.name().toLowerCase()))
                .filter(p -> filter.category() == null
                        || p.getCategory().equals(filter.category()))
                .filter(p -> filter.priceMin() == null
                        || p.getPrice() >= filter.priceMin())
                .filter(p -> filter.priceMax() == null
                        || p.getPrice() <= filter.priceMax())
                .filter(p -> filter.brand() == null
                        || filter.brand().contains(p.getBrand()))
                .skip(filter.pageNumber() == null ? DEFAULT_PAGE_NUMBER :
                        (long) filter.pageNumber() * filter.pageSize())
                .limit(filter.pageSize() == null ? DEFAULT_PAGE_SIZE : filter.pageSize())
                .map(ProductResponseDto::fromEntity)
                .toList();
    }
}
