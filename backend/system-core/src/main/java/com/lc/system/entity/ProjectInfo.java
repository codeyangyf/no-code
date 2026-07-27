package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "project_info")
public class ProjectInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "project_code", nullable = false, length = 64)
    private String projectCode;

    @Column(name = "project_name", nullable = false, length = 128)
    private String projectName;

    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "icon", length = 128)
    private String icon;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "lifecycle_status", nullable = false, length = 32)
    private String lifecycleStatus = "ACTIVE";

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

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