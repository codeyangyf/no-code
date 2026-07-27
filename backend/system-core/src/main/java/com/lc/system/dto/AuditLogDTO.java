package com.lc.system.dto;

import com.lc.system.dto.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

public class AuditLogDTO {

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class QueryRequest extends PageRequest {
        /** 租户ID（超级管理员可指定，普通用户自动取上下文） */
        private Long tenantId;
        /** 项目ID */
        private Long projectId;
        /** 用户ID */
        private Long userId;
        /** 操作动作 */
        private String action;
        /** 资源类型 */
        private String resourceType;
        /** 操作结果 SUCCESS/FAILED */
        private String result;
        /** 起始时间 */
        private LocalDateTime startTime;
        /** 结束时间 */
        private LocalDateTime endTime;
    }

    @Data
    public static class Response {
        private Long id;
        private Long tenantId;
        private Long projectId;
        private Long userId;
        private String userName;
        private String action;
        private String resourceType;
        private String resourceId;
        private String clientIp;
        private String result;
        private String errorMessage;
        private String detail;
        private String requestId;
        private LocalDateTime createdTime;
    }
}
