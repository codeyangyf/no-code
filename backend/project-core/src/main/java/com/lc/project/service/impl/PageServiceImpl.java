package com.lc.project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.common.dto.PageResult;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.project.dto.page.PageDTO;
import com.lc.project.service.PageService;
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
public class PageServiceImpl implements PageService {

    private final ObjectMapper objectMapper;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Override
    public PageResult<PageDTO.PageResponse> list(Long projectId, Integer page, Integer size) {
        String dbUrl = getProjectDatabaseUrl(projectId);

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            int offset = (page - 1) * size;
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                    "SELECT id, project_id, page_code, page_name, path, layout, page_config, status, version, created_time, updated_time " +
                            "FROM page_page WHERE deleted = 0 ORDER BY created_time DESC LIMIT ? OFFSET ?",
                    size, offset
            );

            long total = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM page_page WHERE deleted = 0", Long.class
            );

            return PageResult.of(
                    rows.stream().map(this::toResponse).toList(),
                    total,
                    page,
                    size
            );
        } catch (SQLException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "查询页面列表失败: " + e.getMessage());
        }
    }

    @Override
    public PageDTO.PageResponse getById(Long projectId, Long id) {
        String dbUrl = getProjectDatabaseUrl(projectId);

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            Map<String, Object> row = jdbcTemplate.queryForMap(
                    "SELECT id, project_id, page_code, page_name, path, layout, page_config, status, version, created_time, updated_time " +
                            "FROM page_page WHERE id = ? AND deleted = 0",
                    id
            );

            return toResponse(row);
        } catch (Exception e) {
            throw new BusinessException(GlobalErrorCode.NOT_FOUND);
        }
    }

    @Override
    public PageDTO.PageResponse getByCode(Long projectId, String pageCode) {
        String dbUrl = getProjectDatabaseUrl(projectId);

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            Map<String, Object> row = jdbcTemplate.queryForMap(
                    "SELECT id, project_id, page_code, page_name, path, layout, page_config, status, version, created_time, updated_time " +
                            "FROM page_page WHERE page_code = ? AND deleted = 0",
                    pageCode
            );

            return toResponse(row);
        } catch (Exception e) {
            throw new BusinessException(GlobalErrorCode.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public PageDTO.PageResponse create(Long projectId, Long userId, PageDTO.CreateRequest request) {
        String dbUrl = getProjectDatabaseUrl(projectId);
        String configJson;

        try {
            var config = com.lc.project.dto.config.ConfigDTO.PageConfig.builder()
                    .code(request.getPageCode())
                    .name(request.getPageName())
                    .path(request.getPath())
                    .layout(request.getLayout())
                    .components(toComponentConfig(request.getComponents()))
                    .status(1)
                    .version(1)
                    .build();
            configJson = objectMapper.writeValueAsString(config);
        } catch (JsonProcessingException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "页面配置序列化失败");
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            Integer exists = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM page_page WHERE page_code = ? AND deleted = 0",
                    Integer.class, request.getPageCode()
            );

            if (exists > 0) {
                throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "页面编码已存在");
            }

            jdbcTemplate.update(
                    "INSERT INTO page_page (project_id, page_code, page_name, path, layout, page_config, status, version, deleted, created_by, created_time) " +
                            "VALUES (?, ?, ?, ?, ?, ?, 1, 1, 0, ?, NOW())",
                    projectId, request.getPageCode(), request.getPageName(), request.getPath(),
                    request.getLayout(), configJson, userId
            );

            Map<String, Object> row = jdbcTemplate.queryForMap(
                    "SELECT id, project_id, page_code, page_name, path, layout, page_config, status, version, created_time, updated_time " +
                            "FROM page_page WHERE page_code = ?",
                    request.getPageCode()
            );

            return toResponse(row);
        } catch (SQLException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "创建页面失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PageDTO.PageResponse update(Long projectId, Long id, PageDTO.UpdateRequest request) {
        String dbUrl = getProjectDatabaseUrl(projectId);
        String configJson = null;

        if (request.getComponents() != null || request.getPageName() != null ||
                request.getPath() != null || request.getLayout() != null) {

            Map<String, Object> existing = getExistingPage(dbUrl, id);
            String existingConfigJson = (String) existing.get("page_config");

            try {
                var existingConfig = objectMapper.readValue(existingConfigJson,
                        com.lc.project.dto.config.ConfigDTO.PageConfig.class);

                if (request.getPageName() != null) {
                    existingConfig.setName(request.getPageName());
                }
                if (request.getPath() != null) {
                    existingConfig.setPath(request.getPath());
                }
                if (request.getLayout() != null) {
                    existingConfig.setLayout(request.getLayout());
                }
                if (request.getComponents() != null) {
                    existingConfig.setComponents(toComponentConfig(request.getComponents()));
                }

                configJson = objectMapper.writeValueAsString(existingConfig);
            } catch (JsonProcessingException e) {
                throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "页面配置序列化失败");
            }
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            StringBuilder sql = new StringBuilder("UPDATE page_page SET version = version + 1");
            Object[] params = new Object[10];
            int idx = 0;

            if (request.getPageName() != null) {
                sql.append(", page_name = ?");
                params[idx++] = request.getPageName();
            }
            if (request.getPath() != null) {
                sql.append(", path = ?");
                params[idx++] = request.getPath();
            }
            if (request.getLayout() != null) {
                sql.append(", layout = ?");
                params[idx++] = request.getLayout();
            }
            if (configJson != null) {
                sql.append(", page_config = ?");
                params[idx++] = configJson;
            }
            if (request.getStatus() != null) {
                sql.append(", status = ?");
                params[idx++] = request.getStatus();
            }

            sql.append(" WHERE id = ?");
            params[idx++] = id;

            jdbcTemplate.update(sql.toString(), java.util.Arrays.copyOf(params, idx));

            return getById(projectId, id);
        } catch (SQLException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "更新页面失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(Long projectId, Long id) {
        String dbUrl = getProjectDatabaseUrl(projectId);

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

            jdbcTemplate.update(
                    "UPDATE page_page SET deleted = 1, status = 0 WHERE id = ?",
                    id
            );
        } catch (SQLException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "删除页面失败: " + e.getMessage());
        }
    }

    private Map<String, Object> getExistingPage(String dbUrl, Long id) {
        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));
            return jdbcTemplate.queryForMap("SELECT page_config FROM page_page WHERE id = ?", id);
        } catch (SQLException e) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "查询页面配置失败: " + e.getMessage());
        }
    }

    private List<com.lc.project.dto.config.ConfigDTO.ComponentConfig> toComponentConfig(List<PageDTO.ComponentDTO> components) {
        if (components == null) return null;
        return components.stream().map(c -> com.lc.project.dto.config.ConfigDTO.ComponentConfig.builder()
                .id(c.getId())
                .type(c.getType())
                .name(c.getName())
                .parentId(c.getParentId())
                .props(c.getProps())
                .style(c.getStyle())
                .sortOrder(c.getSortOrder())
                .build()).toList();
    }

    private PageDTO.PageResponse toResponse(Map<String, Object> row) {
        String configJson = (String) row.get("page_config");
        List<PageDTO.ComponentDTO> components = null;

        try {
            if (configJson != null) {
                var config = objectMapper.readValue(configJson,
                        com.lc.project.dto.config.ConfigDTO.PageConfig.class);
                components = config.getComponents().stream().map(c -> PageDTO.ComponentDTO.builder()
                        .id(c.getId())
                        .type(c.getType())
                        .name(c.getName())
                        .parentId(c.getParentId())
                        .props(c.getProps())
                        .style(c.getStyle())
                        .sortOrder(c.getSortOrder())
                        .build()).toList();
            }
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse page config: {}", e.getMessage());
        }

        return PageDTO.PageResponse.builder()
                .id((Long) row.get("id"))
                .projectId((Long) row.get("project_id"))
                .pageCode((String) row.get("page_code"))
                .pageName((String) row.get("page_name"))
                .path((String) row.get("path"))
                .layout((String) row.get("layout"))
                .components(components)
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
