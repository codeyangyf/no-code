package com.lc.system.controller;

import com.lc.common.annotation.AuditLog;
import com.lc.common.annotation.PreAuthorize;
import com.lc.common.context.UserContext;
import com.lc.common.dto.Result;
import com.lc.system.dto.MenuDTO;
import com.lc.system.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    @PreAuthorize("system:menu:list")
    public Result<List<MenuDTO.MenuResponse>> list() {
        return Result.success(menuService.list(UserContext.getTenantId()));
    }

    @GetMapping("/tree")
    @PreAuthorize("system:menu:list")
    public Result<List<MenuDTO.MenuResponse>> getMenuTree() {
        return Result.success(menuService.getMenuTree(UserContext.getTenantId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("system:menu:list")
    public Result<MenuDTO.MenuResponse> getById(@PathVariable Long id) {
        return Result.success(menuService.getById(id));
    }

    @PostMapping
    @PreAuthorize("system:menu:create")
    @AuditLog(action = "创建菜单", resourceType = "MENU")
    public Result<MenuDTO.MenuResponse> create(@RequestBody MenuDTO.CreateRequest request) {
        return Result.success(menuService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("system:menu:update")
    @AuditLog(action = "更新菜单", resourceType = "MENU", resourceIdParam = "#id")
    public Result<MenuDTO.MenuResponse> update(@PathVariable Long id,
                                                @RequestBody MenuDTO.UpdateRequest request) {
        return Result.success(menuService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("system:menu:delete")
    @AuditLog(action = "删除菜单", resourceType = "MENU", resourceIdParam = "#id")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return Result.success();
    }
}
