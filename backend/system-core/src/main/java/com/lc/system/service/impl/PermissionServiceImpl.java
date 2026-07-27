package com.lc.system.service.impl;

import com.lc.system.entity.SysMenu;
import com.lc.system.entity.SysPermission;
import com.lc.system.entity.SysRoleMenu;
import com.lc.system.entity.SysRolePermission;
import com.lc.system.repository.SysMenuRepository;
import com.lc.system.repository.SysPermissionRepository;
import com.lc.system.repository.SysRoleMenuRepository;
import com.lc.system.repository.SysRolePermissionRepository;
import com.lc.system.repository.SysUserRoleRepository;
import com.lc.system.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final SysUserRoleRepository userRoleRepository;
    private final SysRoleMenuRepository roleMenuRepository;
    private final SysRolePermissionRepository rolePermissionRepository;
    private final SysMenuRepository menuRepository;
    private final SysPermissionRepository permissionRepository;

    @Override
    @Transactional(readOnly = true)
    public Set<String> getUserPermissions(Long userId) {
        if (userId == null) {
            return Collections.emptySet();
        }
        List<Long> roleIds = userRoleRepository.findRoleIdsByUserId(userId);
        if (roleIds.isEmpty()) {
            return Collections.emptySet();
        }

        Set<String> permissions = new HashSet<>();

        // 1. 通过 sys_role_menu -> sys_menu.permission 收集
        Set<Long> menuIds = new HashSet<>();
        for (Long roleId : roleIds) {
            for (SysRoleMenu rm : roleMenuRepository.findByRoleId(roleId)) {
                menuIds.add(rm.getMenuId());
            }
        }
        if (!menuIds.isEmpty()) {
            List<SysMenu> menus = menuRepository.findAllById(menuIds);
            for (SysMenu menu : menus) {
                if (menu.getPermission() != null && !menu.getPermission().trim().isEmpty()) {
                    permissions.add(menu.getPermission().trim());
                }
            }
        }

        // 2. 通过 sys_role_permission -> sys_permission.perm_code 收集
        Set<Long> permissionIds = new HashSet<>();
        for (Long roleId : roleIds) {
            for (SysRolePermission rp : rolePermissionRepository.findByRoleId(roleId)) {
                permissionIds.add(rp.getPermissionId());
            }
        }
        if (!permissionIds.isEmpty()) {
            List<SysPermission> perms = permissionRepository.findAllById(permissionIds);
            for (SysPermission perm : perms) {
                if (perm.getPermCode() != null && !perm.getPermCode().trim().isEmpty()) {
                    permissions.add(perm.getPermCode().trim());
                }
            }
        }

        return permissions;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPermission(Long userId, String permission) {
        if (permission == null || permission.trim().isEmpty()) {
            return true;
        }
        return getUserPermissions(userId).contains(permission.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAnyPermission(Long userId, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return true;
        }
        Set<String> userPermissions = getUserPermissions(userId);
        for (String p : permissions) {
            if (p != null && !p.trim().isEmpty() && userPermissions.contains(p.trim())) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAllPermissions(Long userId, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return true;
        }
        Set<String> userPermissions = getUserPermissions(userId);
        for (String p : permissions) {
            if (p == null || p.trim().isEmpty()) {
                continue;
            }
            if (!userPermissions.contains(p.trim())) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getUserRoleIds(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        return userRoleRepository.findRoleIdsByUserId(userId);
    }
}
