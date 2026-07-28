package com.lc.project.service.impl;

import com.lc.project.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseServiceImpl implements DatabaseService {

    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    private String getAdminUrl() {
        return datasourceUrl.substring(0, datasourceUrl.lastIndexOf('/'));
    }

    @Override
    @Transactional
    public void createProjectDatabase(Long projectId) {
        String dbName = "project_" + projectId;
        String userName = "project_user_" + projectId;
        String password = generatePassword();

        try {
            createDatabase(dbName);
            createUser(userName, password);
            grantPrivileges(dbName, userName);
            log.info("Created project database: {}, user: {}", dbName, userName);
        } catch (Exception e) {
            log.error("Failed to create project database: {}", dbName, e);
            cleanupOnFailure(dbName, userName);
            throw new RuntimeException("Failed to create project database", e);
        }
    }

    @Override
    @Transactional
    public void createSandboxDatabase(Long projectId) {
        String dbName = "project_" + projectId + "_sandbox";
        String userName = "project_user_" + projectId + "_sandbox";
        String password = generatePassword();

        try {
            createDatabase(dbName);
            createUser(userName, password);
            grantPrivileges(dbName, userName);
            log.info("Created sandbox database: {}, user: {}", dbName, userName);
        } catch (Exception e) {
            log.error("Failed to create sandbox database: {}", dbName, e);
            cleanupOnFailure(dbName, userName);
            throw new RuntimeException("Failed to create sandbox database", e);
        }
    }

    @Override
    @Transactional
    public void deleteProjectDatabase(Long projectId) {
        String dbName = "project_" + projectId;
        String userName = "project_user_" + projectId;

        try {
            revokePrivileges(dbName, userName);
            dropUser(userName);
            dropDatabase(dbName);
            log.info("Deleted project database: {}", dbName);
        } catch (Exception e) {
            log.error("Failed to delete project database: {}", dbName, e);
            throw new RuntimeException("Failed to delete project database", e);
        }
    }

    @Override
    @Transactional
    public void deleteSandboxDatabase(Long projectId) {
        String dbName = "project_" + projectId + "_sandbox";
        String userName = "project_user_" + projectId + "_sandbox";

        try {
            revokePrivileges(dbName, userName);
            dropUser(userName);
            dropDatabase(dbName);
            log.info("Deleted sandbox database: {}", dbName);
        } catch (Exception e) {
            log.error("Failed to delete sandbox database: {}", dbName, e);
            throw new RuntimeException("Failed to delete sandbox database", e);
        }
    }

    @Override
    public void executeBaselineMigration(Long projectId) {
        String dbName = "project_" + projectId;
        executeBaselineSql(dbName);
    }

    @Override
    public void executeSandboxBaselineMigration(Long projectId) {
        String dbName = "project_" + projectId + "_sandbox";
        executeBaselineSql(dbName);
    }

    private void executeBaselineSql(String dbName) {
        String dbUrl = getAdminUrl() + "/" + dbName;

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            JdbcTemplate projectJdbc = new JdbcTemplate();
            projectJdbc.setDataSource(new SingleConnectionDataSource(conn, true));

            ClassPathResource resource = new ClassPathResource("db/project-migration/V1__baseline.sql");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                String sql = reader.lines().collect(Collectors.joining("\n"));
                // 按分号分割并逐条执行
                String[] statements = sql.split(";");
                for (String stmt : statements) {
                    String trimmed = stmt.trim();
                    if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
                        projectJdbc.execute(trimmed);
                    }
                }
            }
            log.info("Executed baseline migration for project database: {}", dbName);
        } catch (Exception e) {
            log.error("Failed to execute baseline migration for project database: {}", dbName, e);
            throw new RuntimeException("Failed to execute baseline migration", e);
        }
    }

    private void createDatabase(String dbName) {
        jdbcTemplate.execute("CREATE DATABASE IF NOT EXISTS `" + dbName + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
    }

    private void dropDatabase(String dbName) {
        jdbcTemplate.execute("DROP DATABASE IF EXISTS `" + dbName + "`");
    }

    private void createUser(String userName, String password) {
        jdbcTemplate.execute("CREATE USER IF NOT EXISTS '" + userName + "'@'localhost' IDENTIFIED BY '" + password + "'");
    }

    private void dropUser(String userName) {
        jdbcTemplate.execute("DROP USER IF EXISTS '" + userName + "'@'localhost'");
    }

    private void grantPrivileges(String dbName, String userName) {
        jdbcTemplate.execute("GRANT ALL PRIVILEGES ON `" + dbName + "`.* TO '" + userName + "'@'localhost'");
        jdbcTemplate.execute("FLUSH PRIVILEGES");
    }

    private void revokePrivileges(String dbName, String userName) {
        jdbcTemplate.execute("REVOKE ALL PRIVILEGES ON `" + dbName + "`.* FROM '" + userName + "'@'localhost'");
        jdbcTemplate.execute("FLUSH PRIVILEGES");
    }

    private void cleanupOnFailure(String dbName, String userName) {
        try {
            revokePrivileges(dbName, userName);
        } catch (Exception e) {
            log.warn("Failed to revoke privileges during cleanup: {}", e.getMessage());
        }
        try {
            dropUser(userName);
        } catch (Exception e) {
            log.warn("Failed to drop user during cleanup: {}", e.getMessage());
        }
        try {
            dropDatabase(dbName);
        } catch (Exception e) {
            log.warn("Failed to drop database during cleanup: {}", e.getMessage());
        }
    }

    private String generatePassword() {
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
