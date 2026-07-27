package com.lc.system.controller;

import com.lc.common.annotation.PreAuthorize;
import com.lc.common.context.UserContext;
import com.lc.common.dto.PageResult;
import com.lc.common.dto.Result;
import com.lc.system.dto.UserDTO;
import com.lc.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("system:user:list")
    public Result<PageResult<UserDTO.UserResponse>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(userService.list(UserContext.getTenantId(), keyword, page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("system:user:list")
    public Result<UserDTO.UserResponse> getById(@PathVariable Long id) {
        return Result.success(userService.getDetail(id));
    }

    @PostMapping
    @PreAuthorize("system:user:create")
    public Result<UserDTO.UserResponse> create(@RequestBody UserDTO.CreateRequest request) {
        return Result.success(userService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("system:user:update")
    public Result<UserDTO.UserResponse> update(@PathVariable Long id,
                                                @RequestBody UserDTO.UpdateRequest request) {
        return Result.success(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("system:user:delete")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("system:user:update")
    public Result<Void> updateStatus(@PathVariable Long id,
                                      @RequestParam Integer status) {
        userService.updateStatus(id, status);
        return Result.success();
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("system:user:reset")
    public Result<Void> resetPassword(@PathVariable Long id,
                                       @RequestBody UserDTO.ResetPasswordRequest request) {
        userService.resetPassword(id, request.getPassword());
        return Result.success();
    }

    @PutMapping("/{id}/roles")
    @PreAuthorize("system:user:assign")
    public Result<Void> assignRoles(@PathVariable Long id,
                                     @RequestBody UserDTO.AssignRolesRequest request) {
        userService.assignRoles(id, request.getRoleIds());
        return Result.success();
    }

    @GetMapping("/{id}/roles")
    @PreAuthorize("system:user:list")
    public Result<List<Long>> getUserRoleIds(@PathVariable Long id) {
        return Result.success(userService.getUserRoleIds(id));
    }
}
