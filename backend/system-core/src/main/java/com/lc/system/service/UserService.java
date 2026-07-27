package com.lc.system.service;

import com.lc.system.entity.SysUser;

public interface UserService {
    SysUser findByUsername(String username);
    SysUser findByTenantIdAndUsername(Long tenantId, String username);
    SysUser findById(Long id);
    SysUser createUser(SysUser user);
    SysUser updateUser(SysUser user);
    void deleteUser(Long id);
    boolean verifyPassword(String rawPassword, String encodedPassword);
}