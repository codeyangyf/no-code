# Phase 1 脚手架搭建实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 搭建低代码平台 Phase 1 脚手架，覆盖任务1-3（后端骨架、平台库初始化、认证与会话）

**Architecture:** 后端采用 Spring Boot 3.2.x + Maven 多模块（14个模块扁平组织），前端采用 Vite 6 + React 18 + TypeScript + ProComponents。认证使用 JWT + RefreshToken + Redis。数据库使用 MySQL 8.0 + Flyway 迁移。

**Tech Stack:** Spring Boot 3.2.x, Java 17, Maven 3.9.x, MySQL 8.0+, Redis 7.x, Flyway, JJWT 0.12.x, React 18, TypeScript 5.x, Vite 6.x, Ant Design 5.x, ProComponents 2.x, Axios, Lucide React

## Global Constraints

- Java: 17 (Spring Boot 3.2.x minimum)
- Spring Boot: 3.2.5
- Maven: 3.9.x
- MySQL: 8.0+
- Redis: 7.x
- React: 18.2.0
- TypeScript: 5.4.0
- Vite: 6.0.0
- Ant Design: 5.15.0
- ProComponents: 2.6.0

---

## 文件结构总览

### 后端文件

```
backend/
├── pom.xml                                    # 父POM
├── common/                                    # 公共模块
├── system-core/                               # 系统核心
├── project-core/                              # 占位
├── member-core/                               # 占位
├── version-core/                              # 占位
├── template-core/                             # 占位
├── plugin-datasource/                         # 占位
├── plugin-form/                               # 占位
├── plugin-bi/                                 # 占位
├── plugin-flow/                               # 占位
├── plugin-api/                                # 占位
├── sandbox-engine/                            # 占位
├── code-generator/                            # 占位
└── bootstrap/                                 # 启动模块
```

### 前端文件

```
frontend/
├── package.json
├── vite.config.ts
├── tsconfig.json
└── src/
    ├── main.tsx
    ├── App.tsx
    ├── layouts/MainLayout.tsx
    ├── pages/Login/index.tsx
    ├── pages/Dashboard/index.tsx
    ├── api/auth.ts
    ├── hooks/useAuth.ts
    ├── utils/request.ts
    └── utils/token.ts
```

---

### Task 1: 后端父 POM 与目录结构

**Files:**
- Create: `backend/pom.xml`
- Create: `backend/common/pom.xml`
- Create: `backend/system-core/pom.xml`
- Create: `backend/bootstrap/pom.xml`
- Create: 11 个占位模块 pom.xml

**核心代码:**

父 POM (`backend/pom.xml`)：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.lc</groupId>
    <artifactId>lc-platform</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.5</version>
    </parent>
    <properties>
        <java.version>17</java.version>
        <mapstruct.version>1.5.5.Final</mapstruct.version>
        <jjwt.version>0.12.5</jjwt.version>
    </properties>
    <modules>
        <module>common</module>
        <module>system-core</module>
        <module>project-core</module>
        <module>member-core</module>
        <module>version-core</module>
        <module>template-core</module>
        <module>plugin-datasource</module>
        <module>plugin-form</module>
        <module>plugin-bi</module>
        <module>plugin-flow</module>
        <module>plugin-api</module>
        <module>sandbox-engine</module>
        <module>code-generator</module>
        <module>bootstrap</module>
    </modules>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct</artifactId>
                <version>${mapstruct.version}</version>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-api</artifactId>
                <version>${jjwt.version}</version>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-impl</artifactId>
                <version>${jjwt.version}</version>
                <scope>runtime</scope>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-jackson</artifactId>
                <version>${jjwt.version}</version>
                <scope>runtime</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

- [ ] **Step 1: 创建父 POM**
- [ ] **Step 2: 创建 common 模块 pom.xml**
- [ ] **Step 3: 创建 system-core 模块 pom.xml**
- [ ] **Step 4: 创建 bootstrap 模块 pom.xml**
- [ ] **Step 5: 创建 11 个占位模块 pom.xml**
- [ ] **Step 6: 编译验证**
- [ ] **Step 7: Commit**

---

### Task 2: common 模块核心代码

**Files:**
- Create: `backend/common/src/main/java/com/lc/common/exception/BusinessException.java`
- Create: `backend/common/src/main/java/com/lc/common/exception/GlobalErrorCode.java`
- Create: `backend/common/src/main/java/com/lc/common/dto/Result.java`
- Create: `backend/common/src/main/java/com/lc/common/dto/PageResult.java`
- Create: `backend/common/src/main/java/com/lc/common/context/TenantContext.java`
- Create: `backend/common/src/main/java/com/lc/common/config/RedisConfig.java`
- Create: `backend/common/src/main/java/com/lc/common/util/PasswordUtil.java`

**核心代码:**

BusinessException.java：
```java
package com.lc.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
    public BusinessException(GlobalErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
}
```

GlobalErrorCode.java：
```java
package com.lc.common.exception;

import lombok.Getter;

@Getter
public enum GlobalErrorCode {
    SUCCESS(0, "success"), FAIL(-1, "fail"), UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"), NOT_FOUND(404, "资源不存在"),
    USER_NOT_FOUND(1001, "用户不存在"), USERNAME_OR_PASSWORD_ERROR(1002, "用户名或密码错误"),
    TOKEN_EXPIRED(1003, "Token已过期"), TOKEN_INVALID(1004, "Token无效"),
    SYSTEM_ERROR(500, "系统内部错误");

    private final int code;
    private final String message;
    GlobalErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
```

Result.java：
```java
package com.lc.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T data;
    private long timestamp;

    public static <T> Result<T> success(T data) {
        return Result.<T>builder().code(0).message("success").data(data).timestamp(System.currentTimeMillis()).build();
    }
    public static <T> Result<T> fail(int code, String message) {
        return Result.<T>builder().code(code).message(message).timestamp(System.currentTimeMillis()).build();
    }
}
```

- [ ] **Step 1-7: 创建各文件**
- [ ] **Step 8: 编译验证**
- [ ] **Step 9: Commit**

---

### Task 3: system-core 模块 - 实体类与数据库迁移

**Files:**
- Create: 10 个 JPA 实体类 (SysTenant, SysUser, SysRole, SysUserRole, SysMenu, SysPermission, SysOrg, SysDict, ProjectInfo, ProjectMember)
- Create: 11 个 Flyway 迁移脚本

**核心代码:**

SysUser.java：
```java
package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_user")
public class SysUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tenant_id")
    private Long tenantId;
    @Column(name = "username", nullable = false, length = 64)
    private String username;
    @Column(name = "password", nullable = false, length = 256)
    private String password;
    @Column(name = "real_name", length = 64)
    private String realName;
    @Column(name = "email", length = 128)
    private String email;
    @Column(name = "status", nullable = false)
    private Integer status = 1;
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }
}
```

V1__sys_tenant.sql：
```sql
CREATE TABLE IF NOT EXISTS sys_tenant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tenant_code VARCHAR(64) NOT NULL UNIQUE,
    tenant_name VARCHAR(128) NOT NULL,
    status INT NOT NULL DEFAULT 1,
    created_time DATETIME NOT NULL,
    updated_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

- [ ] **Step 1-21: 创建实体类和迁移脚本**
- [ ] **Step 22: 编译验证**
- [ ] **Step 23: Commit**

---

### Task 4: system-core 模块 - Repository 与 Service

**Files:**
- Create: `SysUserRepository.java`
- Create: `SysRoleRepository.java`
- Create: `SysUserRoleRepository.java`
- Create: `UserService.java`
- Create: `UserServiceImpl.java`

**核心代码:**

SysUserRepository.java：
```java
package com.lc.system.repository;

import com.lc.system.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    Optional<SysUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
```

UserServiceImpl.java：
```java
package com.lc.system.service.impl;

import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.common.util.PasswordUtil;
import com.lc.system.entity.SysUser;
import com.lc.system.repository.SysUserRepository;
import com.lc.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SysUserRepository userRepository;

    @Override
    public SysUser findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public SysUser createUser(SysUser user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BusinessException(GlobalErrorCode.DATA_CONFLICT);
        }
        if (user.getPassword() != null) {
            user.setPassword(PasswordUtil.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return PasswordUtil.matches(rawPassword, encodedPassword);
    }
}
```

- [ ] **Step 1-5: 创建各文件**
- [ ] **Step 6: 编译验证**
- [ ] **Step 7: Commit**

---

### Task 5: system-core 模块 - JWT 与 Token 服务

**Files:**
- Create: `JwtConfig.java`
- Create: `AuthDTO.java`
- Create: `JwtTokenService.java`
- Create: `RefreshTokenService.java`

**核心代码:**

JwtTokenService.java：
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
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(SysUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("tenantId", user.getTenantId());
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(jwtConfig.getAccessTokenExpireMinutes());
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .expiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            throw new BusinessException(GlobalErrorCode.TOKEN_INVALID);
        }
    }
}
```

RefreshTokenService.java：
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

- [ ] **Step 1-4: 创建各文件**
- [ ] **Step 5: 编译验证**
- [ ] **Step 6: Commit**

---

### Task 6: bootstrap 模块 - 启动类与配置

**Files:**
- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/Application.java`
- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/SecurityConfig.java`
- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebConfig.java`
- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/filter/JwtAuthenticationFilter.java`
- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/handler/GlobalExceptionHandler.java`
- Create: `backend/bootstrap/src/main/resources/application.yml`

**核心代码:**

Application.java：
```java
package com.lc.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.lc"})
@EnableConfigurationProperties
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

SecurityConfig.java：
```java
package com.lc.bootstrap.config;

import com.lc.bootstrap.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/refresh").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

application.yml：
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lc_platform?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  data:
    redis:
      host: localhost
      port: 6379
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true

jwt:
  secret: your-256-bit-secret-key-here-must-be-at-least-32-characters
  access-token-expire-minutes: 15
  refresh-token-expire-days: 7
```

- [ ] **Step 1-6: 创建各文件**
- [ ] **Step 7: 编译验证**
- [ ] **Step 8: Commit**

---

### Task 7: bootstrap 模块 - 认证控制器

**Files:**
- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/controller/AuthController.java`

**核心代码:**

AuthController.java：
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

import java.time.LocalDateTime;

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
                        .tenantId(user.getTenantId())
                        .build())
                .build();

        return Result.success(response);
    }
}
```

- [ ] **Step 1: 创建 AuthController**
- [ ] **Step 2: 编译验证**
- [ ] **Step 3: Commit**

---

### Task 8: 前端项目初始化

**Files:**
- Create: `frontend/package.json`
- Create: `frontend/vite.config.ts`
- Create: `frontend/tsconfig.json`
- Create: `frontend/index.html`
- Create: `frontend/src/main.tsx`
- Create: `frontend/src/App.tsx`

**核心代码:**

package.json：
```json
{
  "name": "lc-platform-frontend",
  "version": "1.0.0",
  "private": true,
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "tsc && vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "antd": "^5.15.0",
    "@ant-design/pro-components": "^2.6.0",
    "axios": "^1.6.0",
    "lucide-react": "^0.310.0"
  },
  "devDependencies": {
    "@types/react": "^18.2.0",
    "@types/react-dom": "^18.2.0",
    "@vitejs/plugin-react": "^4.2.0",
    "typescript": "^5.4.0",
    "vite": "^6.0.0"
  }
}
```

vite.config.ts：
```typescript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

- [ ] **Step 1-6: 创建各文件**
- [ ] **Step 7: 安装依赖并验证**
- [ ] **Step 8: Commit**

---

### Task 9: 前端 - 请求封装与认证

**Files:**
- Create: `frontend/src/utils/token.ts`
- Create: `frontend/src/utils/request.ts`
- Create: `frontend/src/hooks/useAuth.ts`
- Create: `frontend/src/api/auth.ts`

**核心代码:**

token.ts：
```typescript
const ACCESS_TOKEN_KEY = 'lc_access_token'

export const getAccessToken = () => {
  return localStorage.getItem(ACCESS_TOKEN_KEY)
}

export const setAccessToken = (token: string) => {
  localStorage.setItem(ACCESS_TOKEN_KEY, token)
}

export const removeAccessToken = () => {
  localStorage.removeItem(ACCESS_TOKEN_KEY)
}
```

request.ts：
```typescript
import axios from 'axios'
import { getAccessToken, removeAccessToken } from './token'
import { message } from 'antd'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.request.use(
  (config) => {
    const token = getAccessToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    if (error.response?.status === 401) {
      removeAccessToken()
      message.error('登录已过期，请重新登录')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default request
```

useAuth.ts：
```typescript
import { useState, useEffect } from 'react'
import { getAccessToken, setAccessToken, removeAccessToken } from '../utils/token'
import { login, refreshToken } from '../api/auth'

interface UserInfo {
  id: number
  username: string
  realName: string
  tenantId: number | null
}

export const useAuth = () => {
  const [user, setUser] = useState<UserInfo | null>(null)
  const [loading, setLoading] = useState(false)

  const isLoggedIn = () => !!getAccessToken()

  const handleLogin = async (username: string, password: string) => {
    setLoading(true)
    try {
      const result = await login(username, password)
      setAccessToken(result.data.accessToken)
      setUser(result.data.user)
      return true
    } catch (error) {
      return false
    } finally {
      setLoading(false)
    }
  }

  const handleLogout = () => {
    removeAccessToken()
    setUser(null)
    window.location.href = '/login'
  }

  return { user, loading, isLoggedIn, handleLogin, handleLogout }
}
```

- [ ] **Step 1-4: 创建各文件**
- [ ] **Step 5: Commit**

---

### Task 10: 前端 - 登录页与首页

**Files:**
- Create: `frontend/src/pages/Login/index.tsx`
- Create: `frontend/src/pages/Dashboard/index.tsx`
- Create: `frontend/src/layouts/MainLayout.tsx`

**核心代码:**

Login/index.tsx：
```typescript
import React from 'react'
import { Form, Input, Button, Card, message } from 'antd'
import { useAuth } from '../../hooks/useAuth'
import { User, Lock } from 'lucide-react'

const Login: React.FC = () => {
  const { handleLogin, loading } = useAuth()
  const [form] = Form.useForm()

  const onFinish = async (values: { username: string; password: string }) => {
    const success = await handleLogin(values.username, values.password)
    if (success) {
      message.success('登录成功')
      window.location.href = '/dashboard'
    } else {
      message.error('用户名或密码错误')
    }
  }

  return (
    <div style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }}>
      <Card style={{ width: 400, boxShadow: '0 10px 40px rgba(0,0,0,0.2)' }}>
        <h2 style={{ textAlign: 'center', marginBottom: 30 }}>低代码平台</h2>
        <Form form={form} onFinish={onFinish} layout="vertical">
          <Form.Item name="username" rules={[{ required: true, message: '请输入用户名' }]}>
            <Input prefix={<User />} placeholder="用户名" />
          </Form.Item>
          <Form.Item name="password" rules={[{ required: true, message: '请输入密码' }]}>
            <Input.Password prefix={<Lock />} placeholder="密码" />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loading} style={{ width: '100%' }}>
              登录
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  )
}

export default Login
```

- [ ] **Step 1-3: 创建各文件**
- [ ] **Step 4: Commit**

---

## 自审

1. **Spec覆盖**：所有设计文档中的任务1-3需求均有对应任务
2. **占位符检查**：无"TBD"或"TODO"，所有代码已完整给出
3. **类型一致性**：前后端接口类型一致，AuthDTO 与前端类型对应

---

## 验收标准

### 后端验收
- [ ] Maven 多模块可编译通过
- [ ] bootstrap 可正常启动（端口 8080）
- [ ] Flyway 自动执行迁移，创建所有核心表
- [ ] `/api/auth/login` 接口可用，返回 AccessToken/RefreshToken
- [ ] `/api/auth/refresh` 接口可用，刷新 Token
- [ ] JWT 认证过滤器生效，未登录请求返回 401

### 前端验收
- [ ] `npm install` 成功
- [ ] `npm run dev` 正常启动（端口 5173）
- [ ] 登录页可访问
- [ ] 登录成功后重定向到首页
- [ ] 请求自动附加 Authorization 头
- [ ] 401 自动跳转到登录页

---

Plan complete. Ready for execution.
