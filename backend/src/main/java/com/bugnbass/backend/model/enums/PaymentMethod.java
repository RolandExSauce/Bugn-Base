package com.bugnbass.backend.model.enums;

/**
 * Enum representing available payment methods for orders.
 */
public enum PaymentMethod {

  /** Payment via credit card. */
  CREDITCARD("CREDITCARD"),

  /** Payment via PayPal. */
  PAYPAL("PAYPAL"),

  /** Payment via bank transfer. */
  BANKTRANSFER("BANKTRANSFER");

  private final String label;

  PaymentMethod(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }
}
