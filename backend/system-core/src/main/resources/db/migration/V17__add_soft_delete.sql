-- 为业务主表添加软删除字段
ALTER TABLE sys_tenant ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0=未删除，1=已删除';
ALTER TABLE sys_user   ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0=未删除，1=已删除';
ALTER TABLE sys_role   ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0=未删除，1=已删除';
ALTER TABLE sys_menu   ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0=未删除，1=已删除';

-- 软删除索引（按租户+删除状态查询）
CREATE INDEX idx_sys_tenant_deleted ON sys_tenant(deleted);
CREATE INDEX idx_sys_user_deleted   ON sys_user(deleted);
CREATE INDEX idx_sys_role_deleted   ON sys_role(deleted);
CREATE INDEX idx_sys_menu_deleted   ON sys_menu(deleted);
