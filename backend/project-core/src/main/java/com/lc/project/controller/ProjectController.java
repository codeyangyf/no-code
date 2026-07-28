package com.lc.project.controller;

import com.lc.common.annotation.AuditLog;
import com.lc.common.annotation.PreAuthorize;
import com.lc.common.context.UserContext;
import com.lc.common.dto.PageResult;
import com.lc.common.dto.Result;
import com.lc.project.dto.ProjectDTO;
import com.lc.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    @PreAuthorize("project:list")
    public Result<PageResult<ProjectDTO.ProjectResponse>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return Result.success(projectService.list(UserContext.getTenantId(), page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("project:list")
    public Result<ProjectDTO.ProjectResponse> getById(@PathVariable Long id) {
        return Result.success(projectService.getById(UserContext.getTenantId(), id));
    }

    @PostMapping
    @PreAuthorize("project:create")
    @AuditLog(action = "创建项目", resourceType = "PROJECT")
    public Result<ProjectDTO.ProjectResponse> create(@RequestBody ProjectDTO.CreateRequest request) {
        return Result.success(projectService.create(UserContext.getTenantId(), UserContext.getUserId(), request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("project:update")
    @AuditLog(action = "更新项目", resourceType = "PROJECT", resourceIdParam = "#id")
    public Result<ProjectDTO.ProjectResponse> update(@PathVariable Long id,
                                                     @RequestBody ProjectDTO.UpdateRequest request) {
        return Result.success(projectService.update(UserContext.getTenantId(), id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("project:delete")
    @AuditLog(action = "删除项目", resourceType = "PROJECT", resourceIdParam = "#id")
    public Result<Void> delete(@PathVariable Long id) {
        projectService.delete(UserContext.getTenantId(), id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("project:manage")
    @AuditLog(action = "更新项目状态", resourceType = "PROJECT", resourceIdParam = "#id")
    public Result<ProjectDTO.ProjectResponse> updateStatus(@PathVariable Long id,
                                                           @RequestBody ProjectDTO.UpdateStatusRequest request) {
        return Result.success(projectService.updateStatus(UserContext.getTenantId(), id, request));
    }
}
