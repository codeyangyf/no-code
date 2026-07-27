package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_menu")
@SQLRestriction("deleted = 0")
public class SysMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "menu_name", nullable = false, length = 128)
    private String menuName;

    @Column(name = "path", length = 256)
    private String path;

    @Column(name = "component", length = 256)
    private String component;

    @Column(name = "icon", length = 128)
    private String icon;

    @Column(name = "menu_type", nullable = false, length = 16)
    private String menuType;

    @Column(name = "permission", length = 128)
    private String permission;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "deleted", nullable = false)
    private Integer deleted = 0;

    @Version
    @Column(name = "version", nullable = false)
    private Long version = 0L;

    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }
}