package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.ProductDTO;
import com.bugnbass.backend.exceptions.ProductNotFoundException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.repository.ProductRepository;
import com.bugnbass.backend.validator.ProductValidator;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for handling administrative operations on products. Provides methods to create, read,
 * update, and deactivate products.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AdminProductService {

  private final ProductRepository productRepository;
  private final ProductValidator productValidator;

  /**
   * Retrieves a product by its ID.
   *
   * @param id the ID of the product
   * @return the Product with the specified ID
   * @throws ProductNotFoundException if no product with the given ID exists
   */
  public Product getProduct(String id) {
    return productRepository.findById(Long.valueOf(id)).orElseThrow(ProductNotFoundException::new);
  }

  /**
   * Retrieves all products.
   *
   * @return a list of all products
   */
  public List<Product> getProducts() {
    return productRepository.findAll();
  }

  /**
   * Adds a new product.
   *
   * @param dto the product data transfer object containing product details
   * @return the saved Product
   */
  public Product addProduct(ProductDTO dto) {
    productValidator.validateProductData(dto);
    Product product =
        Product.builder()
            .name(dto.name())
            .category(ProductCategory.valueOf(dto.category().toUpperCase()))
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
   * Deactivates a product by setting its active status to false.
   *
   * @param id the ID of the product to deactivate
   */
  public void deleteProduct(String id) {
    Product product = getProduct(id);
    product.setActive(false);
  }

  /**
   * Updates an existing product with new data. Only non-null fields in the DTO will be updated.
   *
   * @param id the ID of the product to update
   * @param dto the product data transfer object containing updated details
   */
  public void updateProduct(String id, ProductDTO dto) {
    Product product = getProduct(id);
    productValidator.validateProductData(dto);

    if (dto.name() != null) product.setName(dto.name());
    if (dto.category() != null)
      product.setCategory(ProductCategory.valueOf(dto.category().toUpperCase()));
    if (dto.description() != null) product.setDescription(dto.description());
    if (dto.price() != null) product.setPrice(dto.price());
    if (dto.shippingCost() != null) product.setShippingCost(dto.shippingCost());
    if (dto.brand() != null) product.setBrand(dto.brand());
    if (dto.shippingTime() != null) product.setShippingTime(dto.shippingTime());
    if (dto.stockStatus() != null) product.setStockStatus(dto.stockStatus());
    if (dto.active() != null) product.setActive(dto.active());
  }
}
