package com.mtalaat.restaurant.modules.auth.entity;

import com.mtalaat.restaurant.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role_permissions")
public class RolePermission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private MenuItem menuItem;

    @Column(name = "can_read")
    private Boolean canRead;

    @Column(name = "can_create")
    private Boolean canCreate;

    @Column(name = "can_edit")
    private Boolean canEdit;

    @Column(name = "can_delete")
    private Boolean canDelete;
}
