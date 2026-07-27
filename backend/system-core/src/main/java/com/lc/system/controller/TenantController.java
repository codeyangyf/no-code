package com.lc.system.controller;

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
    public Result<PageResult<TenantDTO.TenantResponse>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(tenantService.list(keyword, page, size));
    }

    @GetMapping("/{id}")
    public Result<TenantDTO.TenantResponse> getById(@PathVariable Long id) {
        return Result.success(tenantService.getById(id));
    }

    @PostMapping
    public Result<TenantDTO.TenantResponse> create(@RequestBody TenantDTO.CreateRequest request) {
        return Result.success(tenantService.create(request));
    }

    @PutMapping("/{id}")
    public Result<TenantDTO.TenantResponse> update(@PathVariable Long id,
                                                    @RequestBody TenantDTO.UpdateRequest request) {
        return Result.success(tenantService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tenantService.delete(id);
        return Result.success();
    }

    @PatchMapping("/{id}/status")
    public Result<TenantDTO.TenantResponse> toggleStatus(@PathVariable Long id,
                                                          @RequestParam Integer status) {
        return Result.success(tenantService.toggleStatus(id, status));
    }
}
