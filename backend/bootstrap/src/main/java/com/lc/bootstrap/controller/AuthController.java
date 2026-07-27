package com.lc.bootstrap.controller;

import com.lc.common.annotation.AuditLog;
import com.lc.common.context.UserContext;
import com.lc.common.dto.Result;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.system.dto.AuthDTO;
import com.lc.system.entity.SysTenant;
import com.lc.system.entity.SysUser;
import com.lc.system.security.JwtTokenService;
import com.lc.system.security.RefreshTokenService;
import com.lc.system.service.TenantService;
import com.lc.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;
    private final TenantService tenantService;

    @PostMapping("/login")
    @AuditLog(action = "用户登录", resourceType = "USER", resourceIdParam = "#request.username")
    public Result<AuthDTO.LoginResponse> login(@RequestBody AuthDTO.LoginRequest request) {
        // 按租户编码隔离查询用户，避免跨租户串号
        SysUser user;
        if (StringUtils.hasText(request.getTenantCode())) {
            SysTenant tenant = tenantService.getByCode(request.getTenantCode());
            if (tenant == null || tenant.getStatus() == null || tenant.getStatus() != 1) {
                throw new BusinessException(GlobalErrorCode.TENANT_NOT_FOUND.getCode(), "租户不存在或已禁用");
            }
            user = userService.findByTenantIdAndUsername(tenant.getId(), request.getUsername());
        } else {
            user = userService.findByUsername(request.getUsername());
        }

        if (!userService.verifyPassword(request.getPassword(), user.getPassword())) {
            throw new BusinessException(GlobalErrorCode.USERNAME_OR_PASSWORD_ERROR);
        }

        // 校验账户启用状态（用用户名密码错误码避免泄露账户存在性）
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(GlobalErrorCode.USERNAME_OR_PASSWORD_ERROR.getCode(), "账户已禁用");
        }

        String accessToken = jwtTokenService.generateAccessToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);
        refreshTokenService.saveRefreshToken(user.getId(), refreshToken);

        AuthDTO.LoginResponse response = AuthDTO.LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(AuthDTO.UserInfo.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .realName(user.getRealName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .tenantId(user.getTenantId())
                        .build())
                .build();

        return Result.success(response);
    }

    @PostMapping("/refresh")
    public Result<AuthDTO.LoginResponse> refresh(@RequestBody AuthDTO.RefreshRequest request) {
        // 校验 token 类型必须为 refresh，防止 access token 被用于刷新
        String type = jwtTokenService.getTokenType(request.getRefreshToken());
        if (!"refresh".equals(type)) {
            throw new BusinessException(GlobalErrorCode.REFRESH_TOKEN_INVALID);
        }

        Long userId = jwtTokenService.getUserIdFromToken(request.getRefreshToken());
        refreshTokenService.validateRefreshToken(userId, request.getRefreshToken());
        SysUser user = userService.findById(userId);

        String accessToken = jwtTokenService.generateAccessToken(user);
        String newRefreshToken = jwtTokenService.generateRefreshToken(user);
        refreshTokenService.saveRefreshToken(userId, newRefreshToken);

        AuthDTO.LoginResponse response = AuthDTO.LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .user(AuthDTO.UserInfo.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .realName(user.getRealName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .tenantId(user.getTenantId())
                        .build())
                .build();

        return Result.success(response);
    }

    @PostMapping("/logout")
    @AuditLog(action = "用户登出", resourceType = "USER")
    public Result<Void> logout() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(GlobalErrorCode.UNAUTHORIZED);
        }
        refreshTokenService.invalidateRefreshToken(userId);
        return Result.success();
    }
}
