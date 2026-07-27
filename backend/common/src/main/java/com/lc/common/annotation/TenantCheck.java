package com.lc.common.annotation;

import java.lang.annotation.*;

/**
 * 标注需要租户校验的方法/类。
 * 拦截器会从请求参数中提取 {@link #tenantIdParam()} 指定的字段，
 * 与当前登录用户的租户ID进行比较，不一致则抛出 PERMISSION_DENIED。
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantCheck {
    /** 请求参数中租户ID的字段名，默认为 tenantId */
    String tenantIdParam() default "tenantId";
}
