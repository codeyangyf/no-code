package com.lc.bootstrap.filter;

import com.lc.common.context.TenantContext;
import com.lc.common.context.UserContext;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.system.security.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = extractToken(request);
            if (StringUtils.hasText(token)) {
                // 校验 token 类型必须为 access，防止 refresh token 被当作 access token 使用
                String type = jwtTokenService.getTokenType(token);
                if (!"access".equals(type)) {
                    throw new BusinessException(GlobalErrorCode.TOKEN_INVALID);
                }

                String username = jwtTokenService.getUsernameFromToken(token);
                Long userId = jwtTokenService.getUserIdFromToken(token);
                Long tenantId = jwtTokenService.getTenantIdFromToken(token);

                // 设置用户上下文与租户上下文
                UserContext.set(UserContext.builder()
                        .userId(userId)
                        .tenantId(tenantId)
                        .username(username)
                        .build());
                if (tenantId != null) {
                    TenantContext.setTenantId(tenantId);
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userId, null, Collections.singletonList(new SimpleGrantedAuthority("USER"))
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            log.warn("JWT authentication failed: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            UserContext.clear();
            TenantContext.clear();
        } finally {
            try {
                filterChain.doFilter(request, response);
            } finally {
                // 请求结束后清理 ThreadLocal，避免线程池复用导致的上下文泄漏
                UserContext.clear();
                TenantContext.clear();
            }
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
