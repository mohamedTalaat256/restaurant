package com.mtalaat.restaurant.modules.purchase.dto;

import com.mtalaat.restaurant.modules.settings.entity.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDto {

    private Long id;

    @NotBlank(message = "Invoice number is required")
    private String invoiceNumber;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private String paymentMethodName;

    @NotNull(message = "Supplier is required")
    private Long supplierId;

    private String supplierName;

    @NotNull(message = "Purchase date is required")
    private LocalDate purchaseDate;

    private LocalDate expiryDate;

    private Double totalAmount;

    @NotNull(message = "Paid amount is required")
    private Double paidAmount;

    private String note;

    private List<PurchaseItemDto> purchaseItems;
}
