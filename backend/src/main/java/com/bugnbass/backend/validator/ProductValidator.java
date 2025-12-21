package com.bugnbass.backend.validator;

import com.bugnbass.backend.dto.ProductDto;
import com.bugnbass.backend.exceptions.ProductNameAlreadyExistsException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.repository.ProductRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;


/**
 * Validator class for product-related business rules.
 * Ensures that product data is valid before creating or updating products.
 */
@Component
public class ProductValidator {

  protected ProductRepository productRepository;

  /**
   * Constructor for ProductValidator.
   *
   * @param productRepository repository used to access products
   */
  public ProductValidator(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  /**
   * Validate product data when creating a new product.
   *
   * @param productDto the product data to validate
   * @throws ProductNameAlreadyExistsException if a product with the same name already exists
   */
  public void validateProductData(ProductDto productDto) {
    validateProductData(productDto, null);
  }

  /**
   * Validate product data when updating an existing product.
   * Excludes the product with the given ID from duplicate name checks.
   *
   * @param productDto       the product data to validate
   * @param excludeProductId the ID of the product to exclude from validation (for updates)
   * @throws ProductNameAlreadyExistsException if a product with the same name exists
   * (excluding the given ID)
   */
  public void validateProductData(ProductDto productDto, Long excludeProductId) {
    if (productDto.name() != null) {
      this.validateName(productDto.name(), excludeProductId);
    }
  }

  /**
   * Checks if a product name already exists in the repository, excluding a given product ID.
   *
   * @param name             the name to validate
   * @param excludeProductId the product ID to exclude from validation
   * @throws ProductNameAlreadyExistsException if the name is already used by another product
   */
  private void validateName(String name, Long excludeProductId) {
    Optional<Product> productWithSameName = productRepository.findByName(name);

    if (productWithSameName.isPresent()) {
      Product existingProduct = productWithSameName.get();

      // If updating and found the same product by ID, allow it
      if (existingProduct.getId().equals(excludeProductId)) {
        return;
      }

      // Otherwise, throw exception
      throw new ProductNameAlreadyExistsException();
    }
  }
}
