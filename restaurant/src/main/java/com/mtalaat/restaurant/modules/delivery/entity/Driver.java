package com.mtalaat.restaurant.modules.delivery.entity;

import com.mtalaat.restaurant.modules.delivery.enums.DriverStatus;
import com.mtalaat.restaurant.modules.delivery.enums.VehicleType;
import com.mtalaat.restaurant.modules.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Represents a delivery driver, extending a system User with driver-specific information.
 *
 * Design note: Driver is a separate entity (not a User subtype) to avoid table-per-hierarchy
 * complexity and to allow any User to be promoted to a driver profile independently.
 */
@Entity
@Table(name = "drivers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The underlying system user for this driver.
     * LAZY: we only need driver-specific fields most of the time.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Column(name = "vehicle_plate", nullable = false, length = 20)
    private String vehiclePlate;

    /**
     * Driver's contact phone, may differ from the User account phone.
     */
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    /**
     * Convenience flag: true when the driver is actively available for assignments.
     * Mirrors DriverStatus.ONLINE but allows quick indexed queries.
     */
    @Builder.Default
    @Column(name = "is_online", nullable = false)
    private Boolean isOnline = false;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DriverStatus status = DriverStatus.OFFLINE;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
