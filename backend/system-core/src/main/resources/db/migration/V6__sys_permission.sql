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