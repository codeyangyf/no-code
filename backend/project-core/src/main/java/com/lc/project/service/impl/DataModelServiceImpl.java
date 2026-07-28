package com.lc.project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.common.dto.PageResult;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.project.dto.form.FormDTO;
import com.lc.project.service.DataModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
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
public class DataModelServiceImpl implements DataModelService {

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
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            int offset = (page - 1) * size;
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                    "SELECT id, project_id, model_code as form_code, model_name as form_name, model_config as form_config, status, version, created_time, updated_time " +
                            "FROM dm_data_model WHERE deleted = 0 ORDER BY created_time DESC LIMIT ? OFFSET ?",
                    size, offset
            );

            long total = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM dm_data_model WHERE deleted = 0", Long.class
            );

            return PageResult.of(
                    rows.stream().map(this::toResponse).toList(),
                    total,
                    page,
                    size
            );
        } catch (SQLException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "查询数据模型列表失败: " + e.getMessage());
        }
    }

    @Override
    public FormDTO.FormResponse getById(Long projectId, Long id) {
        String dbUrl = getProjectDatabaseUrl(projectId);

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            Map<String, Object> row = jdbcTemplate.queryForMap(
                    "SELECT id, project_id, model_code as form_code, model_name as form_name, model_config as form_config, status, version, created_time, updated_time " +
                            "FROM dm_data_model WHERE id = ? AND deleted = 0",
                    id
            );

            return toResponse(row);
        } catch (Exception e) {
            throw new BusinessException(GlobalErrorCode.NOT_FOUND);
        }
    }

    @Override
    public FormDTO.FormResponse getByCode(Long projectId, String modelCode) {
        String dbUrl = getProjectDatabaseUrl(projectId);

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            Map<String, Object> row = jdbcTemplate.queryForMap(
                    "SELECT id, project_id, model_code as form_code, model_name as form_name, model_config as form_config, status, version, created_time, updated_time " +
                            "FROM dm_data_model WHERE model_code = ? AND deleted = 0",
                    modelCode
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
            config.put("modelCode", request.getFormCode());
            config.put("modelName", request.getFormName());
            config.put("fields", request.getFields());
            configJson = objectMapper.writeValueAsString(config);
        } catch (JsonProcessingException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "数据模型配置序列化失败");
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            Integer exists = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM dm_data_model WHERE model_code = ? AND deleted = 0",
                    Integer.class, request.getFormCode()
            );

            if (exists > 0) {
                throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "数据模型编码已存在");
            }

            jdbcTemplate.update(
                    "INSERT INTO dm_data_model (project_id, model_code, model_name, model_config, status, version, deleted, created_by, created_time) " +
                            "VALUES (?, ?, ?, ?, 1, 1, 0, ?, NOW())",
                    projectId, request.getFormCode(), request.getFormName(), configJson, userId
            );

            if (request.getFields() != null && !request.getFields().isEmpty()) {
                Long modelId = jdbcTemplate.queryForObject(
                        "SELECT id FROM dm_data_model WHERE model_code = ?", Long.class, request.getFormCode()
                );

                for (FormDTO.FieldDTO field : request.getFields()) {
                    String fieldConfigJson = objectMapper.writeValueAsString(field.getFieldConfig());
                    jdbcTemplate.update(
                            "INSERT INTO dm_model_field (model_id, field_name, field_code, field_type, field_config, sort_order, required, created_by, created_time) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())",
                            modelId, field.getFieldName(), field.getFieldCode(), field.getFieldType(),
                            fieldConfigJson, field.getSortOrder() != null ? field.getSortOrder() : 0,
                            field.getRequired() != null && field.getRequired() ? 1 : 0, userId
                    );
                }

                createBusinessTable(projectId, modelId);
            }

            Map<String, Object> row = jdbcTemplate.queryForMap(
                    "SELECT id, project_id, model_code as form_code, model_name as form_name, model_config as form_config, status, version, created_time, updated_time " +
                            "FROM dm_data_model WHERE model_code = ?",
                    request.getFormCode()
            );

            return toResponse(row);
        } catch (SQLException | JsonProcessingException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "创建数据模型失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FormDTO.FormResponse update(Long projectId, Long id, FormDTO.UpdateRequest request) {
        String dbUrl = getProjectDatabaseUrl(projectId);
        String configJson = null;

        if (request.getFields() != null || request.getFormName() != null) {

            Map<String, Object> existing = getExistingModel(dbUrl, id);
            String existingConfigJson = (String) existing.get("model_config");

            try {
                var existingConfig = objectMapper.readValue(existingConfigJson, java.util.LinkedHashMap.class);

                if (request.getFormName() != null) {
                    existingConfig.put("modelName", request.getFormName());
                }
                if (request.getFields() != null) {
                    existingConfig.put("fields", request.getFields());
                }

                configJson = objectMapper.writeValueAsString(existingConfig);
            } catch (JsonProcessingException e) {
                throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "数据模型配置序列化失败");
            }
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            StringBuilder sql = new StringBuilder("UPDATE dm_data_model SET version = version + 1");
            Object[] params = new Object[10];
            int idx = 0;

            if (request.getFormName() != null) {
                sql.append(", model_name = ?");
                params[idx++] = request.getFormName();
            }
            if (configJson != null) {
                sql.append(", model_config = ?");
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
                jdbcTemplate.update("DELETE FROM dm_model_field WHERE model_id = ?", id);
                for (FormDTO.FieldDTO field : request.getFields()) {
                    String fieldConfigJson = objectMapper.writeValueAsString(field.getFieldConfig());
                    jdbcTemplate.update(
                            "INSERT INTO dm_model_field (model_id, field_name, field_code, field_type, field_config, sort_order, required, created_by, created_time) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())",
                            id, field.getFieldName(), field.getFieldCode(), field.getFieldType(),
                            fieldConfigJson, field.getSortOrder() != null ? field.getSortOrder() : 0,
                            field.getRequired() != null && field.getRequired() ? 1 : 0, 1
                    );
                }

                updateBusinessTable(projectId, id);
            }

            return getById(projectId, id);
        } catch (SQLException | JsonProcessingException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "更新数据模型失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(Long projectId, Long id) {
        String dbUrl = getProjectDatabaseUrl(projectId);

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            Map<String, Object> model = jdbcTemplate.queryForMap("SELECT model_code FROM dm_data_model WHERE id = ?", id);
            String modelCode = (String) model.get("model_code");
            String tableName = "biz_" + modelCode;

            jdbcTemplate.execute("DROP TABLE IF EXISTS `" + tableName + "`");

            jdbcTemplate.update("DELETE FROM dm_model_field WHERE model_id = ?", id);
            jdbcTemplate.update("UPDATE dm_data_model SET deleted = 1, status = 0 WHERE id = ?", id);
        } catch (SQLException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "删除数据模型失败: " + e.getMessage());
        }
    }

    @Override
    public void createBusinessTable(Long projectId, Long modelId) {
        String dbUrl = getProjectDatabaseUrl(projectId);

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            Map<String, Object> model = jdbcTemplate.queryForMap("SELECT model_code, model_config FROM dm_data_model WHERE id = ?", modelId);
            String modelCode = (String) model.get("model_code");
            String tableName = "biz_" + modelCode;

            List<Map<String, Object>> fields = jdbcTemplate.queryForList(
                    "SELECT field_code, field_type FROM dm_model_field WHERE model_id = ? ORDER BY sort_order",
                    modelId
            );

            StringBuilder createSql = new StringBuilder();
            createSql.append("CREATE TABLE IF NOT EXISTS `").append(tableName).append("` (");
            createSql.append("`id` BIGINT PRIMARY KEY AUTO_INCREMENT, ");
            createSql.append("`created_by` BIGINT NOT NULL, ");
            createSql.append("`created_time` DATETIME DEFAULT CURRENT_TIMESTAMP, ");
            createSql.append("`updated_by` BIGINT NULL, ");
            createSql.append("`updated_time` DATETIME ON UPDATE CURRENT_TIMESTAMP, ");
            createSql.append("`deleted` TINYINT DEFAULT 0, ");
            createSql.append("`version` INT DEFAULT 0");

            for (Map<String, Object> field : fields) {
                String fieldCode = (String) field.get("field_code");
                String fieldType = (String) field.get("field_type");
                String sqlType = mapFieldType(fieldType);
                createSql.append(", `").append(fieldCode).append("` ").append(sqlType);
            }

            createSql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

            jdbcTemplate.execute(createSql.toString());
            log.info("Created business table: {}", tableName);
        } catch (SQLException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "创建业务表失败: " + e.getMessage());
        }
    }

    @Override
    public void updateBusinessTable(Long projectId, Long modelId) {
        String dbUrl = getProjectDatabaseUrl(projectId);

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            Map<String, Object> model = jdbcTemplate.queryForMap("SELECT model_code FROM dm_data_model WHERE id = ?", modelId);
            String modelCode = (String) model.get("model_code");
            String tableName = "biz_" + modelCode;

            List<Map<String, Object>> fields = jdbcTemplate.queryForList(
                    "SELECT field_code, field_type FROM dm_model_field WHERE model_id = ? ORDER BY sort_order",
                    modelId
            );

            for (Map<String, Object> field : fields) {
                String fieldCode = (String) field.get("field_code");
                String fieldType = (String) field.get("field_type");
                String sqlType = mapFieldType(fieldType);

                try {
                    jdbcTemplate.execute("ALTER TABLE `" + tableName + "` ADD COLUMN IF NOT EXISTS `" + fieldCode + "` " + sqlType);
                } catch (Exception e) {
                    jdbcTemplate.execute("ALTER TABLE `" + tableName + "` MODIFY COLUMN `" + fieldCode + "` " + sqlType);
                }
            }

            log.info("Updated business table: {}", tableName);
        } catch (SQLException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "更新业务表失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteBusinessTable(Long projectId, Long modelId) {
        String dbUrl = getProjectDatabaseUrl(projectId);

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            Map<String, Object> model = jdbcTemplate.queryForMap("SELECT model_code FROM dm_data_model WHERE id = ?", modelId);
            String modelCode = (String) model.get("model_code");
            String tableName = "biz_" + modelCode;

            jdbcTemplate.execute("DROP TABLE IF EXISTS `" + tableName + "`");
            log.info("Deleted business table: {}", tableName);
        } catch (SQLException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "删除业务表失败: " + e.getMessage());
        }
    }

    private Map<String, Object> getExistingModel(String dbUrl, Long id) {
        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));
            return jdbcTemplate.queryForMap("SELECT model_config FROM dm_data_model WHERE id = ?", id);
        } catch (SQLException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "查询数据模型配置失败: " + e.getMessage());
        }
    }

    private String mapFieldType(String fieldType) {
        return switch (fieldType.toUpperCase()) {
            case "TEXT", "TEXTAREA" -> "VARCHAR(255)";
            case "NUMBER" -> "DECIMAL(18,4)";
            case "DATE" -> "DATE";
            case "DATETIME" -> "DATETIME";
            case "BOOLEAN" -> "TINYINT(1)";
            case "SELECT", "RADIO", "CHECKBOX" -> "VARCHAR(128)";
            case "FILE" -> "VARCHAR(512)";
            case "REFERENCE" -> "BIGINT";
            default -> "VARCHAR(255)";
        };
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
            log.warn("Failed to parse data model config: {}", e.getMessage());
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
