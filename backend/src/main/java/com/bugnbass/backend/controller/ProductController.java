package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.ProductFilter;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.service.ProductService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling product-related operations in the shop. Provides endpoints for retrieving
 * single or multiple products with optional filtering.
 */
@RestController
@RequestMapping("/api/shop/products")
public class ProductController {

  private final ProductService userProductService;

  /**
   * Constructs a ProductController with the given ProductService.
   *
   * @param userProductService the service for product operations
   */
  public ProductController(ProductService userProductService) {
    this.userProductService = userProductService;
  }

  /**
   * Retrieves a product by its ID.
   *
   * @param id the ID of the product
   * @return the Product object with the specified ID
   */
  @GetMapping("/{id}")
  public Product getProduct(@PathVariable String id) {
    return userProductService.getProduct(id);
  }

  /**
   * Retrieves products with optional filtering parameters. If 'id' is provided, returns a single
   * product wrapped in a list.
   *
   * @param id optional product ID
   * @param name optional product name for filtering
   * @param category optional product category for filtering
   * @param priceMin optional minimum price for filtering
   * @param priceMax optional maximum price for filtering
   * @param brand optional list of brands for filtering
   * @param pageNo optional page number for pagination
   * @param pageSize optional page size for pagination
   * @return a list of products matching the filter criteria
   */
  @GetMapping
  public List<Product> getProducts(
      @RequestParam(required = false) String id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) ProductCategory category,
      @RequestParam(required = false) Integer priceMin,
      @RequestParam(required = false) Integer priceMax,
      @RequestParam(required = false) List<String> brand,
      @RequestParam(required = false) Integer pageNo,
      @RequestParam(required = false) Integer pageSize) {

    if (id != null) {
      return List.of(userProductService.getProduct(id));
    }

    return userProductService.getProducts(
        new ProductFilter(name, category, priceMin, priceMax, brand, pageNo, pageSize));
  }
}
