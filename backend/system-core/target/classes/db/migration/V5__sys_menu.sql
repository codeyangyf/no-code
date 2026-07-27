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
    INDEX idx_menu_tenant_id (tenant_id),
    INDEX idx_menu_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';