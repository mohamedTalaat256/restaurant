package com.mtalaat.restaurant.modules.order.service;

import com.mtalaat.restaurant.exceptions.BadRequestException;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFood;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFoodAddOns;
import com.mtalaat.restaurant.modules.foodManagement.entity.ItemFoodVariant;
import com.mtalaat.restaurant.modules.foodManagement.repository.ItemFoodAddOnsRepository;
import com.mtalaat.restaurant.modules.foodManagement.repository.ItemFoodRepository;
import com.mtalaat.restaurant.modules.foodManagement.repository.ItemFoodVariantRepository;
import com.mtalaat.restaurant.modules.order.dto.*;
import com.mtalaat.restaurant.modules.order.entity.*;
import com.mtalaat.restaurant.modules.order.enums.*;
import com.mtalaat.restaurant.modules.account.dto.JournalEntryDTO;
import com.mtalaat.restaurant.modules.account.entity.JournalEntry;
import com.mtalaat.restaurant.modules.account.mapper.JournalMapper;
import com.mtalaat.restaurant.modules.account.service.FinancialPostingService;
import com.mtalaat.restaurant.modules.order.mapping.OrderMapper;
import com.mtalaat.restaurant.modules.order.repository.KitchenOrderRepository;
import com.mtalaat.restaurant.modules.order.repository.OrderRepository;
import com.mtalaat.restaurant.modules.settings.entity.Customer;
import com.mtalaat.restaurant.modules.settings.entity.Kitchen;
import com.mtalaat.restaurant.modules.settings.entity.RestaurantTable;
import com.mtalaat.restaurant.modules.settings.repository.CustomerRepository;
import com.mtalaat.restaurant.modules.settings.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final KitchenOrderRepository kitchenOrderRepository;
    private final FinancialPostingService financialPostingService;
    private final JournalMapper journalMapper;
    private final ItemFoodRepository itemFoodRepository;
    private final ItemFoodVariantRepository itemFoodVariantRepository;
    private final ItemFoodAddOnsRepository itemFoodAddOnsRepository;
    private final CustomerRepository customerRepository;
    private final TableRepository tableRepository;
    private final OrderMapper orderMapper;

    // ─────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────

    public OrderDto createOrder(CreateOrderRequestDto dto, User createdByUser) {
        Order order = buildOrder(dto, createdByUser);
        order = orderRepository.save(order);

        // Generate human-readable order number after persistence
        order.setOrderNumber(generateOrderNumber(order.getId()));
        order = orderRepository.save(order);

        // For PLACE_ORDER, create kitchen orders grouped by kitchen
        if (OrderType.PLACE_ORDER.equals(order.getOrderType())) {
            createKitchenOrders(order);
        }

        return orderMapper.toDto(order);
    }

    // ─────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public OrderDto getById(Long id) {
        Order order = findOrderOrThrow(id);
        return orderMapper.toDto(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderDto> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<OrderDto> getByStatus( Pageable pageable, OrderStatus status) {
        return orderRepository.findByStatus(pageable, status).map(orderMapper::toDto);
    }

    // ─────────────────────────────────────────────
    // UPDATE ITEMS
    // ─────────────────────────────────────────────

    public OrderDto addItem(Long orderId, OrderItemRequestDto itemDto) {
        Order order = findOrderOrThrow(orderId);
        validateOrderEditable(order);

        OrderItem newItem = buildOrderItem(itemDto, order);
        order.getOrderItems().add(newItem);
        recalculateTotal(order);
        order = orderRepository.save(order);

        // Create kitchen order item for the new item if PLACE_ORDER
        if (OrderType.PLACE_ORDER.equals(order.getOrderType())) {
            appendKitchenOrderItem(order, newItem);
        }

        return orderMapper.toDto(order);
    }

    public OrderDto removeItem(Long orderId, Long orderItemId) {
        Order order = findOrderOrThrow(orderId);
        validateOrderEditable(order);

        OrderItem item = order.getOrderItems().stream()
                .filter(i -> i.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + orderItemId));

        validateKitchenItemNotPrepared(item);

        order.getOrderItems().remove(item);
        recalculateTotal(order);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    public OrderDto updateItemQuantity(Long orderId, Long orderItemId, int newQuantity) {
        if (newQuantity < 1) {
            throw new BadRequestException("Quantity must be at least 1");
        }
        Order order = findOrderOrThrow(orderId);
        validateOrderEditable(order);

        OrderItem item = order.getOrderItems().stream()
                .filter(i -> i.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + orderItemId));

        validateKitchenItemNotPrepared(item);
        item.setQuantity(newQuantity);
        item.setTotalPrice((item.getPrice() + item.getAddOnsPrice()) * newQuantity);
        recalculateTotal(order);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    // ─────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────

    public void delete(Long id) {
        Order order = findOrderOrThrow(id);
        // Cannot delete if any kitchen order item has been accepted or further
        boolean hasAcceptedKitchenItems = order.getKitchenOrders().stream()
                .flatMap(ko -> ko.getKitchenOrderItems().stream())
                .anyMatch(ki -> ki.getStatus() != KitchenOrderItemStatus.PENDING);
        if (hasAcceptedKitchenItems) {
            throw new BadRequestException("msg_cannot_delete_order_with_accepted_kitchen_items");
        }
        orderRepository.delete(order);
    }

    // ─────────────────────────────────────────────
    // MERGE
    // ─────────────────────────────────────────────

    public OrderDto mergeOrders(MergeOrdersDto dto) {
        List<Order> orders = dto.getOrderIds().stream()
                .map(this::findOrderOrThrow)
                .collect(Collectors.toList());

        for (Order o : orders) {
            if (Set.of(OrderStatus.COMPLETED, OrderStatus.CHECKED_OUT, OrderStatus.CANCELLED, OrderStatus.MERGED)
                    .contains(o.getStatus())) {
                throw new BadRequestException(
                        "Cannot merge order #" + o.getOrderNumber() + " with status: " + o.getStatus());
            }
        }

        // Use first order as the target; move all items from the rest into it
        Order target = orders.get(0);
        for (int i = 1; i < orders.size(); i++) {
            Order source = orders.get(i);
            for (OrderItem item : source.getOrderItems()) {
                // Check if same itemFood + variant already exists in target, merge quantities
                Optional<OrderItem> existing = target.getOrderItems().stream()
                        .filter(ti -> ti.getItemFood().getId().equals(item.getItemFood().getId())
                                && Objects.equals(
                                        ti.getVariant() != null ? ti.getVariant().getId() : null,
                                        item.getVariant() != null ? item.getVariant().getId() : null))
                        .findFirst();

                if (existing.isPresent()) {
                    OrderItem targetItem = existing.get();
                    targetItem.setQuantity(targetItem.getQuantity() + item.getQuantity());
                    targetItem.setTotalPrice(
                            (targetItem.getPrice() + targetItem.getAddOnsPrice()) * targetItem.getQuantity());
                } else {
                    item.setOrder(target);
                    target.getOrderItems().add(item);
                }
            }
            source.getOrderItems().clear();
            source.setStatus(OrderStatus.MERGED);
            orderRepository.save(source);
        }

        recalculateTotal(target);
        target.setNotes(dto.getNotes() != null ? dto.getNotes() : target.getNotes());
        target = orderRepository.save(target);

        // Re-sync kitchen orders for the merged order
        if (OrderType.PLACE_ORDER.equals(target.getOrderType())) {
            syncKitchenOrders(target);
        }

        return orderMapper.toDto(target);
    }

    // ─────────────────────────────────────────────
    // SPLIT
    // ─────────────────────────────────────────────

    public List<OrderDto> splitOrder(Long orderId, SplitOrderDto dto) {
        Order original = findOrderOrThrow(orderId);

        if (Set.of(OrderStatus.CHECKED_OUT, OrderStatus.CANCELLED, OrderStatus.MERGED)
                .contains(original.getStatus())) {
            throw new BadRequestException("Cannot split order with status: " + original.getStatus());
        }

        List<Order> splitOrders = new ArrayList<>();

        for (List<SplitOrderItemDto> group : dto.getSplitGroups()) {
            Order splitOrder = Order.builder()
                    .orderNumber("TEMP")
                    .orderType(original.getOrderType())
                    .status(OrderStatus.NEW)
                    .customerType(original.getCustomerType())
                    .customer(original.getCustomer())
                    .thirdPartyCustomer(original.getThirdPartyCustomer())
                    .table(original.getTable())
                    .waiter(original.getWaiter())
                    .cashRegister(original.getCashRegister())
                    .totalAmount(0.0)
                    .notes(dto.getNotes())
                    .parentOrderId(original.getId())
                    .build();

            splitOrder = orderRepository.save(splitOrder);
            splitOrder.setOrderNumber(generateOrderNumber(splitOrder.getId()));

            for (SplitOrderItemDto splitItem : group) {
                OrderItem sourceItem = original.getOrderItems().stream()
                        .filter(i -> i.getId().equals(splitItem.getOrderItemId()))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Order item not found: " + splitItem.getOrderItemId()));

                validateKitchenItemNotPrepared(sourceItem);

                int quantityToMove = splitItem.getQuantity();
                if (quantityToMove > sourceItem.getQuantity()) {
                    throw new BadRequestException("Cannot split more than available quantity for item: "
                            + sourceItem.getItemFoodName());
                }

                // Create new item in split order
                OrderItem newItem = OrderItem.builder()
                        .order(splitOrder)
                        .itemFood(sourceItem.getItemFood())
                        .itemFoodName(sourceItem.getItemFoodName())
                        .price(sourceItem.getPrice())
                        .quantity(quantityToMove)
                        .variant(sourceItem.getVariant())
                        .variantName(sourceItem.getVariantName())
                        .addOnsPrice(sourceItem.getAddOnsPrice())
                        .totalPrice((sourceItem.getPrice() + sourceItem.getAddOnsPrice()) * quantityToMove)
                        .notes(sourceItem.getNotes())
                        .build();

                splitOrder.getOrderItems().add(newItem);

                // Reduce quantity in original
                int remaining = sourceItem.getQuantity() - quantityToMove;
                if (remaining == 0) {
                    original.getOrderItems().remove(sourceItem);
                } else {
                    sourceItem.setQuantity(remaining);
                    sourceItem.setTotalPrice(
                            (sourceItem.getPrice() + sourceItem.getAddOnsPrice()) * remaining);
                }
            }

            recalculateTotal(splitOrder);
            splitOrder = orderRepository.save(splitOrder);

            if (OrderType.PLACE_ORDER.equals(splitOrder.getOrderType())) {
                createKitchenOrders(splitOrder);
            }

            splitOrders.add(splitOrder);
        }

        original.setStatus(OrderStatus.SPLIT);
        recalculateTotal(original);
        orderRepository.save(original);

        return splitOrders.stream().map(orderMapper::toDto).toList();
    }

    // ─────────────────────────────────────────────
    // COMPLETE
    // ─────────────────────────────────────────────

    public OrderDto completeOrder(Long id) {
        Order order = findOrderOrThrow(id);
        if (!Set.of(OrderStatus.NEW, OrderStatus.CONFIRMED, OrderStatus.IN_PROGRESS, OrderStatus.READY)
                .contains(order.getStatus())) {
            throw new BadRequestException("Cannot complete order with status: " + order.getStatus());
        }
        order.setStatus(OrderStatus.COMPLETED);
        order.setCompletedAt(LocalDateTime.now());
        return orderMapper.toDto(orderRepository.save(order));
    }

    // ─────────────────────────────────────────────
    // CHECKOUT
    // ─────────────────────────────────────────────

    public JournalEntryDTO checkout(Long orderId, CheckoutDto dto) {
        Order order = findOrderOrThrow(orderId);

        if (!OrderStatus.COMPLETED.equals(order.getStatus())) {
            throw new BadRequestException("Cannot checkout order that is not completed");
        }

        JournalEntry journalEntry = financialPostingService.postOrderSale(
                dto.getPaidAmount(),
                order.getOrderNumber(),
                dto.isCash()
        );

        if (dto.getPaidAmount().compareTo(java.math.BigDecimal.valueOf(order.getTotalAmount())) >= 0) {
            order.setStatus(OrderStatus.CHECKED_OUT);
            orderRepository.save(order);
        }

        return journalMapper.toDTO(journalEntry);
    }

    // ─────────────────────────────────────────────
    // TRACKING
    // ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public OrderTrackingDto getTracking(Long orderId) {
        Order order = findOrderOrThrow(orderId);

        int totalItems = order.getKitchenOrders().stream()
                .mapToInt(ko -> ko.getKitchenOrderItems().size())
                .sum();

        int readyItems = order.getKitchenOrders().stream()
                .flatMap(ko -> ko.getKitchenOrderItems().stream())
                .filter(ki -> ki.getStatus() == KitchenOrderItemStatus.READY
                        || ki.getStatus() == KitchenOrderItemStatus.SERVED)
                .mapToInt(ki -> 1)
                .sum();

        List<OrderTrackingDto.KitchenProgressDto> kitchenProgress = order.getKitchenOrders().stream()
                .map(ko -> OrderTrackingDto.KitchenProgressDto.builder()
                        .kitchenOrderId(ko.getId())
                        .kitchenName(ko.getKitchen().getName())
                        .kitchenOrderStatus(ko.getStatus())
                        .items(ko.getKitchenOrderItems().stream()
                                .map(ki -> OrderTrackingDto.ItemProgressDto.builder()
                                        .kitchenOrderItemId(ki.getId())
                                        .itemFoodName(ki.getOrderItem().getItemFoodName())
                                        .quantity(ki.getOrderItem().getQuantity())
                                        .itemStatus(ki.getStatus())
                                        .build())
                                .toList())
                        .build())
                .toList();

        String overallStatus = totalItems == 0 ? "NO_KITCHEN_ITEMS"
                : readyItems == totalItems ? "ALL_READY"
                : readyItems > 0 ? "PARTIALLY_READY"
                : "PREPARING";

        return OrderTrackingDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .overallStatus(overallStatus)
                .totalItems(totalItems)
                .readyItems(readyItems)
                .kitchenProgress(kitchenProgress)
                .build();
    }

    // ─────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────

    private Order buildOrder(CreateOrderRequestDto dto, User createdByUser) {
        RestaurantTable table = null;
        if (dto.getTableId() != null) {
            table = tableRepository.findById(dto.getTableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Table not found: " + dto.getTableId()));
        }
        if (OrderType.PLACE_ORDER.equals(dto.getOrderType()) && table == null) {
            throw new BadRequestException("Table is required for Place Order");
        }

        Customer customer = null;
        if (dto.getCustomerId() != null) {
            customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + dto.getCustomerId()));
        }

        Customer thirdPartyCustomer = null;
        if (dto.getThirdPartyCustomerId() != null) {
            thirdPartyCustomer = customerRepository.findById(dto.getThirdPartyCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Third party customer not found: " + dto.getThirdPartyCustomerId()));
        }

        User waiter = null;
        if (dto.getWaiterId() != null) {
            waiter = new User();
            waiter.setId(dto.getWaiterId());
        }

        CashRegister cashRegister = null;
        if (dto.getCashRegisterId() != null) {
            cashRegister = new CashRegister();
            cashRegister.setId(dto.getCashRegisterId());
        }

        Order order = Order.builder()
                .orderNumber("TEMP")
                .orderType(dto.getOrderType())
                .status(OrderStatus.NEW)
                .customerType(dto.getCustomerType())
                .customer(customer)
                .thirdPartyCustomer(thirdPartyCustomer)
                .table(table)
                .waiter(waiter)
                .cashRegister(cashRegister)
                .totalAmount(0.0)
                .notes(dto.getNotes())
                .build();

        List<OrderItem> items = dto.getOrderItems().stream()
                .map(itemDto -> buildOrderItem(itemDto, order))
                .collect(Collectors.toList());

        order.setOrderItems(items);
        recalculateTotal(order);
        return order;
    }

    private OrderItem buildOrderItem(OrderItemRequestDto dto, Order order) {
        ItemFood itemFood = itemFoodRepository.findById(dto.getItemFoodId())
                .orElseThrow(() -> new ResourceNotFoundException("Item food not found: " + dto.getItemFoodId()));

        ItemFoodVariant variant = null;
        if (dto.getVariantId() != null) {
            variant = itemFoodVariantRepository.findById(dto.getVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Variant not found: " + dto.getVariantId()));
        }

        double addOnsPrice = dto.getAddOnsPrice() != null ? dto.getAddOnsPrice() : 0.0;
        double totalPrice = (dto.getPrice() + addOnsPrice) * dto.getQuantity();

        OrderItem item = OrderItem.builder()
                .order(order)
                .itemFood(itemFood)
                .itemFoodName(itemFood.getName())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .variant(variant)
                .variantName(dto.getVariantName())
                .addOnsPrice(addOnsPrice)
                .totalPrice(totalPrice)
                .notes(dto.getNotes())
                .build();

        if (dto.getAddOnIds() != null && !dto.getAddOnIds().isEmpty()) {
            List<OrderItemAddOn> addOns = dto.getAddOnIds().stream()
                    .map(addOnId -> {
                        ItemFoodAddOns addOn = itemFoodAddOnsRepository.findById(addOnId)
                                .orElseThrow(() -> new ResourceNotFoundException("Add-on not found: " + addOnId));
                        return OrderItemAddOn.builder()
                                .orderItem(item)
                                .addOn(addOn)
                                .addOnName(addOn.getName())
                                .price(addOn.getPrice())
                                .build();
                    })
                    .collect(Collectors.toList());
            item.setOrderItemAddOns(addOns);
        }

        return item;
    }

    private void createKitchenOrders(Order order) {
        // Group items by kitchen
        Map<Kitchen, List<OrderItem>> itemsByKitchen = order.getOrderItems().stream()
                .filter(item -> item.getItemFood().getKitchen() != null)
                .collect(Collectors.groupingBy(item -> item.getItemFood().getKitchen()));

        for (Map.Entry<Kitchen, List<OrderItem>> entry : itemsByKitchen.entrySet()) {
            KitchenOrder kitchenOrder = KitchenOrder.builder()
                    .order(order)
                    .kitchen(entry.getKey())
                    .status(KitchenOrderStatus.PENDING)
                    .build();

            List<KitchenOrderItem> kitchenItems = entry.getValue().stream()
                    .map(item -> KitchenOrderItem.builder()
                            .kitchenOrder(kitchenOrder)
                            .orderItem(item)
                            .status(KitchenOrderItemStatus.PENDING)
                            .build())
                    .collect(Collectors.toList());

            kitchenOrder.setKitchenOrderItems(kitchenItems);
            order.getKitchenOrders().add(kitchenOrder);
            kitchenOrderRepository.save(kitchenOrder);
        }
    }

    private void appendKitchenOrderItem(Order order, OrderItem newItem) {
        if (newItem.getItemFood().getKitchen() == null) return;

        Kitchen kitchen = newItem.getItemFood().getKitchen();
        Optional<KitchenOrder> existingKitchenOrder = order.getKitchenOrders().stream()
                .filter(ko -> ko.getKitchen().getId().equals(kitchen.getId())
                        && ko.getStatus() == KitchenOrderStatus.PENDING)
                .findFirst();

        if (existingKitchenOrder.isPresent()) {
            KitchenOrderItem kitchenItem = KitchenOrderItem.builder()
                    .kitchenOrder(existingKitchenOrder.get())
                    .orderItem(newItem)
                    .status(KitchenOrderItemStatus.PENDING)
                    .build();
            existingKitchenOrder.get().getKitchenOrderItems().add(kitchenItem);
            kitchenOrderRepository.save(existingKitchenOrder.get());
        } else {
            KitchenOrder kitchenOrder = KitchenOrder.builder()
                    .order(order)
                    .kitchen(kitchen)
                    .status(KitchenOrderStatus.PENDING)
                    .build();
            KitchenOrderItem kitchenItem = KitchenOrderItem.builder()
                    .kitchenOrder(kitchenOrder)
                    .orderItem(newItem)
                    .status(KitchenOrderItemStatus.PENDING)
                    .build();
            kitchenOrder.getKitchenOrderItems().add(kitchenItem);
            order.getKitchenOrders().add(kitchenOrder);
            kitchenOrderRepository.save(kitchenOrder);
        }
    }

    private void syncKitchenOrders(Order order) {
        // Remove existing pending kitchen orders and recreate
        order.getKitchenOrders().removeIf(ko -> ko.getStatus() == KitchenOrderStatus.PENDING);
        createKitchenOrders(order);
    }

    private void recalculateTotal(Order order) {
        double total = order.getOrderItems().stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
        order.setTotalAmount(total);
    }

    private void validateOrderEditable(Order order) {
        if (Set.of(OrderStatus.COMPLETED, OrderStatus.CHECKED_OUT, OrderStatus.CANCELLED, OrderStatus.MERGED)
                .contains(order.getStatus())) {
            throw new BadRequestException("Cannot modify order with status: " + order.getStatus());
        }
    }

    private void validateKitchenItemNotPrepared(OrderItem item) {
        boolean isPrepared = item.getOrder().getKitchenOrders().stream()
                .flatMap(ko -> ko.getKitchenOrderItems().stream())
                .filter(ki -> ki.getOrderItem().getId().equals(item.getId()))
                .anyMatch(ki -> ki.getStatus() != KitchenOrderItemStatus.PENDING);
        if (isPrepared) {
            throw new BadRequestException(
                    "Cannot modify item '" + item.getItemFoodName() + "' that is already accepted by the kitchen");
        }
    }

    private Order findOrderOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    private String generateOrderNumber(Long id) {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "ORD-" + datePart + "-" + String.format("%04d", id);
    }

}

