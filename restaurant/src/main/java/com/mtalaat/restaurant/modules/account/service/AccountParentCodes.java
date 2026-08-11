package com.mtalaat.restaurant.modules.account.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class AccountParentCodes {

    // Level 1
    @Value("${app.accounting.parent-codes.assets}")
    private String assets;

    @Value("${app.accounting.parent-codes.liabilities}")
    private String liabilities;

    @Value("${app.accounting.parent-codes.equity}")
    private String equity;

    @Value("${app.accounting.parent-codes.revenue}")
    private String revenue;

    @Value("${app.accounting.parent-codes.expenses}")
    private String expenses;

    // Level 2 - Assets
    @Value("${app.accounting.parent-codes.cash-and-banks}")
    private String cashAndBanks;

    @Value("${app.accounting.parent-codes.customers}")
    private String customers;

    @Value("${app.accounting.parent-codes.inventory}")
    private String inventory;

    @Value("${app.accounting.parent-codes.employee-advances}")
    private String employeeAdvances;

    // Level 2 - Liabilities
    @Value("${app.accounting.parent-codes.suppliers}")
    private String suppliers;

    @Value("${app.accounting.parent-codes.salaries-payable}")
    private String salariesPayable;

    // Level 2 - Expenses
    @Value("${app.accounting.parent-codes.cogs-raw-materials}")
    private String cogsRawMaterials;

    @Value("${app.accounting.parent-codes.salaries-expense}")
    private String salariesExpense;

    @Value("${app.accounting.parent-codes.waste-expense}")
    private String wasteExpense;

    @Value("${app.accounting.parent-sub-codes.cash-sub-account}")
    private String cashSubAccount;

    @Value("${app.accounting.parent-sub-codes.inventory-sub-account}")
    private String inventorySubAccount;

    @Value("${app.accounting.parent-codes.restaurant-sales}")
    private String restaurantSales;

    // Level 2 - Equity
    @Value("${app.accounting.parent-codes.retained-earnings}")
    private String retainedEarnings;

}