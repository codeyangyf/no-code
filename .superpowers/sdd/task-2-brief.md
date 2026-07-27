# Task 2: common 模块核心代码

**Goal:** 创建 common 模块的核心代码，包括异常体系、统一响应、租户上下文、Redis配置、密码工具。

**Files:**
- Create: `backend/common/src/main/java/com/lc/common/exception/BusinessException.java`
- Create: `backend/common/src/main/java/com/lc/common/exception/GlobalErrorCode.java`
- Create: `backend/common/src/main/java/com/lc/common/dto/Result.java`
- Create: `backend/common/src/main/java/com/lc/common/dto/PageResult.java`
- Create: `backend/common/src/main/java/com/lc/common/context/TenantContext.java`
- Create: `backend/common/src/main/java/com/lc/common/config/RedisConfig.java`
- Create: `backend/common/src/main/java/com/lc/common/util/PasswordUtil.java`

**BusinessException.java:**

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

**GlobalErrorCode.java:**

```java
package com.lc.common.exception;

import lombok.Getter;

@Getter
public enum GlobalErrorCode {
    SUCCESS(0, "success"),
    FAIL(-1, "fail"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    VALIDATION_ERROR(400, "参数校验失败"),
    USER_NOT_FOUND(1001, "用户不存在"),
    USERNAME_OR_PASSWORD_ERROR(1002, "用户名或密码错误"),
    TOKEN_EXPIRED(1003, "Token已过期"),
    TOKEN_INVALID(1004, "Token无效"),
    REFRESH_TOKEN_EXPIRED(1005, "刷新Token已过期"),
    REFRESH_TOKEN_INVALID(1006, "刷新Token无效"),
    TENANT_NOT_FOUND(2001, "租户不存在"),
    ROLE_NOT_FOUND(2002, "角色不存在"),
    PERMISSION_DENIED(2003, "权限不足"),
    DATA_CONFLICT(2004, "数据冲突"),
    SYSTEM_ERROR(500, "系统内部错误");

    private final int code;
    private final String message;

    GlobalErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
```

**Result.java:**

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

    public static <T> Result<T> success() {
        return Result.<T>builder()
                .code(0)
                .message("success")
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .code(0)
                .message("success")
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> Result<T> success(String message, T data) {
        return Result.<T>builder()
                .code(0)
                .message(message)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> Result<T> fail(int code, String message) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> Result<T> fail(String message) {
        return Result.<T>builder()
                .code(-1)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
```

**PageResult.java:**

```java
package com.lc.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private List<T> records;
    private long total;
    private int page;
    private int size;

    public static <T> PageResult<T> of(List<T> records, long total, int page, int size) {
        return PageResult.<T>builder()
                .records(records)
                .total(total)
                .page(page)
                .size(size)
                .build();
    }
}
```

**TenantContext.java:**

```java
package com.lc.common.context;

public class TenantContext {
    private static final ThreadLocal<Long> tenantId = new ThreadLocal<>();

    public static void setTenantId(Long id) {
        tenantId.set(id);
    }

    public static Long getTenantId() {
        return tenantId.get();
    }

    public static void clear() {
        tenantId.remove();
    }
}
```

**RedisConfig.java:**

```java
package com.lc.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
```

**PasswordUtil.java:**

```java
package com.lc.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
```

**Steps:**
1. 创建 BusinessException.java
2. 创建 GlobalErrorCode.java
3. 创建 Result.java
4. 创建 PageResult.java
5. 创建 TenantContext.java
6. 创建 RedisConfig.java
7. 创建 PasswordUtil.java
8. 编译验证：`cd backend && mvn clean compile -q -pl common -am`
9. Commit，提交信息："feat: common模块 - 异常体系、统一响应、租户上下文、Redis配置、密码工具"

**Global Constraints:**
- Java: 17
- Spring Boot: 3.2.5
- Maven: 3.9.x

__tr_native_ec=$?; pwd -P >| '/var/log/tool/jobs/job-a1f204cd985e4ddc98956772cb5b8cf3/cwd.txt'; exit "$__tr_native_ec"