package com.mtalaat.restaurant.modules.account.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.accounting.parent-codes")
@Getter
@Setter
public class AccountParentCodes {
    private String assets;
    private String cashAndBank;
    private String inventory;
    private String customers; // Holds value "1300"
    private String liabilities;
    private String suppliers;
    private String revenue;  //4000
    private String restaurantSalesRevenue;  //4100
    private String cogs;
    private String opex;
    private String equity;
    private String retainedEarnings; // Holds value "3100"
}