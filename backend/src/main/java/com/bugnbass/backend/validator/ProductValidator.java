package com.bugnbass.backend.validator;

import com.bugnbass.backend.dto.ProductDTO;
import com.bugnbass.backend.exceptions.ProductNameAlreadyExistsException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.repository.ProductRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {
  protected ProductRepository productRepository;

  public ProductValidator(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public void validateProductData(ProductDTO productDTO) {
    if (productDTO.name() != null) this.validateName(productDTO.name());
    if (productDTO.category() != null) this.validateCategory(productDTO.category());
  }

  private void validateName(String name) {
    Optional<Product> productWithSameName = productRepository.findByName(name);
    if (productWithSameName.isPresent()) throw new ProductNameAlreadyExistsException();
  }

  private void validateCategory(String category) {
    try {
      ProductCategory.valueOf(category.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Falsche Eíngabe für Kategorie!");
    }
  }
}
