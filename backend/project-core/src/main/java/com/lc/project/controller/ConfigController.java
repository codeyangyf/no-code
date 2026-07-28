package com.lc.project.controller;

import com.lc.common.annotation.AuditLog;
import com.lc.common.annotation.PreAuthorize;
import com.lc.common.context.UserContext;
import com.lc.common.dto.Result;
import com.lc.project.dto.config.ConfigDTO;
import com.lc.project.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigService configService;

    @GetMapping("/project/{projectId}")
    @PreAuthorize("project:view")
    public Result<ConfigDTO.ConfigResponse> getByProjectId(@PathVariable Long projectId) {
        return Result.success(configService.getByProjectId(projectId));
    }

    @PostMapping("/project/{projectId}")
    @PreAuthorize("project:update")
    @AuditLog(action = "保存项目配置", resourceType = "CONFIG", resourceIdParam = "#projectId")
    public Result<ConfigDTO.ConfigResponse> save(@PathVariable Long projectId,
                                                  @RequestBody ConfigDTO.ProjectConfig config) {
        return Result.success(configService.save(projectId, UserContext.getUserId(), config));
    }

    @PostMapping("/validate")
    @PreAuthorize("project:view")
    public Result<ConfigDTO.ValidationResult> validate(@RequestBody ConfigDTO.ProjectConfig config) {
        return Result.success(configService.validate(config));
    }

    @PostMapping("/validate-json")
    @PreAuthorize("project:view")
    public Result<ConfigDTO.ValidationResult> validateJson(@RequestBody Map<String, String> body) {
        String json = body.get("json");
        return Result.success(configService.validateJson(json != null ? json : ""));
    }
}
