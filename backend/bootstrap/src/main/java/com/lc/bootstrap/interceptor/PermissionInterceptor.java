package com.lc.bootstrap.interceptor;

import com.lc.common.annotation.PreAuthorize;
import com.lc.common.context.UserContext;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.system.service.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 接口权限校验拦截器。
 * <p>
 * 解析方法/类上的 {@link PreAuthorize} 注解，根据 {@link PermissionService}
 * 判断当前用户是否具备所需权限。注解不存在时直接放行，由 Security 层保证已认证。
 * </p>
 * <p>
 * 校验顺序：方法级注解优先于类级注解；当方法未标注时回退到类级。
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionInterceptor implements HandlerInterceptor {

    private final PermissionService permissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // 优先方法级注解，其次类级注解
        PreAuthorize preAuthorize = handlerMethod.getMethodAnnotation(PreAuthorize.class);
        if (preAuthorize == null) {
            preAuthorize = handlerMethod.getBeanType().getAnnotation(PreAuthorize.class);
        }
        if (preAuthorize == null) {
            return true;
        }

        String value = preAuthorize.value();
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(GlobalErrorCode.UNAUTHORIZED);
        }

        // 解析逗号分隔的权限码
        List<String> required = new ArrayList<>();
        for (String token : Arrays.asList(value.split(","))) {
            if (token != null && !token.trim().isEmpty()) {
                required.add(token.trim());
            }
        }
        if (required.isEmpty()) {
            return true;
        }

        String[] permissions = required.toArray(new String[0]);
        boolean granted;
        if (preAuthorize.requireAll()) {
            granted = permissionService.hasAllPermissions(userId, permissions);
        } else {
            granted = permissionService.hasAnyPermission(userId, permissions);
        }

        if (!granted) {
            log.warn("Permission denied: userId={}, required={}, requireAll={}",
                    userId, required, preAuthorize.requireAll());
            throw new BusinessException(GlobalErrorCode.PERMISSION_DENIED);
        }
        return true;
    }
}
