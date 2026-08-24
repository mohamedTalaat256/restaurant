package com.mtalaat.restaurant.modules.purchase.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Stub service representing the restaurant cash register (treasury).
 * Replace with real accounting integration when the cash module is implemented.
 */
@Service
public class CashService {

    /**
     * Deducts the given amount from the restaurant treasury (cash paid to supplier).
     *
     * @param amount       the amount to deduct
     * @param referenceId  invoice number or any human-readable reference
     */
    public void deductCash(BigDecimal amount, String referenceId) {
        // TODO: integrate with the real cash/accounting module
    }

    /**
     * Returns / refunds the given amount back to the restaurant treasury.
     *
     * @param amount       the amount to refund
     * @param referenceId  invoice number or any human-readable reference
     */
    public void addCash(BigDecimal amount, String referenceId) {
        // TODO: integrate with the real cash/accounting module
    }
}
