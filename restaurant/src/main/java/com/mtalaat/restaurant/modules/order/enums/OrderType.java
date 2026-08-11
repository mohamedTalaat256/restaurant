package com.mtalaat.restaurant.modules.order.enums;

public enum OrderType {
    QUICK_ORDER,
    PLACE_ORDER,

    /**
     * The order will be delivered to the customer's address.
     * Only DELIVERY orders may have an associated Delivery record.
     */
    DELIVERY
}
