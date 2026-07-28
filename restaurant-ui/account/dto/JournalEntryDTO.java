package com.mtalaat.restaurant.modules.account.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JournalEntryDTO {
    private Long id;
    private String entryNumber;
    private LocalDateTime entryDate;
    private String description;
    private String reference;
    private String source;
    private List<JournalItemDTO> items;
}

