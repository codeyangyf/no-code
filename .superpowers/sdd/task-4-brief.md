### Task 4: 多租户上下文体系

**Files:**
- Create: `backend/common/src/main/java/com/lc/common/context/UserContext.java`
- Create: `backend/common/src/main/java/com/lc/common/annotation/TenantCheck.java`
- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/filter/TenantContextFilter.java`
- Modify: `backend/bootstrap/src/main/java/com/lc/bootstrap/filter/JwtAuthenticationFilter.java`
- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysTenantRepository.java`
- Create: `backend/system-core/src/main/java/com/lc/system/service/TenantService.java`
- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/TenantServiceImpl.java`
- Create: `backend/system-core/src/main/java/com/lc/system/controller/TenantController.java`
- Create: `backend/system-core/src/main/java/com/lc/system/dto/TenantDTO.java`
- Create: `backend/system-core/src/main/java/com/lc/system/dto/PageRequest.java`

**Interfaces:**
- Consumes: JwtTokenService（从Token解析tenantId）, SysTenant实体
- Produces: TenantContextHolder（线程级租户上下文）, TenantService（租户CRUD）

**核心设计：**

1. **租户识别**：从 JWT Token 的 claims 中提取 tenantId，放入 ThreadLocal
2. **租户内唯一约束**：数据库层使用 `(tenant_id, code)` 复合唯一索引，Service 层做前置校验
3. **越权拦截**：通过 TenantCheck 注解 + AOP/拦截器，校验当前用户只能操作本租户数据

**UserContext.java：**
```java
package com.lc.common.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserContext {
    private Long userId;
    private Long tenantId;
    private String username;

    private static final ThreadLocal<UserContext> HOLDER = new ThreadLocal<>();

    public static void set(UserContext context) {
        HOLDER.set(context);
    }

    public static UserContext get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }

    public static Long getUserId() {
        UserContext ctx = HOLDER.get();
        return ctx != null ? ctx.getUserId() : null;
    }

    public static Long getTenantId() {
        UserContext ctx = HOLDER.get();
        return ctx != null ? ctx.getTenantId() : null;
    }
}
```

**TenantCheck.java：**
```java
package com.lc.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantCheck {
    boolean required() default true;
}
```

**TenantContextFilter.java：**
```java
package com.lc.bootstrap.filter;

import com.lc.common.context.UserContext;
import com.lc.system.security.JwtTokenService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TenantContextFilter implements Filter {
    private final JwtTokenService jwtTokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String token = extractToken(httpRequest);
            if (StringUtils.hasText(token)) {
                try {
                    Long userId = jwtTokenService.getUserIdFromToken(token);
                    Long tenantId = jwtTokenService.getTenantIdFromToken(token);
                    String username = jwtTokenService.getUsernameFromToken(token);
                    UserContext.set(UserContext.builder()
                            .userId(userId)
                            .tenantId(tenantId)
                            .username(username)
                            .build());
                } catch (Exception ignored) {
                }
            }
            chain.doFilter(request, response);
        } finally {
            UserContext.clear();
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
```

**TenantService.java：**
```java
package com.lc.system.service;

import com.lc.common.dto.PageResult;
import com.lc.system.dto.TenantDTO;
import com.lc.system.entity.SysTenant;

public interface TenantService {
    PageResult<TenantDTO> list(String keyword, int page, int size);
    TenantDTO getById(Long id);
    TenantDTO create(TenantDTO.CreateRequest request);
    TenantDTO update(Long id, TenantDTO.UpdateRequest request);
    void delete(Long id);
    void toggleStatus(Long id, Integer status);
    SysTenant getByCode(String tenantCode);
}
```

**TenantController.java：**
```java
package com.lc.system.controller;

import com.lc.common.dto.PageResult;
import com.lc.common.dto.Result;
import com.lc.system.dto.TenantDTO;
import com.lc.system.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system/tenants")
@RequiredArgsConstructor
public class TenantController {
    private final TenantService tenantService;

    @GetMapping
    public Result<PageResult<TenantDTO>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(tenantService.list(keyword, page, size));
    }

    @GetMapping("/{id}")
    public Result<TenantDTO> getById(@PathVariable Long id) {
        return Result.success(tenantService.getById(id));
    }

    @PostMapping
    public Result<TenantDTO> create(@RequestBody TenantDTO.CreateRequest request) {
        return Result.success(tenantService.create(request));
    }

    @PutMapping("/{id}")
    public Result<TenantDTO> update(@PathVariable Long id, @RequestBody TenantDTO.UpdateRequest request) {
        return Result.success(tenantService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tenantService.delete(id);
        return Result.success();
    }

    @PatchMapping("/{id}/status")
    public Result<Void> toggleStatus(@PathVariable Long id, @RequestParam Integer status) {
        tenantService.toggleStatus(id, status);
        return Result.success();
    }
}
```

- [ ] **Step 1: 创建 UserContext（用户+租户上下文）**
- [ ] **Step 2: 创建 TenantCheck 注解**
- [ ] **Step 3: 创建 TenantContextFilter**
- [ ] **Step 4: 修改 JwtAuthenticationFilter，整合 UserContext**
- [ ] **Step 5: 创建 SysTenantRepository**
- [ ] **Step 6: 创建 TenantService 接口**
- [ ] **Step 7: 创建 TenantServiceImpl 实现**
- [ ] **Step 8: 创建 TenantDTO**
- [ ] **Step 9: 创建 PageRequest 基类**
- [ ] **Step 10: 创建 TenantController**
- [ ] **Step 11: 编译验证：cd backend && mvn clean compile -q -pl bootstrap -am**
- [ ] **Step 12: Commit: "feat: 多租户上下文体系（租户识别、租户CRUD、上下文传递）"**

---

### Task 4 验收
- [ ] 请求携带JWT时，TenantContext中能获取到tenantId
- [ ] 租户CRUD接口正常工作
- [ ] 不同租户数据隔离（用户只能看到本租户数据）

