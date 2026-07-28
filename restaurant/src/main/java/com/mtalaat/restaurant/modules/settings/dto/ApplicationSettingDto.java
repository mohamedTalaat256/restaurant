package com.mtalaat.restaurant.modules.settings.dto;

import com.mtalaat.restaurant.modules.settings.entity.ApplicationDirection;
import com.mtalaat.restaurant.modules.settings.entity.DiscountType;
import com.mtalaat.restaurant.modules.settings.entity.ServiceChargeType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSettingDto {

    private Long id;

    @NotBlank(message = "Application title is required")
    private String applicationTitle;

    @NotBlank(message = "Store name is required")
    private String storeName;

    private String address;
    private String phone;
    private String icon;
    private String logo;
    private String openingTime;
    private String closingTime;
    private DiscountType discountType;
    private Double discountPercentage;
    private ServiceChargeType serviceChargeType;
    private Double taxPercentage;
    private String taxNumber;
    private Long currencyId;
    private String currencyName;
    private String currencySymbol;
    private String languageCode;
    private String languageName;
    private String dateFormat;
    private String timezone;
    private ApplicationDirection applicationDirection;
    private String poweredByText;
    private String footerText;
}
