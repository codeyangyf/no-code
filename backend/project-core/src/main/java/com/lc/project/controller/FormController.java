package com.lc.project.controller;

import com.lc.common.annotation.AuditLog;
import com.lc.common.annotation.PreAuthorize;
import com.lc.common.context.UserContext;
import com.lc.common.dto.PageResult;
import com.lc.common.dto.Result;
import com.lc.project.dto.form.FormDTO;
import com.lc.project.service.FormService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
public class FormController {

    private final FormService formService;

    @GetMapping("/project/{projectId}")
    @PreAuthorize("project:view")
    public Result<PageResult<FormDTO.FormResponse>> list(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return Result.success(formService.list(projectId, page, size));
    }

    @GetMapping("/project/{projectId}/{id}")
    @PreAuthorize("project:view")
    public Result<FormDTO.FormResponse> getById(@PathVariable Long projectId, @PathVariable Long id) {
        return Result.success(formService.getById(projectId, id));
    }

    @GetMapping("/project/{projectId}/code/{formCode}")
    @PreAuthorize("project:view")
    public Result<FormDTO.FormResponse> getByCode(@PathVariable Long projectId, @PathVariable String formCode) {
        return Result.success(formService.getByCode(projectId, formCode));
    }

    @PostMapping("/project/{projectId}")
    @PreAuthorize("project:update")
    @AuditLog(action = "创建表单", resourceType = "FORM")
    public Result<FormDTO.FormResponse> create(@PathVariable Long projectId,
                                               @RequestBody FormDTO.CreateRequest request) {
        return Result.success(formService.create(projectId, UserContext.getUserId(), request));
    }

    @PutMapping("/project/{projectId}/{id}")
    @PreAuthorize("project:update")
    @AuditLog(action = "更新表单", resourceType = "FORM", resourceIdParam = "#id")
    public Result<FormDTO.FormResponse> update(@PathVariable Long projectId, @PathVariable Long id,
                                               @RequestBody FormDTO.UpdateRequest request) {
        return Result.success(formService.update(projectId, id, request));
    }

    @DeleteMapping("/project/{projectId}/{id}")
    @PreAuthorize("project:delete")
    @AuditLog(action = "删除表单", resourceType = "FORM", resourceIdParam = "#id")
    public Result<Void> delete(@PathVariable Long projectId, @PathVariable Long id) {
        formService.delete(projectId, id);
        return Result.success();
    }
}
