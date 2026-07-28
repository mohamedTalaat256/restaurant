package com.mtalaat.restaurant.modules.account.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManualJournalEntryRequest {

    @NotNull(message = "validation_entry_date_required")
    private LocalDateTime entryDate;

    @NotBlank(message = "validation_description_required")
    private String description;

    private String reference;

    @NotEmpty(message = "validation_items_required")
    @Valid
    private List<ManualJournalItemRequest> items;
}
