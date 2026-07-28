package com.lc.project.service;

import com.lc.project.dto.config.ConfigDTO;

public interface ConfigService {

    ConfigDTO.ConfigResponse getByProjectId(Long projectId);

    ConfigDTO.ConfigResponse save(Long projectId, Long userId, ConfigDTO.ProjectConfig config);

    ConfigDTO.ValidationResult validate(ConfigDTO.ProjectConfig config);

    ConfigDTO.ValidationResult validateJson(String json);
}
