package com.mtalaat.restaurant.modules.delivery.service;

import com.mtalaat.restaurant.exceptions.BadRequestException;
import com.mtalaat.restaurant.exceptions.ResourceNotFoundException;
import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.delivery.dto.request.AssignDriverRequest;
import com.mtalaat.restaurant.modules.delivery.dto.request.CancelDeliveryRequest;
import com.mtalaat.restaurant.modules.delivery.dto.request.CreateDeliveryRequest;
import com.mtalaat.restaurant.modules.delivery.dto.request.ReassignDriverRequest;
import com.mtalaat.restaurant.modules.delivery.dto.response.DeliveryResponse;
import com.mtalaat.restaurant.modules.delivery.entity.Delivery;
import com.mtalaat.restaurant.modules.delivery.entity.DeliveryAssignment;
import com.mtalaat.restaurant.modules.delivery.entity.Driver;
import com.mtalaat.restaurant.modules.delivery.enums.DeliveryStatus;
import com.mtalaat.restaurant.modules.delivery.enums.DriverStatus;
import com.mtalaat.restaurant.modules.delivery.mapper.DeliveryMapper;
import com.mtalaat.restaurant.modules.delivery.repository.DeliveryAssignmentRepository;
import com.mtalaat.restaurant.modules.delivery.repository.DeliveryRepository;
import com.mtalaat.restaurant.modules.delivery.repository.DriverRepository;
import com.mtalaat.restaurant.modules.order.entity.Order;
import com.mtalaat.restaurant.modules.order.enums.OrderStatus;
import com.mtalaat.restaurant.modules.order.enums.OrderType;
import com.mtalaat.restaurant.modules.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Orchestrates the full delivery lifecycle:
 * create → assign → accept → pickup → on-the-way → delivered / cancelled / failed.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DriverRepository driverRepository;
    private final OrderRepository orderRepository;
    private final DeliveryAssignmentRepository assignmentRepository;
    private final DeliveryMapper deliveryMapper;

    // ─────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────

    /**
     * Creates a Delivery record for a DELIVERY-type order.
     *
     * <p>Business rules enforced:</p>
     * <ul>
     *   <li>Order must exist.</li>
     *   <li>Order must be of type DELIVERY.</li>
     *   <li>No delivery record must already exist for that order.</li>
     * </ul>
     */
    public DeliveryResponse createDelivery(CreateDeliveryRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("msg_order_not_found"));

        if (order.getOrderType() != OrderType.DELIVERY) {
            throw new BadRequestException("msg_order_not_delivery_type");
        }

        if (deliveryRepository.existsByOrderId(request.orderId())) {
            throw new BadRequestException("msg_delivery_already_exists");
        }

        Delivery delivery = Delivery.builder()
                .order(order)
                .status(DeliveryStatus.WAITING_ASSIGNMENT)
                .deliveryFee(request.deliveryFee())
                .estimatedDeliveryTime(request.estimatedDeliveryTime())
                .notes(request.notes())
                .build();

        return deliveryMapper.toResponse(deliveryRepository.save(delivery));
    }

    // ─────────────────────────────────────────────
    // ASSIGN DRIVER
    // ─────────────────────────────────────────────

    /**
     * Assigns a driver to a delivery.
     *
     * <p>Business rules:</p>
     * <ul>
     *   <li>Delivery must be in WAITING_ASSIGNMENT or CREATED status.</li>
     *   <li>Driver must be ONLINE.</li>
     *   <li>Driver must not have another active delivery.</li>
     * </ul>
     */
    public DeliveryResponse assignDriver(Long deliveryId, AssignDriverRequest request, User assignedBy) {
        Delivery delivery = findDeliveryById(deliveryId);
        validateNotTerminal(delivery);

        if (delivery.getStatus() != DeliveryStatus.WAITING_ASSIGNMENT
                && delivery.getStatus() != DeliveryStatus.CREATED) {
            throw new BadRequestException("msg_delivery_already_assigned");
        }

        Driver driver = findDriverById(request.driverId());
        validateDriverAvailable(driver);

        delivery.setDriver(driver);
        delivery.setStatus(DeliveryStatus.ASSIGNED);
        delivery.setAssignedAt(LocalDateTime.now());

        driver.setStatus(DriverStatus.BUSY);
        driver.setIsOnline(true);
        driverRepository.save(driver);

        recordAssignment(delivery, driver, assignedBy, request.reason());

        return deliveryMapper.toResponse(deliveryRepository.save(delivery));
    }

    // ─────────────────────────────────────────────
    // REASSIGN DRIVER
    // ─────────────────────────────────────────────

    /**
     * Reassigns a delivery to a different driver, freeing the current driver.
     *
     * <p>Business rules:</p>
     * <ul>
     *   <li>Delivery must not be in a terminal state.</li>
     *   <li>New driver must be ONLINE with no active deliveries.</li>
     *   <li>A mandatory reason must be supplied for audit trail.</li>
     * </ul>
     */
    public DeliveryResponse reassignDriver(Long deliveryId, ReassignDriverRequest request, User assignedBy) {
        Delivery delivery = findDeliveryById(deliveryId);
        validateNotTerminal(delivery);

        Driver newDriver = findDriverById(request.newDriverId());
        validateDriverAvailable(newDriver);

        // Free the current driver if any
        if (delivery.getDriver() != null) {
            Driver currentDriver = delivery.getDriver();
            currentDriver.setStatus(DriverStatus.ONLINE);
            driverRepository.save(currentDriver);
        }

        delivery.setDriver(newDriver);
        delivery.setStatus(DeliveryStatus.ASSIGNED);
        delivery.setAssignedAt(LocalDateTime.now());

        newDriver.setStatus(DriverStatus.BUSY);
        driverRepository.save(newDriver);

        recordAssignment(delivery, newDriver, assignedBy, request.reason());

        return deliveryMapper.toResponse(deliveryRepository.save(delivery));
    }

    // ─────────────────────────────────────────────
    // STATE TRANSITIONS
    // ─────────────────────────────────────────────

    /**
     * Driver accepts the delivery request.
     * Transition: ASSIGNED → ACCEPTED
     */
    public DeliveryResponse acceptDelivery(Long deliveryId) {
        Delivery delivery = findDeliveryById(deliveryId);
        requireStatus(delivery, DeliveryStatus.ASSIGNED, "msg_delivery_not_assigned");
        delivery.setStatus(DeliveryStatus.ACCEPTED);
        delivery.setAcceptedAt(LocalDateTime.now());
        return deliveryMapper.toResponse(deliveryRepository.save(delivery));
    }

    /**
     * Driver arrived at the restaurant.
     * Transition: ACCEPTED → ARRIVED_AT_RESTAURANT
     */
    public DeliveryResponse arrivedAtRestaurant(Long deliveryId) {
        Delivery delivery = findDeliveryById(deliveryId);
        requireStatus(delivery, DeliveryStatus.ACCEPTED, "msg_delivery_not_accepted");
        delivery.setStatus(DeliveryStatus.ARRIVED_AT_RESTAURANT);
        return deliveryMapper.toResponse(deliveryRepository.save(delivery));
    }

    /**
     * Driver picked up the order from the restaurant.
     * Transition: ARRIVED_AT_RESTAURANT → PICKED_UP
     *
     * <p>Business rule: order must be READY before pickup.</p>
     */
    public DeliveryResponse pickupOrder(Long deliveryId) {
        Delivery delivery = findDeliveryById(deliveryId);
        requireStatus(delivery, DeliveryStatus.ARRIVED_AT_RESTAURANT, "msg_delivery_not_arrived");

        Order order = delivery.getOrder();
        if (order.getStatus() != OrderStatus.READY) {
            throw new BadRequestException("msg_order_not_ready_for_pickup");
        }

        delivery.setStatus(DeliveryStatus.PICKED_UP);
        delivery.setPickedUpAt(LocalDateTime.now());
        return deliveryMapper.toResponse(deliveryRepository.save(delivery));
    }

    /**
     * Driver is on the way to the customer.
     * Transition: PICKED_UP → ON_THE_WAY
     */
    public DeliveryResponse startDelivery(Long deliveryId) {
        Delivery delivery = findDeliveryById(deliveryId);
        requireStatus(delivery, DeliveryStatus.PICKED_UP, "msg_delivery_not_picked_up");
        delivery.setStatus(DeliveryStatus.ON_THE_WAY);
        return deliveryMapper.toResponse(deliveryRepository.save(delivery));
    }

    /**
     * Order successfully delivered to the customer.
     * Transition: ON_THE_WAY → DELIVERED
     *
     * <p>Business rule: delivery cannot be completed before pickup.</p>
     */
    public DeliveryResponse completeDelivery(Long deliveryId) {
        Delivery delivery = findDeliveryById(deliveryId);
        requireStatus(delivery, DeliveryStatus.ON_THE_WAY, "msg_delivery_not_on_the_way");

        if (delivery.getPickedUpAt() == null) {
            throw new BadRequestException("msg_delivery_not_picked_up_yet");
        }

        delivery.setStatus(DeliveryStatus.DELIVERED);
        delivery.setDeliveredAt(LocalDateTime.now());
        delivery.setActualDeliveryTime(LocalDateTime.now());

        // Release the driver
        freeDriver(delivery.getDriver());

        return deliveryMapper.toResponse(deliveryRepository.save(delivery));
    }

    /**
     * Cancels a non-terminal delivery.
     * Business rule: cancelled deliveries cannot be updated afterward.
     */
    public DeliveryResponse cancelDelivery(Long deliveryId, CancelDeliveryRequest request) {
        Delivery delivery = findDeliveryById(deliveryId);
        validateNotTerminal(delivery);

        delivery.setStatus(DeliveryStatus.CANCELLED);
        delivery.setCancelledAt(LocalDateTime.now());

        if (request.reason() != null) {
            delivery.setNotes(request.reason());
        }

        freeDriver(delivery.getDriver());

        return deliveryMapper.toResponse(deliveryRepository.save(delivery));
    }

    // ─────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public DeliveryResponse getDeliveryById(Long deliveryId) {
        return deliveryMapper.toResponse(findDeliveryById(deliveryId));
    }

    @Transactional(readOnly = true)
    public DeliveryResponse getDeliveryByOrderId(Long orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("msg_delivery_not_found"));
        return deliveryMapper.toResponse(delivery);
    }

    @Transactional(readOnly = true)
    public List<DeliveryResponse> getAllDeliveries() {
        return deliveryRepository.findAll().stream()
                .map(deliveryMapper::toResponse)
                .toList();
    }

    // ─────────────────────────────────────────────
    // INTERNAL HELPERS
    // ─────────────────────────────────────────────

    private Delivery findDeliveryById(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("msg_delivery_not_found"));
    }

    private Driver findDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("msg_driver_not_found"));
    }

    private void validateDriverAvailable(Driver driver) {
        if (!driver.getIsOnline() || driver.getStatus() != DriverStatus.ONLINE) {
            throw new BadRequestException("msg_driver_not_available");
        }
        deliveryRepository.findActiveDeliveryByDriverId(driver.getId()).ifPresent(d -> {
            throw new BadRequestException("msg_driver_has_active_delivery");
        });
    }

    private void validateNotTerminal(Delivery delivery) {
        DeliveryStatus status = delivery.getStatus();
        if (status == DeliveryStatus.DELIVERED
                || status == DeliveryStatus.CANCELLED
                || status == DeliveryStatus.FAILED) {
            throw new BadRequestException("msg_delivery_in_terminal_state");
        }
    }

    private void requireStatus(Delivery delivery, DeliveryStatus expected, String errorKey) {
        if (delivery.getStatus() != expected) {
            throw new BadRequestException(errorKey);
        }
    }

    private void recordAssignment(Delivery delivery, Driver driver, User assignedBy, String reason) {
        DeliveryAssignment assignment = DeliveryAssignment.builder()
                .delivery(delivery)
                .driver(driver)
                .assignedBy(assignedBy)
                .reason(reason)
                .build();
        assignmentRepository.save(assignment);
    }

    private void freeDriver(Driver driver) {
        if (driver != null) {
            driver.setStatus(DriverStatus.ONLINE);
            driverRepository.save(driver);
        }
    }
}
