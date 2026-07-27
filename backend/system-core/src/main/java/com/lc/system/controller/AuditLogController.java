package com.lc.system.controller;

import com.lc.common.annotation.AuditLog;
import com.lc.common.annotation.PreAuthorize;
import com.lc.common.context.UserContext;
import com.lc.common.dto.PageResult;
import com.lc.common.dto.Result;
import com.lc.system.dto.AuditLogDTO;
import com.lc.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 审计日志查询 REST 接口。
 * <p>
 * 普通用户只能查询本租户的审计日志；超级管理员（{@link UserContext#getTenantId()} 为 null）
 * 可查询任意租户（按 request.tenantId 过滤）。
 */
@RestController
@RequestMapping("/api/system/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("audit:log:list")
    @AuditLog(action = "查询审计日志", resourceType = "AUDIT_LOG")
    public Result<PageResult<AuditLogDTO.Response>> list(AuditLogDTO.QueryRequest request) {
        // 租户隔离：普通用户强制覆盖 tenantId 为当前用户租户
        Long currentTenantId = UserContext.getTenantId();
        if (currentTenantId != null) {
            // 普通用户：强制只能查自己租户
            request.setTenantId(currentTenantId);
        }
        // currentTenantId == null 表示超级管理员，允许查任意租户（按 request.tenantId 过滤）
        return Result.success(auditLogService.pageQuery(request));
    }
}
