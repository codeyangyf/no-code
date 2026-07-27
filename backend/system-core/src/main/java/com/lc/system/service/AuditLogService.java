package com.lc.system.service;

import com.lc.common.dto.PageResult;
import com.lc.system.dto.AuditLogDTO;
import com.lc.system.entity.AuditLogEntity;

public interface AuditLogService {
    /**
     * 异步保存审计日志（AOP 切面调用）。
     * 不抛异常，失败仅记录 WARN 日志，避免影响主业务流程。
     */
    void save(AuditLogEntity entity);

    /**
     * 分页查询审计日志。
     * 普通用户只能查询自己租户的日志，超级管理员（tenantId 为 null）可查询所有。
     */
    PageResult<AuditLogDTO.Response> pageQuery(AuditLogDTO.QueryRequest request);
}
