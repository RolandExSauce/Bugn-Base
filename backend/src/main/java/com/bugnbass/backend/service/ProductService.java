package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.ProductFilter;
import com.bugnbass.backend.exceptions.ProductNotFoundException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service for managing products for shop users. Provides methods for retrieving single or multiple
 * products with optional filtering.
 */
@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  /**
   * Retrieves a product by its ID if it is active.
   *
   * @param id the product ID
   * @return the active Product
   * @throws ProductNotFoundException if no active product with the given ID exists
   */
  public Product getProduct(String id) {
    return productRepository
        .findByIdAndActiveTrue(Long.valueOf(id))
        .orElseThrow(ProductNotFoundException::new);
  }

  /**
   * Retrieves a list of products filtered by the specified criteria. Supports pagination and
   * filtering by name, category, price range, and brand.
   *
   * @param filter the product filter containing criteria and pagination info
   * @return a list of products matching the filter
   */
  public List<Product> getProducts(ProductFilter filter) {

    final int DEFAULT_PAGE_SIZE = 25;
    final int DEFAULT_PAGE_NUMBER = 0;

    List<Product> allProducts =
        productRepository.findAll().stream().filter(Product::getActive).toList();

    return allProducts.stream()
        .filter(
            p ->
                filter.name() == null
                    || p.getName().toLowerCase().contains(filter.name().toLowerCase()))
        .filter(p -> filter.category() == null || p.getCategory().equals(filter.category()))
        .filter(p -> filter.priceMin() == null || p.getPrice() >= filter.priceMin())
        .filter(p -> filter.priceMax() == null || p.getPrice() <= filter.priceMax())
        .filter(p -> filter.brand() == null || filter.brand().contains(p.getBrand()))
        .skip(
            filter.pageNumber() == null
                ? DEFAULT_PAGE_NUMBER
                : (long) filter.pageNumber() * filter.pageSize())
        .limit(filter.pageSize() == null ? DEFAULT_PAGE_SIZE : filter.pageSize())
        .toList();
  }
}
