package com.lc.system.service;

import java.util.List;
import java.util.Set;

/**
 * 权限校验服务。
 * <p>
 * 提供基于用户-角色-权限/菜单三层模型的权限查询能力，
 * 由 {@code PermissionInterceptor} 在请求进入 Controller 前调用。
 * </p>
 */
public interface PermissionService {

    /**
     * 获取用户的全部权限码集合。
     * <p>
     * 来源：
     * <ol>
     *   <li>用户角色关联的角色 -> sys_role_menu -> sys_menu.permission 字段（非空）</li>
     *   <li>用户角色关联的角色 -> sys_role_permission -> sys_permission.perm_code</li>
     * </ol>
     * </p>
     */
    Set<String> getUserPermissions(Long userId);

    /** 用户是否持有指定权限 */
    boolean hasPermission(Long userId, String permission);

    /** 用户是否持有给定权限中的任一个 */
    boolean hasAnyPermission(Long userId, String... permissions);

    /** 用户是否持有全部给定权限 */
    boolean hasAllPermissions(Long userId, String... permissions);

    /** 获取用户关联的角色 ID 列表 */
    List<Long> getUserRoleIds(Long userId);
}
