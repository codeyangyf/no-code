## Task 4: 多租户上下文 ⬜ 待实施

> 设计文档 Task 4：租户识别、租户内唯一约束、越权拦截器

**目标：** 从JWT解析租户ID写入ThreadLocal上下文，提供租户CRUD接口，拦截器校验越权访问。

**Files:**
- Modify: `backend/bootstrap/src/main/java/com/lc/bootstrap/filter/JwtAuthenticationFilter.java`（整合UserContext设置）
- Create: `backend/common/src/main/java/com/lc/common/annotation/TenantCheck.java`
- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/interceptor/TenantInterceptor.java`
- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebMvcConfig.java`（注册拦截器，如不存在则创建）
- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysTenantRepository.java`
- Create: `backend/system-core/src/main/java/com/lc/system/dto/TenantDTO.java`
- Create: `backend/system-core/src/main/java/com/lc/system/dto/PageRequest.java`
- Create: `backend/system-core/src/main/java/com/lc/system/service/TenantService.java`
- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/TenantServiceImpl.java`
- Create: `backend/system-core/src/main/java/com/lc/system/controller/TenantController.java`

**Interfaces:**
- Consumes: `JwtTokenService.getTenantIdFromToken()`, `UserContext`（已存在）, `SysTenant`实体（已存在）
- Produces: `TenantInterceptor`（越权拦截）, `TenantService`（租户CRUD）, `TenantController`（REST接口）

**关键设计：**
1. 租户识别：修改 `JwtAuthenticationFilter`，在解析token后调用 `UserContext.set()` 设置用户上下文（当前filter只设置了SecurityContext，没设置UserContext）
2. 租户内唯一约束：数据库层已有复合唯一索引（如 sys_user 的 tenant_id+username），Service层做前置校验并返回友好错误
3. 越权拦截：`@TenantCheck` 注解 + `TenantInterceptor`，校验请求参数中的 tenantId 与当前用户 tenantId 一致

**TenantCheck.java：**
```java
package com.lc.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantCheck {
    /** 参数中租户ID的字段名，默认为 tenantId */
    String tenantIdParam() default "tenantId";
}
```

**TenantInterceptor.java 核心逻辑：**
```java
// 1. 从 UserContext.getTenantId() 获取当前用户租户ID
// 2. 从请求参数中提取目标租户ID（@TenantCheck指定的参数名）
// 3. 若不一致，抛出 BusinessException(PERMISSION_DENIED)
// 4. 若 UserContext.getTenantId() 为 null，跳过（如超级管理员）
```

- [ ] **Task 4-1: 修改 JwtAuthenticationFilter，整合 UserContext 设置**
- [ ] **Task 4-2: 创建 TenantCheck 注解 + TenantInterceptor 拦截器**
- [ ] **Task 4-3: 创建 WebMvcConfig 注册拦截器**
- [ ] **Task 4-4: 创建 SysTenantRepository + TenantDTO + PageRequest + TenantService + TenantServiceImpl + TenantController**
- [ ] **Task 4-5: 编译验证 + Commit**

---

### Task 4 验收
- [ ] 请求携带JWT时，UserContext 中能获取到 tenantId
- [ ] 租户CRUD接口正常工作（GET/POST/PUT/DELETE /api/system/tenants）
- [ ] @TenantCheck 注解生效，跨租户访问返回 403

