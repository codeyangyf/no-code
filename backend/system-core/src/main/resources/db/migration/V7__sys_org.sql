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