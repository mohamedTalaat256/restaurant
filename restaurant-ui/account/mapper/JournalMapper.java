package com.mtalaat.restaurant.modules.account.mapper;

import com.mtalaat.restaurant.modules.account.dto.JournalEntryDTO;
import com.mtalaat.restaurant.modules.account.dto.JournalItemDTO;
import com.mtalaat.restaurant.modules.account.entity.JournalEntry;
import com.mtalaat.restaurant.modules.account.entity.JournalItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class JournalMapper {

    public JournalEntryDTO toDTO(JournalEntry entry) {
        if (entry == null) return null;
        return JournalEntryDTO.builder()
                .id(entry.getId())
                .entryNumber(entry.getEntryNumber())
                .entryDate(entry.getEntryDate())
                .description(entry.getDescription())
                .reference(entry.getReference())
                .source(entry.getSource() != null ? entry.getSource().name() : null)
                .items(entry.getItems().stream().map(this::toItemDTO).collect(Collectors.toList()))
                .build();
    }

    private JournalItemDTO toItemDTO(JournalItem item) {
        if (item == null) return null;
        return JournalItemDTO.builder()
                .accountId(item.getAccount().getId())
                .accountName(item.getAccount().getName())
                .accountCode(item.getAccount().getCode())
                .debit(item.getDebit())
                .credit(item.getCredit())
                .costCenterId(item.getCostCenter() != null ? item.getCostCenter().getId() : null)
                .costCenterName(item.getCostCenter() != null ? item.getCostCenter().getName() : null)
                .build();
    }
}