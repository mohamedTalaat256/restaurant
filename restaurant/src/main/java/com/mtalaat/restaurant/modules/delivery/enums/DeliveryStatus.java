package com.mtalaat.restaurant.modules.delivery.enums;

/**
 * Lifecycle of a delivery record.
 *
 * Kept independent from OrderStatus because:
 * - An order can be READY while delivery is still WAITING_ASSIGNMENT.
 * - A delivery can FAIL or be CANCELLED while the order might be retried.
 * - Fine-grained driver tracking needs states that have no meaning at the order level.
 */
public enum DeliveryStatus {

    /** Delivery record created, not yet dispatched to any driver. */
    CREATED,

    /** Waiting for a driver to be assigned. */
    WAITING_ASSIGNMENT,

    /** A driver has been assigned but hasn't accepted yet. */
    ASSIGNED,

    /** Driver accepted the delivery request. */
    ACCEPTED,

    /** Driver arrived at the restaurant to pick up the order. */
    ARRIVED_AT_RESTAURANT,

    /** Driver picked up the order from the restaurant. */
    PICKED_UP,

    /** Driver is on the way to the customer. */
    ON_THE_WAY,

    /** Order successfully delivered to the customer. */
    DELIVERED,

    /** Delivery attempt failed (e.g. customer not found, unreachable). */
    FAILED,

    /** Delivery was cancelled before completion. */
    CANCELLED
}
