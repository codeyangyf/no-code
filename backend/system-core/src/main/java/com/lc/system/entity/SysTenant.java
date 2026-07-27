package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_tenant")
@SQLRestriction("deleted = 0")
public class SysTenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_code", nullable = false, length = 64, unique = true)
    private String tenantCode;

    @Column(name = "tenant_name", nullable = false, length = 128)
    private String tenantName;

    @Column(name = "logo_url", length = 512)
    private String logoUrl;

    @Column(name = "domain", length = 256)
    private String domain;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "deleted", nullable = false)
    private Integer deleted = 0;

    @Version
    @Column(name = "version", nullable = false)
    private Long version = 0L;

    @Column(name = "expire_time")
    private LocalDateTime expireTime;

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