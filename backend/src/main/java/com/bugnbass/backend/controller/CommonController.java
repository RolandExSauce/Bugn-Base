package com.bugnbass.backend.controller;

import com.bugnbass.backend.model.enums.ProductCategory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for common endpoints accessible to all users. Provides endpoints for retrieving
 * general application data.
 */
@RestController
@RequestMapping("/api")
public class CommonController {

  /**
   * Retrieves all available product categories.
   *
   * @return an array of all ProductCategory enum values
   */
  @GetMapping("/categories")
  public ProductCategory[] getCategories() {
    return ProductCategory.values();
  }
}
