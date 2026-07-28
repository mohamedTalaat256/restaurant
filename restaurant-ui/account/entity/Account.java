package com.mtalaat.restaurant.modules.account.entity;

import com.mtalaat.restaurant.modules.account.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "acc_accounts",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @Builder.Default
    @Column(nullable = false)
    private Boolean allowTransaction = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean status = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Account parent;

    @Column(nullable = false)
    private BigDecimal balance;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}