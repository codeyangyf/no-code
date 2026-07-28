package com.lc.project.service.impl;

import com.lc.project.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;

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
        String dbUrl = getAdminUrl() + "/" + dbName;

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            Flyway flyway = Flyway.configure()
                    .dataSource(conn)
                    .locations("classpath:db/project-migration")
                    .baselineOnMigrate(true)
                    .load();
            flyway.migrate();
            log.info("Executed baseline migration for project database: {}", dbName);
        } catch (Exception e) {
            log.error("Failed to execute baseline migration for project database: {}", dbName, e);
            throw new RuntimeException("Failed to execute baseline migration", e);
        }
    }

    @Override
    public void executeSandboxBaselineMigration(Long projectId) {
        String dbName = "project_" + projectId + "_sandbox";
        String dbUrl = getAdminUrl() + "/" + dbName;

        try (Connection conn = DriverManager.getConnection(dbUrl, datasourceUsername, datasourcePassword)) {
            Flyway flyway = Flyway.configure()
                    .dataSource(conn)
                    .locations("classpath:db/project-migration")
                    .baselineOnMigrate(true)
                    .load();
            flyway.migrate();
            log.info("Executed baseline migration for sandbox database: {}", dbName);
        } catch (Exception e) {
            log.error("Failed to execute baseline migration for sandbox database: {}", dbName, e);
            throw new RuntimeException("Failed to execute sandbox baseline migration", e);
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
