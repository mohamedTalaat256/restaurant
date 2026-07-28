package com.mtalaat.restaurant.modules.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name= "is_page", nullable = false)
    private Boolean isPage;

    @Column(name = "page_url")
    private String pageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private ModuleEntity module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_menu_id")
    private MenuItem parentMenu;

    @Builder.Default
    @OneToMany(mappedBy = "parentMenu", cascade = CascadeType.ALL)
    private Set<MenuItem> children = new HashSet<>();

    @Column(name = "is_report")
    private Boolean isReport;

    @Builder.Default
    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RolePermission> rolePermissions = new HashSet<>();
}
