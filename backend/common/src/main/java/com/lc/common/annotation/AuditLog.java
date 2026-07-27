package com.lc.common.annotation;

import java.lang.annotation.*;

/**
 * 审计日志注解，标注在 Controller 方法上，由 AOP 切面自动记录审计日志。
 * 操作结果（成功/失败）、操作人、IP、UserAgent 由切面自动填充。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {
    /** 操作动作，如 "用户登录"、"创建项目" */
    String action();

    /** 资源类型，如 "USER"、"PROJECT"、"ROLE" */
    String resourceType();

    /**
     * 资源ID参数名，从方法参数中按名取值，支持 SpEL 表达式 #id。
     * 若不指定或为空字符串，则不记录 resource_id。
     */
    String resourceIdParam() default "";
}
