package com.lc.project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.project.dto.config.ConfigDTO;
import com.lc.project.service.ConfigService;
import com.lc.project.validation.ConfigValidator;
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
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfigServiceImpl implements ConfigService {

    private final ConfigValidator configValidator;
    private final ObjectMapper objectMapper;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Override
    public ConfigDTO.ConfigResponse getByProjectId(Long projectId) {
        String dbUrl = getProjectDatabaseUrl(projectId);

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            Map<String, Object> configMap = jdbcTemplate.queryForMap(
                    "SELECT id, project_id, config_json, schema_version, version FROM config_project_config WHERE project_id = ?",
                    projectId
            );

            String configJson = (String) configMap.get("config_json");
            ConfigDTO.ProjectConfig config = objectMapper.readValue(configJson, ConfigDTO.ProjectConfig.class);

            return ConfigDTO.ConfigResponse.builder()
                    .id((Long) configMap.get("id"))
                    .projectId((Long) configMap.get("project_id"))
                    .config(config)
                    .schemaVersion((String) configMap.get("schema_version"))
                    .version((Integer) configMap.get("version"))
                    .build();
        } catch (Exception e) {
            log.warn("Config not found for project {}, returning empty config: {}", projectId, e.getMessage());
            return createDefaultConfig(projectId);
        }
    }

    @Override
    @Transactional
    public ConfigDTO.ConfigResponse save(Long projectId, Long userId, ConfigDTO.ProjectConfig config) {
        ConfigDTO.ValidationResult result = configValidator.validate(config);
        if (!result.getValid()) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(),
                    "配置校验失败: " + String.join(", ", result.getErrors()));
        }

        String dbUrl = getProjectDatabaseUrl(projectId);
        String configJson;

        try {
            configJson = objectMapper.writeValueAsString(config);
        } catch (JsonProcessingException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "配置序列化失败");
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            int updated = jdbcTemplate.update(
                    "UPDATE config_project_config SET config_json = ?, schema_version = ?, version = version + 1, updated_by = ?, updated_time = NOW() WHERE project_id = ?",
                    configJson, config.getSchemaVersion(), userId, projectId
            );

            if (updated == 0) {
                jdbcTemplate.update(
                        "INSERT INTO config_project_config (project_id, config_json, schema_version, version, created_by, created_time) VALUES (?, ?, ?, 1, ?, NOW())",
                        projectId, configJson, config.getSchemaVersion(), userId
                );
            }

            return getByProjectId(projectId);
        } catch (SQLException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "配置保存失败: " + e.getMessage());
        }
    }

    @Override
    public ConfigDTO.ValidationResult validate(ConfigDTO.ProjectConfig config) {
        return configValidator.validate(config);
    }

    @Override
    public ConfigDTO.ValidationResult validateJson(String json) {
        return configValidator.validateJson(json);
    }

    private String getProjectDatabaseUrl(Long projectId) {
        String baseUrl = datasourceUrl.substring(0, datasourceUrl.lastIndexOf('/'));
        return baseUrl + "/project_" + projectId;
    }

    private ConfigDTO.ConfigResponse createDefaultConfig(Long projectId) {
        ConfigDTO.ProjectConfig defaultConfig = ConfigDTO.ProjectConfig.builder()
                .schemaVersion("1.0.0")
                .project(ConfigDTO.ProjectMeta.builder()
                        .id(String.valueOf(projectId))
                        .code("project_" + projectId)
                        .name("未命名项目")
                        .build())
                .pages(new java.util.ArrayList<>())
                .dataModels(new java.util.ArrayList<>())
                .dataSources(new java.util.ArrayList<>())
                .actions(new java.util.ArrayList<>())
                .permissions(new java.util.ArrayList<>())
                .assets(new java.util.ArrayList<>())
                .build();

        return ConfigDTO.ConfigResponse.builder()
                .projectId(projectId)
                .config(defaultConfig)
                .schemaVersion("1.0.0")
                .version(0)
                .build();
    }
}
