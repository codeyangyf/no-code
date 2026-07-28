-- ============================================================
-- 低代码平台 数据库初始化脚本
-- 合并自 V1~V19 Flyway 迁移脚本 + 项目数据库 baseline
-- 执行方式: mysql -u root -p < init.sql
-- ============================================================

-- 创建数据库（如不存在）
CREATE DATABASE IF NOT EXISTS lc_platform DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
USE lc_platform;

-- ============================================================
-- 1. 租户表 (V1 + V17软删除 + V18乐观锁)
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_tenant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_code VARCHAR(64) NOT NULL UNIQUE COMMENT '租户编码',
    tenant_name VARCHAR(128) NOT NULL COMMENT '租户名称',
    logo_url VARCHAR(512) COMMENT 'Logo地址',
    domain VARCHAR(256) COMMENT '域名',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    expire_time DATETIME COMMENT '过期时间',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0=未删除，1=已删除',
    version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    INDEX idx_tenant_code (tenant_code),
    INDEX idx_sys_tenant_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户表';

-- ============================================================
-- 2. 用户表 (V2 + V17 + V18)
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT COMMENT '租户ID',
    username VARCHAR(64) NOT NULL COMMENT '用户名',
    password VARCHAR(256) NOT NULL COMMENT '密码(BCrypt)',
    real_name VARCHAR(64) COMMENT '真实姓名',
    email VARCHAR(128) COMMENT '邮箱',
    phone VARCHAR(32) COMMENT '手机号',
    avatar_url VARCHAR(512) COMMENT '头像地址',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    last_login_time DATETIME COMMENT '最后登录时间',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0=未删除，1=已删除',
    version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    INDEX idx_user_tenant_id (tenant_id),
    INDEX idx_user_username (username),
    INDEX idx_sys_user_deleted (deleted),
    CONSTRAINT uk_user_tenant_username UNIQUE (tenant_id, username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================================
-- 3. 角色表 (V3 + V17 + V18)
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT COMMENT '租户ID',
    role_code VARCHAR(64) NOT NULL COMMENT '角色编码',
    role_name VARCHAR(128) NOT NULL COMMENT '角色名称',
    description VARCHAR(512) COMMENT '描述',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    sort_order INT COMMENT '排序',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0=未删除，1=已删除',
    version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    INDEX idx_role_tenant_id (tenant_id),
    INDEX idx_sys_role_deleted (deleted),
    CONSTRAINT uk_role_tenant_code UNIQUE (tenant_id, role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- ============================================================
-- 4. 用户角色关联表 (V4)
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    CONSTRAINT uk_user_role UNIQUE (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- ============================================================
-- 5. 菜单表 (V5 + V17 + V18)
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT COMMENT '租户ID',
    parent_id BIGINT COMMENT '父菜单ID',
    menu_name VARCHAR(128) NOT NULL COMMENT '菜单名称',
    path VARCHAR(256) COMMENT '路由路径',
    component VARCHAR(256) COMMENT '组件路径',
    icon VARCHAR(128) COMMENT '图标',
    menu_type VARCHAR(16) NOT NULL COMMENT '菜单类型 MENU/BUTTON/DIRECTORY',
    permission VARCHAR(128) COMMENT '权限标识',
    sort_order INT COMMENT '排序',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0=未删除，1=已删除',
    version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    INDEX idx_menu_tenant_id (tenant_id),
    INDEX idx_menu_parent_id (parent_id),
    INDEX idx_sys_menu_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';

-- ============================================================
-- 6. 权限表 (V6)
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT COMMENT '租户ID',
    perm_code VARCHAR(128) NOT NULL COMMENT '权限编码',
    perm_name VARCHAR(128) NOT NULL COMMENT '权限名称',
    description VARCHAR(512) COMMENT '描述',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    INDEX idx_perm_tenant_id (tenant_id),
    CONSTRAINT uk_perm_tenant_code UNIQUE (tenant_id, perm_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- ============================================================
-- 7. 组织表 (V7)
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_org (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT COMMENT '租户ID',
    parent_id BIGINT COMMENT '父组织ID',
    org_code VARCHAR(64) NOT NULL COMMENT '组织编码',
    org_name VARCHAR(128) NOT NULL COMMENT '组织名称',
    org_type VARCHAR(32) COMMENT '组织类型',
    sort_order INT COMMENT '排序',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间',
    INDEX idx_org_tenant_id (tenant_id),
    INDEX idx_org_parent_id (parent_id),
    CONSTRAINT uk_org_tenant_code UNIQUE (tenant_id, org_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织表';

-- ============================================================
-- 8. 字典表 (V8)
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_dict (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT COMMENT '租户ID',
    dict_code VARCHAR(64) NOT NULL COMMENT '字典编码',
    dict_name VARCHAR(128) NOT NULL COMMENT '字典名称',
    description VARCHAR(512) COMMENT '描述',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间',
    INDEX idx_dict_tenant_id (tenant_id),
    CONSTRAINT uk_dict_tenant_code UNIQUE (tenant_id, dict_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典表';

-- ============================================================
-- 9. 项目信息表 (V9)
-- ============================================================
CREATE TABLE IF NOT EXISTS project_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    project_code VARCHAR(64) NOT NULL COMMENT '项目编码',
    project_name VARCHAR(128) NOT NULL COMMENT '项目名称',
    description VARCHAR(1024) COMMENT '描述',
    icon VARCHAR(128) COMMENT '图标',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    lifecycle_status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '生命周期状态',
    created_by BIGINT NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间',
    INDEX idx_project_tenant_id (tenant_id),
    INDEX idx_project_code (project_code),
    CONSTRAINT uk_project_tenant_code UNIQUE (tenant_id, project_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目信息表';

-- ============================================================
-- 10. 项目成员表 (V10)
-- ============================================================
CREATE TABLE IF NOT EXISTS project_member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    project_id BIGINT NOT NULL COMMENT '项目ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role VARCHAR(32) NOT NULL COMMENT '角色 READ_ONLY/EDITOR/ADMIN/PUBLISHER',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    joined_time DATETIME NOT NULL COMMENT '加入时间',
    CONSTRAINT uk_project_member UNIQUE (project_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目成员表';

-- ============================================================
-- 11. 审计日志表 (V11 + V15字段对齐)
-- ============================================================
CREATE TABLE IF NOT EXISTS audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT COMMENT '租户ID',
    project_id BIGINT COMMENT '项目ID',
    user_id BIGINT COMMENT '操作用户ID',
    user_name VARCHAR(64) COMMENT '操作用户名',
    operation VARCHAR(128) NOT NULL COMMENT '操作类型',
    target_type VARCHAR(64) COMMENT '目标类型',
    target_id BIGINT COMMENT '目标ID',
    before_data TEXT COMMENT '操作前数据',
    after_data TEXT COMMENT '操作后数据',
    detail VARCHAR(2048) COMMENT '操作详情',
    ip VARCHAR(64) COMMENT 'IP地址',
    user_agent VARCHAR(512) COMMENT 'UserAgent',
    request_id VARCHAR(64) COMMENT '请求ID',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    action VARCHAR(128) COMMENT '操作动作',
    resource_type VARCHAR(64) COMMENT '资源类型',
    resource_id VARCHAR(128) COMMENT '资源ID',
    client_ip VARCHAR(64) COMMENT '客户端IP',
    result VARCHAR(32) NOT NULL DEFAULT 'SUCCESS' COMMENT '操作结果 SUCCESS/FAILED',
    error_message VARCHAR(512) COMMENT '错误信息',
    INDEX idx_audit_tenant_id (tenant_id),
    INDEX idx_audit_project_id (project_id),
    INDEX idx_audit_user_id (user_id),
    INDEX idx_audit_created_time (created_time),
    INDEX idx_audit_result (result),
    INDEX idx_audit_action (action),
    INDEX idx_audit_resource_type (resource_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审计日志表';

-- ============================================================
-- 12. 角色菜单关联表 (V13)
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_role_menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    UNIQUE KEY uk_role_menu (role_id, menu_id),
    INDEX idx_role_menu_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- ============================================================
-- 13. 角色权限关联表 (V14)
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    UNIQUE KEY uk_role_permission (role_id, permission_id),
    INDEX idx_role_perm_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- ============================================================
-- 初始数据 (V12)
-- ============================================================
INSERT INTO sys_tenant (tenant_code, tenant_name, status, created_time)
VALUES ('default', '默认租户', 1, NOW());

INSERT INTO sys_user (tenant_id, username, password, real_name, email, phone, status, created_time)
VALUES (1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', 'admin@example.com', '13800138000', 1, NOW());

-- ============================================================
-- RBAC种子数据 (V16)
-- ============================================================
-- 创建租户管理员角色
INSERT INTO sys_role (tenant_id, role_code, role_name, status, created_time)
VALUES (1, 'tenant_admin', '租户管理员', 1, NOW());

SET @role_id = LAST_INSERT_ID();

-- 系统权限
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:user:list',   '用户列表',   NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:user:manage', '用户管理',   NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:user:reset',  '重置用户密码', NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:user:assign', '分配用户角色', NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:role:list',   '角色列表',   NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:role:manage', '角色管理',   NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:role:assign', '分配角色菜单', NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:menu:list',   '菜单列表',   NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:menu:manage', '菜单管理',   NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:tenant:list',   '租户列表', NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:tenant:manage', '租户管理', NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'audit:log:list', '审计日志列表', NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'file:upload', '文件上传', NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'file:delete', '文件删除', NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:user:create', '创建用户', NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:user:update', '更新用户', NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:user:delete', '删除用户', NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:role:create', '创建角色', NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:role:update', '更新角色', NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:role:delete', '删除角色', NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:menu:create', '创建菜单', NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:menu:update', '更新菜单', NOW());
INSERT INTO sys_permission (tenant_id, perm_code, perm_name, created_time) VALUES (1, 'system:menu:delete', '删除菜单', NOW());

-- 角色-权限关联
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

-- 用户-角色关联
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, @role_id);

-- ============================================================
-- 菜单种子数据 (V19)
-- ============================================================
-- 一级目录：系统管理
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, NULL, '系统管理', '/system', NULL, 'Setting', 'DIRECTORY', NULL, 1, 1, NOW());
SET @dir_system = LAST_INSERT_ID();

-- 用户管理
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @dir_system, '用户管理', '/system/users', 'system/user/List', 'User', 'MENU', 'system:user:list', 1, 1, NOW());
SET @menu_user = LAST_INSERT_ID();

INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_user, '创建用户', NULL, NULL, NULL, 'BUTTON', 'system:user:create', 1, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_user, '更新用户', NULL, NULL, NULL, 'BUTTON', 'system:user:update', 2, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_user, '删除用户', NULL, NULL, NULL, 'BUTTON', 'system:user:delete', 3, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_user, '重置密码', NULL, NULL, NULL, 'BUTTON', 'system:user:reset', 4, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_user, '分配角色', NULL, NULL, NULL, 'BUTTON', 'system:user:assign', 5, 1, NOW());

-- 角色管理
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @dir_system, '角色管理', '/system/roles', 'system/role/List', 'Team', 'MENU', 'system:role:list', 2, 1, NOW());
SET @menu_role = LAST_INSERT_ID();

INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_role, '创建角色', NULL, NULL, NULL, 'BUTTON', 'system:role:create', 1, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_role, '更新角色', NULL, NULL, NULL, 'BUTTON', 'system:role:update', 2, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_role, '删除角色', NULL, NULL, NULL, 'BUTTON', 'system:role:delete', 3, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_role, '分配菜单', NULL, NULL, NULL, 'BUTTON', 'system:role:assign', 4, 1, NOW());

-- 菜单管理
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @dir_system, '菜单管理', '/system/menus', 'system/menu/List', 'Menu', 'MENU', 'system:menu:list', 3, 1, NOW());
SET @menu_menu = LAST_INSERT_ID();

INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_menu, '创建菜单', NULL, NULL, NULL, 'BUTTON', 'system:menu:create', 1, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_menu, '更新菜单', NULL, NULL, NULL, 'BUTTON', 'system:menu:update', 2, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_menu, '删除菜单', NULL, NULL, NULL, 'BUTTON', 'system:menu:delete', 3, 1, NOW());

-- 租户管理
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, @dir_system, '租户管理', '/system/tenants', 'system/tenant/List', 'Building', 'MENU', 'system:tenant:list', 4, 1, NOW());
SET @menu_tenant = LAST_INSERT_ID();

INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_tenant, '创建租户', NULL, NULL, NULL, 'BUTTON', 'system:tenant:manage', 1, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_tenant, '更新租户', NULL, NULL, NULL, 'BUTTON', 'system:tenant:manage', 2, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_tenant, '删除租户', NULL, NULL, NULL, 'BUTTON', 'system:tenant:manage', 3, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_tenant, '切换状态', NULL, NULL, NULL, 'BUTTON', 'system:tenant:manage', 4, 1, NOW());

-- 审计日志
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, NULL, '审计日志', '/system/audit-logs', 'system/audit/List', 'FileSearch', 'MENU', 'audit:log:list', 2, 1, NOW());

-- 文件管理
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time)
VALUES (1, NULL, '文件管理', '/system/files', 'system/file/List', 'FolderOpen', 'MENU', 'file:upload', 3, 1, NOW());
SET @menu_file = LAST_INSERT_ID();

INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_file, '上传文件', NULL, NULL, NULL, 'BUTTON', 'file:upload', 1, 1, NOW());
INSERT INTO sys_menu (tenant_id, parent_id, menu_name, path, component, icon, menu_type, permission, sort_order, status, created_time) VALUES (1, @menu_file, '删除文件', NULL, NULL, NULL, 'BUTTON', 'file:delete', 2, 1, NOW());

-- 角色-菜单关联
SET @role_id = (SELECT id FROM sys_role WHERE tenant_id = 1 AND role_code = 'tenant_admin');
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @role_id, id FROM sys_menu WHERE tenant_id = 1;

-- ============================================================
-- 初始化完成
-- ============================================================
