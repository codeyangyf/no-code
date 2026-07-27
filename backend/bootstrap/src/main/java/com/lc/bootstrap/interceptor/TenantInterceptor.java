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
        String paramValue = request.getParameter(paramName);
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
}
