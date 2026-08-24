package com.mtalaat.restaurant.modules.purchase.mapping;

import com.mtalaat.restaurant.modules.purchase.dto.PurchaseDto;
import com.mtalaat.restaurant.modules.purchase.dto.PurchaseItemDto;
import com.mtalaat.restaurant.modules.purchase.entity.Purchase;
import com.mtalaat.restaurant.modules.purchase.entity.PurchaseItem;
import com.mtalaat.restaurant.modules.purchase.entity.Supplier;
import com.mtalaat.restaurant.modules.purchase.enums.PurchaseStatus;
import com.mtalaat.restaurant.modules.settings.entity.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PurchaseMapper {

    private final PurchaseItemMapper purchaseItemMapper;

    public PurchaseDto toDto(Purchase entity) {

        List<PurchaseItemDto> items = entity.getItems().stream().map(
                purchaseItemMapper::toDto
        ).toList();

        Double totalAmount = calculateTotalAmountDto(items);

        return PurchaseDto.builder()
                .id(entity.getId())
                .invoiceNumber(entity.getInvoiceNumber())
                .paymentMethod(entity.getPaymentMethod())
                .supplierId(entity.getSupplier() != null ? entity.getSupplier().getId() : null)
                .supplierName(entity.getSupplier() != null ? entity.getSupplier().getName() : null)
                .purchaseDate(entity.getPurchaseDate())
                .status(entity.getStatus())
                .totalAmount(totalAmount)
                .paidAmount(entity.getPaidAmount())
                .note(entity.getNote())
                .purchaseItems(items)
                .build();
    }

    public Purchase toEntity(PurchaseDto dto, PaymentMethod paymentMethod, Supplier supplier) {
        Purchase entity = new Purchase();

        List<PurchaseItem> purchaseItems = dto.getPurchaseItems().stream().map(purchaseItemMapper::toEntity).toList();

        entity.setInvoiceNumber(dto.getInvoiceNumber());
        entity.setPaymentMethod(paymentMethod);
        entity.setSupplier(supplier);
        entity.setPurchaseDate(dto.getPurchaseDate());
        entity.setStatus(PurchaseStatus.DRAFT);
        entity.setTotalAmount(calculateTotalAmount(purchaseItems));
        entity.setPaidAmount(dto.getPaidAmount());
        entity.setNote(dto.getNote());
        entity.setItems(purchaseItems);
        return entity;
    }

    private Double calculateTotalAmount(List<PurchaseItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
    }

     private Double calculateTotalAmountDto(List<PurchaseItemDto> items) {
        return items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
    }
}
