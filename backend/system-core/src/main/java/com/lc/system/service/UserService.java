package com.lc.system.service;

import com.lc.common.dto.PageResult;
import com.lc.system.dto.UserDTO;
import com.lc.system.entity.SysUser;

import java.util.List;

public interface UserService {
    SysUser findByUsername(String username);
    SysUser findByTenantIdAndUsername(Long tenantId, String username);
    SysUser findById(Long id);
    SysUser createUser(SysUser user);
    SysUser updateUser(SysUser user);
    void deleteUser(Long id);
    boolean verifyPassword(String rawPassword, String encodedPassword);

    // ===== 用户管理（DTO-based，供 UserController 使用） =====
    PageResult<UserDTO.UserResponse> list(Long tenantId, String keyword, int page, int size);

    UserDTO.UserResponse getDetail(Long id);

    UserDTO.UserResponse create(UserDTO.CreateRequest request);

    UserDTO.UserResponse update(Long id, UserDTO.UpdateRequest request);

    void resetPassword(Long id, String password);

    void assignRoles(Long userId, List<Long> roleIds);

    void updateStatus(Long id, Integer status);

    List<Long> getUserRoleIds(Long userId);
}
