package com.lc.system.controller;

import com.lc.common.annotation.AuditLog;
import com.lc.common.annotation.PreAuthorize;
import com.lc.common.dto.PageResult;
import com.lc.common.dto.Result;
import com.lc.system.dto.TenantDTO;
import com.lc.system.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @GetMapping
    @PreAuthorize("system:tenant:list")
    public Result<PageResult<TenantDTO.TenantResponse>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(tenantService.list(keyword, page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("system:tenant:list")
    public Result<TenantDTO.TenantResponse> getById(@PathVariable Long id) {
        return Result.success(tenantService.getById(id));
    }

    @PostMapping
    @PreAuthorize("system:tenant:manage")
    @AuditLog(action = "创建租户", resourceType = "TENANT")
    public Result<TenantDTO.TenantResponse> create(@RequestBody TenantDTO.CreateRequest request) {
        return Result.success(tenantService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("system:tenant:manage")
    @AuditLog(action = "更新租户", resourceType = "TENANT", resourceIdParam = "#id")
    public Result<TenantDTO.TenantResponse> update(@PathVariable Long id,
                                                    @RequestBody TenantDTO.UpdateRequest request) {
        return Result.success(tenantService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("system:tenant:manage")
    @AuditLog(action = "删除租户", resourceType = "TENANT", resourceIdParam = "#id")
    public Result<Void> delete(@PathVariable Long id) {
        tenantService.delete(id);
        return Result.success();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("system:tenant:manage")
    @AuditLog(action = "切换租户状态", resourceType = "TENANT", resourceIdParam = "#id")
    public Result<TenantDTO.TenantResponse> toggleStatus(@PathVariable Long id,
                                                          @RequestParam Integer status) {
        return Result.success(tenantService.toggleStatus(id, status));
    }
}
