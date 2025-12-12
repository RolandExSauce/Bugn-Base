package com.bugnbass.backend.model.enums;

import lombok.Getter;

@Getter
public enum StockStatus {
  IN_STOCK("In Stock"),
  OUT_OF_STOCK("Out of Stock");

  private final String label;

  StockStatus(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }
}
