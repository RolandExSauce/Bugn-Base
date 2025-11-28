package com.bugnbass.backend.service;
import com.bugnbass.backend.dto.ProductDTO;
import com.bugnbass.backend.exceptions.ProductNotFoundException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.repository.ProductRepository;
import com.bugnbass.backend.validator.ProductValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    public Product getProduct(String id) {
        return productRepository.findById(Long.valueOf(id))
                .orElseThrow(ProductNotFoundException::new);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product addProduct(ProductDTO dto) {

        productValidator.validateProductData(dto);
        Product product = Product.builder()
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

        return productRepository.save(product); // <-- VERY IMPORTANT
    }

    public void deleteProduct(String id) {
        Product product = getProduct(id);
        product.setActive(false);
    }

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
