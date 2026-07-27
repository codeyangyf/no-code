package com.lc.common.exception;

import lombok.Getter;

@Getter
public enum GlobalErrorCode {
    SUCCESS(0, "success"),
    FAIL(-1, "fail"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    VALIDATION_ERROR(400, "参数校验失败"),
    USER_NOT_FOUND(1001, "用户不存在"),
    USERNAME_OR_PASSWORD_ERROR(1002, "用户名或密码错误"),
    TOKEN_EXPIRED(1003, "Token已过期"),
    TOKEN_INVALID(1004, "Token无效"),
    REFRESH_TOKEN_EXPIRED(1005, "刷新Token已过期"),
    REFRESH_TOKEN_INVALID(1006, "刷新Token无效"),
    TENANT_NOT_FOUND(2001, "租户不存在"),
    ROLE_NOT_FOUND(2002, "角色不存在"),
    PERMISSION_DENIED(2003, "权限不足"),
    DATA_CONFLICT(2004, "数据冲突"),
    SYSTEM_ERROR(500, "系统内部错误");

    private final int code;
    private final String message;

    GlobalErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}