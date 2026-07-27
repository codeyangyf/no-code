# Task 7: bootstrap 模块 - 认证控制器

**Goal:** 创建 bootstrap 模块的认证控制器，提供登录和刷新 Token 的 REST API。

**Files:**
- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/controller/AuthController.java`

**AuthController.java:**

```java
package com.lc.bootstrap.controller;

import com.lc.common.dto.Result;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.system.dto.AuthDTO;
import com.lc.system.entity.SysUser;
import com.lc.system.security.JwtTokenService;
import com.lc.system.security.RefreshTokenService;
import com.lc.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public Result<AuthDTO.LoginResponse> login(@RequestBody AuthDTO.LoginRequest request) {
        SysUser user = userService.findByUsername(request.getUsername());
        if (!userService.verifyPassword(request.getPassword(), user.getPassword())) {
            throw new BusinessException(GlobalErrorCode.USERNAME_OR_PASSWORD_ERROR);
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
    public Result<Void> logout(@RequestAttribute("userId") Long userId) {
        refreshTokenService.invalidateRefreshToken(userId);
        return Result.success();
    }
}
```

**Steps:**
1. 创建 AuthController.java
2. 编译验证：`cd backend && mvn clean compile -q -pl bootstrap -am`
3. Commit，提交信息："feat: bootstrap模块 - 认证控制器（登录/刷新/登出）"

**Global Constraints:**
- Java: 17
- Spring Boot: 3.2.5
- Maven: 3.9.x

__tr_native_ec=$?; pwd -P >| '/var/log/tool/jobs/job-dbbb89026873417ca870083dbe8b9f78/cwd.txt'; exit "$__tr_native_ec"