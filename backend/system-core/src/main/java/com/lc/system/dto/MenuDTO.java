package com.lc.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class MenuDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        private Long parentId;
        private String menuName;
        private String path;
        private String component;
        private String icon;
        private String menuType;
        private String permission;
        private Integer sortOrder;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private Long parentId;
        private String menuName;
        private String path;
        private String component;
        private String icon;
        private String menuType;
        private String permission;
        private Integer sortOrder;
        private Integer status;
        private Long version;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuResponse {
        private Long id;
        private Long tenantId;
        private Long parentId;
        private String menuName;
        private String path;
        private String component;
        private String icon;
        private String menuType;
        private String permission;
        private Integer sortOrder;
        private Integer status;
        private Long version;
        private LocalDateTime createdTime;
        private LocalDateTime updatedTime;
        private List<MenuResponse> children;
    }
}
