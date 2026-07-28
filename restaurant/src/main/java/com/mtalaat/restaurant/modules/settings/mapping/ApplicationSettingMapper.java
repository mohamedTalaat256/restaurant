package com.mtalaat.restaurant.modules.settings.mapping;

import com.mtalaat.restaurant.modules.settings.dto.ApplicationSettingDto;
import com.mtalaat.restaurant.modules.settings.entity.ApplicationSetting;
import com.mtalaat.restaurant.modules.settings.entity.Currency;
import com.mtalaat.restaurant.modules.settings.entity.Language;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSettingMapper {

    public ApplicationSettingDto toDto(ApplicationSetting entity) {
        return ApplicationSettingDto.builder()
                .id(entity.getId())
                .applicationTitle(entity.getApplicationTitle())
                .storeName(entity.getStoreName())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .icon(entity.getIcon())
                .logo(entity.getLogo())
                .openingTime(entity.getOpeningTime())
                .closingTime(entity.getClosingTime())
                .discountType(entity.getDiscountType())
                .discountPercentage(entity.getDiscountPercentage())
                .serviceChargeType(entity.getServiceChargeType())
                .taxPercentage(entity.getTaxPercentage())
                .taxNumber(entity.getTaxNumber())
                .currencyId(entity.getCurrency() != null ? entity.getCurrency().getId() : null)
                .currencyName(entity.getCurrency() != null ? entity.getCurrency().getName() : null)
                .currencySymbol(entity.getCurrency() != null ? entity.getCurrency().getSymbol() : null)
                .languageCode(entity.getLanguage() != null ? entity.getLanguage().getLanguageCode() : null)
                .languageName(entity.getLanguage() != null ? entity.getLanguage().getName() : null)
                .dateFormat(entity.getDateFormat())
                .timezone(entity.getTimezone())
                .applicationDirection(entity.getApplicationDirection())
                .poweredByText(entity.getPoweredByText())
                .footerText(entity.getFooterText())
                .build();
    }

    public ApplicationSetting toEntity(ApplicationSettingDto dto, Currency currency, Language language) {
        return ApplicationSetting.builder()
                .applicationTitle(dto.getApplicationTitle())
                .storeName(dto.getStoreName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .icon(dto.getIcon())
                .logo(dto.getLogo())
                .openingTime(dto.getOpeningTime())
                .closingTime(dto.getClosingTime())
                .discountType(dto.getDiscountType())
                .discountPercentage(dto.getDiscountPercentage())
                .serviceChargeType(dto.getServiceChargeType())
                .taxPercentage(dto.getTaxPercentage())
                .taxNumber(dto.getTaxNumber())
                .currency(currency)
                .language(language)
                .dateFormat(dto.getDateFormat())
                .timezone(dto.getTimezone())
                .applicationDirection(dto.getApplicationDirection())
                .poweredByText(dto.getPoweredByText())
                .footerText(dto.getFooterText())
                .build();
    }
}
