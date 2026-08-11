package com.mtalaat.restaurant.modules.delivery.entity;

import com.mtalaat.restaurant.modules.settings.entity.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A saved delivery address belonging to a customer.
 *
 * These are the customer's address book entries. They are NEVER used directly
 * on an order — instead, they are snapshotted into {@link OrderDeliveryAddress}
 * when the order is placed, so that later edits to this address do not alter
 * historical order data.
 */
@Entity
@Table(name = "delivery_addresses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The customer who owns this address.
     * LAZY: address lists are only needed when managing the customer profile.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /** Human-readable label, e.g. "Home", "Work", "Gym". */
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

    /** Marks the default address to pre-select during checkout. */
    @Builder.Default
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
