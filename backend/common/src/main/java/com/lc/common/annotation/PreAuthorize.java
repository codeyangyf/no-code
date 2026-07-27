package com.lc.common.annotation;

import java.lang.annotation.*;

/**
 * 接口权限校验注解。
 * <p>
 * 由 {@code PermissionInterceptor} 在请求进入 Controller 前解析：
 * <ul>
 *   <li>{@link #value()} 为逗号分隔的权限码列表，对应 {@code PermissionService#getUserPermissions} 返回的权限集合</li>
 *   <li>{@link #requireAll()} 为 true 时要求用户持有全部权限；为 false（默认）时持有任一即可</li>
 * </ul>
 * </p>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuthorize {
    /** 所需权限码，多个用逗号分隔 */
    String value() default "";
    /** true=需全部权限，false=任一权限即可 */
    boolean requireAll() default false;
}
