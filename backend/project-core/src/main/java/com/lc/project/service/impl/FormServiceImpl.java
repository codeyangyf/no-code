package com.lc.project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.common.dto.PageResult;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.project.dto.form.FormDTO;
import com.lc.project.service.FormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormServiceImpl implements FormService {

    private final ObjectMapper objectMapper;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Override
    public PageResult<FormDTO.FormResponse> list(Long projectId, Integer page, Integer size) {
        String dbUrl = getProjectDatabaseUrl(projectId);

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(() -> conn);

            int offset = (page - 1) * size;
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                    "SELECT id, project_id, form_code, form_name, form_config, status, version, created_time, updated_time " +
                            "FROM form_form WHERE deleted = 0 ORDER BY created_time DESC LIMIT ? OFFSET ?",
                    size, offset
            );

            long total = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM form_form WHERE deleted = 0", Long.class
            );

            return PageResult.of(
                    rows.stream().map(this::toResponse).toList(),
                    total,
                    page,
                    size
            );
        } catch (SQLException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "查询表单列表失败: " + e.getMessage());
        }
    }

    @Override
    public FormDTO.FormResponse getById(Long projectId, Long id) {
        String dbUrl = getProjectDatabaseUrl(projectId);

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(() -> conn);

            Map<String, Object> row = jdbcTemplate.queryForMap(
                    "SELECT id, project_id, form_code, form_name, form_config, status, version, created_time, updated_time " +
                            "FROM form_form WHERE id = ? AND deleted = 0",
                    id
            );

            return toResponse(row);
        } catch (Exception e) {
            throw new BusinessException(GlobalErrorCode.NOT_FOUND);
        }
    }

    @Override
    public FormDTO.FormResponse getByCode(Long projectId, String formCode) {
        String dbUrl = getProjectDatabaseUrl(projectId);

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(() -> conn);

            Map<String, Object> row = jdbcTemplate.queryForMap(
                    "SELECT id, project_id, form_code, form_name, form_config, status, version, created_time, updated_time " +
                            "FROM form_form WHERE form_code = ? AND deleted = 0",
                    formCode
            );

            return toResponse(row);
        } catch (Exception e) {
            throw new BusinessException(GlobalErrorCode.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public FormDTO.FormResponse create(Long projectId, Long userId, FormDTO.CreateRequest request) {
        String dbUrl = getProjectDatabaseUrl(projectId);
        String configJson;

        try {
            var config = new java.util.LinkedHashMap<String, Object>();
            config.put("formCode", request.getFormCode());
            config.put("formName", request.getFormName());
            config.put("fields", request.getFields());
            configJson = objectMapper.writeValueAsString(config);
        } catch (JsonProcessingException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "表单配置序列化失败");
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(() -> conn);

            Integer exists = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM form_form WHERE form_code = ? AND deleted = 0",
                    Integer.class, request.getFormCode()
            );

            if (exists > 0) {
                throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "表单编码已存在");
            }

            jdbcTemplate.update(
                    "INSERT INTO form_form (project_id, form_code, form_name, form_config, status, version, deleted, created_by, created_time) " +
                            "VALUES (?, ?, ?, ?, 1, 1, 0, ?, NOW())",
                    projectId, request.getFormCode(), request.getFormName(), configJson, userId
            );

            if (request.getFields() != null && !request.getFields().isEmpty()) {
                Long formId = jdbcTemplate.queryForObject(
                        "SELECT id FROM form_form WHERE form_code = ?", Long.class, request.getFormCode()
                );

                for (FormDTO.FieldDTO field : request.getFields()) {
                    String fieldConfigJson = objectMapper.writeValueAsString(field.getFieldConfig());
                    jdbcTemplate.update(
                            "INSERT INTO form_field (form_id, field_name, field_code, field_type, field_config, sort_order, required, created_by, created_time) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())",
                            formId, field.getFieldName(), field.getFieldCode(), field.getFieldType(),
                            fieldConfigJson, field.getSortOrder() != null ? field.getSortOrder() : 0,
                            field.getRequired() != null && field.getRequired() ? 1 : 0, userId
                    );
                }
            }

            Map<String, Object> row = jdbcTemplate.queryForMap(
                    "SELECT id, project_id, form_code, form_name, form_config, status, version, created_time, updated_time " +
                            "FROM form_form WHERE form_code = ?",
                    request.getFormCode()
            );

            return toResponse(row);
        } catch (SQLException | JsonProcessingException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "创建表单失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FormDTO.FormResponse update(Long projectId, Long id, FormDTO.UpdateRequest request) {
        String dbUrl = getProjectDatabaseUrl(projectId);
        String configJson = null;

        if (request.getFields() != null || request.getFormName() != null) {

            Map<String, Object> existing = getExistingForm(dbUrl, id);
            String existingConfigJson = (String) existing.get("form_config");

            try {
                var existingConfig = objectMapper.readValue(existingConfigJson, java.util.LinkedHashMap.class);

                if (request.getFormName() != null) {
                    existingConfig.put("formName", request.getFormName());
                }
                if (request.getFields() != null) {
                    existingConfig.put("fields", request.getFields());
                }

                configJson = objectMapper.writeValueAsString(existingConfig);
            } catch (JsonProcessingException e) {
                throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "表单配置序列化失败");
            }
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(() -> conn);

            StringBuilder sql = new StringBuilder("UPDATE form_form SET version = version + 1");
            Object[] params = new Object[10];
            int idx = 0;

            if (request.getFormName() != null) {
                sql.append(", form_name = ?");
                params[idx++] = request.getFormName();
            }
            if (configJson != null) {
                sql.append(", form_config = ?");
                params[idx++] = configJson;
            }
            if (request.getStatus() != null) {
                sql.append(", status = ?");
                params[idx++] = request.getStatus();
            }

            sql.append(" WHERE id = ?");
            params[idx++] = id;

            jdbcTemplate.update(sql.toString(), java.util.Arrays.copyOf(params, idx));

            if (request.getFields() != null) {
                jdbcTemplate.update("DELETE FROM form_field WHERE form_id = ?", id);
                for (FormDTO.FieldDTO field : request.getFields()) {
                    String fieldConfigJson = objectMapper.writeValueAsString(field.getFieldConfig());
                    jdbcTemplate.update(
                            "INSERT INTO form_field (form_id, field_name, field_code, field_type, field_config, sort_order, required, created_by, created_time) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())",
                            id, field.getFieldName(), field.getFieldCode(), field.getFieldType(),
                            fieldConfigJson, field.getSortOrder() != null ? field.getSortOrder() : 0,
                            field.getRequired() != null && field.getRequired() ? 1 : 0, 1
                    );
                }
            }

            return getById(projectId, id);
        } catch (SQLException | JsonProcessingException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "更新表单失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(Long projectId, Long id) {
        String dbUrl = getProjectDatabaseUrl(projectId);

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(() -> conn);

            jdbcTemplate.update("DELETE FROM form_field WHERE form_id = ?", id);
            jdbcTemplate.update("UPDATE form_form SET deleted = 1, status = 0 WHERE id = ?", id);
        } catch (SQLException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "删除表单失败: " + e.getMessage());
        }
    }

    private Map<String, Object> getExistingForm(String dbUrl, Long id) throws SQLException {
        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(() -> conn);
            return jdbcTemplate.queryForMap("SELECT form_config FROM form_form WHERE id = ?", id);
        }
    }

    private FormDTO.FormResponse toResponse(Map<String, Object> row) {
        String configJson = (String) row.get("form_config");
        List<FormDTO.FieldDTO> fields = null;

        try {
            if (configJson != null) {
                var config = objectMapper.readValue(configJson, java.util.LinkedHashMap.class);
                var fieldsJson = objectMapper.writeValueAsString(config.get("fields"));
                fields = objectMapper.readValue(fieldsJson,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, FormDTO.FieldDTO.class));
            }
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse form config: {}", e.getMessage());
        }

        return FormDTO.FormResponse.builder()
                .id((Long) row.get("id"))
                .projectId((Long) row.get("project_id"))
                .formCode((String) row.get("form_code"))
                .formName((String) row.get("form_name"))
                .fields(fields)
                .status((Integer) row.get("status"))
                .version((Integer) row.get("version"))
                .createdTime((java.time.LocalDateTime) row.get("created_time"))
                .updatedTime((java.time.LocalDateTime) row.get("updated_time"))
                .build();
    }

    private String getProjectDatabaseUrl(Long projectId) {
        String baseUrl = datasourceUrl.substring(0, datasourceUrl.lastIndexOf('/'));
        return baseUrl + "/project_" + projectId;
    }
}
