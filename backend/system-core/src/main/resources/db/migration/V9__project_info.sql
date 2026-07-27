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