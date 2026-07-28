package com.lc.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ProjectDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        private String projectName;
        private String projectCode;
        private String description;
        private String icon;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String projectName;
        private String description;
        private String icon;
        private Integer status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateStatusRequest {
        private String lifecycleStatus;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectResponse {
        private Long id;
        private Long tenantId;
        private String projectCode;
        private String projectName;
        private String description;
        private String icon;
        private Integer status;
        private String lifecycleStatus;
        private Long createdBy;
        private LocalDateTime createdTime;
        private LocalDateTime updatedTime;
    }
}
