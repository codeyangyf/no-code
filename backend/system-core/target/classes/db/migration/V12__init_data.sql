INSERT INTO sys_tenant (tenant_code, tenant_name, status, created_time)
VALUES ('default', '默认租户', 1, NOW());

INSERT INTO sys_user (tenant_id, username, password, real_name, email, phone, status, created_time)
VALUES (1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', 'admin@example.com', '13800138000', 1, NOW());
