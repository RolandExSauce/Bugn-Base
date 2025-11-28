package com.bugnbass.backend.model.enums;
import lombok.Getter;

@Getter
public enum PaymentMethod {

    CREDIT_CARD("creditcard"),
    PAYPAL("paypal"),
    BANK_TRANSFER("banktransfer");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
