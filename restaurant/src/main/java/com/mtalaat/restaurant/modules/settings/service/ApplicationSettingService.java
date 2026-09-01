package com.mtalaat.restaurant.modules.settings.service;

import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.settings.dto.ApplicationSettingDto;
import com.mtalaat.restaurant.modules.settings.entity.ApplicationSetting;
import com.mtalaat.restaurant.modules.settings.entity.Currency;
import com.mtalaat.restaurant.modules.settings.entity.Language;
import com.mtalaat.restaurant.modules.settings.mapping.ApplicationSettingMapper;
import com.mtalaat.restaurant.modules.settings.repository.ApplicationSettingRepository;
import com.mtalaat.restaurant.modules.settings.repository.CurrencyRepository;
import com.mtalaat.restaurant.modules.settings.repository.LanguageRepository;
import com.mtalaat.restaurant.utility.FileUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ApplicationSettingService {

    private final ApplicationSettingRepository applicationSettingRepository;
    private final CurrencyRepository currencyRepository;
    private final LanguageRepository languageRepository;
    private final ApplicationSettingMapper applicationSettingMapper;
    private final FileUtility fileUtility;



    public ApplicationSettingDto getApplicationSettings() {
        ApplicationSetting entity = applicationSettingRepository.findFirstByOrderByIdAsc();
        return applicationSettingMapper.toDto(entity);
    }

    public ApplicationSettingDto getApplicationSettingsPublic(){
        return applicationSettingMapper.toDtoPublic(applicationSettingRepository.findFirstByOrderByIdAsc());
    }

    public ApplicationSettingDto update(ApplicationSettingDto dto, MultipartFile iconFile, MultipartFile logoFile) {
        ApplicationSetting entity = applicationSettingRepository.findFirstByOrderByIdAsc();

        Currency currency = null;
        if (dto.getCurrencyId() != null) {
            currency = currencyRepository.findById(dto.getCurrencyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + dto.getCurrencyId()));
        }

        Language language = null;
        if (dto.getLanguageCode() != null) {
            language = languageRepository.findByLanguageCode(dto.getLanguageCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + dto.getLanguageCode()));
        }

        entity.setApplicationTitle(dto.getApplicationTitle());
        entity.setStoreName(dto.getStoreName());
        entity.setAddress(dto.getAddress());
        entity.setPhone(dto.getPhone());
        entity.setOpeningTime(dto.getOpeningTime());
        entity.setClosingTime(dto.getClosingTime());
        entity.setDiscountType(dto.getDiscountType());
        entity.setDiscountPercentage(dto.getDiscountPercentage());
        entity.setServiceChargeType(dto.getServiceChargeType());
        entity.setTaxPercentage(dto.getTaxPercentage());
        entity.setTaxNumber(dto.getTaxNumber());
        entity.setCurrency(currency);
        entity.setLanguage(language);
        entity.setDateFormat(dto.getDateFormat());
        entity.setTimezone(dto.getTimezone());
        entity.setApplicationDirection(dto.getApplicationDirection());
        entity.setPoweredByText(dto.getPoweredByText());
        entity.setFooterText(dto.getFooterText());

        if (iconFile != null && !iconFile.isEmpty()) {
            String iconPath = fileUtility.saveFile(iconFile, "settings");
            entity.setIcon(iconPath);
        }

        if (logoFile != null && !logoFile.isEmpty()) {
            String logoPath = fileUtility.saveFile(logoFile, "settings");
            entity.setLogo(logoPath);
        }

        return applicationSettingMapper.toDto(applicationSettingRepository.save(entity));
    }


    public String GetCurrentLanguageCode() {
        ApplicationSetting entity = applicationSettingRepository.findFirstByOrderByIdAsc();
        return entity.getLanguage().getLanguageCode();
    }
}
