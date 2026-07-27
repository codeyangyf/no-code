-- 对齐设计文档要求的审计日志字段
-- 新增字段：action / resource_type / resource_id / client_ip / result / error_message
-- 旧字段 operation / target_type / target_id / ip 保留以兼容历史数据

ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS action VARCHAR(128) COMMENT '操作动作(对齐设计文档，如 用户登录/创建项目)';
ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS resource_type VARCHAR(64) COMMENT '资源类型(对齐设计文档，如 USER/PROJECT/ROLE)';
ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS resource_id VARCHAR(128) COMMENT '资源ID(对齐设计文档)';
ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS client_ip VARCHAR(64) COMMENT '客户端IP(对齐设计文档)';
ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS result VARCHAR(32) NOT NULL DEFAULT 'SUCCESS' COMMENT '操作结果 SUCCESS/FAILED';
ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS error_message VARCHAR(512) COMMENT '错误信息(FAILED时记录)';

-- 将旧字段数据迁移到新字段（仅当新字段为 NULL 时）
UPDATE audit_log SET action = operation WHERE action IS NULL AND operation IS NOT NULL;
UPDATE audit_log SET resource_type = target_type WHERE resource_type IS NULL AND target_type IS NOT NULL;
UPDATE audit_log SET resource_id = CAST(target_id AS CHAR) WHERE resource_id IS NULL AND target_id IS NOT NULL;
UPDATE audit_log SET client_ip = ip WHERE client_ip IS NULL AND ip IS NOT NULL;

-- 辅助查询索引：按结果统计成功率/失败率、按动作/资源类型筛选
CREATE INDEX idx_audit_result ON audit_log(result);
CREATE INDEX idx_audit_action ON audit_log(action);
CREATE INDEX idx_audit_resource_type ON audit_log(resource_type);
