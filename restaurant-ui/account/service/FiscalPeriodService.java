package com.mtalaat.restaurant.modules.account.service;

import com.mtalaat.restaurant.exceptions.BadRequestException;
import com.mtalaat.restaurant.exceptions.PeriodLockedException;
import com.mtalaat.restaurant.modules.account.dto.FiscalPeriodDTO;
import com.mtalaat.restaurant.modules.account.entity.FiscalPeriod;
import com.mtalaat.restaurant.modules.account.mapper.FiscalPeriodMapper;
import com.mtalaat.restaurant.modules.account.repository.FiscalPeriodRepository;
import com.mtalaat.restaurant.modules.settings.service.LanguageTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FiscalPeriodService {

    private final FiscalPeriodRepository fiscalPeriodRepository;
    private final FiscalPeriodMapper fiscalPeriodMapper;
    private final LanguageTranslationService translate;

    public boolean isPeriodLocked(LocalDateTime date) {
        if (date == null) return false;
        return fiscalPeriodRepository.existsByYearAndMonthAndLockedTrue(date.getYear(), date.getMonthValue());
    }

    public void assertPeriodNotLocked(LocalDateTime date) {
        if (isPeriodLocked(date)) {
            throw new PeriodLockedException(
                    translate.get("error_period_locked") + " " + date.getYear() + "-" + String.format("%02d", date.getMonthValue()));
        }
    }

    @Transactional(readOnly = true)
    public List<FiscalPeriodDTO> getAllPeriods() {
        return fiscalPeriodRepository.findAllByOrderByYearDescMonthDesc()
                .stream().map(fiscalPeriodMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FiscalPeriodDTO> getPeriodsByYear(Integer year) {
        return fiscalPeriodRepository.findByYearOrderByMonthAsc(year)
                .stream().map(fiscalPeriodMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public FiscalPeriodDTO lockPeriod(Integer year, Integer month) {
        FiscalPeriod period = getOrCreatePeriod(year, month);

        if (Boolean.TRUE.equals(period.getLocked())) {
            throw new BadRequestException(translate.get("error_period_already_locked"));
        }

        period.setLocked(true);
        period.setLockedAt(LocalDateTime.now());
        period.setLockedByUsername(getCurrentUsername());

        return fiscalPeriodMapper.toDTO(fiscalPeriodRepository.save(period));
    }

    @Transactional
    public FiscalPeriodDTO unlockPeriod(Integer year, Integer month) {
        FiscalPeriod period = fiscalPeriodRepository.findByYearAndMonth(year, month)
                .orElseThrow(() -> new BadRequestException(translate.get("error_period_not_found")));

        if (Boolean.FALSE.equals(period.getLocked())) {
            throw new BadRequestException(translate.get("error_period_not_locked"));
        }

        period.setLocked(false);
        period.setLockedAt(null);
        period.setLockedByUsername(null);

        return fiscalPeriodMapper.toDTO(fiscalPeriodRepository.save(period));
    }

    private FiscalPeriod getOrCreatePeriod(Integer year, Integer month) {
        return fiscalPeriodRepository.findByYearAndMonth(year, month)
                .orElseGet(() -> {
                    YearMonth ym = YearMonth.of(year, month);
                    FiscalPeriod newPeriod = FiscalPeriod.builder()
                            .year(year)
                            .month(month)
                            .startDate(ym.atDay(1))
                            .endDate(ym.atEndOfMonth())
                            .locked(false)
                            .build();
                    return fiscalPeriodRepository.save(newPeriod);
                });
    }

    private String getCurrentUsername() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "SYSTEM";
    }
}
