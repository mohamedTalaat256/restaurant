package com.mtalaat.restaurant.modules.order.service;

import com.mtalaat.restaurant.exceptions.BadRequestException;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.order.dto.KitchenOrderDto;
import com.mtalaat.restaurant.modules.order.dto.KitchenOrderActionDto;
import com.mtalaat.restaurant.modules.order.entity.KitchenOrder;
import com.mtalaat.restaurant.modules.order.entity.KitchenOrderItem;
import com.mtalaat.restaurant.modules.order.enums.KitchenOrderItemStatus;
import com.mtalaat.restaurant.modules.order.enums.KitchenOrderStatus;
import com.mtalaat.restaurant.modules.order.mapping.KitchenOrderMapper;
import com.mtalaat.restaurant.modules.order.repository.KitchenOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class KitchenOrderService {

    private final KitchenOrderRepository kitchenOrderRepository;
    private final KitchenOrderMapper kitchenOrderMapper;

    // ─────────────────────────────────────────────
    // KITCHEN DASHBOARD: GET BY KITCHEN
    // ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<KitchenOrderDto> getByKitchen(Long kitchenId) {
        return kitchenOrderRepository.findByKitchenId(kitchenId)
                .stream().map(kitchenOrderMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<KitchenOrderDto> getByKitchenAndStatus(Long kitchenId, KitchenOrderStatus status) {
        return kitchenOrderRepository.findByKitchenIdAndStatus(kitchenId, status)
                .stream().map(kitchenOrderMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public KitchenOrderDto getById(Long id) {
        return kitchenOrderMapper.toDto(findOrThrow(id));
    }

    // ─────────────────────────────────────────────
    // KITCHEN ORDER LIFECYCLE
    // ─────────────────────────────────────────────

    public KitchenOrderDto accept(Long kitchenOrderId, KitchenOrderActionDto dto) {
        KitchenOrder ko = findOrThrow(kitchenOrderId);
        if (ko.getStatus() != KitchenOrderStatus.PENDING) {
            throw new BadRequestException("Only PENDING kitchen orders can be accepted");
        }
        ko.setStatus(KitchenOrderStatus.ACCEPTED);
        ko.setAcceptedAt(LocalDateTime.now());
        if (dto != null) ko.setNotes(dto.getNotes());

        ko.getKitchenOrderItems().forEach(item -> item.setStatus(KitchenOrderItemStatus.ACCEPTED));
        return kitchenOrderMapper.toDto(kitchenOrderRepository.save(ko));
    }

    public KitchenOrderDto reject(Long kitchenOrderId, KitchenOrderActionDto dto) {
        KitchenOrder ko = findOrThrow(kitchenOrderId);
        if (ko.getStatus() != KitchenOrderStatus.PENDING) {
            throw new BadRequestException("Only PENDING kitchen orders can be rejected");
        }
        ko.setStatus(KitchenOrderStatus.REJECTED);
        ko.setRejectedAt(LocalDateTime.now());
        if (dto != null) ko.setNotes(dto.getNotes());

        ko.getKitchenOrderItems().forEach(item -> item.setStatus(KitchenOrderItemStatus.REJECTED));
        return kitchenOrderMapper.toDto(kitchenOrderRepository.save(ko));
    }

    public KitchenOrderDto startPreparing(Long kitchenOrderId) {
        KitchenOrder ko = findOrThrow(kitchenOrderId);
        if (ko.getStatus() != KitchenOrderStatus.ACCEPTED) {
            throw new BadRequestException("Only ACCEPTED kitchen orders can be started for preparation");
        }
        ko.setStatus(KitchenOrderStatus.PREPARING);
        ko.setPreparedAt(LocalDateTime.now());

        ko.getKitchenOrderItems().stream()
                .filter(i -> i.getStatus() == KitchenOrderItemStatus.ACCEPTED)
                .forEach(i -> i.setStatus(KitchenOrderItemStatus.PREPARING));

        return kitchenOrderMapper.toDto(kitchenOrderRepository.save(ko));
    }

    public KitchenOrderDto markReady(Long kitchenOrderId) {
        KitchenOrder ko = findOrThrow(kitchenOrderId);
        if (!Set.of(KitchenOrderStatus.ACCEPTED, KitchenOrderStatus.PREPARING).contains(ko.getStatus())) {
            throw new BadRequestException("Kitchen order must be ACCEPTED or PREPARING to be marked READY");
        }
        ko.setStatus(KitchenOrderStatus.READY);
        ko.setReadyAt(LocalDateTime.now());

        ko.getKitchenOrderItems().stream()
                .filter(i -> i.getStatus() != KitchenOrderItemStatus.REJECTED)
                .forEach(i -> i.setStatus(KitchenOrderItemStatus.READY));

        return kitchenOrderMapper.toDto(kitchenOrderRepository.save(ko));
    }

    // ─────────────────────────────────────────────
    // KITCHEN ITEM LIFECYCLE
    // ─────────────────────────────────────────────

    public KitchenOrderDto markItemReady(Long kitchenOrderId, Long kitchenOrderItemId) {
        KitchenOrder ko = findOrThrow(kitchenOrderId);
        KitchenOrderItem item = ko.getKitchenOrderItems().stream()
                .filter(i -> i.getId().equals(kitchenOrderItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Kitchen order item not found: " + kitchenOrderItemId));

        if (!Set.of(KitchenOrderItemStatus.ACCEPTED, KitchenOrderItemStatus.PREPARING)
                .contains(item.getStatus())) {
            throw new BadRequestException("Item must be ACCEPTED or PREPARING to be marked READY");
        }
        item.setStatus(KitchenOrderItemStatus.READY);

        // Auto-advance kitchen order to READY if all non-rejected items are ready
        boolean allReady = ko.getKitchenOrderItems().stream()
                .filter(i -> i.getStatus() != KitchenOrderItemStatus.REJECTED)
                .allMatch(i -> i.getStatus() == KitchenOrderItemStatus.READY);
        if (allReady) {
            ko.setStatus(KitchenOrderStatus.READY);
            ko.setReadyAt(LocalDateTime.now());
        }

        return kitchenOrderMapper.toDto(kitchenOrderRepository.save(ko));
    }

    public KitchenOrderDto markItemServed(Long kitchenOrderId, Long kitchenOrderItemId) {
        KitchenOrder ko = findOrThrow(kitchenOrderId);
        KitchenOrderItem item = ko.getKitchenOrderItems().stream()
                .filter(i -> i.getId().equals(kitchenOrderItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Kitchen order item not found: " + kitchenOrderItemId));

        if (item.getStatus() != KitchenOrderItemStatus.READY) {
            throw new BadRequestException("Item must be READY before it can be marked SERVED");
        }
        item.setStatus(KitchenOrderItemStatus.SERVED);
        return kitchenOrderMapper.toDto(kitchenOrderRepository.save(ko));
    }

    // ─────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────

    private KitchenOrder findOrThrow(Long id) {
        return kitchenOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitchen order not found with id: " + id));
    }
}
