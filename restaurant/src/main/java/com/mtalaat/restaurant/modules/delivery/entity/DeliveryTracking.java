package com.mtalaat.restaurant.modules.delivery.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A single GPS location snapshot from a driver during an active delivery.
 *
 * <p>Records are append-only: once written they are never updated or deleted
 * in normal operation. This ensures an immutable audit trail of the driver's route.</p>
 */
@Entity
@Table(name = "delivery_tracking")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DeliveryTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The delivery this tracking point belongs to.
     * LAZY: tracking points are loaded only for live-map use-cases.
     * No cascade from here — tracking points are owned via Delivery.trackingPoints.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    /** Speed in km/h at the time of recording. Null if unavailable. */
    @Column(name = "speed")
    private Double speed;

    /** Heading in degrees (0–360) relative to true north. Null if unavailable. */
    @Column(name = "heading")
    private Double heading;

    /** Auto-populated; never updated — record is immutable after insert. */
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
}
