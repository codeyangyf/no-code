## Task 5: RBAC权限体系 ⬜ 待实施

> 设计文档 Task 5：用户角色、菜单权限、项目成员角色、接口权限声明

**目标：** 实现用户-角色-权限三层模型的完整CRUD和接口级权限控制。

**Files:**
- Create: `backend/common/src/main/java/com/lc/common/annotation/PreAuthorize.java`
- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysMenuRepository.java`
- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysPermissionRepository.java`
- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysOrgRepository.java`
- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysDictRepository.java`
- Create: `backend/system-core/src/main/java/com/lc/system/dto/UserDTO.java`
- Create: `backend/system-core/src/main/java/com/lc/system/dto/RoleDTO.java`
- Create: `backend/system-core/src/main/java/com/lc/system/dto/MenuDTO.java`
- Create: `backend/system-core/src/main/java/com/lc/system/service/PermissionService.java`
- Create: `backend/system-core/src/main/java/com/lc/system/service/RoleService.java`
- Create: `backend/system-core/src/main/java/com/lc/system/service/MenuService.java`
- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/PermissionServiceImpl.java`
- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/RoleServiceImpl.java`
- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/MenuServiceImpl.java`
- Create: `backend/system-core/src/main/java/com/lc/system/controller/RoleController.java`
- Create: `backend/system-core/src/main/java/com/lc/system/controller/MenuController.java`
- Create: `backend/system-core/src/main/java/com/lc/system/controller/UserController.java`
- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/interceptor/PermissionInterceptor.java`
- Modify: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebMvcConfig.java`（注册权限拦截器）

**Interfaces:**
- Consumes: `UserContext`, `SysRoleRepository`（已存在）, `SysUserRoleRepository`（已存在）, `SysMenu/SysPermission`实体（已存在）
- Produces: `PermissionService`（权限校验）, `PreAuthorize`注解, `PermissionInterceptor`, 角色/菜单/用户管理REST接口

**关键设计：**
1. 菜单权限：树形菜单结构，角色通过 sys_role_menu 关联菜单（需确认该关联表是否存在，若不存在需新增迁移脚本）
2. 项目成员角色：viewer/editor/admin/publisher 四级，存储在 project_member 表的 role 字段
3. 接口权限声明：`@PreAuthorize("user:list")` 注解，PermissionInterceptor 解析注解并调用 PermissionService 校验

**PreAuthorize.java：**
```java
package com.lc.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuthorize {
    /** 所需权限码，多个用逗号分隔 */
    String value() default "";
    /** true=需全部权限，false=任一权限即可 */
    boolean requireAll() default false;
}
```

**PermissionService.java：**
```java
public interface PermissionService {
    Set<String> getUserPermissions(Long userId);
    boolean hasPermission(Long userId, String permission);
    boolean hasAnyPermission(Long userId, String... permissions);
    boolean hasAllPermissions(Long userId, String... permissions);
    List<Long> getUserRoleIds(Long userId);
}
```

**需确认：** sys_role_menu 关联表是否在 V3/V5 迁移脚本中已创建。若未创建，Task 5-1 需新增迁移脚本。

- [ ] **Task 5-1: 确认/补建 sys_role_menu 关联表迁移脚本**
- [ ] **Task 5-2: 创建 PreAuthorize 注解 + PermissionService 接口 + PermissionServiceImpl**
- [ ] **Task 5-3: 创建 PermissionInterceptor + 注册到 WebMvcConfig**
- [ ] **Task 5-4: 创建 RoleService/MenuService + 实现 + Repository + DTO**
- [ ] **Task 5-5: 创建 RoleController/MenuController/UserController**
- [ ] **Task 5-6: 编译验证 + Commit**

---

### Task 5 验收
- [ ] 角色管理接口正常（创建、编辑、删除、分配菜单）
- [ ] 菜单管理接口正常（树形结构返回）
- [ ] @PreAuthorize 注解生效，无权限返回 403
- [ ] 用户管理接口正常（分页查询、创建、编辑、删除、重置密码）

