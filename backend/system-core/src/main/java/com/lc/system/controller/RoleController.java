package com.lc.system.controller;

import com.lc.common.annotation.PreAuthorize;
import com.lc.common.dto.PageResult;
import com.lc.common.dto.Result;
import com.lc.system.dto.RoleDTO;
import com.lc.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("system:role:list")
    public Result<PageResult<RoleDTO.RoleResponse>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(roleService.list(keyword, page, size));
    }

    @GetMapping("/all")
    @PreAuthorize("system:role:list")
    public Result<List<RoleDTO.RoleResponse>> listAll() {
        return Result.success(roleService.listAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("system:role:list")
    public Result<RoleDTO.RoleResponse> getById(@PathVariable Long id) {
        return Result.success(roleService.getById(id));
    }

    @PostMapping
    @PreAuthorize("system:role:create")
    public Result<RoleDTO.RoleResponse> create(@RequestBody RoleDTO.CreateRequest request) {
        return Result.success(roleService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("system:role:update")
    public Result<RoleDTO.RoleResponse> update(@PathVariable Long id,
                                                @RequestBody RoleDTO.UpdateRequest request) {
        return Result.success(roleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("system:role:delete")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.success();
    }

    @PutMapping("/{id}/menus")
    @PreAuthorize("system:role:assign")
    public Result<Void> assignMenus(@PathVariable Long id,
                                     @RequestBody RoleDTO.AssignMenusRequest request) {
        roleService.assignMenus(id, request.getMenuIds());
        return Result.success();
    }

    @GetMapping("/{id}/menus")
    @PreAuthorize("system:role:list")
    public Result<List<Long>> getRoleMenuIds(@PathVariable Long id) {
        return Result.success(roleService.getRoleMenuIds(id));
    }
}
