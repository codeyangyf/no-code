package com.lc.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TenantDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        private String tenantCode;
        private String tenantName;
        private String domain;
        private LocalDateTime expireTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String tenantName;
        private String domain;
        private Integer status;
        private LocalDateTime expireTime;
        private Long version;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TenantResponse {
        private Long id;
        private String tenantCode;
        private String tenantName;
        private String logoUrl;
        private String domain;
        private Integer status;
        private Long version;
        private LocalDateTime expireTime;
        private LocalDateTime createdTime;
        private LocalDateTime updatedTime;
    }
}
