package com.mtalaat.restaurant.exceptions;

public class PeriodLockedException extends RuntimeException {

    public PeriodLockedException(String message) {
        super(message);
    }
}
