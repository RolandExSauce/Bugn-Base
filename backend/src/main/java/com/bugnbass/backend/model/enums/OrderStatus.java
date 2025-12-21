package com.bugnbass.backend.model.enums;

/**
 * Enum representing the possible statuses of an order in the system.
 */
public enum OrderStatus {
    /** Order has been canceled by the user or admin. */
    CANCELED,

    /** Order has been received and is pending processing. */
    RECEIVED,

    /** Order is currently being shipped to the customer. */
    SHIPPING,

    /** Order has been delivered to the customer. */
    DELIVERED,

    /** Order has been returned by the customer. */
    RETURNED
}
