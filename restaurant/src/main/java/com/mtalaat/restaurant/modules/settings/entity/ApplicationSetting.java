package com.mtalaat.restaurant.modules.settings.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "application_settings")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_title", nullable = false)
    private String applicationTitle;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "icon")
    private String icon;

    @Column(name = "logo")
    private String logo;

    @Column(name = "opening_time")
    private String openingTime;

    @Column(name = "closing_time")
    private String closingTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;

    @Column(name = "discount_percentage")
    private Double discountPercentage;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_charge_type")
    private ServiceChargeType serviceChargeType;

    @Column(name = "tax_percentage")
    private Double taxPercentage;

    @Column(name = "tax_number")
    private String taxNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_code")
    private Language language;

    @Column(name = "date_format")
    private String dateFormat;

    @Column(name = "timezone")
    private String timezone;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_direction")
    private ApplicationDirection applicationDirection;

    @Column(name = "powered_by_text")
    private String poweredByText;

    @Column(name = "footer_text")
    private String footerText;
}
