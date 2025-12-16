package com.bugnbass.backend.validator;
import com.bugnbass.backend.dto.ProductDTO;
import com.bugnbass.backend.exceptions.ProductNameAlreadyExistsException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.repository.ProductRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ProductValidator {
    protected ProductRepository productRepository;

    public ProductValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //when creating new products
    public void validateProductData(ProductDTO productDTO) {
        validateProductData(productDTO, null);
    }

    // For updating existing products (with ID to exclude)
    public void validateProductData(ProductDTO productDTO, Long excludeProductId) {
        if (productDTO.name() != null) {
            this.validateName(productDTO.name(), excludeProductId);
        }
    }

    private void validateName(String name, Long excludeProductId) {
        Optional<Product> productWithSameName = productRepository.findByName(name);

        if (productWithSameName.isPresent()) {
            Product existingProduct = productWithSameName.get();

            // If we're updating a product and found the same product (by ID), that's OK
            if (existingProduct.getId().equals(excludeProductId)) {
                return; // It's the same product, allow the update
            }

            // Otherwise, the name already exists on a different product
            throw new ProductNameAlreadyExistsException();
        }
    }
}
