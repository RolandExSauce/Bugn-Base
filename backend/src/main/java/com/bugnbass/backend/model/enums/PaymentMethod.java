package com.bugnbass.backend.model.enums;
import lombok.Getter;

@Getter
public enum PaymentMethod {

    CREDITCARD("CREDITCARD"),
    PAYPAL("PAYPAL"),
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
