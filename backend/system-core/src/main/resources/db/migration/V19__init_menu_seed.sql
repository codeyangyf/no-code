-- V19: 初始化默认租户菜单种子数据（树形结构）
-- 菜单类型：DIRECTORY(目录)、MENU(菜单)、BUTTON(按钮)

-- ========================================
-- 一级目录：系统管理
-- ========================================
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, NULL, '系统管理', '/system', NULL, 'Setting', 'DIRECTORY', NULL, 1, 1, NOW());

SET @dir_system = LAST_INSERT_ID();

-- ========================================
-- 系统管理子菜单
-- ========================================

-- 用户管理
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @dir_system, '用户管理', '/system/users', 'system/user/List', 'User', 'MENU', 'system:user:list', 1, 1, NOW());

SET @menu_user = LAST_INSERT_ID();

-- 用户管理按钮权限
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_user, '创建用户', NULL, NULL, NULL, 'BUTTON', 'system:user:create', 1, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_user, '更新用户', NULL, NULL, NULL, 'BUTTON', 'system:user:update', 2, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_user, '删除用户', NULL, NULL, NULL, 'BUTTON', 'system:user:delete', 3, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_user, '重置密码', NULL, NULL, NULL, 'BUTTON', 'system:user:reset', 4, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_user, '分配角色', NULL, NULL, NULL, 'BUTTON', 'system:user:assign', 5, 1, NOW());

-- 角色管理
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @dir_system, '角色管理', '/system/roles', 'system/role/List', 'Team', 'MENU', 'system:role:list', 2, 1, NOW());

SET @menu_role = LAST_INSERT_ID();

-- 角色管理按钮权限
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_role, '创建角色', NULL, NULL, NULL, 'BUTTON', 'system:role:create', 1, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_role, '更新角色', NULL, NULL, NULL, 'BUTTON', 'system:role:update', 2, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_role, '删除角色', NULL, NULL, NULL, 'BUTTON', 'system:role:delete', 3, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_role, '分配菜单', NULL, NULL, NULL, 'BUTTON', 'system:role:assign', 4, 1, NOW());

-- 菜单管理
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @dir_system, '菜单管理', '/system/menus', 'system/menu/List', 'Menu', 'MENU', 'system:menu:list', 3, 1, NOW());

SET @menu_menu = LAST_INSERT_ID();

-- 菜单管理按钮权限
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_menu, '创建菜单', NULL, NULL, NULL, 'BUTTON', 'system:menu:create', 1, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_menu, '更新菜单', NULL, NULL, NULL, 'BUTTON', 'system:menu:update', 2, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_menu, '删除菜单', NULL, NULL, NULL, 'BUTTON', 'system:menu:delete', 3, 1, NOW());

-- 租户管理
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @dir_system, '租户管理', '/system/tenants', 'system/tenant/List', 'Building', 'MENU', 'system:tenant:list', 4, 1, NOW());

SET @menu_tenant = LAST_INSERT_ID();

-- 租户管理按钮权限
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_tenant, '创建租户', NULL, NULL, NULL, 'BUTTON', 'system:tenant:manage', 1, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_tenant, '更新租户', NULL, NULL, NULL, 'BUTTON', 'system:tenant:manage', 2, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_tenant, '删除租户', NULL, NULL, NULL, 'BUTTON', 'system:tenant:manage', 3, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_tenant, '切换状态', NULL, NULL, NULL, 'BUTTON', 'system:tenant:manage', 4, 1, NOW());

-- ========================================
-- 一级菜单：审计日志
-- ========================================
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, NULL, '审计日志', '/system/audit-logs', 'system/audit/List', 'FileSearch', 'MENU', 'audit:log:list', 2, 1, NOW());

-- ========================================
-- 一级菜单：文件管理
-- ========================================
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, NULL, '文件管理', '/system/files', 'system/file/List', 'FolderOpen', 'MENU', 'file:upload', 3, 1, NOW());

SET @menu_file = LAST_INSERT_ID();

-- 文件管理按钮权限
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_file, '上传文件', NULL, NULL, NULL, 'BUTTON', 'file:upload', 1, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @menu_file, '删除文件', NULL, NULL, NULL, 'BUTTON', 'file:delete', 2, 1, NOW());

-- ========================================
-- 角色-菜单关联：tenant_admin 角色关联所有菜单
-- ========================================
-- 获取 tenant_admin 角色ID
SET @role_id = (SELECT id FROM sys_role WHERE tenant_id = 1 AND role_code = 'tenant_admin');

-- 将 tenant_admin 关联到所有菜单(tenant_id=1)
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @role_id, id FROM sys_menu WHERE tenant_id = 1;
