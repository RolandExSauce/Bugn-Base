package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.ProductDto;
import com.bugnbass.backend.exceptions.ProductNotFoundException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.repository.ProductRepository;
import com.bugnbass.backend.validator.ProductValidator;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service responsible for administrative product operations such as retrieval,
 * creation, update, and soft deletion.
 */
@Service
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    /**
     * Constructs the AdminProductService with required dependencies.
     *
     * @param productRepository repository for product persistence
     * @param productValidator  validator for product business rules
     */
    public AdminProductService(
            ProductRepository productRepository,
            ProductValidator productValidator
    ) {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
    }

    /**
     * Retrieves a product by its identifier.
     *
     * @param id the product identifier as string
     * @return the {@link Product} entity
     * @throws ProductNotFoundException if no product with the given ID exists
     */
    public Product getProduct(String id) {
        return productRepository.findById(Long.valueOf(id))
                .orElseThrow(ProductNotFoundException::new);
    }

    /**
     * Retrieves all products.
     *
     * @return list of all {@link Product} entities
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * Creates and persists a new product.
     *
     * @param dto the product data transfer object
     * @return the persisted {@link Product}
     */
    public Product addProduct(ProductDto dto) {
        productValidator.validateProductData(dto);

        Product product = Product.builder()
                .name(dto.name())
                .category(dto.category())
                .description(dto.description())
                .price(dto.price())
                .shippingCost(dto.shippingCost())
                .brand(dto.brand())
                .stockStatus(dto.stockStatus())
                .shippingTime(dto.shippingTime())
                .active(dto.active())
                .build();

        return productRepository.save(product);
    }

    /**
     * Soft deletes a product by marking it as inactive.
     *
     * @param id the product identifier as string
     */
    public void deleteProduct(String id) {
        Product product = getProduct(id);
        product.setActive(false);
        productRepository.save(product);
    }

    /**
     * Updates an existing product with provided values.
     *
     * <p>Only non-null fields in the DTO are applied.
     *
     * @param id  the product identifier as string
     * @param dto the product update data
     */
    public void updateProduct(String id, ProductDto dto) {
        Long productId = Long.valueOf(id);
        Product product = getProduct(id);

        productValidator.validateProductData(dto, productId);

        if (dto.name() != null) {
            product.setName(dto.name());
        }
        if (dto.category() != null) {
            product.setCategory(dto.category());
        }
        if (dto.description() != null) {
            product.setDescription(dto.description());
        }
        if (dto.price() != null) {
            product.setPrice(dto.price());
        }
        if (dto.shippingCost() != null) {
            product.setShippingCost(dto.shippingCost());
        }
        if (dto.brand() != null) {
            product.setBrand(dto.brand());
        }
        if (dto.shippingTime() != null) {
            product.setShippingTime(dto.shippingTime());
        }
        if (dto.stockStatus() != null) {
            product.setStockStatus(dto.stockStatus());
        }
        if (dto.active() != null) {
            product.setActive(dto.active());
        }

        productRepository.save(product);
    }
}
