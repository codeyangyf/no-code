package com.lc.bootstrap.interceptor;

import com.lc.common.annotation.TenantCheck;
import com.lc.common.context.UserContext;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

/**
 * 多租户越权校验拦截器。
 * <p>
 * 当前支持的 tenantId 提取方式：
 * <ul>
 *   <li>query parameter：通过 {@code request.getParameter(tenantIdParam())} 提取</li>
 *   <li>path variable：通过 Spring MVC 解析后的 URI template variables
 *       （{@link HandlerMapping#URI_TEMPLATE_VARIABLES_ATTRIBUTE}）提取</li>
 * </ul>
 * request body 中的 tenantId 暂不支持，后续可通过 AOP 切面（如 @Around 切
 * @RequestBody 参数反序列化后的对象）扩展，避免在拦截器层重复读取 body 流。
 * </p>
 */
@Slf4j
@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // 优先方法级注解，其次类级注解
        TenantCheck tenantCheck = handlerMethod.getMethodAnnotation(TenantCheck.class);
        if (tenantCheck == null) {
            tenantCheck = handlerMethod.getBeanType().getAnnotation(TenantCheck.class);
        }
        if (tenantCheck == null) {
            return true;
        }

        // 当前登录用户的租户ID为空时（如超级管理员），跳过校验
        Long currentTenantId = UserContext.getTenantId();
        if (currentTenantId == null) {
            return true;
        }

        String paramName = tenantCheck.tenantIdParam();
        // 1. 优先从 query parameter 提取
        String paramValue = request.getParameter(paramName);
        // 2. query parameter 未携带时，尝试从 path variable 提取
        if (paramValue == null || paramValue.trim().isEmpty()) {
            paramValue = extractPathVariable(request, paramName);
        }
        // 请求未携带目标租户ID时跳过校验（交由业务层处理）
        if (paramValue == null || paramValue.trim().isEmpty()) {
            return true;
        }

        Long targetTenantId;
        try {
            targetTenantId = Long.parseLong(paramValue.trim());
        } catch (NumberFormatException e) {
            log.warn("Invalid tenantId param: {}", paramValue);
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR);
        }

        if (!currentTenantId.equals(targetTenantId)) {
            log.warn("Tenant permission denied: current={}, target={}", currentTenantId, targetTenantId);
            throw new BusinessException(GlobalErrorCode.PERMISSION_DENIED);
        }
        return true;
    }

    /**
     * 从 Spring MVC 解析后的 URI template variables 中提取 path variable。
     * 该属性由 RequestMappingHandlerMapping 在请求映射完成后写入 request attribute。
     */
    private String extractPathVariable(HttpServletRequest request, String paramName) {
        Object pathVars = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVars instanceof Map<?, ?> map) {
            Object value = map.get(paramName);
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }
}
