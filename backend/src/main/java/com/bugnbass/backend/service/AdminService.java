package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.ProductDTO;
import com.bugnbass.backend.exceptions.ProductNotFoundException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.repository.ProductRepository;
import com.bugnbass.backend.validator.ProductValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

  private final ProductRepository productRepository;
  private final ProductValidator productValidator;

  public AdminService(ProductRepository productRepository, ProductValidator productValidator) {
    this.productRepository = productRepository;
    this.productValidator = productValidator;
  }

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

  //TODO: shouldn't this actually delete the product from the DB ?  🤔
  public void deleteProduct(String id) {
    Product product = getProduct(id);
    product.setActive(false);
  }

  public void updateProduct(String id, ProductDTO dto) {
    Long productId = Long.valueOf(id);
    Product product = getProduct(id);
    productValidator.validateProductData(dto, productId);

    if (dto.name() != null) product.setName(dto.name());
    if (dto.category() != null)
      product.setCategory(dto.category());
    if (dto.description() != null) product.setDescription(dto.description());
    if (dto.price() != null) product.setPrice(dto.price());
    if (dto.shippingCost() != null) product.setShippingCost(dto.shippingCost());
    if (dto.brand() != null) product.setBrand(dto.brand());
    if (dto.shippingTime() != null) product.setShippingTime(dto.shippingTime());
    if (dto.stockStatus() != null) product.setStockStatus(dto.stockStatus());
    if (dto.active() != null) product.setActive(dto.active());

    productRepository.save(product);
  }
}
