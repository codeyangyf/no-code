package com.lc.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class RoleDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        private String roleCode;
        private String roleName;
        private String description;
        private Integer sortOrder;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String roleName;
        private String description;
        private Integer sortOrder;
        private Integer status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleResponse {
        private Long id;
        private Long tenantId;
        private String roleCode;
        private String roleName;
        private String description;
        private Integer status;
        private Integer sortOrder;
        private LocalDateTime createdTime;
        private LocalDateTime updatedTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignMenusRequest {
        private List<Long> menuIds;
    }
}
