package com.mtalaat.restaurant.modules.delivery.enums;

/**
 * Availability status of a driver at any point in time.
 */
public enum DriverStatus {

    /** Driver is logged in and available to receive deliveries. */
    ONLINE,

    /** Driver is logged out / not accepting deliveries. */
    OFFLINE,

    /** Driver currently has an active delivery. */
    BUSY,

    /** Driver is on a break and temporarily unavailable. */
    BREAK
}
