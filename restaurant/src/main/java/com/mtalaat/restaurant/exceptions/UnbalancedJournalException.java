package com.mtalaat.restaurant.exceptions;

public class UnbalancedJournalException
        extends RuntimeException {

    public UnbalancedJournalException() {
        super("Journal entry is not balanced.");
    }

}