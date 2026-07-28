package com.lc.project.dto.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class FormDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        private Long projectId;
        private String formCode;
        private String formName;
        private List<FieldDTO> fields;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String formName;
        private List<FieldDTO> fields;
        private Integer status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldDTO {
        private String id;
        private String fieldCode;
        private String fieldName;
        private String fieldType;
        private Object fieldConfig;
        private Integer sortOrder;
        private Boolean required;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FormResponse {
        private Long id;
        private Long projectId;
        private String formCode;
        private String formName;
        private List<FieldDTO> fields;
        private Integer status;
        private Integer version;
        private LocalDateTime createdTime;
        private LocalDateTime updatedTime;
    }
}
