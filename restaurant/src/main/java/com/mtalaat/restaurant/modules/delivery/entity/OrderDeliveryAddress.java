package com.mtalaat.restaurant.modules.delivery.entity;

import com.mtalaat.restaurant.modules.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * An immutable snapshot of the delivery address captured at the moment an order is placed.
 *
 * <p><strong>Why snapshot?</strong> If the customer later edits their saved address, it must
 * not silently change the destination of an in-flight delivery. This record is write-once:
 * created when the order is confirmed and never updated thereafter.</p>
 */
@Entity
@Table(name = "order_delivery_addresses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderDeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The order this address snapshot belongs to.
     * FK lives here (on the "owned" side of the 1-to-1).
     * LAZY: we only need the address when displaying delivery details.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    /** Label copied from the source DeliveryAddress at snapshot time. */
    @Column(name = "label", length = 50)
    private String label;

    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "building_number", length = 20)
    private String buildingNumber;

    @Column(name = "floor_number", length = 10)
    private String floorNumber;

    @Column(name = "apartment_number", length = 10)
    private String apartmentNumber;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "additional_notes", length = 500)
    private String additionalNotes;

    /**
     * Timestamp only — no updatedAt because this record is append-only.
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
}
