package com.lc.project.dto.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PageDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        private Long projectId;
        private String pageCode;
        private String pageName;
        private String path;
        private String layout;
        private List<ComponentDTO> components;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String pageName;
        private String path;
        private String layout;
        private List<ComponentDTO> components;
        private Integer status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComponentDTO {
        private String id;
        private String type;
        private String name;
        private String parentId;
        private Object props;
        private Object style;
        private Integer sortOrder;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageResponse {
        private Long id;
        private Long projectId;
        private String pageCode;
        private String pageName;
        private String path;
        private String layout;
        private List<ComponentDTO> components;
        private Integer status;
        private Integer version;
        private LocalDateTime createdTime;
        private LocalDateTime updatedTime;
    }
}
