# Task 5: system-core 模块 - JWT 与 Token 服务

**Goal:** 创建 JWT 配置类、认证 DTO、JWT Token 服务和 Refresh Token 服务。

**Files:**
- Create: `backend/system-core/src/main/java/com/lc/system/config/JwtConfig.java`
- Create: `backend/system-core/src/main/java/com/lc/system/dto/AuthDTO.java`
- Create: `backend/system-core/src/main/java/com/lc/system/security/JwtTokenService.java`
- Create: `backend/system-core/src/main/java/com/lc/system/security/RefreshTokenService.java`

**JwtConfig.java:**

```java
package com.lc.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secret = "your-256-bit-secret-key-here-must-be-at-least-32-characters";
    private int accessTokenExpireMinutes = 15;
    private int refreshTokenExpireDays = 7;
}
```

**AuthDTO.java:**

```java
package com.lc.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDTO {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponse {
        private String accessToken;
        private String refreshToken;
        private LocalDateTime accessTokenExpireTime;
        private LocalDateTime refreshTokenExpireTime;
        private UserInfo user;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String username;
        private String realName;
        private String email;
        private String phone;
        private Long tenantId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshRequest {
        private String refreshToken;
    }
}
```

**JwtTokenService.java:**

```java
package com.lc.system.security;

import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.system.config.JwtConfig;
import com.lc.system.entity.SysUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtConfig jwtConfig;

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(SysUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("tenantId", user.getTenantId());

        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(jwtConfig.getAccessTokenExpireMinutes());
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .expiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(SysUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());

        LocalDateTime expireTime = LocalDateTime.now().plusDays(jwtConfig.getRefreshTokenExpireDays());
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .expiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new BusinessException(GlobalErrorCode.TOKEN_INVALID);
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    public Long getTenantIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("tenantId", Long.class);
    }
}
```

**RefreshTokenService.java:**

```java
package com.lc.system.security;

import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.system.config.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtConfig jwtConfig;

    private static final String REFRESH_TOKEN_KEY = "refresh:%s";

    public void saveRefreshToken(Long userId, String refreshToken) {
        String key = String.format(REFRESH_TOKEN_KEY, userId);
        redisTemplate.opsForValue().set(key, refreshToken, jwtConfig.getRefreshTokenExpireDays(), TimeUnit.DAYS);
    }

    public String getRefreshToken(Long userId) {
        String key = String.format(REFRESH_TOKEN_KEY, userId);
        Object token = redisTemplate.opsForValue().get(key);
        return token != null ? token.toString() : null;
    }

    public void invalidateRefreshToken(Long userId) {
        String key = String.format(REFRESH_TOKEN_KEY, userId);
        redisTemplate.delete(key);
    }

    public boolean validateRefreshToken(Long userId, String token) {
        String storedToken = getRefreshToken(userId);
        if (storedToken == null) {
            throw new BusinessException(GlobalErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        if (!storedToken.equals(token)) {
            throw new BusinessException(GlobalErrorCode.REFRESH_TOKEN_INVALID);
        }
        return true;
    }
}
```

**Steps:**
1. 创建 JwtConfig.java
2. 创建 AuthDTO.java
3. 创建 JwtTokenService.java
4. 创建 RefreshTokenService.java
5. 编译验证：`cd backend && mvn clean compile -q -pl system-core -am`
6. Commit，提交信息："feat: system-core模块 - JWT与Token服务"

**Global Constraints:**
- Java: 17
- Spring Boot: 3.2.5
- Maven: 3.9.x
- JJWT: 0.12.5

__tr_native_ec=$?; pwd -P >| '/var/log/tool/jobs/job-e7c83eb457a8441cacadbf2402a3cd14/cwd.txt'; exit "$__tr_native_ec"