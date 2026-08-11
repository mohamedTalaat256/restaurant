package com.mtalaat.restaurant.modules.delivery.entity;

import com.mtalaat.restaurant.modules.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Audit record of every driver assignment (and re-assignment) for a delivery.
 *
 * <p>This entity is append-only. When a driver is reassigned, a new record is created
 * rather than updating the previous one, preserving the full assignment history.</p>
 */
@Entity
@Table(name = "delivery_assignments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DeliveryAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The delivery this assignment belongs to.
     * LAZY: we usually navigate from delivery → assignments, not the reverse.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    /**
     * The driver assigned in this record.
     * LAZY: driver data loaded only when displaying assignment history.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    /**
     * The system user who performed this assignment (dispatcher/manager).
     * LAZY: audit data, rarely needed in hot paths.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")
    private User assignedBy;

    /** When the assignment was made. Set automatically via @CreatedDate. */
    @CreatedDate
    @Column(name = "assigned_at", updatable = false, nullable = false)
    private LocalDateTime assignedAt;

    /** Optional reason for reassignment (e.g. "Driver unavailable", "Driver request"). */
    @Column(name = "reason", length = 500)
    private String reason;
}
