-- V16: 初始化租户管理员角色与系统权限种子数据
-- 为默认租户(tenant_id=1)创建 tenant_admin 角色，关联系统级权限，并绑定 admin 用户。
-- 注：权限校验为精确匹配(PermissionServiceImpl#hasPermission 用 Set.contains)，
-- 现有 User/Role/Menu 控制器使用 create/update/delete 细粒度权限码，
-- TenantController 使用 manage 聚合权限码，因此两类权限均需 seed 以保证 admin 可操作。

-- 1. 创建租户管理员角色
INSERT INTO sys_role (tenant_id, role_code, role_name, status, created_time)
VALUES (1, 'tenant_admin', '租户管理员', 1, NOW());

-- 捕获新角色自增ID，供后续角色-权限关联使用
SET @role_id = LAST_INSERT_ID();

-- 2. 系统权限（tenant_id=1）
-- 2.1 任务显式要求的聚合权限码（system:*:manage / list / reset / assign，tenant manage/list，audit，file）
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:user:list',   '用户列表',   1, NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:user:manage', '用户管理',   1, NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:user:reset',  '重置用户密码', 1, NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:user:assign', '分配用户角色', 1, NOW());

INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:role:list',   '角色列表',   1, NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:role:manage', '角色管理',   1, NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:role:assign', '分配角色菜单', 1, NOW());

INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:menu:list',   '菜单列表',   1, NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:menu:manage', '菜单管理',   1, NOW());

INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:tenant:list',   '租户列表', 1, NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:tenant:manage', '租户管理', 1, NOW());

INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'audit:log:list', '审计日志列表', 1, NOW());

INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'file:upload', '文件上传', 1, NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'file:delete', '文件删除', 1, NOW());

-- 2.2 现有 UserController/RoleController/MenuController 实际 @PreAuthorize 使用的细粒度权限码
-- （PermissionServiceImpl 为精确匹配，聚合 manage 无法满足 create/update/delete 校验）
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:user:create', '创建用户', 1, NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:user:update', '更新用户', 1, NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:user:delete', '删除用户', 1, NOW());

INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:role:create', '创建角色', 1, NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:role:update', '更新角色', 1, NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:role:delete', '删除角色', 1, NOW());

INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:menu:create', '创建菜单', 1, NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:menu:update', '更新菜单', 1, NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, status, created_time) VALUES (1, 'system:menu:delete', '删除菜单', 1, NOW());

-- 3. 角色-权限关联：tenant_admin 关联上述全部权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT @role_id, id FROM sys_permission
WHERE tenant_id = 1
  AND perm_code IN (
    'system:user:list', 'system:user:manage', 'system:user:reset', 'system:user:assign',
    'system:role:list', 'system:role:manage', 'system:role:assign',
    'system:menu:list', 'system:menu:manage',
    'system:tenant:list', 'system:tenant:manage',
    'audit:log:list',
    'file:upload', 'file:delete',
    'system:user:create', 'system:user:update', 'system:user:delete',
    'system:role:create', 'system:role:update', 'system:role:delete',
    'system:menu:create', 'system:menu:update', 'system:menu:delete'
  );

-- 4. 用户-角色关联：将 admin 用户(id=1)绑定 tenant_admin 角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, @role_id);
