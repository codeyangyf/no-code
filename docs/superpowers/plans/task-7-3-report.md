# Task 7-3 实施报告

## 1. 修改的文件清单

| # | 文件路径 | 说明 |
|---|---------|------|
| 1 | `/workspace/backend/bootstrap/pom.xml` | 新增 `spring-boot-starter-aop` 依赖 |
| 2 | `/workspace/backend/bootstrap/src/main/java/com/lc/bootstrap/aspect/AuditLogAspect.java` | 新建审计日志 AOP 切面，拦截 `@AuditLog` 注解方法 |

## 2. AuditLogAspect 完整代码

```java
package com.lc.bootstrap.aspect;

import com.lc.common.annotation.AuditLog;
import com.lc.common.context.UserContext;
import com.lc.system.entity.AuditLogEntity;
import com.lc.system.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 审计日志 AOP 切面：拦截标注了 {@link AuditLog} 的方法，自动记录审计日志。
 * 操作人、租户、IP、UserAgent、RequestId 由切面从请求上下文与 UserContext 自动填充。
 * 异常路径下会重新抛出原业务异常，确保不改变业务行为。
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogAspect {

    private final AuditLogService auditLogService;
    private final ExpressionParser spelParser = new SpelExpressionParser();

    @Around("@annotation(auditLog)")
    public Object around(ProceedingJoinPoint joinPoint, AuditLog auditLog) throws Throwable {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            // 非 HTTP 上下文（如异步任务），跳过审计日志记录
            return joinPoint.proceed();
        }

        String clientIp = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String requestId = request.getHeader("X-Request-Id");
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString().replace("-", "");
        }

        try {
            Object result = joinPoint.proceed();
            try {
                AuditLogEntity entity = buildEntity(auditLog, joinPoint, clientIp, userAgent, requestId,
                        "SUCCESS", null);
                auditLogService.save(entity);
            } catch (Exception saveEx) {
                log.warn("Failed to save audit log (success path), action={}, resourceType={}: {}",
                        auditLog.action(), auditLog.resourceType(), saveEx.getMessage());
            }
            return result;
        } catch (Throwable e) {
            try {
                AuditLogEntity entity = buildEntity(auditLog, joinPoint, clientIp, userAgent, requestId,
                        "FAILED", truncate(e.getMessage(), 500));
                auditLogService.save(entity);
            } catch (Exception saveEx) {
                log.warn("Failed to save audit log (failure path), action={}, resourceType={}: {}",
                        auditLog.action(), auditLog.resourceType(), saveEx.getMessage());
            }
            // re-throw 原业务异常，不吞异常
            throw e;
        }
    }

    private AuditLogEntity buildEntity(AuditLog auditLog, ProceedingJoinPoint joinPoint,
                                       String clientIp, String userAgent, String requestId,
                                       String result, String errorMessage) {
        AuditLogEntity entity = new AuditLogEntity();
        entity.setTenantId(UserContext.getTenantId());
        entity.setUserId(UserContext.getUserId());
        entity.setUserName(UserContext.getUsername());
        entity.setAction(auditLog.action());
        entity.setResourceType(auditLog.resourceType());
        entity.setResourceId(extractResourceId(joinPoint, auditLog));
        entity.setClientIp(clientIp);
        entity.setIp(clientIp);
        entity.setUserAgent(userAgent);
        entity.setRequestId(requestId);
        entity.setCreatedTime(LocalDateTime.now());
        entity.setResult(result);
        entity.setErrorMessage(errorMessage);
        return entity;
    }

    /**
     * 提取资源 ID。
     * - 若 resourceIdParam 为空串，返回 null
     * - 以 # 开头：按 SpEL 解析，方法参数名作为变量
     * - 否则：作为参数名直接匹配
     * - 解析失败返回 null，不抛异常
     */
    private String extractResourceId(ProceedingJoinPoint joinPoint, AuditLog auditLog) {
        String param = auditLog.resourceIdParam();
        if (param == null || param.isEmpty()) {
            return null;
        }
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] paramNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();

            if (param.startsWith("#")) {
                StandardEvaluationContext context = new StandardEvaluationContext();
                if (paramNames != null) {
                    for (int i = 0; i < paramNames.length; i++) {
                        context.setVariable(paramNames[i], args[i]);
                    }
                }
                Expression expression = spelParser.parseExpression(param);
                Object value = expression.getValue(context);
                return value != null ? value.toString() : null;
            } else {
                if (paramNames != null) {
                    for (int i = 0; i < paramNames.length; i++) {
                        if (param.equals(paramNames[i])) {
                            return args[i] != null ? args[i].toString() : null;
                        }
                    }
                }
                return null;
            }
        } catch (Exception e) {
            log.warn("Failed to extract resourceId from param '{}': {}", param, e.getMessage());
            return null;
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // 取第一个（最原始客户端）
            return ip.split(",")[0].trim();
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        return request.getRemoteAddr();
    }

    private HttpServletRequest getCurrentRequest() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes sra) {
            return sra.getRequest();
        }
        return null;
    }

    private String truncate(String s, int maxLen) {
        if (s == null) {
            return null;
        }
        return s.length() <= maxLen ? s : s.substring(0, maxLen);
    }
}
```

## 3. pom.xml 新增依赖片段

在 `/workspace/backend/bootstrap/pom.xml` 中，于 `spring-boot-starter-web` 之后、`spring-boot-starter-security` 之前新增：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

完整上下文：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

## 4. 编译验证命令及输出

命令：
```bash
cd /workspace/backend && mvn -pl bootstrap -am compile -q
```

输出：`-q` 静默模式，无错误输出，退出码 `0`，编译通过。

产物验证：
```
$ ls -la /workspace/backend/bootstrap/target/classes/com/lc/bootstrap/aspect/
total 16
-rw-r--r-- 1 root root 7948 Jul 27 15:11 AuditLogAspect.class
```

`AuditLogAspect.class` 已生成，说明 AOP 切面编译成功。

## 5. Commit Hash

```
71c9a16301e5aeeb564dd3c4774c2d2b474b2d22
```

提交信息：
```
feat(task-7-3): add AOP dependency and AuditLogAspect for @AuditLog annotation auto-recording
```

分支：`feature/phase1-security-rbac`，2 files changed, 174 insertions(+)。

## 6. 疑虑

1. **SpEL 参数名解析依赖 `-parameters` 编译选项**：`MethodSignature.getParameterNames()` 在 Spring Boot 3.2 + `spring-boot-starter-parent` 默认开启 `-parameters` 编译参数下可正常工作；若后续模块改用自定义 `maven-compiler-plugin` 配置且未保留参数名，则 SpEL 变量绑定与"按参数名直接匹配"路径会失效（`paramNames` 返回 `arg0`/`arg1`）。建议后续在 controller 加 `@AuditLog(resourceIdParam = "#id")` 时确认参数名可用。

2. **`UserContext.getTenantId()` 与 `TenantContext.getTenantId()` 双源**：规范明确 `tenantId = UserContext.getTenantId()`，未使用 `TenantContext`。若实际运行时 `UserContext` 未填充 tenantId（如 JWT 仅设置 TenantContext），审计日志的 tenantId 列将为 null。需确认上游 `JwtAuthenticationFilter` 是否同时填充两者，否则应改用 `TenantContext.getTenantId()` 或 fallback 逻辑。

3. **save 调用已包裹 try/catch**：虽然 `AuditLogService.save()` 文档声明不抛异常，但切面在成功/失败两条路径均对 `save` 做了 `try/catch Exception` 兜底。这是为了在异常路径下严格保证 `throw e` 执行（满足"不要吞异常"硬性要求），即使 save 真的抛异常也不会替换业务异常。已保留 WARN 日志便于排查。
