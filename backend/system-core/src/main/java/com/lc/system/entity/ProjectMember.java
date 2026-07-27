package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "project_member", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_id", "user_id"})
})
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "role", nullable = false, length = 32)
    private String role;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "joined_time", nullable = false)
    private LocalDateTime joinedTime;

    @PrePersist
    protected void onCreate() {
        joinedTime = LocalDateTime.now();
    }
}