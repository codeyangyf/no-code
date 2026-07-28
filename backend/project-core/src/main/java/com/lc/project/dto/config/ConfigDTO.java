package com.lc.project.dto.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ConfigDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectConfig {
        private String schemaVersion;
        private ProjectMeta project;
        private List<PageConfig> pages;
        private List<DataModelConfig> dataModels;
        private List<DataSourceConfig> dataSources;
        private List<ActionConfig> actions;
        private List<PermissionConfig> permissions;
        private List<AssetConfig> assets;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectMeta {
        private String id;
        private String code;
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageConfig {
        private String id;
        private String code;
        private String name;
        private String path;
        private String layout;
        private List<ComponentConfig> components;
        private Integer status;
        private Integer version;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComponentConfig {
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
    public static class DataModelConfig {
        private String id;
        private String code;
        private String name;
        private String tableName;
        private List<ModelFieldConfig> fields;
        private Integer status;
        private Integer version;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModelFieldConfig {
        private String id;
        private String code;
        private String name;
        private String type;
        private Object constraints;
        private Integer sortOrder;
        private Boolean required;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataSourceConfig {
        private String id;
        private String name;
        private String type;
        private Object connectionParams;
        private String secretRef;
        private Object networkPolicy;
        private Integer status;
        private Integer version;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionConfig {
        private String id;
        private String name;
        private String type;
        private Object config;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PermissionConfig {
        private String id;
        private String name;
        private String code;
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssetConfig {
        private String id;
        private String name;
        private String type;
        private String storageKey;
        private String mimeType;
        private Long size;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveRequest {
        private Long projectId;
        private ProjectConfig config;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfigResponse {
        private Long id;
        private Long projectId;
        private ProjectConfig config;
        private String schemaVersion;
        private Integer version;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationResult {
        private Boolean valid;
        private List<String> errors;
        private List<String> warnings;
    }
}
