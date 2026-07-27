package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_org")
public class SysOrg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "org_code", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "org_name", nullable = false, length = 128)
    private String orgName;

    @Column(name = "org_type", length = 32)
    private String orgType;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

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