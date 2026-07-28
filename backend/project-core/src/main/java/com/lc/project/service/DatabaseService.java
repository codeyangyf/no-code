package com.lc.project.service;

public interface DatabaseService {

    void createProjectDatabase(Long projectId);

    void createSandboxDatabase(Long projectId);

    void deleteProjectDatabase(Long projectId);

    void deleteSandboxDatabase(Long projectId);

    void executeBaselineMigration(Long projectId);

    void executeSandboxBaselineMigration(Long projectId);
}
