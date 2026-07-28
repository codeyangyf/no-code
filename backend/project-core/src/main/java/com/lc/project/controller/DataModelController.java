package com.lc.project.controller;

import com.lc.common.annotation.AuditLog;
import com.lc.common.annotation.PreAuthorize;
import com.lc.common.context.UserContext;
import com.lc.common.dto.PageResult;
import com.lc.common.dto.Result;
import com.lc.project.dto.form.FormDTO;
import com.lc.project.service.DataModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data-models")
@RequiredArgsConstructor
public class DataModelController {

    private final DataModelService dataModelService;

    @GetMapping("/project/{projectId}")
    @PreAuthorize("project:view")
    public Result<PageResult<FormDTO.FormResponse>> list(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return Result.success(dataModelService.list(projectId, page, size));
    }

    @GetMapping("/project/{projectId}/{id}")
    @PreAuthorize("project:view")
    public Result<FormDTO.FormResponse> getById(@PathVariable Long projectId, @PathVariable Long id) {
        return Result.success(dataModelService.getById(projectId, id));
    }

    @GetMapping("/project/{projectId}/code/{modelCode}")
    @PreAuthorize("project:view")
    public Result<FormDTO.FormResponse> getByCode(@PathVariable Long projectId, @PathVariable String modelCode) {
        return Result.success(dataModelService.getByCode(projectId, modelCode));
    }

    @PostMapping("/project/{projectId}")
    @PreAuthorize("project:update")
    @AuditLog(action = "创建数据模型", resourceType = "DATA_MODEL")
    public Result<FormDTO.FormResponse> create(@PathVariable Long projectId,
                                               @RequestBody FormDTO.CreateRequest request) {
        return Result.success(dataModelService.create(projectId, UserContext.getUserId(), request));
    }

    @PutMapping("/project/{projectId}/{id}")
    @PreAuthorize("project:update")
    @AuditLog(action = "更新数据模型", resourceType = "DATA_MODEL", resourceIdParam = "#id")
    public Result<FormDTO.FormResponse> update(@PathVariable Long projectId, @PathVariable Long id,
                                               @RequestBody FormDTO.UpdateRequest request) {
        return Result.success(dataModelService.update(projectId, id, request));
    }

    @DeleteMapping("/project/{projectId}/{id}")
    @PreAuthorize("project:delete")
    @AuditLog(action = "删除数据模型", resourceType = "DATA_MODEL", resourceIdParam = "#id")
    public Result<Void> delete(@PathVariable Long projectId, @PathVariable Long id) {
        dataModelService.delete(projectId, id);
        return Result.success();
    }
}