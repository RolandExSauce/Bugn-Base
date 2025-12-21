package com.bugnbass.backend.model.enums;

import lombok.Getter;

/**
 * Enum representing the stock status of a product.
 */
@Getter
public enum StockStatus {

  /** Product is available in stock. */
  IN_STOCK("In Stock"),

  /** Product is currently out of stock. */
  OUT_OF_STOCK("Out of Stock"),

  /** Product stock is low and may run out soon. */
  LOW_STOCK("Low Stock");

  private final String label;

  StockStatus(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }
}
