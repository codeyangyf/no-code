# Phase 1 Final Review Package

**Branch:** feature/phase1-security-rbac
**Merge base:** 1eed17224fb9413c78c6537ee79898139e20c68d
**HEAD:** c28205bbbfa78f3ea41f76eb4b6fa1d841322dc2

## Commit List
c28205bb docs: update Task 7 final commit hash in plan progress table
35c2aaed feat(task-7-5): harden CORS with configurable origin whitelist + mark Task 7 complete
5c90321c feat(task-7-4): add FileUploadValidator, SsrfProtector, AuditLogController; refactor FileController to use validator
71c9a163 feat(task-7-3): add AOP dependency and AuditLogAspect for @AuditLog annotation auto-recording
d578e52e feat(task-7-2): add AuditLogRepository, AuditLogDTO, AuditLogService + Impl with dynamic Specification query
6dc93d04 feat(task-7-1): add @AuditLog annotation, V15 audit_log align migration, AuditLogEntity
a7756753 fix: 修复Task 6安全问题（路径遍历、MIME校验、租户隔离）
6ee2d63e feat: 对象存储与密钥管理（存储抽象、文件上传、AES加密）
7a55ef1a chore: 更新计划文档标记Task5完成
72a152e1 fix: 修复Task 5 RBAC安全问题（租户隔离、越权访问、关联清理）
29ac5811 feat: RBAC权限体系（菜单权限、角色管理、接口权限注解）
93cdd026 chore: 更新计划文档标记Task4完成，添加.gitignore移除target跟踪
d5fad041 fix: 修复Task 4审查问题（TenantInterceptor测试+path variable支持+UserContext递归bug）
9c3d46a2 feat: 多租户上下文体系（租户识别、租户CRUD、越权拦截器）
cd57019d feat: 低代码平台项目规划
1e202f2f feat: 低代码平台项目规划

## Diff Stat
 .gitignore                                         |   23 +
 .superpowers/sdd/progress.md                       |    2 +
 .superpowers/sdd/task-4-brief.md                   |  216 +----
 .superpowers/sdd/task-5-brief.md                   |  299 ++----
 .superpowers/sdd/task-6-brief.md                   |  311 ++----
 backend/bootstrap/pom.xml                          |   10 +
 .../com/lc/bootstrap/aspect/AuditLogAspect.java    |  170 ++++
 .../com/lc/bootstrap/config/StorageConfig.java     |   69 ++
 .../java/com/lc/bootstrap/config/WebConfig.java    |   59 +-
 .../bootstrap/filter/JwtAuthenticationFilter.java  |   26 +-
 .../interceptor/PermissionInterceptor.java         |   88 ++
 .../bootstrap/interceptor/TenantInterceptor.java   |   96 ++
 .../bootstrap/src/main/resources/application.yml   |   20 +
 .../interceptor/TenantInterceptorTest.java         |  157 +++
 backend/bootstrap/target/classes/application.yml   |   27 -
 .../classes/com/lc/bootstrap/Application.class     |  Bin 896 -> 0 bytes
 .../com/lc/bootstrap/config/SecurityConfig.class   |  Bin 5345 -> 0 bytes
 .../com/lc/bootstrap/config/WebConfig.class        |  Bin 1565 -> 0 bytes
 .../lc/bootstrap/controller/AuthController.class   |  Bin 5652 -> 0 bytes
 .../bootstrap/filter/JwtAuthenticationFilter.class |  Bin 3457 -> 0 bytes
 .../bootstrap/handler/GlobalExceptionHandler.class |  Bin 5625 -> 0 bytes
 .../compile/default-compile/createdFiles.lst       |    6 -
 .../compile/default-compile/inputFiles.lst         |    6 -
 backend/common/pom.xml                             |   10 +
 .../java/com/lc/common/annotation/AuditLog.java    |   24 +
 .../com/lc/common/annotation/PreAuthorize.java     |   23 +
 .../java/com/lc/common/annotation/TenantCheck.java |   16 +
 .../java/com/lc/common/context/UserContext.java    |   45 +
 .../lc/common/security/FileUploadValidator.java    |  105 ++
 .../java/com/lc/common/security/SsrfProtector.java |  154 +++
 .../lc/common/storage/LocalStorageServiceImpl.java |   86 ++
 .../lc/common/storage/MinioStorageServiceImpl.java |  123 +++
 .../com/lc/common/storage/StorageProperties.java   |   48 +
 .../java/com/lc/common/storage/StorageService.java |   58 ++
 .../main/java/com/lc/common/util/EncryptUtil.java  |   78 ++
 .../classes/com/lc/common/config/RedisConfig.class |  Bin 1801 -> 0 bytes
 .../com/lc/common/context/TenantContext.class      |  Bin 935 -> 0 bytes
 .../common/dto/PageResult$PageResultBuilder.class  |  Bin 2636 -> 0 bytes
 .../classes/com/lc/common/dto/PageResult.class     |  Bin 4457 -> 0 bytes
 .../com/lc/common/dto/Result$ResultBuilder.class   |  Bin 2702 -> 0 bytes
 .../target/classes/com/lc/common/dto/Result.class  |  Bin 5572 -> 0 bytes
 .../lc/common/exception/BusinessException.class    |  Bin 951 -> 0 bytes
 .../com/lc/common/exception/GlobalErrorCode.class  |  Bin 2997 -> 0 bytes
 .../classes/com/lc/common/util/PasswordUtil.class  |  Bin 985 -> 0 bytes
 .../compile/default-compile/createdFiles.lst       |    9 -
 .../compile/default-compile/inputFiles.lst         |    7 -
 .../lc/system/controller/AuditLogController.java   |   41 +
 .../com/lc/system/controller/FileController.java   |  132 +++
 .../com/lc/system/controller/MenuController.java   |   57 ++
 .../com/lc/system/controller/RoleController.java   |   74 ++
 .../com/lc/system/controller/TenantController.java |   52 +
 .../com/lc/system/controller/UserController.java   |   85 ++
 .../main/java/com/lc/system/dto/AuditLogDTO.java   |   48 +
 .../src/main/java/com/lc/system/dto/MenuDTO.java   |   64 ++
 .../main/java/com/lc/system/dto/PageRequest.java   |   16 +
 .../src/main/java/com/lc/system/dto/RoleDTO.java   |   58 ++
 .../src/main/java/com/lc/system/dto/TenantDTO.java |   49 +
 .../src/main/java/com/lc/system/dto/UserDTO.java   |   64 ++
 .../java/com/lc/system/entity/AuditLogEntity.java  |   75 ++
 .../java/com/lc/system/entity/SysRoleMenu.java     |   21 +
 .../com/lc/system/entity/SysRolePermission.java    |   21 +
 .../lc/system/repository/AuditLogRepository.java   |   11 +
 .../lc/system/repository/SysMenuRepository.java    |   14 +
 .../system/repository/SysPermissionRepository.java |   14 +
 .../system/repository/SysRoleMenuRepository.java   |   19 +
 .../repository/SysRolePermissionRepository.java    |   18 +
 .../lc/system/repository/SysRoleRepository.java    |   11 +-
 .../lc/system/repository/SysTenantRepository.java  |   17 +
 .../lc/system/repository/SysUserRepository.java    |    9 +-
 .../system/repository/SysUserRoleRepository.java   |   10 +-
 .../com/lc/system/service/AuditLogService.java     |   19 +
 .../java/com/lc/system/service/MenuService.java    |   21 +
 .../com/lc/system/service/PermissionService.java   |   38 +
 .../java/com/lc/system/service/RoleService.java    |   24 +
 .../java/com/lc/system/service/TenantService.java  |   15 +
 .../java/com/lc/system/service/UserService.java    |   23 +-
 .../system/service/impl/AuditLogServiceImpl.java   |  100 ++
 .../lc/system/service/impl/MenuServiceImpl.java    |  205 ++++
 .../system/service/impl/PermissionServiceImpl.java |  131 +++
 .../lc/system/service/impl/RoleServiceImpl.java    |  182 ++++
 .../lc/system/service/impl/TenantServiceImpl.java  |  128 +++
 .../lc/system/service/impl/UserServiceImpl.java    |  168 +++-
 .../resources/db/migration/V13__sys_role_menu.sql  |    7 +
 .../db/migration/V14__sys_role_permission.sql      |    7 +
 .../db/migration/V15__audit_log_align.sql          |   21 +
 .../classes/com/lc/system/config/JwtConfig.class   |  Bin 2871 -> 0 bytes
 .../AuthDTO$LoginRequest$LoginRequestBuilder.class |  Bin 1800 -> 0 bytes
 .../com/lc/system/dto/AuthDTO$LoginRequest.class   |  Bin 2866 -> 0 bytes
 ...uthDTO$LoginResponse$LoginResponseBuilder.class |  Bin 2836 -> 0 bytes
 .../com/lc/system/dto/AuthDTO$LoginResponse.class  |  Bin 4942 -> 0 bytes
 ...hDTO$RefreshRequest$RefreshRequestBuilder.class |  Bin 1639 -> 0 bytes
 .../com/lc/system/dto/AuthDTO$RefreshRequest.class |  Bin 2393 -> 0 bytes
 .../dto/AuthDTO$UserInfo$UserInfoBuilder.class     |  Bin 2584 -> 0 bytes
 .../com/lc/system/dto/AuthDTO$UserInfo.class       |  Bin 4843 -> 0 bytes
 .../target/classes/com/lc/system/dto/AuthDTO.class |  Bin 982 -> 0 bytes
 .../classes/com/lc/system/entity/ProjectInfo.class |  Bin 8206 -> 0 bytes
 .../com/lc/system/entity/ProjectMember.class       |  Bin 5283 -> 0 bytes
 .../classes/com/lc/system/entity/SysDict.class     |  Bin 6463 -> 0 bytes
 .../classes/com/lc/system/entity/SysMenu.class     |  Bin 9098 -> 0 bytes
 .../classes/com/lc/system/entity/SysOrg.class      |  Bin 7496 -> 0 bytes
 .../com/lc/system/entity/SysPermission.class       |  Bin 5142 -> 0 bytes
 .../classes/com/lc/system/entity/SysRole.class     |  Bin 7001 -> 0 bytes
 .../classes/com/lc/system/entity/SysTenant.class   |  Bin 7045 -> 0 bytes
 .../classes/com/lc/system/entity/SysUser.class     |  Bin 8597 -> 0 bytes
 .../classes/com/lc/system/entity/SysUserRole.class |  Bin 3279 -> 0 bytes
 .../lc/system/repository/SysRoleRepository.class   |  Bin 843 -> 0 bytes
 .../lc/system/repository/SysUserRepository.class   |  Bin 998 -> 0 bytes
 .../system/repository/SysUserRoleRepository.class  |  Bin 681 -> 0 bytes
 .../security/InMemoryRefreshTokenService.class     |  Bin 2083 -> 0 bytes
 .../com/lc/system/security/JwtTokenService.class   |  Bin 4938 -> 0 bytes
 .../system/security/RedisRefreshTokenService.class |  Bin 3253 -> 0 bytes
 .../lc/system/security/RefreshTokenService.class   |  Bin 503 -> 0 bytes
 .../com/lc/system/service/UserService.class        |  Bin 765 -> 0 bytes
 .../lc/system/service/impl/UserServiceImpl.class   |  Bin 4347 -> 0 bytes
 .../classes/db/migration/V10__project_member.sql   |    9 -
 .../target/classes/db/migration/V11__audit_log.sql |   21 -
 .../target/classes/db/migration/V12__init_data.sql |    5 -
 .../target/classes/db/migration/V1__sys_tenant.sql |   12 -
 .../target/classes/db/migration/V2__sys_user.sql   |   17 -
 .../target/classes/db/migration/V3__sys_role.sql   |   13 -
 .../classes/db/migration/V4__sys_user_role.sql     |    6 -
 .../target/classes/db/migration/V5__sys_menu.sql   |   17 -
 .../classes/db/migration/V6__sys_permission.sql    |   10 -
 .../target/classes/db/migration/V7__sys_org.sql    |   15 -
 .../target/classes/db/migration/V8__sys_dict.sql   |   12 -
 .../classes/db/migration/V9__project_info.sql      |   16 -
 .../compile/default-compile/createdFiles.lst       |   29 -
 .../compile/default-compile/inputFiles.lst         |   21 -
 docs/superpowers/plans/2026-07-27-phase1-plan.md   |  431 +++++++++
 .../plans/2026-07-27-phase1-scaffold-plan.md       |  976 -------------------
 .../plans/2026-07-27-phase1-tasks4-7-plan.md       | 1005 --------------------
 131 files changed, 4141 insertions(+), 2914 deletions(-)

## Full Diff (with 5 lines context)
diff --git a/.gitignore b/.gitignore
new file mode 100644
index 00000000..f7bf5f91
--- /dev/null
+++ b/.gitignore
@@ -0,0 +1,23 @@
+# Maven
+target/
+
+# IDE
+.idea/
+*.iml
+.vscode/
+*.swp
+
+# OS
+.DS_Store
+Thumbs.db
+
+# Node
+node_modules/
+dist/
+
+# Env
+.env
+.env.local
+
+# Logs
+*.log
diff --git a/.superpowers/sdd/progress.md b/.superpowers/sdd/progress.md
index 7a497f97..b7c123bf 100644
--- a/.superpowers/sdd/progress.md
+++ b/.superpowers/sdd/progress.md
@@ -6,5 +6,7 @@ Task 5: complete (commits b34e6d8..f43436a, review clean)
 Task 6: complete (commits f43436a..7a9d249, review clean)
 Task 7: complete (commits 7a9d249..d6311f9, review clean)
 Task 8: complete (commits d6311f9d..a7eb1bb3, review clean)
 Task 9: complete (commits a7eb1bb3..deecda5c, review clean)
 Task 10: complete (commits deecda5c..282aeff7, review clean)
+Task 4 (Phase1): complete (commits cd57019d..d5fad041, review clean after fix)
+---
diff --git a/.superpowers/sdd/task-4-brief.md b/.superpowers/sdd/task-4-brief.md
index b1f3b1e7..4ce55208 100644
--- a/.superpowers/sdd/task-4-brief.md
+++ b/.superpowers/sdd/task-4-brief.md
@@ -1,183 +1,63 @@
-# Task 4: system-core 模块 - Repository 与 UserService
+## Task 4: 多租户上下文 ⬜ 待实施
 
-**Goal:** 创建 system-core 模块的 Repository 接口和 UserService 接口及实现。
+> 设计文档 Task 4：租户识别、租户内唯一约束、越权拦截器
 
-**Files:**
-- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysUserRepository.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysRoleRepository.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysUserRoleRepository.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/service/UserService.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/UserServiceImpl.java`
-
-**SysUserRepository.java:**
-
-```java
-package com.lc.system.repository;
-
-import com.lc.system.entity.SysUser;
-import org.springframework.data.jpa.repository.JpaRepository;
-import org.springframework.stereotype.Repository;
-
-import java.util.Optional;
-
-@Repository
-public interface SysUserRepository extends JpaRepository<SysUser, Long> {
-    Optional<SysUser> findByUsername(String username);
-    Optional<SysUser> findByTenantIdAndUsername(Long tenantId, String username);
-    boolean existsByUsername(String username);
-    boolean existsByTenantIdAndUsername(Long tenantId, String username);
-}
-```
-
-**SysRoleRepository.java:**
-
-```java
-package com.lc.system.repository;
-
-import com.lc.system.entity.SysRole;
-import org.springframework.data.jpa.repository.JpaRepository;
-import org.springframework.stereotype.Repository;
-
-import java.util.Optional;
-
-@Repository
-public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
-    Optional<SysRole> findByRoleCode(String roleCode);
-    Optional<SysRole> findByTenantIdAndRoleCode(Long tenantId, String roleCode);
-}
-```
-
-**SysUserRoleRepository.java:**
+**目标：** 从JWT解析租户ID写入ThreadLocal上下文，提供租户CRUD接口，拦截器校验越权访问。
 
+**Files:**
+- Modify: `backend/bootstrap/src/main/java/com/lc/bootstrap/filter/JwtAuthenticationFilter.java`（整合UserContext设置）
+- Create: `backend/common/src/main/java/com/lc/common/annotation/TenantCheck.java`
+- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/interceptor/TenantInterceptor.java`
+- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebMvcConfig.java`（注册拦截器，如不存在则创建）
+- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysTenantRepository.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/dto/TenantDTO.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/dto/PageRequest.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/TenantService.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/TenantServiceImpl.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/controller/TenantController.java`
+
+**Interfaces:**
+- Consumes: `JwtTokenService.getTenantIdFromToken()`, `UserContext`（已存在）, `SysTenant`实体（已存在）
+- Produces: `TenantInterceptor`（越权拦截）, `TenantService`（租户CRUD）, `TenantController`（REST接口）
+
+**关键设计：**
+1. 租户识别：修改 `JwtAuthenticationFilter`，在解析token后调用 `UserContext.set()` 设置用户上下文（当前filter只设置了SecurityContext，没设置UserContext）
+2. 租户内唯一约束：数据库层已有复合唯一索引（如 sys_user 的 tenant_id+username），Service层做前置校验并返回友好错误
+3. 越权拦截：`@TenantCheck` 注解 + `TenantInterceptor`，校验请求参数中的 tenantId 与当前用户 tenantId 一致
+
+**TenantCheck.java：**
 ```java
-package com.lc.system.repository;
+package com.lc.common.annotation;
 
-import com.lc.system.entity.SysUserRole;
-import org.springframework.data.jpa.repository.JpaRepository;
-import org.springframework.stereotype.Repository;
+import java.lang.annotation.*;
 
-import java.util.List;
-
-@Repository
-public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {
-    List<SysUserRole> findByUserId(Long userId);
-    void deleteByUserId(Long userId);
+@Target({ElementType.METHOD, ElementType.TYPE})
+@Retention(RetentionPolicy.RUNTIME)
+@Documented
+public @interface TenantCheck {
+    /** 参数中租户ID的字段名，默认为 tenantId */
+    String tenantIdParam() default "tenantId";
 }
 ```
 
-**UserService.java:**
-
+**TenantInterceptor.java 核心逻辑：**
 ```java
-package com.lc.system.service;
-
-import com.lc.system.entity.SysUser;
-
-public interface UserService {
-    SysUser findByUsername(String username);
-    SysUser findByTenantIdAndUsername(Long tenantId, String username);
-    SysUser findById(Long id);
-    SysUser createUser(SysUser user);
-    SysUser updateUser(SysUser user);
-    void deleteUser(Long id);
-    boolean verifyPassword(String rawPassword, String encodedPassword);
-}
+// 1. 从 UserContext.getTenantId() 获取当前用户租户ID
+// 2. 从请求参数中提取目标租户ID（@TenantCheck指定的参数名）
+// 3. 若不一致，抛出 BusinessException(PERMISSION_DENIED)
+// 4. 若 UserContext.getTenantId() 为 null，跳过（如超级管理员）
 ```
 
-**UserServiceImpl.java:**
-
-```java
-package com.lc.system.service.impl;
-
-import com.lc.common.exception.BusinessException;
-import com.lc.common.exception.GlobalErrorCode;
-import com.lc.common.util.PasswordUtil;
-import com.lc.system.entity.SysUser;
-import com.lc.system.repository.SysUserRepository;
-import com.lc.system.service.UserService;
-import lombok.RequiredArgsConstructor;
-import org.springframework.stereotype.Service;
-import org.springframework.transaction.annotation.Transactional;
-
-@Service
-@RequiredArgsConstructor
-public class UserServiceImpl implements UserService {
-
-    private final SysUserRepository userRepository;
-
-    @Override
-    public SysUser findByUsername(String username) {
-        return userRepository.findByUsername(username)
-                .orElseThrow(() -> new BusinessException(GlobalErrorCode.USER_NOT_FOUND));
-    }
-
-    @Override
-    public SysUser findByTenantIdAndUsername(Long tenantId, String username) {
-        return userRepository.findByTenantIdAndUsername(tenantId, username)
-                .orElseThrow(() -> new BusinessException(GlobalErrorCode.USER_NOT_FOUND));
-    }
-
-    @Override
-    public SysUser findById(Long id) {
-        return userRepository.findById(id)
-                .orElseThrow(() -> new BusinessException(GlobalErrorCode.USER_NOT_FOUND));
-    }
-
-    @Override
-    @Transactional
-    public SysUser createUser(SysUser user) {
-        if (userRepository.existsByUsername(user.getUsername())) {
-            throw new BusinessException(GlobalErrorCode.DATA_CONFLICT);
-        }
-        if (user.getPassword() != null) {
-            user.setPassword(PasswordUtil.encode(user.getPassword()));
-        }
-        return userRepository.save(user);
-    }
-
-    @Override
-    @Transactional
-    public SysUser updateUser(SysUser user) {
-        SysUser existing = findById(user.getId());
-        if (!existing.getUsername().equals(user.getUsername()) &&
-                userRepository.existsByUsername(user.getUsername())) {
-            throw new BusinessException(GlobalErrorCode.DATA_CONFLICT);
-        }
-        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
-            user.setPassword(PasswordUtil.encode(user.getPassword()));
-        } else {
-            user.setPassword(existing.getPassword());
-        }
-        return userRepository.save(user);
-    }
-
-    @Override
-    @Transactional
-    public void deleteUser(Long id) {
-        if (!userRepository.existsById(id)) {
-            throw new BusinessException(GlobalErrorCode.USER_NOT_FOUND);
-        }
-        userRepository.deleteById(id);
-    }
-
-    @Override
-    public boolean verifyPassword(String rawPassword, String encodedPassword) {
-        return PasswordUtil.matches(rawPassword, encodedPassword);
-    }
-}
-```
+- [ ] **Task 4-1: 修改 JwtAuthenticationFilter，整合 UserContext 设置**
+- [ ] **Task 4-2: 创建 TenantCheck 注解 + TenantInterceptor 拦截器**
+- [ ] **Task 4-3: 创建 WebMvcConfig 注册拦截器**
+- [ ] **Task 4-4: 创建 SysTenantRepository + TenantDTO + PageRequest + TenantService + TenantServiceImpl + TenantController**
+- [ ] **Task 4-5: 编译验证 + Commit**
 
-**Steps:**
-1. 创建 SysUserRepository.java
-2. 创建 SysRoleRepository.java
-3. 创建 SysUserRoleRepository.java
-4. 创建 UserService.java
-5. 创建 UserServiceImpl.java
-6. 编译验证：`cd backend && mvn clean compile -q -pl system-core -am`
-7. Commit，提交信息："feat: system-core模块 - Repository与UserService"
+---
 
-**Global Constraints:**
-- Java: 17
-- Spring Boot: 3.2.5
-- Maven: 3.9.x
+### Task 4 验收
+- [ ] 请求携带JWT时，UserContext 中能获取到 tenantId
+- [ ] 租户CRUD接口正常工作（GET/POST/PUT/DELETE /api/system/tenants）
+- [ ] @TenantCheck 注解生效，跨租户访问返回 403
 
-__tr_native_ec=$?; pwd -P >| '/var/log/tool/jobs/job-a17d8d0b458a46c2ac5c9bc573a1937c/cwd.txt'; exit "$__tr_native_ec"
\ No newline at end of file
diff --git a/.superpowers/sdd/task-5-brief.md b/.superpowers/sdd/task-5-brief.md
index c0909ffb..0d7dbb77 100644
--- a/.superpowers/sdd/task-5-brief.md
+++ b/.superpowers/sdd/task-5-brief.md
@@ -1,248 +1,81 @@
-# Task 5: system-core 模块 - JWT 与 Token 服务
+## Task 5: RBAC权限体系 ⬜ 待实施
 
-**Goal:** 创建 JWT 配置类、认证 DTO、JWT Token 服务和 Refresh Token 服务。
+> 设计文档 Task 5：用户角色、菜单权限、项目成员角色、接口权限声明
 
-**Files:**
-- Create: `backend/system-core/src/main/java/com/lc/system/config/JwtConfig.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/dto/AuthDTO.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/security/JwtTokenService.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/security/RefreshTokenService.java`
-
-**JwtConfig.java:**
+**目标：** 实现用户-角色-权限三层模型的完整CRUD和接口级权限控制。
 
+**Files:**
+- Create: `backend/common/src/main/java/com/lc/common/annotation/PreAuthorize.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysMenuRepository.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysPermissionRepository.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysOrgRepository.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysDictRepository.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/dto/UserDTO.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/dto/RoleDTO.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/dto/MenuDTO.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/PermissionService.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/RoleService.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/MenuService.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/PermissionServiceImpl.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/RoleServiceImpl.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/MenuServiceImpl.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/controller/RoleController.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/controller/MenuController.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/controller/UserController.java`
+- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/interceptor/PermissionInterceptor.java`
+- Modify: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebMvcConfig.java`（注册权限拦截器）
+
+**Interfaces:**
+- Consumes: `UserContext`, `SysRoleRepository`（已存在）, `SysUserRoleRepository`（已存在）, `SysMenu/SysPermission`实体（已存在）
+- Produces: `PermissionService`（权限校验）, `PreAuthorize`注解, `PermissionInterceptor`, 角色/菜单/用户管理REST接口
+
+**关键设计：**
+1. 菜单权限：树形菜单结构，角色通过 sys_role_menu 关联菜单（需确认该关联表是否存在，若不存在需新增迁移脚本）
+2. 项目成员角色：viewer/editor/admin/publisher 四级，存储在 project_member 表的 role 字段
+3. 接口权限声明：`@PreAuthorize("user:list")` 注解，PermissionInterceptor 解析注解并调用 PermissionService 校验
+
+**PreAuthorize.java：**
 ```java
-package com.lc.system.config;
-
-import lombok.Data;
-import org.springframework.boot.context.properties.ConfigurationProperties;
-import org.springframework.context.annotation.Configuration;
-
-@Data
-@Configuration
-@ConfigurationProperties(prefix = "jwt")
-public class JwtConfig {
-    private String secret = "your-256-bit-secret-key-here-must-be-at-least-32-characters";
-    private int accessTokenExpireMinutes = 15;
-    private int refreshTokenExpireDays = 7;
+package com.lc.common.annotation;
+
+import java.lang.annotation.*;
+
+@Target({ElementType.METHOD, ElementType.TYPE})
+@Retention(RetentionPolicy.RUNTIME)
+@Documented
+public @interface PreAuthorize {
+    /** 所需权限码，多个用逗号分隔 */
+    String value() default "";
+    /** true=需全部权限，false=任一权限即可 */
+    boolean requireAll() default false;
 }
 ```
 
-**AuthDTO.java:**
-
+**PermissionService.java：**
 ```java
-package com.lc.system.dto;
-
-import lombok.AllArgsConstructor;
-import lombok.Builder;
-import lombok.Data;
-import lombok.NoArgsConstructor;
-
-import java.time.LocalDateTime;
-
-@Data
-@Builder
-@NoArgsConstructor
-@AllArgsConstructor
-public class AuthDTO {
-    @Data
-    @Builder
-    @NoArgsConstructor
-    @AllArgsConstructor
-    public static class LoginRequest {
-        private String username;
-        private String password;
-    }
-
-    @Data
-    @Builder
-    @NoArgsConstructor
-    @AllArgsConstructor
-    public static class LoginResponse {
-        private String accessToken;
-        private String refreshToken;
-        private LocalDateTime accessTokenExpireTime;
-        private LocalDateTime refreshTokenExpireTime;
-        private UserInfo user;
-    }
-
-    @Data
-    @Builder
-    @NoArgsConstructor
-    @AllArgsConstructor
-    public static class UserInfo {
-        private Long id;
-        private String username;
-        private String realName;
-        private String email;
-        private String phone;
-        private Long tenantId;
-    }
-
-    @Data
-    @Builder
-    @NoArgsConstructor
-    @AllArgsConstructor
-    public static class RefreshRequest {
-        private String refreshToken;
-    }
+public interface PermissionService {
+    Set<String> getUserPermissions(Long userId);
+    boolean hasPermission(Long userId, String permission);
+    boolean hasAnyPermission(Long userId, String... permissions);
+    boolean hasAllPermissions(Long userId, String... permissions);
+    List<Long> getUserRoleIds(Long userId);
 }
 ```
 
-**JwtTokenService.java:**
-
-```java
-package com.lc.system.security;
-
-import com.lc.common.exception.BusinessException;
-import com.lc.common.exception.GlobalErrorCode;
-import com.lc.system.config.JwtConfig;
-import com.lc.system.entity.SysUser;
-import io.jsonwebtoken.Claims;
-import io.jsonwebtoken.Jwts;
-import io.jsonwebtoken.security.Keys;
-import lombok.RequiredArgsConstructor;
-import org.springframework.stereotype.Service;
-
-import javax.crypto.SecretKey;
-import java.nio.charset.StandardCharsets;
-import java.time.LocalDateTime;
-import java.time.ZoneId;
-import java.util.Date;
-import java.util.HashMap;
-import java.util.Map;
-
-@Service
-@RequiredArgsConstructor
-public class JwtTokenService {
-
-    private final JwtConfig jwtConfig;
-
-    private SecretKey getSigningKey() {
-        byte[] keyBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
-        return Keys.hmacShaKeyFor(keyBytes);
-    }
-
-    public String generateAccessToken(SysUser user) {
-        Map<String, Object> claims = new HashMap<>();
-        claims.put("userId", user.getId());
-        claims.put("username", user.getUsername());
-        claims.put("tenantId", user.getTenantId());
-
-        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(jwtConfig.getAccessTokenExpireMinutes());
-        return Jwts.builder()
-                .claims(claims)
-                .subject(user.getUsername())
-                .expiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
-                .signWith(getSigningKey(), Jwts.SIG.HS256)
-                .compact();
-    }
+**需确认：** sys_role_menu 关联表是否在 V3/V5 迁移脚本中已创建。若未创建，Task 5-1 需新增迁移脚本。
 
-    public String generateRefreshToken(SysUser user) {
-        Map<String, Object> claims = new HashMap<>();
-        claims.put("userId", user.getId());
-        claims.put("username", user.getUsername());
-
-        LocalDateTime expireTime = LocalDateTime.now().plusDays(jwtConfig.getRefreshTokenExpireDays());
-        return Jwts.builder()
-                .claims(claims)
-                .subject(user.getUsername())
-                .expiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
-                .signWith(getSigningKey(), Jwts.SIG.HS256)
-                .compact();
-    }
-
-    public Claims parseToken(String token) {
-        try {
-            return Jwts.parser()
-                    .verifyWith(getSigningKey())
-                    .build()
-                    .parseSignedClaims(token)
-                    .getPayload();
-        } catch (Exception e) {
-            throw new BusinessException(GlobalErrorCode.TOKEN_INVALID);
-        }
-    }
-
-    public Long getUserIdFromToken(String token) {
-        Claims claims = parseToken(token);
-        return claims.get("userId", Long.class);
-    }
-
-    public String getUsernameFromToken(String token) {
-        Claims claims = parseToken(token);
-        return claims.getSubject();
-    }
-
-    public Long getTenantIdFromToken(String token) {
-        Claims claims = parseToken(token);
-        return claims.get("tenantId", Long.class);
-    }
-}
-```
-
-**RefreshTokenService.java:**
-
-```java
-package com.lc.system.security;
-
-import com.lc.common.exception.BusinessException;
-import com.lc.common.exception.GlobalErrorCode;
-import com.lc.system.config.JwtConfig;
-import lombok.RequiredArgsConstructor;
-import org.springframework.data.redis.core.RedisTemplate;
-import org.springframework.stereotype.Service;
-
-import java.util.concurrent.TimeUnit;
-
-@Service
-@RequiredArgsConstructor
-public class RefreshTokenService {
-    private final RedisTemplate<String, Object> redisTemplate;
-    private final JwtConfig jwtConfig;
-
-    private static final String REFRESH_TOKEN_KEY = "refresh:%s";
-
-    public void saveRefreshToken(Long userId, String refreshToken) {
-        String key = String.format(REFRESH_TOKEN_KEY, userId);
-        redisTemplate.opsForValue().set(key, refreshToken, jwtConfig.getRefreshTokenExpireDays(), TimeUnit.DAYS);
-    }
-
-    public String getRefreshToken(Long userId) {
-        String key = String.format(REFRESH_TOKEN_KEY, userId);
-        Object token = redisTemplate.opsForValue().get(key);
-        return token != null ? token.toString() : null;
-    }
-
-    public void invalidateRefreshToken(Long userId) {
-        String key = String.format(REFRESH_TOKEN_KEY, userId);
-        redisTemplate.delete(key);
-    }
-
-    public boolean validateRefreshToken(Long userId, String token) {
-        String storedToken = getRefreshToken(userId);
-        if (storedToken == null) {
-            throw new BusinessException(GlobalErrorCode.REFRESH_TOKEN_EXPIRED);
-        }
-        if (!storedToken.equals(token)) {
-            throw new BusinessException(GlobalErrorCode.REFRESH_TOKEN_INVALID);
-        }
-        return true;
-    }
-}
-```
+- [ ] **Task 5-1: 确认/补建 sys_role_menu 关联表迁移脚本**
+- [ ] **Task 5-2: 创建 PreAuthorize 注解 + PermissionService 接口 + PermissionServiceImpl**
+- [ ] **Task 5-3: 创建 PermissionInterceptor + 注册到 WebMvcConfig**
+- [ ] **Task 5-4: 创建 RoleService/MenuService + 实现 + Repository + DTO**
+- [ ] **Task 5-5: 创建 RoleController/MenuController/UserController**
+- [ ] **Task 5-6: 编译验证 + Commit**
 
-**Steps:**
-1. 创建 JwtConfig.java
-2. 创建 AuthDTO.java
-3. 创建 JwtTokenService.java
-4. 创建 RefreshTokenService.java
-5. 编译验证：`cd backend && mvn clean compile -q -pl system-core -am`
-6. Commit，提交信息："feat: system-core模块 - JWT与Token服务"
+---
 
-**Global Constraints:**
-- Java: 17
-- Spring Boot: 3.2.5
-- Maven: 3.9.x
-- JJWT: 0.12.5
+### Task 5 验收
+- [ ] 角色管理接口正常（创建、编辑、删除、分配菜单）
+- [ ] 菜单管理接口正常（树形结构返回）
+- [ ] @PreAuthorize 注解生效，无权限返回 403
+- [ ] 用户管理接口正常（分页查询、创建、编辑、删除、重置密码）
 
-__tr_native_ec=$?; pwd -P >| '/var/log/tool/jobs/job-e7c83eb457a8441cacadbf2402a3cd14/cwd.txt'; exit "$__tr_native_ec"
\ No newline at end of file
diff --git a/.superpowers/sdd/task-6-brief.md b/.superpowers/sdd/task-6-brief.md
index e6d0eccb..6373cda3 100644
--- a/.superpowers/sdd/task-6-brief.md
+++ b/.superpowers/sdd/task-6-brief.md
@@ -1,270 +1,65 @@
-# Task 6: bootstrap 模块 - 启动类与安全配置
+## Task 6: 对象存储与密钥管理 ⬜ 待实施
 
-**Goal:** 创建 bootstrap 模块的 Spring Boot 启动类、安全配置、JWT 过滤器和全局异常处理器。
+> 设计文档 Task 6：快照/产物/附件存储，密钥加密与脱敏
 
-**Files:**
-- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/Application.java`
-- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/SecurityConfig.java`
-- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebConfig.java`
-- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/filter/JwtAuthenticationFilter.java`
-- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/handler/GlobalExceptionHandler.java`
-- Create: `backend/bootstrap/src/main/resources/application.yml`
-
-**Application.java:**
-
-```java
-package com.lc.bootstrap;
-
-import org.springframework.boot.SpringApplication;
-import org.springframework.boot.autoconfigure.SpringBootApplication;
-import org.springframework.boot.context.properties.EnableConfigurationProperties;
-import org.springframework.context.annotation.ComponentScan;
-
-@SpringBootApplication
-@ComponentScan(basePackages = {"com.lc"})
-@EnableConfigurationProperties
-public class Application {
-    public static void main(String[] args) {
-        SpringApplication.run(Application.class, args);
-    }
-}
-```
-
-**SecurityConfig.java:**
-
-```java
-package com.lc.bootstrap.config;
-
-import com.lc.bootstrap.filter.JwtAuthenticationFilter;
-import lombok.RequiredArgsConstructor;
-import org.springframework.context.annotation.Bean;
-import org.springframework.context.annotation.Configuration;
-import org.springframework.security.authentication.AuthenticationManager;
-import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
-import org.springframework.security.config.annotation.web.builders.HttpSecurity;
-import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
-import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
-import org.springframework.security.config.http.SessionCreationPolicy;
-import org.springframework.security.web.SecurityFilterChain;
-import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
-
-@Configuration
-@EnableWebSecurity
-@RequiredArgsConstructor
-public class SecurityConfig {
-    private final JwtAuthenticationFilter jwtAuthenticationFilter;
-
-    @Bean
-    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
-        http
-                .csrf(AbstractHttpConfigurer::disable)
-                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
-                .authorizeHttpRequests(auth -> auth
-                        .requestMatchers("/api/auth/login", "/api/auth/refresh").permitAll()
-                        .anyRequest().authenticated())
-                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
-        return http.build();
-    }
-
-    @Bean
-    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
-        return config.getAuthenticationManager();
-    }
-}
-```
-
-**WebConfig.java:**
-
-```java
-package com.lc.bootstrap.config;
-
-import org.springframework.context.annotation.Configuration;
-import org.springframework.web.servlet.config.annotation.CorsRegistry;
-import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
-
-@Configuration
-public class WebConfig implements WebMvcConfigurer {
-    @Override
-    public void addCorsMappings(CorsRegistry registry) {
-        registry.addMapping("/api/**")
-                .allowedOriginPatterns("*")
-                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
-                .allowedHeaders("*")
-                .allowCredentials(true)
-                .maxAge(3600);
-    }
-}
-```
-
-**JwtAuthenticationFilter.java:**
+**目标：** 抽象对象存储接口，支持本地文件系统和MinIO切换，提供AES加密工具和文件上传接口。
 
+**Files:**
+- Create: `backend/common/src/main/java/com/lc/common/storage/StorageService.java`
+- Create: `backend/common/src/main/java/com/lc/common/storage/StorageProperties.java`
+- Create: `backend/common/src/main/java/com/lc/common/storage/LocalStorageServiceImpl.java`
+- Create: `backend/common/src/main/java/com/lc/common/storage/MinioStorageServiceImpl.java`
+- Create: `backend/common/src/main/java/com/lc/common/util/EncryptUtil.java`
+- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/StorageConfig.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/controller/FileController.java`
+- Modify: `backend/bootstrap/src/main/resources/application.yml`（添加 storage 配置）
+- Modify: `backend/common/pom.xml`（添加 MinIO SDK 依赖，optional）
+
+**Interfaces:**
+- Consumes: `StorageProperties`（配置）, `UserContext`（租户目录隔离）
+- Produces: `StorageService`（上传/下载/删除/预签名URL）, `FileController`（文件上传REST接口）, `EncryptUtil`（AES加解密）
+
+**关键设计：**
+1. 对象存储抽象：`StorageService` 接口统一上传/下载/删除/预签名URL，通过 `storage.type` 配置切换实现
+2. 密钥管理：`EncryptUtil` 提供 AES/ECB/PKCS5Padding 加解密，密钥通过环境变量 `LC_ENCRYPT_KEY` 注入
+3. 文件上传：类型校验（MIME白名单）、大小限制（默认10MB）、随机文件名、按 `tenant/{tenantId}/` 目录隔离
+
+**StorageService.java：**
 ```java
-package com.lc.bootstrap.filter;
-
-import com.lc.system.security.JwtTokenService;
-import jakarta.servlet.FilterChain;
-import jakarta.servlet.ServletException;
-import jakarta.servlet.http.HttpServletRequest;
-import jakarta.servlet.http.HttpServletResponse;
-import lombok.RequiredArgsConstructor;
-import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
-import org.springframework.security.core.authority.SimpleGrantedAuthority;
-import org.springframework.security.core.context.SecurityContextHolder;
-import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
-import org.springframework.stereotype.Component;
-import org.springframework.util.StringUtils;
-import org.springframework.web.filter.OncePerRequestFilter;
-
-import java.io.IOException;
-import java.util.Collections;
-
-@Component
-@RequiredArgsConstructor
-public class JwtAuthenticationFilter extends OncePerRequestFilter {
-    private final JwtTokenService jwtTokenService;
-
-    @Override
-    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
-            throws ServletException, IOException {
-        try {
-            String token = extractToken(request);
-            if (StringUtils.hasText(token)) {
-                String username = jwtTokenService.getUsernameFromToken(token);
-                Long userId = jwtTokenService.getUserIdFromToken(token);
-
-                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
-                        userId, null, Collections.singletonList(new SimpleGrantedAuthority("USER"))
-                );
-                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
-                SecurityContextHolder.getContext().setAuthentication(authToken);
-            }
-        } catch (Exception e) {
-            SecurityContextHolder.clearContext();
-        }
-        filterChain.doFilter(request, response);
-    }
-
-    private String extractToken(HttpServletRequest request) {
-        String bearerToken = request.getHeader("Authorization");
-        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
-            return bearerToken.substring(7);
-        }
-        return null;
-    }
+public interface StorageService {
+    String upload(String bucket, String key, InputStream input, String contentType, long size);
+    InputStream download(String bucket, String key);
+    void delete(String bucket, String key);
+    String getPresignedUrl(String bucket, String key, long expireSeconds);
+    boolean exists(String bucket, String key);
 }
 ```
 
-**GlobalExceptionHandler.java:**
-
-```java
-package com.lc.bootstrap.handler;
-
-import com.lc.common.dto.Result;
-import com.lc.common.exception.BusinessException;
-import lombok.extern.slf4j.Slf4j;
-import org.springframework.http.HttpStatus;
-import org.springframework.security.authentication.BadCredentialsException;
-import org.springframework.security.core.AuthenticationException;
-import org.springframework.validation.FieldError;
-import org.springframework.web.bind.MethodArgumentNotValidException;
-import org.springframework.web.bind.annotation.ExceptionHandler;
-import org.springframework.web.bind.annotation.ResponseStatus;
-import org.springframework.web.bind.annotation.RestControllerAdvice;
-
-import java.util.HashMap;
-import java.util.Map;
-
-@Slf4j
-@RestControllerAdvice
-public class GlobalExceptionHandler {
-
-    @ExceptionHandler(BusinessException.class)
-    public Result<Void> handleBusinessException(BusinessException e) {
-        log.warn("Business exception: code={}, message={}", e.getCode(), e.getMessage());
-        return Result.fail(e.getCode(), e.getMessage());
-    }
-
-    @ExceptionHandler(AuthenticationException.class)
-    @ResponseStatus(HttpStatus.UNAUTHORIZED)
-    public Result<Void> handleAuthenticationException(AuthenticationException e) {
-        log.warn("Authentication exception: {}", e.getMessage());
-        return Result.fail(401, "未授权");
-    }
-
-    @ExceptionHandler(BadCredentialsException.class)
-    @ResponseStatus(HttpStatus.UNAUTHORIZED)
-    public Result<Void> handleBadCredentialsException(BadCredentialsException e) {
-        log.warn("Bad credentials: {}", e.getMessage());
-        return Result.fail(401, "用户名或密码错误");
-    }
-
-    @ExceptionHandler(MethodArgumentNotValidException.class)
-    @ResponseStatus(HttpStatus.BAD_REQUEST)
-    public Result<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
-        Map<String, String> errors = new HashMap<>();
-        e.getBindingResult().getAllErrors().forEach(error -> {
-            String fieldName = ((FieldError) error).getField();
-            String errorMessage = error.getDefaultMessage();
-            errors.put(fieldName, errorMessage);
-        });
-        log.warn("Validation failed: {}", errors);
-        return Result.success(errors);
-    }
-
-    @ExceptionHandler(Exception.class)
-    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
-    public Result<Void> handleGenericException(Exception e) {
-        log.error("Unexpected error: ", e);
-        return Result.fail(500, "系统内部错误");
-    }
-}
-```
-
-**application.yml:**
-
+**application.yml 新增配置：**
 ```yaml
-server:
-  port: 8080
-
-spring:
-  datasource:
-    url: jdbc:mysql://localhost:3306/lc_platform?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
-    username: root
-    password: password
-    driver-class-name: com.mysql.cj.jdbc.Driver
-  data:
-    redis:
-      host: localhost
-      port: 6379
-  flyway:
-    enabled: true
-    locations: classpath:db/migration
-    baseline-on-migrate: true
-
-jwt:
-  secret: your-256-bit-secret-key-here-must-be-at-least-32-characters
-  access-token-expire-minutes: 15
-  refresh-token-expire-days: 7
-
-logging:
-  level:
-    com.lc: DEBUG
-    org.springframework.security: DEBUG
+storage:
+  type: local          # local | minio
+  local-path: ./storage
+  endpoint: ${MINIO_ENDPOINT:}
+  access-key: ${MINIO_ACCESS_KEY:}
+  secret-key: ${MINIO_SECRET_KEY:}
+  default-bucket: lc-platform
+  max-file-size: 10485760
+
+encrypt:
+  key: ${LC_ENCRYPT_KEY:lc-platform-2026-secure-key-32b}
 ```
 
-**Steps:**
-1. 创建 Application.java
-2. 创建 SecurityConfig.java
-3. 创建 WebConfig.java
-4. 创建 JwtAuthenticationFilter.java
-5. 创建 GlobalExceptionHandler.java
-6. 创建 application.yml
-7. 编译验证：`cd backend && mvn clean compile -q -pl bootstrap -am`
-8. Commit，提交信息："feat: bootstrap模块 - 启动类与安全配置"
+- [ ] **Task 6-1: 创建 StorageService 接口 + StorageProperties + LocalStorageServiceImpl**
+- [ ] **Task 6-2: 创建 MinioStorageServiceImpl + StorageConfig + 添加MinIO依赖(optional)**
+- [ ] **Task 6-3: 创建 EncryptUtil 加密工具**
+- [ ] **Task 6-4: 创建 FileController 文件上传接口 + 更新 application.yml**
+- [ ] **Task 6-5: 编译验证 + Commit**
+
+---
 
-**Global Constraints:**
-- Java: 17
-- Spring Boot: 3.2.5
-- Maven: 3.9.x
+### Task 6 验收
+- [ ] 文件上传接口可用，文件保存到 ./storage/{bucket}/{key}
+- [ ] 可通过 storage.type 配置切换本地存储和MinIO存储
+- [ ] AES 加解密工具正常工作
 
-__tr_native_ec=$?; pwd -P >| '/var/log/tool/jobs/job-4c7d5e3859c4413fb33b36d2dd966273/cwd.txt'; exit "$__tr_native_ec"
\ No newline at end of file
diff --git a/backend/bootstrap/pom.xml b/backend/bootstrap/pom.xml
index aebe9b34..84391c87 100644
--- a/backend/bootstrap/pom.xml
+++ b/backend/bootstrap/pom.xml
@@ -71,19 +71,29 @@
 
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-web</artifactId>
         </dependency>
+        <dependency>
+            <groupId>org.springframework.boot</groupId>
+            <artifactId>spring-boot-starter-aop</artifactId>
+        </dependency>
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-security</artifactId>
         </dependency>
         <dependency>
             <groupId>org.projectlombok</groupId>
             <artifactId>lombok</artifactId>
             <optional>true</optional>
         </dependency>
+
+        <dependency>
+            <groupId>org.springframework.boot</groupId>
+            <artifactId>spring-boot-starter-test</artifactId>
+            <scope>test</scope>
+        </dependency>
     </dependencies>
 
     <build>
         <plugins>
             <plugin>
diff --git a/backend/bootstrap/src/main/java/com/lc/bootstrap/aspect/AuditLogAspect.java b/backend/bootstrap/src/main/java/com/lc/bootstrap/aspect/AuditLogAspect.java
new file mode 100644
index 00000000..c6a10386
--- /dev/null
+++ b/backend/bootstrap/src/main/java/com/lc/bootstrap/aspect/AuditLogAspect.java
@@ -0,0 +1,170 @@
+package com.lc.bootstrap.aspect;
+
+import com.lc.common.annotation.AuditLog;
+import com.lc.common.context.UserContext;
+import com.lc.system.entity.AuditLogEntity;
+import com.lc.system.service.AuditLogService;
+import jakarta.servlet.http.HttpServletRequest;
+import lombok.RequiredArgsConstructor;
+import lombok.extern.slf4j.Slf4j;
+import org.aspectj.lang.ProceedingJoinPoint;
+import org.aspectj.lang.annotation.Around;
+import org.aspectj.lang.annotation.Aspect;
+import org.aspectj.lang.reflect.MethodSignature;
+import org.springframework.expression.Expression;
+import org.springframework.expression.ExpressionParser;
+import org.springframework.expression.spel.standard.SpelExpressionParser;
+import org.springframework.expression.spel.support.StandardEvaluationContext;
+import org.springframework.stereotype.Component;
+import org.springframework.web.context.request.RequestAttributes;
+import org.springframework.web.context.request.RequestContextHolder;
+import org.springframework.web.context.request.ServletRequestAttributes;
+
+import java.time.LocalDateTime;
+import java.util.UUID;
+
+/**
+ * 审计日志 AOP 切面：拦截标注了 {@link AuditLog} 的方法，自动记录审计日志。
+ * 操作人、租户、IP、UserAgent、RequestId 由切面从请求上下文与 UserContext 自动填充。
+ * 异常路径下会重新抛出原业务异常，确保不改变业务行为。
+ */
+@Aspect
+@Component
+@RequiredArgsConstructor
+@Slf4j
+public class AuditLogAspect {
+
+    private final AuditLogService auditLogService;
+    private final ExpressionParser spelParser = new SpelExpressionParser();
+
+    @Around("@annotation(auditLog)")
+    public Object around(ProceedingJoinPoint joinPoint, AuditLog auditLog) throws Throwable {
+        HttpServletRequest request = getCurrentRequest();
+        if (request == null) {
+            // 非 HTTP 上下文（如异步任务），跳过审计日志记录
+            return joinPoint.proceed();
+        }
+
+        String clientIp = getClientIp(request);
+        String userAgent = request.getHeader("User-Agent");
+        String requestId = request.getHeader("X-Request-Id");
+        if (requestId == null || requestId.isEmpty()) {
+            requestId = UUID.randomUUID().toString().replace("-", "");
+        }
+
+        try {
+            Object result = joinPoint.proceed();
+            try {
+                AuditLogEntity entity = buildEntity(auditLog, joinPoint, clientIp, userAgent, requestId,
+                        "SUCCESS", null);
+                auditLogService.save(entity);
+            } catch (Exception saveEx) {
+                log.warn("Failed to save audit log (success path), action={}, resourceType={}: {}",
+                        auditLog.action(), auditLog.resourceType(), saveEx.getMessage());
+            }
+            return result;
+        } catch (Throwable e) {
+            try {
+                AuditLogEntity entity = buildEntity(auditLog, joinPoint, clientIp, userAgent, requestId,
+                        "FAILED", truncate(e.getMessage(), 500));
+                auditLogService.save(entity);
+            } catch (Exception saveEx) {
+                log.warn("Failed to save audit log (failure path), action={}, resourceType={}: {}",
+                        auditLog.action(), auditLog.resourceType(), saveEx.getMessage());
+            }
+            // re-throw 原业务异常，不吞异常
+            throw e;
+        }
+    }
+
+    private AuditLogEntity buildEntity(AuditLog auditLog, ProceedingJoinPoint joinPoint,
+                                       String clientIp, String userAgent, String requestId,
+                                       String result, String errorMessage) {
+        AuditLogEntity entity = new AuditLogEntity();
+        entity.setTenantId(UserContext.getTenantId());
+        entity.setUserId(UserContext.getUserId());
+        entity.setUserName(UserContext.getUsername());
+        entity.setAction(auditLog.action());
+        entity.setResourceType(auditLog.resourceType());
+        entity.setResourceId(extractResourceId(joinPoint, auditLog));
+        entity.setClientIp(clientIp);
+        entity.setIp(clientIp);
+        entity.setUserAgent(userAgent);
+        entity.setRequestId(requestId);
+        entity.setCreatedTime(LocalDateTime.now());
+        entity.setResult(result);
+        entity.setErrorMessage(errorMessage);
+        return entity;
+    }
+
+    /**
+     * 提取资源 ID。
+     * - 若 resourceIdParam 为空串，返回 null
+     * - 以 # 开头：按 SpEL 解析，方法参数名作为变量
+     * - 否则：作为参数名直接匹配
+     * - 解析失败返回 null，不抛异常
+     */
+    private String extractResourceId(ProceedingJoinPoint joinPoint, AuditLog auditLog) {
+        String param = auditLog.resourceIdParam();
+        if (param == null || param.isEmpty()) {
+            return null;
+        }
+        try {
+            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
+            String[] paramNames = signature.getParameterNames();
+            Object[] args = joinPoint.getArgs();
+
+            if (param.startsWith("#")) {
+                StandardEvaluationContext context = new StandardEvaluationContext();
+                if (paramNames != null) {
+                    for (int i = 0; i < paramNames.length; i++) {
+                        context.setVariable(paramNames[i], args[i]);
+                    }
+                }
+                Expression expression = spelParser.parseExpression(param);
+                Object value = expression.getValue(context);
+                return value != null ? value.toString() : null;
+            } else {
+                if (paramNames != null) {
+                    for (int i = 0; i < paramNames.length; i++) {
+                        if (param.equals(paramNames[i])) {
+                            return args[i] != null ? args[i].toString() : null;
+                        }
+                    }
+                }
+                return null;
+            }
+        } catch (Exception e) {
+            log.warn("Failed to extract resourceId from param '{}': {}", param, e.getMessage());
+            return null;
+        }
+    }
+
+    private String getClientIp(HttpServletRequest request) {
+        String ip = request.getHeader("X-Forwarded-For");
+        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
+            // 取第一个（最原始客户端）
+            return ip.split(",")[0].trim();
+        }
+        ip = request.getHeader("X-Real-IP");
+        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
+            return ip.trim();
+        }
+        return request.getRemoteAddr();
+    }
+
+    private HttpServletRequest getCurrentRequest() {
+        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
+        if (attrs instanceof ServletRequestAttributes sra) {
+            return sra.getRequest();
+        }
+        return null;
+    }
+
+    private String truncate(String s, int maxLen) {
+        if (s == null) {
+            return null;
+        }
+        return s.length() <= maxLen ? s : s.substring(0, maxLen);
+    }
+}
diff --git a/backend/bootstrap/src/main/java/com/lc/bootstrap/config/StorageConfig.java b/backend/bootstrap/src/main/java/com/lc/bootstrap/config/StorageConfig.java
new file mode 100644
index 00000000..10186ac8
--- /dev/null
+++ b/backend/bootstrap/src/main/java/com/lc/bootstrap/config/StorageConfig.java
@@ -0,0 +1,69 @@
+package com.lc.bootstrap.config;
+
+import com.lc.common.exception.BusinessException;
+import com.lc.common.exception.GlobalErrorCode;
+import com.lc.common.storage.LocalStorageServiceImpl;
+import com.lc.common.storage.StorageProperties;
+import com.lc.common.storage.StorageService;
+import lombok.RequiredArgsConstructor;
+import lombok.extern.slf4j.Slf4j;
+import org.springframework.boot.context.properties.EnableConfigurationProperties;
+import org.springframework.context.annotation.Bean;
+import org.springframework.context.annotation.Configuration;
+
+import java.io.IOException;
+import java.lang.reflect.Constructor;
+import java.nio.file.Files;
+import java.nio.file.Paths;
+
+/**
+ * 对象存储配置。
+ * 根据 storage.type 创建本地存储或 MinIO 存储实现。
+ * MinIO 实现通过反射加载，避免在 type=local 时硬依赖 MinIO SDK。
+ */
+@Slf4j
+@Configuration
+@EnableConfigurationProperties(StorageProperties.class)
+@RequiredArgsConstructor
+public class StorageConfig {
+
+    private static final String MINIO_IMPL_CLASS = "com.lc.common.storage.MinioStorageServiceImpl";
+
+    private final StorageProperties storageProperties;
+
+    @Bean
+    public StorageService storageService() {
+        String type = storageProperties.getType();
+        if ("minio".equalsIgnoreCase(type)) {
+            return createMinioStorage();
+        }
+        return createLocalStorage();
+    }
+
+    private StorageService createLocalStorage() {
+        try {
+            Files.createDirectories(Paths.get(storageProperties.getLocalPath()));
+            log.info("Local storage initialized at: {}", storageProperties.getLocalPath());
+        } catch (IOException e) {
+            log.error("Failed to create local storage dir: {}", storageProperties.getLocalPath(), e);
+            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
+        }
+        return new LocalStorageServiceImpl(storageProperties);
+    }
+
+    private StorageService createMinioStorage() {
+        try {
+            Class<?> clazz = Class.forName(MINIO_IMPL_CLASS);
+            Constructor<?> ctor = clazz.getConstructor(StorageProperties.class);
+            StorageService service = (StorageService) ctor.newInstance(storageProperties);
+            log.info("MinIO storage initialized at: {}", storageProperties.getEndpoint());
+            return service;
+        } catch (ClassNotFoundException e) {
+            log.error("MinIO SDK not on classpath. Add io.minio:minio dependency to use storage.type=minio", e);
+            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
+        } catch (Exception e) {
+            log.error("Failed to initialize MinIO storage", e);
+            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
+        }
+    }
+}
diff --git a/backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebConfig.java b/backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebConfig.java
index ce1ba6e2..88638209 100644
--- a/backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebConfig.java
+++ b/backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebConfig.java
@@ -1,18 +1,65 @@
 package com.lc.bootstrap.config;
 
+import com.lc.bootstrap.interceptor.PermissionInterceptor;
+import com.lc.bootstrap.interceptor.TenantInterceptor;
+import lombok.RequiredArgsConstructor;
+import org.springframework.beans.factory.annotation.Value;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.web.servlet.config.annotation.CorsRegistry;
+import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
 import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
+import java.util.Arrays;
+import java.util.List;
+
+/**
+ * Web MVC 配置：CORS、拦截器注册。
+ * <p>
+ * CORS 使用可配置白名单（{@code cors.allowed-origins}），默认仅放行本地前端开发端口，
+ * 避免通配符 + 凭证组合带来的跨域信任放大风险。
+ */
 @Configuration
+@RequiredArgsConstructor
 public class WebConfig implements WebMvcConfigurer {
+    private final TenantInterceptor tenantInterceptor;
+    private final PermissionInterceptor permissionInterceptor;
+
+    @Value("${cors.allowed-origins:http://localhost:5173,http://localhost:3000}")
+    private List<String> allowedOrigins;
+
+    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
+    private String allowedMethods;
+
+    @Value("${cors.allow-credentials:true}")
+    private boolean allowCredentials;
+
+    @Value("${cors.max-age:3600}")
+    private long maxAge;
+
     @Override
     public void addCorsMappings(CorsRegistry registry) {
+        String[] origins = allowedOrigins.toArray(new String[0]);
+        String[] methods = Arrays.stream(allowedMethods.split(","))
+                .map(String::trim)
+                .filter(s -> !s.isEmpty())
+                .toArray(String[]::new);
         registry.addMapping("/api/**")
-                .allowedOriginPatterns("*")
-                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
-                .allowedHeaders("*")
-                .allowCredentials(true)
-                .maxAge(3600);
+                .allowedOrigins(origins)
+                .allowedMethods(methods)
+                .allowedHeaders("Authorization", "Content-Type", "X-Request-Id", "X-Requested-With")
+                .exposedHeaders("X-Request-Id", "Content-Disposition")
+                .allowCredentials(allowCredentials)
+                .maxAge(maxAge);
+    }
+
+    @Override
+    public void addInterceptors(InterceptorRegistry registry) {
+        registry.addInterceptor(tenantInterceptor)
+                .addPathPatterns("/api/**")
+                .excludePathPatterns("/api/auth/**");
+        // 权限拦截器：在租户校验之后执行，覆盖所有 /api/** 接口（认证接口除外）
+        registry.addInterceptor(permissionInterceptor)
+                .addPathPatterns("/api/**")
+                .excludePathPatterns("/api/auth/**");
     }
-}
\ No newline at end of file
+}
diff --git a/backend/bootstrap/src/main/java/com/lc/bootstrap/filter/JwtAuthenticationFilter.java b/backend/bootstrap/src/main/java/com/lc/bootstrap/filter/JwtAuthenticationFilter.java
index 30939808..a4fcac21 100644
--- a/backend/bootstrap/src/main/java/com/lc/bootstrap/filter/JwtAuthenticationFilter.java
+++ b/backend/bootstrap/src/main/java/com/lc/bootstrap/filter/JwtAuthenticationFilter.java
@@ -1,7 +1,9 @@
 package com.lc.bootstrap.filter;
 
+import com.lc.common.context.TenantContext;
+import com.lc.common.context.UserContext;
 import com.lc.system.security.JwtTokenService;
 import jakarta.servlet.FilterChain;
 import jakarta.servlet.ServletException;
 import jakarta.servlet.http.HttpServletRequest;
 import jakarta.servlet.http.HttpServletResponse;
@@ -28,26 +30,46 @@ public class JwtAuthenticationFilter extends OncePerRequestFilter {
         try {
             String token = extractToken(request);
             if (StringUtils.hasText(token)) {
                 String username = jwtTokenService.getUsernameFromToken(token);
                 Long userId = jwtTokenService.getUserIdFromToken(token);
+                Long tenantId = jwtTokenService.getTenantIdFromToken(token);
+
+                // 设置用户上下文与租户上下文
+                UserContext.set(UserContext.builder()
+                        .userId(userId)
+                        .tenantId(tenantId)
+                        .username(username)
+                        .build());
+                if (tenantId != null) {
+                    TenantContext.setTenantId(tenantId);
+                }
 
                 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                         userId, null, Collections.singletonList(new SimpleGrantedAuthority("USER"))
                 );
                 authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                 SecurityContextHolder.getContext().setAuthentication(authToken);
             }
         } catch (Exception e) {
             SecurityContextHolder.clearContext();
+            UserContext.clear();
+            TenantContext.clear();
+        } finally {
+            try {
+                filterChain.doFilter(request, response);
+            } finally {
+                // 请求结束后清理 ThreadLocal，避免线程池复用导致的上下文泄漏
+                UserContext.clear();
+                TenantContext.clear();
+            }
         }
-        filterChain.doFilter(request, response);
     }
 
     private String extractToken(HttpServletRequest request) {
         String bearerToken = request.getHeader("Authorization");
         if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
             return bearerToken.substring(7);
         }
         return null;
     }
-}
\ No newline at end of file
+}
diff --git a/backend/bootstrap/src/main/java/com/lc/bootstrap/interceptor/PermissionInterceptor.java b/backend/bootstrap/src/main/java/com/lc/bootstrap/interceptor/PermissionInterceptor.java
new file mode 100644
index 00000000..1e59e02b
--- /dev/null
+++ b/backend/bootstrap/src/main/java/com/lc/bootstrap/interceptor/PermissionInterceptor.java
@@ -0,0 +1,88 @@
+package com.lc.bootstrap.interceptor;
+
+import com.lc.common.annotation.PreAuthorize;
+import com.lc.common.context.UserContext;
+import com.lc.common.exception.BusinessException;
+import com.lc.common.exception.GlobalErrorCode;
+import com.lc.system.service.PermissionService;
+import jakarta.servlet.http.HttpServletRequest;
+import jakarta.servlet.http.HttpServletResponse;
+import lombok.RequiredArgsConstructor;
+import lombok.extern.slf4j.Slf4j;
+import org.springframework.stereotype.Component;
+import org.springframework.web.method.HandlerMethod;
+import org.springframework.web.servlet.HandlerInterceptor;
+
+import java.util.ArrayList;
+import java.util.Arrays;
+import java.util.List;
+
+/**
+ * 接口权限校验拦截器。
+ * <p>
+ * 解析方法/类上的 {@link PreAuthorize} 注解，根据 {@link PermissionService}
+ * 判断当前用户是否具备所需权限。注解不存在时直接放行，由 Security 层保证已认证。
+ * </p>
+ * <p>
+ * 校验顺序：方法级注解优先于类级注解；当方法未标注时回退到类级。
+ * </p>
+ */
+@Slf4j
+@Component
+@RequiredArgsConstructor
+public class PermissionInterceptor implements HandlerInterceptor {
+
+    private final PermissionService permissionService;
+
+    @Override
+    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
+        if (!(handler instanceof HandlerMethod handlerMethod)) {
+            return true;
+        }
+
+        // 优先方法级注解，其次类级注解
+        PreAuthorize preAuthorize = handlerMethod.getMethodAnnotation(PreAuthorize.class);
+        if (preAuthorize == null) {
+            preAuthorize = handlerMethod.getBeanType().getAnnotation(PreAuthorize.class);
+        }
+        if (preAuthorize == null) {
+            return true;
+        }
+
+        String value = preAuthorize.value();
+        if (value == null || value.trim().isEmpty()) {
+            return true;
+        }
+
+        Long userId = UserContext.getUserId();
+        if (userId == null) {
+            throw new BusinessException(GlobalErrorCode.UNAUTHORIZED);
+        }
+
+        // 解析逗号分隔的权限码
+        List<String> required = new ArrayList<>();
+        for (String token : Arrays.asList(value.split(","))) {
+            if (token != null && !token.trim().isEmpty()) {
+                required.add(token.trim());
+            }
+        }
+        if (required.isEmpty()) {
+            return true;
+        }
+
+        String[] permissions = required.toArray(new String[0]);
+        boolean granted;
+        if (preAuthorize.requireAll()) {
+            granted = permissionService.hasAllPermissions(userId, permissions);
+        } else {
+            granted = permissionService.hasAnyPermission(userId, permissions);
+        }
+
+        if (!granted) {
+            log.warn("Permission denied: userId={}, required={}, requireAll={}",
+                    userId, required, preAuthorize.requireAll());
+            throw new BusinessException(GlobalErrorCode.PERMISSION_DENIED);
+        }
+        return true;
+    }
+}
diff --git a/backend/bootstrap/src/main/java/com/lc/bootstrap/interceptor/TenantInterceptor.java b/backend/bootstrap/src/main/java/com/lc/bootstrap/interceptor/TenantInterceptor.java
new file mode 100644
index 00000000..528f4f7c
--- /dev/null
+++ b/backend/bootstrap/src/main/java/com/lc/bootstrap/interceptor/TenantInterceptor.java
@@ -0,0 +1,96 @@
+package com.lc.bootstrap.interceptor;
+
+import com.lc.common.annotation.TenantCheck;
+import com.lc.common.context.UserContext;
+import com.lc.common.exception.BusinessException;
+import com.lc.common.exception.GlobalErrorCode;
+import jakarta.servlet.http.HttpServletRequest;
+import jakarta.servlet.http.HttpServletResponse;
+import lombok.extern.slf4j.Slf4j;
+import org.springframework.stereotype.Component;
+import org.springframework.web.method.HandlerMethod;
+import org.springframework.web.servlet.HandlerInterceptor;
+import org.springframework.web.servlet.HandlerMapping;
+
+import java.util.Map;
+
+/**
+ * 多租户越权校验拦截器。
+ * <p>
+ * 当前支持的 tenantId 提取方式：
+ * <ul>
+ *   <li>query parameter：通过 {@code request.getParameter(tenantIdParam())} 提取</li>
+ *   <li>path variable：通过 Spring MVC 解析后的 URI template variables
+ *       （{@link HandlerMapping#URI_TEMPLATE_VARIABLES_ATTRIBUTE}）提取</li>
+ * </ul>
+ * request body 中的 tenantId 暂不支持，后续可通过 AOP 切面（如 @Around 切
+ * @RequestBody 参数反序列化后的对象）扩展，避免在拦截器层重复读取 body 流。
+ * </p>
+ */
+@Slf4j
+@Component
+public class TenantInterceptor implements HandlerInterceptor {
+
+    @Override
+    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
+        if (!(handler instanceof HandlerMethod handlerMethod)) {
+            return true;
+        }
+
+        // 优先方法级注解，其次类级注解
+        TenantCheck tenantCheck = handlerMethod.getMethodAnnotation(TenantCheck.class);
+        if (tenantCheck == null) {
+            tenantCheck = handlerMethod.getBeanType().getAnnotation(TenantCheck.class);
+        }
+        if (tenantCheck == null) {
+            return true;
+        }
+
+        // 当前登录用户的租户ID为空时（如超级管理员），跳过校验
+        Long currentTenantId = UserContext.getTenantId();
+        if (currentTenantId == null) {
+            return true;
+        }
+
+        String paramName = tenantCheck.tenantIdParam();
+        // 1. 优先从 query parameter 提取
+        String paramValue = request.getParameter(paramName);
+        // 2. query parameter 未携带时，尝试从 path variable 提取
+        if (paramValue == null || paramValue.trim().isEmpty()) {
+            paramValue = extractPathVariable(request, paramName);
+        }
+        // 请求未携带目标租户ID时跳过校验（交由业务层处理）
+        if (paramValue == null || paramValue.trim().isEmpty()) {
+            return true;
+        }
+
+        Long targetTenantId;
+        try {
+            targetTenantId = Long.parseLong(paramValue.trim());
+        } catch (NumberFormatException e) {
+            log.warn("Invalid tenantId param: {}", paramValue);
+            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR);
+        }
+
+        if (!currentTenantId.equals(targetTenantId)) {
+            log.warn("Tenant permission denied: current={}, target={}", currentTenantId, targetTenantId);
+            throw new BusinessException(GlobalErrorCode.PERMISSION_DENIED);
+        }
+        return true;
+    }
+
+    /**
+     * 从 Spring MVC 解析后的 URI template variables 中提取 path variable。
+     * 该属性由 RequestMappingHandlerMapping 在请求映射完成后写入 request attribute。
+     */
+    private String extractPathVariable(HttpServletRequest request, String paramName) {
+        Object pathVars = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
+        if (pathVars instanceof Map<?, ?> map) {
+            Object value = map.get(paramName);
+            if (value != null) {
+                return value.toString();
+            }
+        }
+        return null;
+    }
+}
diff --git a/backend/bootstrap/src/main/resources/application.yml b/backend/bootstrap/src/main/resources/application.yml
index c988427d..05e4b3b3 100644
--- a/backend/bootstrap/src/main/resources/application.yml
+++ b/backend/bootstrap/src/main/resources/application.yml
@@ -23,5 +23,25 @@ jwt:
 
 logging:
   level:
     com.lc: DEBUG
     org.springframework.security: INFO
+
+storage:
+  type: local
+  local-path: ./storage
+  endpoint: ${MINIO_ENDPOINT:}
+  access-key: ${MINIO_ACCESS_KEY:}
+  secret-key: ${MINIO_SECRET_KEY:}
+  default-bucket: lc-platform
+  max-file-size: 10485760
+
+encrypt:
+  key: ${LC_ENCRYPT_KEY:lc-platform-2026-secure-key-32bx}
+
+cors:
+  allowed-origins:
+    - http://localhost:5173
+    - http://localhost:3000
+  allowed-methods: GET,POST,PUT,DELETE,OPTIONS
+  allow-credentials: true
+  max-age: 3600
diff --git a/backend/bootstrap/src/test/java/com/lc/bootstrap/interceptor/TenantInterceptorTest.java b/backend/bootstrap/src/test/java/com/lc/bootstrap/interceptor/TenantInterceptorTest.java
new file mode 100644
index 00000000..5be82288
--- /dev/null
+++ b/backend/bootstrap/src/test/java/com/lc/bootstrap/interceptor/TenantInterceptorTest.java
@@ -0,0 +1,157 @@
+package com.lc.bootstrap.interceptor;
+
+import com.lc.common.annotation.TenantCheck;
+import com.lc.common.context.UserContext;
+import com.lc.common.exception.BusinessException;
+import com.lc.common.exception.GlobalErrorCode;
+import jakarta.servlet.http.HttpServletRequest;
+import jakarta.servlet.http.HttpServletResponse;
+import org.junit.jupiter.api.*;
+import org.springframework.web.method.HandlerMethod;
+import org.springframework.web.servlet.HandlerMapping;
+
+import java.util.Map;
+
+import static org.junit.jupiter.api.Assertions.*;
+import static org.mockito.Mockito.*;
+
+class TenantInterceptorTest {
+
+    private TenantInterceptor interceptor;
+
+    @BeforeEach
+    void setUp() {
+        interceptor = new TenantInterceptor();
+    }
+
+    @AfterEach
+    void tearDown() {
+        UserContext.clear();
+    }
+
+    @Test
+    @DisplayName("非HandlerMethod类型直接放行")
+    void shouldPassWhenNotHandlerMethod() {
+        HttpServletRequest request = mock(HttpServletRequest.class);
+        HttpServletResponse response = mock(HttpServletResponse.class);
+        Object handler = new Object();
+        assertTrue(interceptor.preHandle(request, response, handler));
+    }
+
+    @Test
+    @DisplayName("方法无@TenantCheck注解时放行")
+    void shouldPassWhenNoTenantCheckAnnotation() throws NoSuchMethodException {
+        HttpServletRequest request = mock(HttpServletRequest.class);
+        HttpServletResponse response = mock(HttpServletResponse.class);
+        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "noAnnotation");
+        assertTrue(interceptor.preHandle(request, response, handlerMethod));
+    }
+
+    @Test
+    @DisplayName("当前用户tenantId为null时放行（超级管理员）")
+    void shouldPassWhenTenantIdIsNull() throws NoSuchMethodException {
+        UserContext.set(UserContext.builder().userId(1L).tenantId(null).username("admin").build());
+        HttpServletRequest request = mock(HttpServletRequest.class);
+        HttpServletResponse response = mock(HttpServletResponse.class);
+        when(request.getParameter("tenantId")).thenReturn("999");
+        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "withTenantCheck");
+        assertTrue(interceptor.preHandle(request, response, handlerMethod));
+    }
+
+    @Test
+    @DisplayName("请求参数中无tenantId时放行")
+    void shouldPassWhenNoTenantIdParam() throws NoSuchMethodException {
+        UserContext.set(UserContext.builder().userId(1L).tenantId(1L).username("user").build());
+        HttpServletRequest request = mock(HttpServletRequest.class);
+        HttpServletResponse response = mock(HttpServletResponse.class);
+        when(request.getParameter("tenantId")).thenReturn(null);
+        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "withTenantCheck");
+        assertTrue(interceptor.preHandle(request, response, handlerMethod));
+    }
+
+    @Test
+    @DisplayName("tenantId一致时放行")
+    void shouldPassWhenTenantIdMatches() throws NoSuchMethodException {
+        UserContext.set(UserContext.builder().userId(1L).tenantId(1L).username("user").build());
+        HttpServletRequest request = mock(HttpServletRequest.class);
+        HttpServletResponse response = mock(HttpServletResponse.class);
+        when(request.getParameter("tenantId")).thenReturn("1");
+        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "withTenantCheck");
+        assertTrue(interceptor.preHandle(request, response, handlerMethod));
+    }
+
+    @Test
+    @DisplayName("tenantId不一致时抛出PERMISSION_DENIED")
+    void shouldThrowWhenTenantIdMismatch() throws NoSuchMethodException {
+        UserContext.set(UserContext.builder().userId(1L).tenantId(1L).username("user").build());
+        HttpServletRequest request = mock(HttpServletRequest.class);
+        HttpServletResponse response = mock(HttpServletResponse.class);
+        when(request.getParameter("tenantId")).thenReturn("999");
+        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "withTenantCheck");
+        BusinessException ex = assertThrows(BusinessException.class, () ->
+            interceptor.preHandle(request, response, handlerMethod));
+        assertEquals(GlobalErrorCode.PERMISSION_DENIED.getCode(), ex.getCode());
+    }
+
+    @Test
+    @DisplayName("query parameter缺失时从path variable提取且一致时放行")
+    void shouldPassWhenPathVariableMatches() throws NoSuchMethodException {
+        UserContext.set(UserContext.builder().userId(1L).tenantId(1L).username("user").build());
+        HttpServletRequest request = mock(HttpServletRequest.class);
+        HttpServletResponse response = mock(HttpServletResponse.class);
+        when(request.getParameter("tenantId")).thenReturn(null);
+        when(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
+            .thenReturn(Map.of("tenantId", "1"));
+        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "withTenantCheck");
+        assertTrue(interceptor.preHandle(request, response, handlerMethod));
+    }
+
+    @Test
+    @DisplayName("query parameter缺失时从path variable提取且不一致时抛PERMISSION_DENIED")
+    void shouldThrowWhenPathVariableMismatch() throws NoSuchMethodException {
+        UserContext.set(UserContext.builder().userId(1L).tenantId(1L).username("user").build());
+        HttpServletRequest request = mock(HttpServletRequest.class);
+        HttpServletResponse response = mock(HttpServletResponse.class);
+        when(request.getParameter("tenantId")).thenReturn(null);
+        when(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
+            .thenReturn(Map.of("tenantId", "999"));
+        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "withTenantCheck");
+        BusinessException ex = assertThrows(BusinessException.class, () ->
+            interceptor.preHandle(request, response, handlerMethod));
+        assertEquals(GlobalErrorCode.PERMISSION_DENIED.getCode(), ex.getCode());
+    }
+
+    @Test
+    @DisplayName("query parameter与path variable均缺失时放行")
+    void shouldPassWhenBothQueryAndPathVariableAbsent() throws NoSuchMethodException {
+        UserContext.set(UserContext.builder().userId(1L).tenantId(1L).username("user").build());
+        HttpServletRequest request = mock(HttpServletRequest.class);
+        HttpServletResponse response = mock(HttpServletResponse.class);
+        when(request.getParameter("tenantId")).thenReturn(null);
+        when(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
+            .thenReturn(null);
+        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "withTenantCheck");
+        assertTrue(interceptor.preHandle(request, response, handlerMethod));
+    }
+
+    @Test
+    @DisplayName("query parameter优先级高于path variable")
+    void shouldPreferQueryParameterOverPathVariable() throws NoSuchMethodException {
+        UserContext.set(UserContext.builder().userId(1L).tenantId(1L).username("user").build());
+        HttpServletRequest request = mock(HttpServletRequest.class);
+        HttpServletResponse response = mock(HttpServletResponse.class);
+        when(request.getParameter("tenantId")).thenReturn("1");
+        when(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
+            .thenReturn(Map.of("tenantId", "999"));
+        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "withTenantCheck");
+        assertTrue(interceptor.preHandle(request, response, handlerMethod));
+    }
+
+    // 测试用Controller
+    static class TestController {
+        public void noAnnotation() {}
+
+        @TenantCheck(tenantIdParam = "tenantId")
+        public void withTenantCheck() {}
+    }
+}
diff --git a/backend/bootstrap/target/classes/application.yml b/backend/bootstrap/target/classes/application.yml
deleted file mode 100644
index c988427d..00000000
--- a/backend/bootstrap/target/classes/application.yml
+++ /dev/null
@@ -1,27 +0,0 @@
-server:
-  port: 8080
-
-spring:
-  datasource:
-    url: jdbc:mysql://localhost:3306/lc_platform?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
-    username: root
-    password: password
-    driver-class-name: com.mysql.cj.jdbc.Driver
-  data:
-    redis:
-      host: localhost
-      port: 6379
-  flyway:
-    enabled: true
-    locations: classpath:db/migration
-    baseline-on-migrate: true
-
-jwt:
-  secret: your-256-bit-secret-key-here-must-be-at-least-32-characters
-  access-token-expire-minutes: 15
-  refresh-token-expire-days: 7
-
-logging:
-  level:
-    com.lc: DEBUG
-    org.springframework.security: INFO
diff --git a/backend/bootstrap/target/classes/com/lc/bootstrap/Application.class b/backend/bootstrap/target/classes/com/lc/bootstrap/Application.class
deleted file mode 100644
index 7042b3e8..00000000
Binary files a/backend/bootstrap/target/classes/com/lc/bootstrap/Application.class and /dev/null differ
diff --git a/backend/bootstrap/target/classes/com/lc/bootstrap/config/SecurityConfig.class b/backend/bootstrap/target/classes/com/lc/bootstrap/config/SecurityConfig.class
deleted file mode 100644
index 41a5fec4..00000000
Binary files a/backend/bootstrap/target/classes/com/lc/bootstrap/config/SecurityConfig.class and /dev/null differ
diff --git a/backend/bootstrap/target/classes/com/lc/bootstrap/config/WebConfig.class b/backend/bootstrap/target/classes/com/lc/bootstrap/config/WebConfig.class
deleted file mode 100644
index 04b7b0f1..00000000
Binary files a/backend/bootstrap/target/classes/com/lc/bootstrap/config/WebConfig.class and /dev/null differ
diff --git a/backend/bootstrap/target/classes/com/lc/bootstrap/controller/AuthController.class b/backend/bootstrap/target/classes/com/lc/bootstrap/controller/AuthController.class
deleted file mode 100644
index 22e8af57..00000000
Binary files a/backend/bootstrap/target/classes/com/lc/bootstrap/controller/AuthController.class and /dev/null differ
diff --git a/backend/bootstrap/target/classes/com/lc/bootstrap/filter/JwtAuthenticationFilter.class b/backend/bootstrap/target/classes/com/lc/bootstrap/filter/JwtAuthenticationFilter.class
deleted file mode 100644
index 8fa8bb6a..00000000
Binary files a/backend/bootstrap/target/classes/com/lc/bootstrap/filter/JwtAuthenticationFilter.class and /dev/null differ
diff --git a/backend/bootstrap/target/classes/com/lc/bootstrap/handler/GlobalExceptionHandler.class b/backend/bootstrap/target/classes/com/lc/bootstrap/handler/GlobalExceptionHandler.class
deleted file mode 100644
index 6b0331df..00000000
Binary files a/backend/bootstrap/target/classes/com/lc/bootstrap/handler/GlobalExceptionHandler.class and /dev/null differ
diff --git a/backend/bootstrap/target/maven-status/maven-compiler-plugin/compile/default-compile/createdFiles.lst b/backend/bootstrap/target/maven-status/maven-compiler-plugin/compile/default-compile/createdFiles.lst
deleted file mode 100644
index 0dad19e9..00000000
--- a/backend/bootstrap/target/maven-status/maven-compiler-plugin/compile/default-compile/createdFiles.lst
+++ /dev/null
@@ -1,6 +0,0 @@
-com/lc/bootstrap/Application.class
-com/lc/bootstrap/filter/JwtAuthenticationFilter.class
-com/lc/bootstrap/config/SecurityConfig.class
-com/lc/bootstrap/config/WebConfig.class
-com/lc/bootstrap/handler/GlobalExceptionHandler.class
-com/lc/bootstrap/controller/AuthController.class
diff --git a/backend/bootstrap/target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst b/backend/bootstrap/target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst
deleted file mode 100644
index 2458d40e..00000000
--- a/backend/bootstrap/target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst
+++ /dev/null
@@ -1,6 +0,0 @@
-/workspace/backend/bootstrap/src/main/java/com/lc/bootstrap/filter/JwtAuthenticationFilter.java
-/workspace/backend/bootstrap/src/main/java/com/lc/bootstrap/config/SecurityConfig.java
-/workspace/backend/bootstrap/src/main/java/com/lc/bootstrap/controller/AuthController.java
-/workspace/backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebConfig.java
-/workspace/backend/bootstrap/src/main/java/com/lc/bootstrap/handler/GlobalExceptionHandler.java
-/workspace/backend/bootstrap/src/main/java/com/lc/bootstrap/Application.java
diff --git a/backend/common/pom.xml b/backend/common/pom.xml
index a6984440..67031673 100644
--- a/backend/common/pom.xml
+++ b/backend/common/pom.xml
@@ -22,10 +22,14 @@
         </dependency>
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-validation</artifactId>
         </dependency>
+        <dependency>
+            <groupId>org.springframework</groupId>
+            <artifactId>spring-web</artifactId>
+        </dependency>
         <dependency>
             <groupId>org.projectlombok</groupId>
             <artifactId>lombok</artifactId>
             <optional>true</optional>
         </dependency>
@@ -35,7 +39,13 @@
         </dependency>
         <dependency>
             <groupId>org.springframework.security</groupId>
             <artifactId>spring-security-crypto</artifactId>
         </dependency>
+        <dependency>
+            <groupId>io.minio</groupId>
+            <artifactId>minio</artifactId>
+            <version>8.5.9</version>
+            <optional>true</optional>
+        </dependency>
     </dependencies>
 </project>
\ No newline at end of file
diff --git a/backend/common/src/main/java/com/lc/common/annotation/AuditLog.java b/backend/common/src/main/java/com/lc/common/annotation/AuditLog.java
new file mode 100644
index 00000000..463cb55a
--- /dev/null
+++ b/backend/common/src/main/java/com/lc/common/annotation/AuditLog.java
@@ -0,0 +1,24 @@
+package com.lc.common.annotation;
+
+import java.lang.annotation.*;
+
+/**
+ * 审计日志注解，标注在 Controller 方法上，由 AOP 切面自动记录审计日志。
+ * 操作结果（成功/失败）、操作人、IP、UserAgent 由切面自动填充。
+ */
+@Target(ElementType.METHOD)
+@Retention(RetentionPolicy.RUNTIME)
+@Documented
+public @interface AuditLog {
+    /** 操作动作，如 "用户登录"、"创建项目" */
+    String action();
+
+    /** 资源类型，如 "USER"、"PROJECT"、"ROLE" */
+    String resourceType();
+
+    /**
+     * 资源ID参数名，从方法参数中按名取值，支持 SpEL 表达式 #id。
+     * 若不指定或为空字符串，则不记录 resource_id。
+     */
+    String resourceIdParam() default "";
+}
diff --git a/backend/common/src/main/java/com/lc/common/annotation/PreAuthorize.java b/backend/common/src/main/java/com/lc/common/annotation/PreAuthorize.java
new file mode 100644
index 00000000..0ce7290d
--- /dev/null
+++ b/backend/common/src/main/java/com/lc/common/annotation/PreAuthorize.java
@@ -0,0 +1,23 @@
+package com.lc.common.annotation;
+
+import java.lang.annotation.*;
+
+/**
+ * 接口权限校验注解。
+ * <p>
+ * 由 {@code PermissionInterceptor} 在请求进入 Controller 前解析：
+ * <ul>
+ *   <li>{@link #value()} 为逗号分隔的权限码列表，对应 {@code PermissionService#getUserPermissions} 返回的权限集合</li>
+ *   <li>{@link #requireAll()} 为 true 时要求用户持有全部权限；为 false（默认）时持有任一即可</li>
+ * </ul>
+ * </p>
+ */
+@Target({ElementType.METHOD, ElementType.TYPE})
+@Retention(RetentionPolicy.RUNTIME)
+@Documented
+public @interface PreAuthorize {
+    /** 所需权限码，多个用逗号分隔 */
+    String value() default "";
+    /** true=需全部权限，false=任一权限即可 */
+    boolean requireAll() default false;
+}
diff --git a/backend/common/src/main/java/com/lc/common/annotation/TenantCheck.java b/backend/common/src/main/java/com/lc/common/annotation/TenantCheck.java
new file mode 100644
index 00000000..acb74df3
--- /dev/null
+++ b/backend/common/src/main/java/com/lc/common/annotation/TenantCheck.java
@@ -0,0 +1,16 @@
+package com.lc.common.annotation;
+
+import java.lang.annotation.*;
+
+/**
+ * 标注需要租户校验的方法/类。
+ * 拦截器会从请求参数中提取 {@link #tenantIdParam()} 指定的字段，
+ * 与当前登录用户的租户ID进行比较，不一致则抛出 PERMISSION_DENIED。
+ */
+@Target({ElementType.METHOD, ElementType.TYPE})
+@Retention(RetentionPolicy.RUNTIME)
+@Documented
+public @interface TenantCheck {
+    /** 请求参数中租户ID的字段名，默认为 tenantId */
+    String tenantIdParam() default "tenantId";
+}
diff --git a/backend/common/src/main/java/com/lc/common/context/UserContext.java b/backend/common/src/main/java/com/lc/common/context/UserContext.java
new file mode 100644
index 00000000..dd29ee9c
--- /dev/null
+++ b/backend/common/src/main/java/com/lc/common/context/UserContext.java
@@ -0,0 +1,45 @@
+package com.lc.common.context;
+
+import lombok.AllArgsConstructor;
+import lombok.Builder;
+import lombok.Data;
+import lombok.NoArgsConstructor;
+
+@Data
+@Builder
+@NoArgsConstructor
+@AllArgsConstructor
+public class UserContext {
+    private Long userId;
+    private Long tenantId;
+    private String username;
+
+    private static final ThreadLocal<UserContext> HOLDER = new ThreadLocal<>();
+
+    public static void set(UserContext context) {
+        HOLDER.set(context);
+    }
+
+    public static UserContext get() {
+        return HOLDER.get();
+    }
+
+    public static void clear() {
+        HOLDER.remove();
+    }
+
+    public static Long getUserId() {
+        UserContext ctx = HOLDER.get();
+        return ctx != null ? ctx.userId : null;
+    }
+
+    public static Long getTenantId() {
+        UserContext ctx = HOLDER.get();
+        return ctx != null ? ctx.tenantId : null;
+    }
+
+    public static String getUsername() {
+        UserContext ctx = HOLDER.get();
+        return ctx != null ? ctx.username : null;
+    }
+}
diff --git a/backend/common/src/main/java/com/lc/common/security/FileUploadValidator.java b/backend/common/src/main/java/com/lc/common/security/FileUploadValidator.java
new file mode 100644
index 00000000..f0d58191
--- /dev/null
+++ b/backend/common/src/main/java/com/lc/common/security/FileUploadValidator.java
@@ -0,0 +1,105 @@
+package com.lc.common.security;
+
+import com.lc.common.exception.BusinessException;
+import com.lc.common.exception.GlobalErrorCode;
+import com.lc.common.storage.StorageProperties;
+import lombok.RequiredArgsConstructor;
+import lombok.extern.slf4j.Slf4j;
+import org.springframework.stereotype.Component;
+import org.springframework.web.multipart.MultipartFile;
+
+import java.util.Set;
+
+/**
+ * 文件上传校验器，统一封装 MIME 白名单、大小上限与文件名安全校验。
+ * <p>
+ * 通用 {@link #validate(MultipartFile, Set)} 允许调用方传入自定义白名单；
+ * {@link #validateImage(MultipartFile)} 与 {@link #validateDocument(MultipartFile)}
+ * 分别使用预定义的图片/文档白名单；{@link #validateGeneral(MultipartFile)}
+ * 使用图片与文档白名单的并集，适合通用上传场景。
+ */
+@Slf4j
+@Component
+@RequiredArgsConstructor
+public class FileUploadValidator {
+
+    /** 图片 MIME 白名单 */
+    public static final Set<String> IMAGE_TYPES = Set.of(
+            "image/jpeg", "image/png", "image/gif", "image/webp", "image/svg+xml"
+    );
+
+    /** 文档 MIME 白名单 */
+    public static final Set<String> DOC_TYPES = Set.of(
+            "application/pdf",
+            "text/plain",
+            "application/json",
+            "application/zip",
+            "application/x-zip-compressed",
+            "application/msword",
+            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
+            "application/vnd.ms-excel",
+            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
+            "application/vnd.ms-powerpoint",
+            "application/vnd.openxmlformats-officedocument.presentationml.presentation"
+    );
+
+    /** 通用白名单：图片与文档的并集 */
+    private static final Set<String> GENERAL_TYPES = mergeSets(IMAGE_TYPES, DOC_TYPES);
+
+    private final StorageProperties storageProperties;
+
+    /**
+     * 校验图片文件：仅允许 image/* 类型。
+     */
+    public void validateImage(MultipartFile file) {
+        validate(file, IMAGE_TYPES);
+    }
+
+    /**
+     * 校验文档文件：允许 pdf/txt/json/zip/doc/docx/xls/xlsx/ppt/pptx 等。
+     */
+    public void validateDocument(MultipartFile file) {
+        validate(file, DOC_TYPES);
+    }
+
+    /**
+     * 通用校验：使用图片与文档并集白名单。
+     */
+    public void validateGeneral(MultipartFile file) {
+        validate(file, GENERAL_TYPES);
+    }
+
+    /**
+     * 通用校验：白名单 + 大小 + 文件名安全（无路径遍历）。
+     *
+     * @param file         待校验文件
+     * @param allowedTypes 允许的 Content-Type 集合
+     */
+    public void validate(MultipartFile file, Set<String> allowedTypes) {
+        if (file == null || file.isEmpty()) {
+            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "文件不能为空");
+        }
+        if (file.getSize() > storageProperties.getMaxFileSize()) {
+            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "文件大小超过限制");
+        }
+
+        String contentType = file.getContentType();
+        if (contentType == null || !allowedTypes.contains(contentType.toLowerCase())) {
+            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(),
+                    "不支持的文件类型: " + contentType);
+        }
+
+        String originalName = file.getOriginalFilename();
+        if (originalName != null
+                && (originalName.contains("..") || originalName.contains("/") || originalName.contains("\\"))) {
+            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "文件名非法");
+        }
+    }
+
+    private static Set<String> mergeSets(Set<String> a, Set<String> b) {
+        Set<String> merged = new java.util.HashSet<>(a.size() + b.size());
+        merged.addAll(a);
+        merged.addAll(b);
+        return Set.copyOf(merged);
+    }
+}
diff --git a/backend/common/src/main/java/com/lc/common/security/SsrfProtector.java b/backend/common/src/main/java/com/lc/common/security/SsrfProtector.java
new file mode 100644
index 00000000..bb3d4c73
--- /dev/null
+++ b/backend/common/src/main/java/com/lc/common/security/SsrfProtector.java
@@ -0,0 +1,154 @@
+package com.lc.common.security;
+
+import com.lc.common.exception.BusinessException;
+import com.lc.common.exception.GlobalErrorCode;
+import lombok.extern.slf4j.Slf4j;
+import org.springframework.stereotype.Component;
+
+import java.net.InetAddress;
+import java.net.URI;
+import java.net.URISyntaxException;
+import java.net.UnknownHostException;
+
+/**
+ * SSRF（服务端请求伪造）防护工具。
+ * <p>
+ * 在服务端发起外网请求前，通过 {@link #validateUrl(String)} 或 {@link #isSafeUrl(String)}
+ * 校验目标 URL：禁止协议非法、指向内网/回环/链路本地/保留地址的目标，并对 host 做 DNS
+ * 解析后再次检查解析结果，避免使用域名绕过 IP 段校验。
+ */
+@Slf4j
+@Component
+public class SsrfProtector {
+
+    /**
+     * 校验 URL 是否安全（非内网、协议合法）。
+     *
+     * @param url 待校验的 URL
+     * @throws BusinessException 当 URL 指向内网或协议非法时
+     */
+    public void validateUrl(String url) {
+        String host = doCheck(url);
+        if (host != null) {
+            throw new BusinessException(GlobalErrorCode.PERMISSION_DENIED.getCode(),
+                    "URL 不允许访问: " + host);
+        }
+    }
+
+    /**
+     * 判断 URL 是否安全（不抛异常，返回 boolean）。
+     *
+     * @param url 待校验的 URL
+     * @return true 表示安全，false 表示不安全或校验异常
+     */
+    public boolean isSafeUrl(String url) {
+        try {
+            return doCheck(url) == null;
+        } catch (Exception e) {
+            log.debug("SSRF isSafeUrl check failed for url={}", url, e);
+            return false;
+        }
+    }
+
+    /**
+     * 执行校验逻辑。
+     *
+     * @return null 表示安全；非 null 表示不安全，返回值为拒绝原因中使用的 host
+     */
+    private String doCheck(String url) {
+        if (url == null || url.isBlank()) {
+            return "";
+        }
+
+        URI uri;
+        try {
+            uri = new URI(url);
+        } catch (URISyntaxException e) {
+            log.debug("SSRF check: invalid url syntax {}", url);
+            return "";
+        }
+
+        String scheme = uri.getScheme();
+        if (scheme == null
+                || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) {
+            log.debug("SSRF check: illegal scheme {}", scheme);
+            return "";
+        }
+
+        String host = uri.getHost();
+        if (host == null || host.isBlank()) {
+            return "";
+        }
+        String lowerHost = host.toLowerCase();
+
+        // 1. 基于字符串的内网/保留地址判定
+        if (isReservedHost(lowerHost)) {
+            return host;
+        }
+
+        // 2. DNS 解析后再判定，防止用域名绕过
+        InetAddress[] addresses;
+        try {
+            addresses = InetAddress.getAllByName(host);
+        } catch (UnknownHostException e) {
+            // DNS 解析失败：拒绝，避免解析失败绕过
+            log.debug("SSRF check: DNS resolution failed for {}", host);
+            return host;
+        }
+        for (InetAddress addr : addresses) {
+            if (addr.isSiteLocalAddress() || addr.isLoopbackAddress()
+                    || addr.isLinkLocalAddress() || addr.isAnyLocalAddress()) {
+                return host;
+            }
+        }
+
+        return null;
+    }
+
+    /**
+     * 基于字符串判定 host 是否属于内网/回环/链路本地/保留地址。
+     */
+    private boolean isReservedHost(String host) {
+        if ("localhost".equals(host) || host.endsWith(".localhost")) {
+            return true;
+        }
+        if ("127.0.0.1".equals(host) || host.startsWith("127.")) {
+            return true;
+        }
+        if (host.startsWith("10.")) {
+            return true;
+        }
+        if (host.startsWith("192.168.")) {
+            return true;
+        }
+        if (host.startsWith("169.254.")) {
+            return true;
+        }
+        if ("0.0.0.0".equals(host)) {
+            return true;
+        }
+        // IPv6 回环
+        if ("::1".equals(host) || "[::1]".equals(host)) {
+            return true;
+        }
+        // IPv6 ULA（fc00::/7）
+        if (host.startsWith("fc") || host.startsWith("fd")) {
+            return true;
+        }
+        // 172.16.0.0 ~ 172.31.255.255
+        if (host.startsWith("172.")) {
+            String[] parts = host.split("\\.");
+            if (parts.length >= 2) {
+                try {
+                    int second = Integer.parseInt(parts[1]);
+                    if (second >= 16 && second <= 31) {
+                        return true;
+                    }
+                } catch (NumberFormatException ignore) {
+                    // 非 IPv4 数字段，交给 DNS 解析判定
+                }
+            }
+        }
+        return false;
+    }
+}
diff --git a/backend/common/src/main/java/com/lc/common/storage/LocalStorageServiceImpl.java b/backend/common/src/main/java/com/lc/common/storage/LocalStorageServiceImpl.java
new file mode 100644
index 00000000..634cc7c5
--- /dev/null
+++ b/backend/common/src/main/java/com/lc/common/storage/LocalStorageServiceImpl.java
@@ -0,0 +1,86 @@
+package com.lc.common.storage;
+
+import com.lc.common.exception.BusinessException;
+import com.lc.common.exception.GlobalErrorCode;
+import lombok.extern.slf4j.Slf4j;
+import org.springframework.util.FileCopyUtils;
+
+import java.io.IOException;
+import java.io.InputStream;
+import java.nio.file.Files;
+import java.nio.file.Path;
+import java.nio.file.Paths;
+
+/**
+ * 本地文件系统存储实现。
+ * 文件保存在 {localPath}/{bucket}/{key} 路径下。
+ */
+@Slf4j
+public class LocalStorageServiceImpl implements StorageService {
+
+    private final StorageProperties properties;
+
+    public LocalStorageServiceImpl(StorageProperties properties) {
+        this.properties = properties;
+    }
+
+    @Override
+    public String upload(String bucket, String key, InputStream input, String contentType, long size) {
+        try {
+            Path filePath = resolvePath(bucket, key);
+            Files.createDirectories(filePath.getParent());
+            FileCopyUtils.copy(input, Files.newOutputStream(filePath));
+            log.debug("Local storage uploaded: {}/{}", bucket, key);
+            return key;
+        } catch (IOException e) {
+            log.error("Local storage upload failed: {}/{}", bucket, key, e);
+            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
+        }
+    }
+
+    @Override
+    public InputStream download(String bucket, String key) {
+        try {
+            Path filePath = resolvePath(bucket, key);
+            if (!Files.exists(filePath)) {
+                throw new BusinessException(GlobalErrorCode.NOT_FOUND);
+            }
+            return Files.newInputStream(filePath);
+        } catch (IOException e) {
+            log.error("Local storage download failed: {}/{}", bucket, key, e);
+            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
+        }
+    }
+
+    @Override
+    public void delete(String bucket, String key) {
+        try {
+            Path filePath = resolvePath(bucket, key);
+            Files.deleteIfExists(filePath);
+            log.debug("Local storage deleted: {}/{}", bucket, key);
+        } catch (IOException e) {
+            log.error("Local storage delete failed: {}/{}", bucket, key, e);
+            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
+        }
+    }
+
+    @Override
+    public String getPresignedUrl(String bucket, String key, long expireSeconds) {
+        return "/api/files/" + bucket + "/" + key;
+    }
+
+    @Override
+    public boolean exists(String bucket, String key) {
+        return Files.exists(resolvePath(bucket, key));
+    }
+
+    private Path resolvePath(String bucket, String key) {
+        Path base = Paths.get(properties.getLocalPath()).toAbsolutePath().normalize();
+        Path target = base.resolve(bucket).resolve(key).normalize();
+        if (!target.startsWith(base)) {
+            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(),
+                    "Invalid path: path traversal detected");
+        }
+        return target;
+    }
+}
diff --git a/backend/common/src/main/java/com/lc/common/storage/MinioStorageServiceImpl.java b/backend/common/src/main/java/com/lc/common/storage/MinioStorageServiceImpl.java
new file mode 100644
index 00000000..f994c9f9
--- /dev/null
+++ b/backend/common/src/main/java/com/lc/common/storage/MinioStorageServiceImpl.java
@@ -0,0 +1,123 @@
+package com.lc.common.storage;
+
+import com.lc.common.exception.BusinessException;
+import com.lc.common.exception.GlobalErrorCode;
+import io.minio.BucketExistsArgs;
+import io.minio.GetObjectArgs;
+import io.minio.GetObjectResponse;
+import io.minio.MakeBucketArgs;
+import io.minio.MinioClient;
+import io.minio.PutObjectArgs;
+import io.minio.RemoveObjectArgs;
+import io.minio.StatObjectArgs;
+import io.minio.StatObjectResponse;
+import io.minio.errors.ErrorResponseException;
+import lombok.extern.slf4j.Slf4j;
+
+import java.io.InputStream;
+
+/**
+ * MinIO 对象存储实现。
+ * 通过 MinIO Java SDK 操作对象存储。
+ */
+@Slf4j
+public class MinioStorageServiceImpl implements StorageService {
+
+    private final MinioClient minioClient;
+
+    public MinioStorageServiceImpl(StorageProperties properties) {
+        this.minioClient = MinioClient.builder()
+                .endpoint(properties.getEndpoint())
+                .credentials(properties.getAccessKey(), properties.getSecretKey())
+                .build();
+    }
+
+    @Override
+    public String upload(String bucket, String key, InputStream input, String contentType, long size) {
+        try {
+            ensureBucket(bucket);
+            minioClient.putObject(PutObjectArgs.builder()
+                    .bucket(bucket)
+                    .object(key)
+                    .stream(input, size, -1)
+                    .contentType(contentType)
+                    .build());
+            log.debug("MinIO uploaded: {}/{}", bucket, key);
+            return key;
+        } catch (Exception e) {
+            log.error("MinIO upload failed: {}/{}", bucket, key, e);
+            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
+        }
+    }
+
+    @Override
+    public InputStream download(String bucket, String key) {
+        try {
+            GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
+                    .bucket(bucket)
+                    .object(key)
+                    .build());
+            return response;
+        } catch (ErrorResponseException e) {
+            log.warn("MinIO object not found: {}/{}", bucket, key);
+            throw new BusinessException(GlobalErrorCode.NOT_FOUND);
+        } catch (Exception e) {
+            log.error("MinIO download failed: {}/{}", bucket, key, e);
+            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
+        }
+    }
+
+    @Override
+    public void delete(String bucket, String key) {
+        try {
+            minioClient.removeObject(RemoveObjectArgs.builder()
+                    .bucket(bucket)
+                    .object(key)
+                    .build());
+            log.debug("MinIO deleted: {}/{}", bucket, key);
+        } catch (Exception e) {
+            log.error("MinIO delete failed: {}/{}", bucket, key, e);
+            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
+        }
+    }
+
+    @Override
+    public String getPresignedUrl(String bucket, String key, long expireSeconds) {
+        try {
+            return minioClient.getPresignedObjectUrl(
+                    io.minio.GetPresignedObjectUrlArgs.builder()
+                            .method(io.minio.http.Method.GET)
+                            .bucket(bucket)
+                            .object(key)
+                            .expiry((int) expireSeconds)
+                            .build());
+        } catch (Exception e) {
+            log.error("MinIO getPresignedUrl failed: {}/{}", bucket, key, e);
+            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
+        }
+    }
+
+    @Override
+    public boolean exists(String bucket, String key) {
+        try {
+            StatObjectResponse response = minioClient.statObject(StatObjectArgs.builder()
+                    .bucket(bucket)
+                    .object(key)
+                    .build());
+            return response != null;
+        } catch (ErrorResponseException e) {
+            return false;
+        } catch (Exception e) {
+            log.error("MinIO exists check failed: {}/{}", bucket, key, e);
+            return false;
+        }
+    }
+
+    private void ensureBucket(String bucket) throws Exception {
+        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
+        if (!exists) {
+            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
+            log.info("MinIO bucket created: {}", bucket);
+        }
+    }
+}
diff --git a/backend/common/src/main/java/com/lc/common/storage/StorageProperties.java b/backend/common/src/main/java/com/lc/common/storage/StorageProperties.java
new file mode 100644
index 00000000..ac511f49
--- /dev/null
+++ b/backend/common/src/main/java/com/lc/common/storage/StorageProperties.java
@@ -0,0 +1,48 @@
+package com.lc.common.storage;
+
+import lombok.Data;
+import org.springframework.boot.context.properties.ConfigurationProperties;
+
+/**
+ * 对象存储配置属性。
+ * 通过 storage.type 配置切换本地实现或 MinIO 实现。
+ */
+@Data
+@ConfigurationProperties(prefix = "storage")
+public class StorageProperties {
+
+    /**
+     * 存储类型：local | minio
+     */
+    private String type = "local";
+
+    /**
+     * 本地存储根路径
+     */
+    private String localPath = "./storage";
+
+    /**
+     * MinIO 服务端点
+     */
+    private String endpoint;
+
+    /**
+     * MinIO 访问密钥
+     */
+    private String accessKey;
+
+    /**
+     * MinIO 秘密密钥
+     */
+    private String secretKey;
+
+    /**
+     * 默认存储桶
+     */
+    private String defaultBucket = "lc-platform";
+
+    /**
+     * 文件大小上限（字节），默认 10MB
+     */
+    private long maxFileSize = 10485760L;
+}
diff --git a/backend/common/src/main/java/com/lc/common/storage/StorageService.java b/backend/common/src/main/java/com/lc/common/storage/StorageService.java
new file mode 100644
index 00000000..87f69449
--- /dev/null
+++ b/backend/common/src/main/java/com/lc/common/storage/StorageService.java
@@ -0,0 +1,58 @@
+package com.lc.common.storage;
+
+import java.io.InputStream;
+
+/**
+ * 对象存储抽象接口，统一上传/下载/删除/预签名URL。
+ * 通过 storage.type 配置切换本地实现或 MinIO 实现。
+ */
+public interface StorageService {
+
+    /**
+     * 上传文件
+     *
+     * @param bucket      存储桶
+     * @param key         对象 key
+     * @param input       输入流
+     * @param contentType 内容类型
+     * @param size        文件大小（字节）
+     * @return 上传后的对象 key
+     */
+    String upload(String bucket, String key, InputStream input, String contentType, long size);
+
+    /**
+     * 下载文件
+     *
+     * @param bucket 存储桶
+     * @param key    对象 key
+     * @return 文件输入流
+     */
+    InputStream download(String bucket, String key);
+
+    /**
+     * 删除文件
+     *
+     * @param bucket 存储桶
+     * @param key    对象 key
+     */
+    void delete(String bucket, String key);
+
+    /**
+     * 获取预签名 URL
+     *
+     * @param bucket        存储桶
+     * @param key           对象 key
+     * @param expireSeconds 过期时间（秒）
+     * @return 预签名 URL
+     */
+    String getPresignedUrl(String bucket, String key, long expireSeconds);
+
+    /**
+     * 判断对象是否存在
+     *
+     * @param bucket 存储桶
+     * @param key    对象 key
+     * @return 是否存在
+     */
+    boolean exists(String bucket, String key);
+}
diff --git a/backend/common/src/main/java/com/lc/common/util/EncryptUtil.java b/backend/common/src/main/java/com/lc/common/util/EncryptUtil.java
new file mode 100644
index 00000000..be6468d3
--- /dev/null
+++ b/backend/common/src/main/java/com/lc/common/util/EncryptUtil.java
@@ -0,0 +1,78 @@
+package com.lc.common.util;
+
+import com.lc.common.exception.BusinessException;
+import com.lc.common.exception.GlobalErrorCode;
+import lombok.extern.slf4j.Slf4j;
+
+import javax.crypto.Cipher;
+import javax.crypto.spec.SecretKeySpec;
+import java.nio.charset.StandardCharsets;
+import java.util.Base64;
+
+/**
+ * AES 加解密工具。
+ * 算法：AES/ECB/PKCS5Padding
+ * 密钥长度必须为 16/24/32 字节。
+ */
+@Slf4j
+public class EncryptUtil {
+
+    private static final String ALGORITHM = "AES";
+    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
+
+    private EncryptUtil() {
+    }
+
+    /**
+     * 加密
+     *
+     * @param plainText 明文
+     * @param key       密钥（16/24/32 字节）
+     * @return Base64 编码的密文
+     */
+    public static String encrypt(String plainText, String key) {
+        validateKey(key);
+        try {
+            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
+            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
+            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
+            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
+            return Base64.getEncoder().encodeToString(encrypted);
+        } catch (Exception e) {
+            log.error("AES encrypt failed", e);
+            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
+        }
+    }
+
+    /**
+     * 解密
+     *
+     * @param cipherText Base64 编码的密文
+     * @param key        密钥（16/24/32 字节）
+     * @return 明文
+     */
+    public static String decrypt(String cipherText, String key) {
+        validateKey(key);
+        try {
+            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
+            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
+            cipher.init(Cipher.DECRYPT_MODE, keySpec);
+            byte[] decoded = Base64.getDecoder().decode(cipherText);
+            byte[] decrypted = cipher.doFinal(decoded);
+            return new String(decrypted, StandardCharsets.UTF_8);
+        } catch (Exception e) {
+            log.error("AES decrypt failed", e);
+            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
+        }
+    }
+
+    private static void validateKey(String key) {
+        if (key == null) {
+            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR);
+        }
+        int len = key.getBytes(StandardCharsets.UTF_8).length;
+        if (len != 16 && len != 24 && len != 32) {
+            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR);
+        }
+    }
+}
diff --git a/backend/common/target/classes/com/lc/common/config/RedisConfig.class b/backend/common/target/classes/com/lc/common/config/RedisConfig.class
deleted file mode 100644
index a75f0b77..00000000
Binary files a/backend/common/target/classes/com/lc/common/config/RedisConfig.class and /dev/null differ
diff --git a/backend/common/target/classes/com/lc/common/context/TenantContext.class b/backend/common/target/classes/com/lc/common/context/TenantContext.class
deleted file mode 100644
index 20d388c1..00000000
Binary files a/backend/common/target/classes/com/lc/common/context/TenantContext.class and /dev/null differ
diff --git a/backend/common/target/classes/com/lc/common/dto/PageResult$PageResultBuilder.class b/backend/common/target/classes/com/lc/common/dto/PageResult$PageResultBuilder.class
deleted file mode 100644
index cc023e26..00000000
Binary files a/backend/common/target/classes/com/lc/common/dto/PageResult$PageResultBuilder.class and /dev/null differ
diff --git a/backend/common/target/classes/com/lc/common/dto/PageResult.class b/backend/common/target/classes/com/lc/common/dto/PageResult.class
deleted file mode 100644
index 1c1b304f..00000000
Binary files a/backend/common/target/classes/com/lc/common/dto/PageResult.class and /dev/null differ
diff --git a/backend/common/target/classes/com/lc/common/dto/Result$ResultBuilder.class b/backend/common/target/classes/com/lc/common/dto/Result$ResultBuilder.class
deleted file mode 100644
index 398bb36b..00000000
Binary files a/backend/common/target/classes/com/lc/common/dto/Result$ResultBuilder.class and /dev/null differ
diff --git a/backend/common/target/classes/com/lc/common/dto/Result.class b/backend/common/target/classes/com/lc/common/dto/Result.class
deleted file mode 100644
index c6dcc3b2..00000000
Binary files a/backend/common/target/classes/com/lc/common/dto/Result.class and /dev/null differ
diff --git a/backend/common/target/classes/com/lc/common/exception/BusinessException.class b/backend/common/target/classes/com/lc/common/exception/BusinessException.class
deleted file mode 100644
index 83dddfb3..00000000
Binary files a/backend/common/target/classes/com/lc/common/exception/BusinessException.class and /dev/null differ
diff --git a/backend/common/target/classes/com/lc/common/exception/GlobalErrorCode.class b/backend/common/target/classes/com/lc/common/exception/GlobalErrorCode.class
deleted file mode 100644
index 6463e1bd..00000000
Binary files a/backend/common/target/classes/com/lc/common/exception/GlobalErrorCode.class and /dev/null differ
diff --git a/backend/common/target/classes/com/lc/common/util/PasswordUtil.class b/backend/common/target/classes/com/lc/common/util/PasswordUtil.class
deleted file mode 100644
index 4400dbf2..00000000
Binary files a/backend/common/target/classes/com/lc/common/util/PasswordUtil.class and /dev/null differ
diff --git a/backend/common/target/maven-status/maven-compiler-plugin/compile/default-compile/createdFiles.lst b/backend/common/target/maven-status/maven-compiler-plugin/compile/default-compile/createdFiles.lst
deleted file mode 100644
index a3a241bf..00000000
--- a/backend/common/target/maven-status/maven-compiler-plugin/compile/default-compile/createdFiles.lst
+++ /dev/null
@@ -1,9 +0,0 @@
-com/lc/common/dto/PageResult$PageResultBuilder.class
-com/lc/common/dto/Result.class
-com/lc/common/exception/GlobalErrorCode.class
-com/lc/common/dto/PageResult.class
-com/lc/common/config/RedisConfig.class
-com/lc/common/dto/Result$ResultBuilder.class
-com/lc/common/context/TenantContext.class
-com/lc/common/exception/BusinessException.class
-com/lc/common/util/PasswordUtil.class
diff --git a/backend/common/target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst b/backend/common/target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst
deleted file mode 100644
index e7a954d4..00000000
--- a/backend/common/target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst
+++ /dev/null
@@ -1,7 +0,0 @@
-/workspace/backend/common/src/main/java/com/lc/common/exception/GlobalErrorCode.java
-/workspace/backend/common/src/main/java/com/lc/common/dto/PageResult.java
-/workspace/backend/common/src/main/java/com/lc/common/dto/Result.java
-/workspace/backend/common/src/main/java/com/lc/common/util/PasswordUtil.java
-/workspace/backend/common/src/main/java/com/lc/common/context/TenantContext.java
-/workspace/backend/common/src/main/java/com/lc/common/config/RedisConfig.java
-/workspace/backend/common/src/main/java/com/lc/common/exception/BusinessException.java
diff --git a/backend/system-core/src/main/java/com/lc/system/controller/AuditLogController.java b/backend/system-core/src/main/java/com/lc/system/controller/AuditLogController.java
new file mode 100644
index 00000000..b1c19e79
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/controller/AuditLogController.java
@@ -0,0 +1,41 @@
+package com.lc.system.controller;
+
+import com.lc.common.annotation.AuditLog;
+import com.lc.common.annotation.PreAuthorize;
+import com.lc.common.context.UserContext;
+import com.lc.common.dto.PageResult;
+import com.lc.common.dto.Result;
+import com.lc.system.dto.AuditLogDTO;
+import com.lc.system.service.AuditLogService;
+import lombok.RequiredArgsConstructor;
+import org.springframework.web.bind.annotation.GetMapping;
+import org.springframework.web.bind.annotation.RequestMapping;
+import org.springframework.web.bind.annotation.RestController;
+
+/**
+ * 审计日志查询 REST 接口。
+ * <p>
+ * 普通用户只能查询本租户的审计日志；超级管理员（{@link UserContext#getTenantId()} 为 null）
+ * 可查询任意租户（按 request.tenantId 过滤）。
+ */
+@RestController
+@RequestMapping("/api/system/audit-logs")
+@RequiredArgsConstructor
+public class AuditLogController {
+
+    private final AuditLogService auditLogService;
+
+    @GetMapping
+    @PreAuthorize("audit:log:list")
+    @AuditLog(action = "查询审计日志", resourceType = "AUDIT_LOG")
+    public Result<PageResult<AuditLogDTO.Response>> list(AuditLogDTO.QueryRequest request) {
+        // 租户隔离：普通用户强制覆盖 tenantId 为当前用户租户
+        Long currentTenantId = UserContext.getTenantId();
+        if (currentTenantId != null) {
+            // 普通用户：强制只能查自己租户
+            request.setTenantId(currentTenantId);
+        }
+        // currentTenantId == null 表示超级管理员，允许查任意租户（按 request.tenantId 过滤）
+        return Result.success(auditLogService.pageQuery(request));
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/controller/FileController.java b/backend/system-core/src/main/java/com/lc/system/controller/FileController.java
new file mode 100644
index 00000000..e1b1739b
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/controller/FileController.java
@@ -0,0 +1,132 @@
+package com.lc.system.controller;
+
+import com.lc.common.context.UserContext;
+import com.lc.common.dto.Result;
+import com.lc.common.exception.BusinessException;
+import com.lc.common.exception.GlobalErrorCode;
+import com.lc.common.security.FileUploadValidator;
+import com.lc.common.storage.StorageProperties;
+import com.lc.common.storage.StorageService;
+import lombok.AllArgsConstructor;
+import lombok.Builder;
+import lombok.Data;
+import lombok.NoArgsConstructor;
+import lombok.RequiredArgsConstructor;
+import lombok.extern.slf4j.Slf4j;
+import org.springframework.core.io.InputStreamResource;
+import org.springframework.http.HttpHeaders;
+import org.springframework.http.MediaType;
+import org.springframework.http.ResponseEntity;
+import org.springframework.web.bind.annotation.*;
+import org.springframework.web.multipart.MultipartFile;
+
+import java.io.IOException;
+import java.io.InputStream;
+import java.time.LocalDate;
+import java.time.format.DateTimeFormatter;
+import java.util.UUID;
+
+/**
+ * 文件上传/下载/删除 REST 接口。
+ * 按 tenant/{tenantId}/{yyyy/MM/dd}/{uuid}.{ext} 路径隔离租户文件。
+ */
+@Slf4j
+@RestController
+@RequestMapping("/api/files")
+@RequiredArgsConstructor
+public class FileController {
+
+    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy/MM/dd");
+
+    private final StorageService storageService;
+    private final StorageProperties storageProperties;
+    private final FileUploadValidator fileUploadValidator;
+
+    @PostMapping("/upload")
+    public Result<FileUploadResponse> upload(@RequestParam("file") MultipartFile file) {
+        fileUploadValidator.validateGeneral(file);
+
+        Long tenantId = UserContext.getTenantId();
+        if (tenantId == null) {
+            throw new BusinessException(GlobalErrorCode.UNAUTHORIZED);
+        }
+
+        String originalName = file.getOriginalFilename();
+        String ext = extractExtension(originalName);
+        String uuid = UUID.randomUUID().toString().replace("-", "");
+        String fileName = ext.isEmpty() ? uuid : uuid + "." + ext;
+        String datePath = LocalDate.now().format(DATE_FMT);
+        String key = String.format("tenant/%d/%s/%s", tenantId, datePath, fileName);
+
+        String bucket = storageProperties.getDefaultBucket();
+        try (InputStream input = file.getInputStream()) {
+            storageService.upload(bucket, key, input, file.getContentType(), file.getSize());
+        } catch (IOException e) {
+            log.error("File upload read failed", e);
+            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
+        }
+
+        String url = storageService.getPresignedUrl(bucket, key, 3600);
+        FileUploadResponse resp = FileUploadResponse.builder()
+                .key(key)
+                .fileName(originalName)
+                .size(file.getSize())
+                .url(url)
+                .build();
+        return Result.success(resp);
+    }
+
+    @GetMapping("/{bucket}/{key:.+}")
+    public ResponseEntity<InputStreamResource> download(@PathVariable String bucket,
+                                                          @PathVariable String key) {
+        validateTenantAccess(key);
+        InputStream input = storageService.download(bucket, key);
+        String fileName = key.contains("/") ? key.substring(key.lastIndexOf('/') + 1) : key;
+        HttpHeaders headers = new HttpHeaders();
+        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
+        return ResponseEntity.ok()
+                .headers(headers)
+                .contentType(MediaType.APPLICATION_OCTET_STREAM)
+                .body(new InputStreamResource(input));
+    }
+
+    @DeleteMapping("/{bucket}/{key:.+}")
+    public Result<Void> delete(@PathVariable String bucket, @PathVariable String key) {
+        validateTenantAccess(key);
+        storageService.delete(bucket, key);
+        return Result.success();
+    }
+
+    private String extractExtension(String fileName) {
+        if (fileName == null) {
+            return "";
+        }
+        int idx = fileName.lastIndexOf('.');
+        if (idx < 0 || idx == fileName.length() - 1) {
+            return "";
+        }
+        return fileName.substring(idx + 1);
+    }
+
+    private void validateTenantAccess(String key) {
+        Long currentTenantId = UserContext.getTenantId();
+        if (currentTenantId != null) {
+            // key 格式: tenant/{tenantId}/...
+            if (!key.startsWith("tenant/" + currentTenantId + "/")) {
+                throw new BusinessException(GlobalErrorCode.PERMISSION_DENIED.getCode(),
+                        "无权访问该文件");
+            }
+        }
+    }
+
+    @Data
+    @Builder
+    @NoArgsConstructor
+    @AllArgsConstructor
+    public static class FileUploadResponse {
+        private String key;
+        private String fileName;
+        private long size;
+        private String url;
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/controller/MenuController.java b/backend/system-core/src/main/java/com/lc/system/controller/MenuController.java
new file mode 100644
index 00000000..21ded62a
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/controller/MenuController.java
@@ -0,0 +1,57 @@
+package com.lc.system.controller;
+
+import com.lc.common.annotation.PreAuthorize;
+import com.lc.common.context.UserContext;
+import com.lc.common.dto.Result;
+import com.lc.system.dto.MenuDTO;
+import com.lc.system.service.MenuService;
+import lombok.RequiredArgsConstructor;
+import org.springframework.web.bind.annotation.*;
+
+import java.util.List;
+
+@RestController
+@RequestMapping("/api/system/menus")
+@RequiredArgsConstructor
+public class MenuController {
+
+    private final MenuService menuService;
+
+    @GetMapping
+    @PreAuthorize("system:menu:list")
+    public Result<List<MenuDTO.MenuResponse>> list() {
+        return Result.success(menuService.list(UserContext.getTenantId()));
+    }
+
+    @GetMapping("/tree")
+    @PreAuthorize("system:menu:list")
+    public Result<List<MenuDTO.MenuResponse>> getMenuTree() {
+        return Result.success(menuService.getMenuTree(UserContext.getTenantId()));
+    }
+
+    @GetMapping("/{id}")
+    @PreAuthorize("system:menu:list")
+    public Result<MenuDTO.MenuResponse> getById(@PathVariable Long id) {
+        return Result.success(menuService.getById(id));
+    }
+
+    @PostMapping
+    @PreAuthorize("system:menu:create")
+    public Result<MenuDTO.MenuResponse> create(@RequestBody MenuDTO.CreateRequest request) {
+        return Result.success(menuService.create(request));
+    }
+
+    @PutMapping("/{id}")
+    @PreAuthorize("system:menu:update")
+    public Result<MenuDTO.MenuResponse> update(@PathVariable Long id,
+                                                @RequestBody MenuDTO.UpdateRequest request) {
+        return Result.success(menuService.update(id, request));
+    }
+
+    @DeleteMapping("/{id}")
+    @PreAuthorize("system:menu:delete")
+    public Result<Void> delete(@PathVariable Long id) {
+        menuService.delete(id);
+        return Result.success();
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/controller/RoleController.java b/backend/system-core/src/main/java/com/lc/system/controller/RoleController.java
new file mode 100644
index 00000000..f031879f
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/controller/RoleController.java
@@ -0,0 +1,74 @@
+package com.lc.system.controller;
+
+import com.lc.common.annotation.PreAuthorize;
+import com.lc.common.dto.PageResult;
+import com.lc.common.dto.Result;
+import com.lc.system.dto.RoleDTO;
+import com.lc.system.service.RoleService;
+import lombok.RequiredArgsConstructor;
+import org.springframework.web.bind.annotation.*;
+
+import java.util.List;
+
+@RestController
+@RequestMapping("/api/system/roles")
+@RequiredArgsConstructor
+public class RoleController {
+
+    private final RoleService roleService;
+
+    @GetMapping
+    @PreAuthorize("system:role:list")
+    public Result<PageResult<RoleDTO.RoleResponse>> list(
+            @RequestParam(required = false) String keyword,
+            @RequestParam(defaultValue = "1") int page,
+            @RequestParam(defaultValue = "10") int size) {
+        return Result.success(roleService.list(keyword, page, size));
+    }
+
+    @GetMapping("/all")
+    @PreAuthorize("system:role:list")
+    public Result<List<RoleDTO.RoleResponse>> listAll() {
+        return Result.success(roleService.listAll());
+    }
+
+    @GetMapping("/{id}")
+    @PreAuthorize("system:role:list")
+    public Result<RoleDTO.RoleResponse> getById(@PathVariable Long id) {
+        return Result.success(roleService.getById(id));
+    }
+
+    @PostMapping
+    @PreAuthorize("system:role:create")
+    public Result<RoleDTO.RoleResponse> create(@RequestBody RoleDTO.CreateRequest request) {
+        return Result.success(roleService.create(request));
+    }
+
+    @PutMapping("/{id}")
+    @PreAuthorize("system:role:update")
+    public Result<RoleDTO.RoleResponse> update(@PathVariable Long id,
+                                                @RequestBody RoleDTO.UpdateRequest request) {
+        return Result.success(roleService.update(id, request));
+    }
+
+    @DeleteMapping("/{id}")
+    @PreAuthorize("system:role:delete")
+    public Result<Void> delete(@PathVariable Long id) {
+        roleService.delete(id);
+        return Result.success();
+    }
+
+    @PutMapping("/{id}/menus")
+    @PreAuthorize("system:role:assign")
+    public Result<Void> assignMenus(@PathVariable Long id,
+                                     @RequestBody RoleDTO.AssignMenusRequest request) {
+        roleService.assignMenus(id, request.getMenuIds());
+        return Result.success();
+    }
+
+    @GetMapping("/{id}/menus")
+    @PreAuthorize("system:role:list")
+    public Result<List<Long>> getRoleMenuIds(@PathVariable Long id) {
+        return Result.success(roleService.getRoleMenuIds(id));
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/controller/TenantController.java b/backend/system-core/src/main/java/com/lc/system/controller/TenantController.java
new file mode 100644
index 00000000..c36d31d0
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/controller/TenantController.java
@@ -0,0 +1,52 @@
+package com.lc.system.controller;
+
+import com.lc.common.dto.PageResult;
+import com.lc.common.dto.Result;
+import com.lc.system.dto.TenantDTO;
+import com.lc.system.service.TenantService;
+import lombok.RequiredArgsConstructor;
+import org.springframework.web.bind.annotation.*;
+
+@RestController
+@RequestMapping("/api/system/tenants")
+@RequiredArgsConstructor
+public class TenantController {
+
+    private final TenantService tenantService;
+
+    @GetMapping
+    public Result<PageResult<TenantDTO.TenantResponse>> list(
+            @RequestParam(required = false) String keyword,
+            @RequestParam(defaultValue = "1") int page,
+            @RequestParam(defaultValue = "10") int size) {
+        return Result.success(tenantService.list(keyword, page, size));
+    }
+
+    @GetMapping("/{id}")
+    public Result<TenantDTO.TenantResponse> getById(@PathVariable Long id) {
+        return Result.success(tenantService.getById(id));
+    }
+
+    @PostMapping
+    public Result<TenantDTO.TenantResponse> create(@RequestBody TenantDTO.CreateRequest request) {
+        return Result.success(tenantService.create(request));
+    }
+
+    @PutMapping("/{id}")
+    public Result<TenantDTO.TenantResponse> update(@PathVariable Long id,
+                                                    @RequestBody TenantDTO.UpdateRequest request) {
+        return Result.success(tenantService.update(id, request));
+    }
+
+    @DeleteMapping("/{id}")
+    public Result<Void> delete(@PathVariable Long id) {
+        tenantService.delete(id);
+        return Result.success();
+    }
+
+    @PatchMapping("/{id}/status")
+    public Result<TenantDTO.TenantResponse> toggleStatus(@PathVariable Long id,
+                                                          @RequestParam Integer status) {
+        return Result.success(tenantService.toggleStatus(id, status));
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/controller/UserController.java b/backend/system-core/src/main/java/com/lc/system/controller/UserController.java
new file mode 100644
index 00000000..1d021066
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/controller/UserController.java
@@ -0,0 +1,85 @@
+package com.lc.system.controller;
+
+import com.lc.common.annotation.PreAuthorize;
+import com.lc.common.context.UserContext;
+import com.lc.common.dto.PageResult;
+import com.lc.common.dto.Result;
+import com.lc.system.dto.UserDTO;
+import com.lc.system.service.UserService;
+import lombok.RequiredArgsConstructor;
+import org.springframework.web.bind.annotation.*;
+
+import java.util.List;
+
+@RestController
+@RequestMapping("/api/system/users")
+@RequiredArgsConstructor
+public class UserController {
+
+    private final UserService userService;
+
+    @GetMapping
+    @PreAuthorize("system:user:list")
+    public Result<PageResult<UserDTO.UserResponse>> list(
+            @RequestParam(required = false) String keyword,
+            @RequestParam(defaultValue = "1") int page,
+            @RequestParam(defaultValue = "10") int size) {
+        return Result.success(userService.list(UserContext.getTenantId(), keyword, page, size));
+    }
+
+    @GetMapping("/{id}")
+    @PreAuthorize("system:user:list")
+    public Result<UserDTO.UserResponse> getById(@PathVariable Long id) {
+        return Result.success(userService.getDetail(id));
+    }
+
+    @PostMapping
+    @PreAuthorize("system:user:create")
+    public Result<UserDTO.UserResponse> create(@RequestBody UserDTO.CreateRequest request) {
+        return Result.success(userService.create(request));
+    }
+
+    @PutMapping("/{id}")
+    @PreAuthorize("system:user:update")
+    public Result<UserDTO.UserResponse> update(@PathVariable Long id,
+                                                @RequestBody UserDTO.UpdateRequest request) {
+        return Result.success(userService.update(id, request));
+    }
+
+    @DeleteMapping("/{id}")
+    @PreAuthorize("system:user:delete")
+    public Result<Void> delete(@PathVariable Long id) {
+        userService.deleteUser(id);
+        return Result.success();
+    }
+
+    @PatchMapping("/{id}/status")
+    @PreAuthorize("system:user:update")
+    public Result<Void> updateStatus(@PathVariable Long id,
+                                      @RequestParam Integer status) {
+        userService.updateStatus(id, status);
+        return Result.success();
+    }
+
+    @PutMapping("/{id}/password")
+    @PreAuthorize("system:user:reset")
+    public Result<Void> resetPassword(@PathVariable Long id,
+                                       @RequestBody UserDTO.ResetPasswordRequest request) {
+        userService.resetPassword(id, request.getPassword());
+        return Result.success();
+    }
+
+    @PutMapping("/{id}/roles")
+    @PreAuthorize("system:user:assign")
+    public Result<Void> assignRoles(@PathVariable Long id,
+                                     @RequestBody UserDTO.AssignRolesRequest request) {
+        userService.assignRoles(id, request.getRoleIds());
+        return Result.success();
+    }
+
+    @GetMapping("/{id}/roles")
+    @PreAuthorize("system:user:list")
+    public Result<List<Long>> getUserRoleIds(@PathVariable Long id) {
+        return Result.success(userService.getUserRoleIds(id));
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/dto/AuditLogDTO.java b/backend/system-core/src/main/java/com/lc/system/dto/AuditLogDTO.java
new file mode 100644
index 00000000..6753920c
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/dto/AuditLogDTO.java
@@ -0,0 +1,48 @@
+package com.lc.system.dto;
+
+import com.lc.system.dto.PageRequest;
+import lombok.Data;
+import lombok.EqualsAndHashCode;
+import java.time.LocalDateTime;
+
+public class AuditLogDTO {
+
+    @Data
+    @EqualsAndHashCode(callSuper = true)
+    public static class QueryRequest extends PageRequest {
+        /** 租户ID（超级管理员可指定，普通用户自动取上下文） */
+        private Long tenantId;
+        /** 项目ID */
+        private Long projectId;
+        /** 用户ID */
+        private Long userId;
+        /** 操作动作 */
+        private String action;
+        /** 资源类型 */
+        private String resourceType;
+        /** 操作结果 SUCCESS/FAILED */
+        private String result;
+        /** 起始时间 */
+        private LocalDateTime startTime;
+        /** 结束时间 */
+        private LocalDateTime endTime;
+    }
+
+    @Data
+    public static class Response {
+        private Long id;
+        private Long tenantId;
+        private Long projectId;
+        private Long userId;
+        private String userName;
+        private String action;
+        private String resourceType;
+        private String resourceId;
+        private String clientIp;
+        private String result;
+        private String errorMessage;
+        private String detail;
+        private String requestId;
+        private LocalDateTime createdTime;
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/dto/MenuDTO.java b/backend/system-core/src/main/java/com/lc/system/dto/MenuDTO.java
new file mode 100644
index 00000000..3ee5b16e
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/dto/MenuDTO.java
@@ -0,0 +1,64 @@
+package com.lc.system.dto;
+
+import lombok.AllArgsConstructor;
+import lombok.Builder;
+import lombok.Data;
+import lombok.NoArgsConstructor;
+
+import java.time.LocalDateTime;
+import java.util.List;
+
+public class MenuDTO {
+
+    @Data
+    @Builder
+    @NoArgsConstructor
+    @AllArgsConstructor
+    public static class CreateRequest {
+        private Long parentId;
+        private String menuName;
+        private String path;
+        private String component;
+        private String icon;
+        private String menuType;
+        private String permission;
+        private Integer sortOrder;
+    }
+
+    @Data
+    @Builder
+    @NoArgsConstructor
+    @AllArgsConstructor
+    public static class UpdateRequest {
+        private Long parentId;
+        private String menuName;
+        private String path;
+        private String component;
+        private String icon;
+        private String menuType;
+        private String permission;
+        private Integer sortOrder;
+        private Integer status;
+    }
+
+    @Data
+    @Builder
+    @NoArgsConstructor
+    @AllArgsConstructor
+    public static class MenuResponse {
+        private Long id;
+        private Long tenantId;
+        private Long parentId;
+        private String menuName;
+        private String path;
+        private String component;
+        private String icon;
+        private String menuType;
+        private String permission;
+        private Integer sortOrder;
+        private Integer status;
+        private LocalDateTime createdTime;
+        private LocalDateTime updatedTime;
+        private List<MenuResponse> children;
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/dto/PageRequest.java b/backend/system-core/src/main/java/com/lc/system/dto/PageRequest.java
new file mode 100644
index 00000000..8b30109a
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/dto/PageRequest.java
@@ -0,0 +1,16 @@
+package com.lc.system.dto;
+
+import lombok.Data;
+
+/**
+ * 分页请求基类。
+ */
+@Data
+public class PageRequest {
+    private int page = 1;
+    private int size = 10;
+
+    public int getOffset() {
+        return (page - 1) * size;
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/dto/RoleDTO.java b/backend/system-core/src/main/java/com/lc/system/dto/RoleDTO.java
new file mode 100644
index 00000000..008aeeec
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/dto/RoleDTO.java
@@ -0,0 +1,58 @@
+package com.lc.system.dto;
+
+import lombok.AllArgsConstructor;
+import lombok.Builder;
+import lombok.Data;
+import lombok.NoArgsConstructor;
+
+import java.time.LocalDateTime;
+import java.util.List;
+
+public class RoleDTO {
+
+    @Data
+    @Builder
+    @NoArgsConstructor
+    @AllArgsConstructor
+    public static class CreateRequest {
+        private String roleCode;
+        private String roleName;
+        private String description;
+        private Integer sortOrder;
+    }
+
+    @Data
+    @Builder
+    @NoArgsConstructor
+    @AllArgsConstructor
+    public static class UpdateRequest {
+        private String roleName;
+        private String description;
+        private Integer sortOrder;
+        private Integer status;
+    }
+
+    @Data
+    @Builder
+    @NoArgsConstructor
+    @AllArgsConstructor
+    public static class RoleResponse {
+        private Long id;
+        private Long tenantId;
+        private String roleCode;
+        private String roleName;
+        private String description;
+        private Integer status;
+        private Integer sortOrder;
+        private LocalDateTime createdTime;
+        private LocalDateTime updatedTime;
+    }
+
+    @Data
+    @Builder
+    @NoArgsConstructor
+    @AllArgsConstructor
+    public static class AssignMenusRequest {
+        private List<Long> menuIds;
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/dto/TenantDTO.java b/backend/system-core/src/main/java/com/lc/system/dto/TenantDTO.java
new file mode 100644
index 00000000..ca1c237b
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/dto/TenantDTO.java
@@ -0,0 +1,49 @@
+package com.lc.system.dto;
+
+import lombok.AllArgsConstructor;
+import lombok.Builder;
+import lombok.Data;
+import lombok.NoArgsConstructor;
+
+import java.time.LocalDateTime;
+
+public class TenantDTO {
+
+    @Data
+    @Builder
+    @NoArgsConstructor
+    @AllArgsConstructor
+    public static class CreateRequest {
+        private String tenantCode;
+        private String tenantName;
+        private String domain;
+        private LocalDateTime expireTime;
+    }
+
+    @Data
+    @Builder
+    @NoArgsConstructor
+    @AllArgsConstructor
+    public static class UpdateRequest {
+        private String tenantName;
+        private String domain;
+        private Integer status;
+        private LocalDateTime expireTime;
+    }
+
+    @Data
+    @Builder
+    @NoArgsConstructor
+    @AllArgsConstructor
+    public static class TenantResponse {
+        private Long id;
+        private String tenantCode;
+        private String tenantName;
+        private String logoUrl;
+        private String domain;
+        private Integer status;
+        private LocalDateTime expireTime;
+        private LocalDateTime createdTime;
+        private LocalDateTime updatedTime;
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/dto/UserDTO.java b/backend/system-core/src/main/java/com/lc/system/dto/UserDTO.java
new file mode 100644
index 00000000..3671bf2d
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/dto/UserDTO.java
@@ -0,0 +1,64 @@
+package com.lc.system.dto;
+
+import lombok.AllArgsConstructor;
+import lombok.Builder;
+import lombok.Data;
+import lombok.NoArgsConstructor;
+
+import java.time.LocalDateTime;
+import java.util.List;
+
+public class UserDTO {
+
+    @Data
+    @Builder
+    @NoArgsConstructor
+    @AllArgsConstructor
+    public static class CreateRequest {
+        private Long tenantId;
+        private String username;
+        private String password;
+        private String realName;
+        private String email;
+    }
+
+    @Data
+    @Builder
+    @NoArgsConstructor
+    @AllArgsConstructor
+    public static class UpdateRequest {
+        private String realName;
+        private String email;
+        private Integer status;
+    }
+
+    @Data
+    @Builder
+    @NoArgsConstructor
+    @AllArgsConstructor
+    public static class UserResponse {
+        private Long id;
+        private Long tenantId;
+        private String username;
+        private String realName;
+        private String email;
+        private Integer status;
+        private LocalDateTime createdTime;
+    }
+
+    @Data
+    @Builder
+    @NoArgsConstructor
+    @AllArgsConstructor
+    public static class ResetPasswordRequest {
+        private String password;
+    }
+
+    @Data
+    @Builder
+    @NoArgsConstructor
+    @AllArgsConstructor
+    public static class AssignRolesRequest {
+        private List<Long> roleIds;
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/entity/AuditLogEntity.java b/backend/system-core/src/main/java/com/lc/system/entity/AuditLogEntity.java
new file mode 100644
index 00000000..754bed12
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/entity/AuditLogEntity.java
@@ -0,0 +1,75 @@
+package com.lc.system.entity;
+
+import jakarta.persistence.*;
+import lombok.Data;
+
+import java.time.LocalDateTime;
+
+@Data
+@Entity
+@Table(name = "audit_log")
+public class AuditLogEntity {
+    @Id
+    @GeneratedValue(strategy = GenerationType.IDENTITY)
+    private Long id;
+
+    @Column(name = "tenant_id")
+    private Long tenantId;
+
+    @Column(name = "project_id")
+    private Long projectId;
+
+    @Column(name = "user_id")
+    private Long userId;
+
+    @Column(name = "user_name")
+    private String userName;
+
+    @Column(name = "operation")
+    private String operation;
+
+    @Column(name = "target_type")
+    private String targetType;
+
+    @Column(name = "target_id")
+    private Long targetId;
+
+    @Column(name = "before_data")
+    private String beforeData;
+
+    @Column(name = "after_data")
+    private String afterData;
+
+    @Column(name = "detail")
+    private String detail;
+
+    @Column(name = "ip")
+    private String ip;
+
+    @Column(name = "user_agent")
+    private String userAgent;
+
+    @Column(name = "request_id")
+    private String requestId;
+
+    @Column(name = "created_time")
+    private LocalDateTime createdTime;
+
+    @Column(name = "action")
+    private String action;
+
+    @Column(name = "resource_type")
+    private String resourceType;
+
+    @Column(name = "resource_id")
+    private String resourceId;
+
+    @Column(name = "client_ip")
+    private String clientIp;
+
+    @Column(name = "result")
+    private String result = "SUCCESS";
+
+    @Column(name = "error_message")
+    private String errorMessage;
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/entity/SysRoleMenu.java b/backend/system-core/src/main/java/com/lc/system/entity/SysRoleMenu.java
new file mode 100644
index 00000000..faa3e9fc
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/entity/SysRoleMenu.java
@@ -0,0 +1,21 @@
+package com.lc.system.entity;
+
+import jakarta.persistence.*;
+import lombok.Data;
+
+@Data
+@Entity
+@Table(name = "sys_role_menu", uniqueConstraints = {
+        @UniqueConstraint(columnNames = {"role_id", "menu_id"})
+})
+public class SysRoleMenu {
+    @Id
+    @GeneratedValue(strategy = GenerationType.IDENTITY)
+    private Long id;
+
+    @Column(name = "role_id", nullable = false)
+    private Long roleId;
+
+    @Column(name = "menu_id", nullable = false)
+    private Long menuId;
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/entity/SysRolePermission.java b/backend/system-core/src/main/java/com/lc/system/entity/SysRolePermission.java
new file mode 100644
index 00000000..d33401d3
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/entity/SysRolePermission.java
@@ -0,0 +1,21 @@
+package com.lc.system.entity;
+
+import jakarta.persistence.*;
+import lombok.Data;
+
+@Data
+@Entity
+@Table(name = "sys_role_permission", uniqueConstraints = {
+        @UniqueConstraint(columnNames = {"role_id", "permission_id"})
+})
+public class SysRolePermission {
+    @Id
+    @GeneratedValue(strategy = GenerationType.IDENTITY)
+    private Long id;
+
+    @Column(name = "role_id", nullable = false)
+    private Long roleId;
+
+    @Column(name = "permission_id", nullable = false)
+    private Long permissionId;
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/repository/AuditLogRepository.java b/backend/system-core/src/main/java/com/lc/system/repository/AuditLogRepository.java
new file mode 100644
index 00000000..4ddd66ca
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/repository/AuditLogRepository.java
@@ -0,0 +1,11 @@
+package com.lc.system.repository;
+
+import com.lc.system.entity.AuditLogEntity;
+import org.springframework.data.jpa.repository.JpaRepository;
+import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
+import org.springframework.stereotype.Repository;
+
+@Repository
+public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long>,
+        JpaSpecificationExecutor<AuditLogEntity> {
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/repository/SysMenuRepository.java b/backend/system-core/src/main/java/com/lc/system/repository/SysMenuRepository.java
new file mode 100644
index 00000000..ecee8190
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/repository/SysMenuRepository.java
@@ -0,0 +1,14 @@
+package com.lc.system.repository;
+
+import com.lc.system.entity.SysMenu;
+import org.springframework.data.jpa.repository.JpaRepository;
+import org.springframework.stereotype.Repository;
+
+import java.util.List;
+
+@Repository
+public interface SysMenuRepository extends JpaRepository<SysMenu, Long> {
+    List<SysMenu> findByTenantId(Long tenantId);
+    List<SysMenu> findByTenantIdAndStatus(Long tenantId, Integer status);
+    List<SysMenu> findByParentIdAndTenantId(Long parentId, Long tenantId);
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/repository/SysPermissionRepository.java b/backend/system-core/src/main/java/com/lc/system/repository/SysPermissionRepository.java
new file mode 100644
index 00000000..2f1fb278
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/repository/SysPermissionRepository.java
@@ -0,0 +1,14 @@
+package com.lc.system.repository;
+
+import com.lc.system.entity.SysPermission;
+import org.springframework.data.jpa.repository.JpaRepository;
+import org.springframework.stereotype.Repository;
+
+import java.util.List;
+import java.util.Optional;
+
+@Repository
+public interface SysPermissionRepository extends JpaRepository<SysPermission, Long> {
+    List<SysPermission> findByTenantId(Long tenantId);
+    Optional<SysPermission> findByPermCode(String permCode);
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/repository/SysRoleMenuRepository.java b/backend/system-core/src/main/java/com/lc/system/repository/SysRoleMenuRepository.java
new file mode 100644
index 00000000..0cdeaf54
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/repository/SysRoleMenuRepository.java
@@ -0,0 +1,19 @@
+package com.lc.system.repository;
+
+import com.lc.system.entity.SysRoleMenu;
+import org.springframework.data.jpa.repository.JpaRepository;
+import org.springframework.data.jpa.repository.Query;
+import org.springframework.data.repository.query.Param;
+import org.springframework.stereotype.Repository;
+
+import java.util.List;
+
+@Repository
+public interface SysRoleMenuRepository extends JpaRepository<SysRoleMenu, Long> {
+    List<SysRoleMenu> findByRoleId(Long roleId);
+    List<SysRoleMenu> findByMenuId(Long menuId);
+    void deleteByRoleId(Long roleId);
+
+    @Query("SELECT rm.menuId FROM SysRoleMenu rm WHERE rm.roleId = :roleId")
+    List<Long> findMenuIdsByRoleId(@Param("roleId") Long roleId);
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/repository/SysRolePermissionRepository.java b/backend/system-core/src/main/java/com/lc/system/repository/SysRolePermissionRepository.java
new file mode 100644
index 00000000..0ad16c8c
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/repository/SysRolePermissionRepository.java
@@ -0,0 +1,18 @@
+package com.lc.system.repository;
+
+import com.lc.system.entity.SysRolePermission;
+import org.springframework.data.jpa.repository.JpaRepository;
+import org.springframework.data.jpa.repository.Query;
+import org.springframework.data.repository.query.Param;
+import org.springframework.stereotype.Repository;
+
+import java.util.List;
+
+@Repository
+public interface SysRolePermissionRepository extends JpaRepository<SysRolePermission, Long> {
+    List<SysRolePermission> findByRoleId(Long roleId);
+    void deleteByRoleId(Long roleId);
+
+    @Query("SELECT rp.permissionId FROM SysRolePermission rp WHERE rp.roleId = :roleId")
+    List<Long> findPermissionIdsByRoleId(@Param("roleId") Long roleId);
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/repository/SysRoleRepository.java b/backend/system-core/src/main/java/com/lc/system/repository/SysRoleRepository.java
index 07e39f2c..d09282d1 100644
--- a/backend/system-core/src/main/java/com/lc/system/repository/SysRoleRepository.java
+++ b/backend/system-core/src/main/java/com/lc/system/repository/SysRoleRepository.java
@@ -1,13 +1,22 @@
 package com.lc.system.repository;
 
 import com.lc.system.entity.SysRole;
+import org.springframework.data.domain.Page;
+import org.springframework.data.domain.Pageable;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.stereotype.Repository;
 
+import java.util.List;
 import java.util.Optional;
 
 @Repository
 public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
     Optional<SysRole> findByRoleCode(String roleCode);
     Optional<SysRole> findByTenantIdAndRoleCode(Long tenantId, String roleCode);
-}
\ No newline at end of file
+
+    List<SysRole> findByTenantId(Long tenantId);
+
+    Page<SysRole> findByTenantIdAndRoleNameContaining(Long tenantId, String roleName, Pageable pageable);
+
+    Page<SysRole> findByTenantId(Long tenantId, Pageable pageable);
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/repository/SysTenantRepository.java b/backend/system-core/src/main/java/com/lc/system/repository/SysTenantRepository.java
new file mode 100644
index 00000000..779ae47c
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/repository/SysTenantRepository.java
@@ -0,0 +1,17 @@
+package com.lc.system.repository;
+
+import com.lc.system.entity.SysTenant;
+import org.springframework.data.domain.Page;
+import org.springframework.data.domain.Pageable;
+import org.springframework.data.jpa.repository.JpaRepository;
+import org.springframework.stereotype.Repository;
+
+import java.util.Optional;
+
+@Repository
+public interface SysTenantRepository extends JpaRepository<SysTenant, Long> {
+    Optional<SysTenant> findByTenantCode(String tenantCode);
+    boolean existsByTenantCode(String tenantCode);
+
+    Page<SysTenant> findByTenantNameContainingOrTenantCodeContaining(String tenantName, String tenantCode, Pageable pageable);
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/repository/SysUserRepository.java b/backend/system-core/src/main/java/com/lc/system/repository/SysUserRepository.java
index dcd20612..578daa6b 100644
--- a/backend/system-core/src/main/java/com/lc/system/repository/SysUserRepository.java
+++ b/backend/system-core/src/main/java/com/lc/system/repository/SysUserRepository.java
@@ -1,8 +1,10 @@
 package com.lc.system.repository;
 
 import com.lc.system.entity.SysUser;
+import org.springframework.data.domain.Page;
+import org.springframework.data.domain.Pageable;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.stereotype.Repository;
 
 import java.util.Optional;
 
@@ -10,6 +12,11 @@ import java.util.Optional;
 public interface SysUserRepository extends JpaRepository<SysUser, Long> {
     Optional<SysUser> findByUsername(String username);
     Optional<SysUser> findByTenantIdAndUsername(Long tenantId, String username);
     boolean existsByUsername(String username);
     boolean existsByTenantIdAndUsername(Long tenantId, String username);
-}
\ No newline at end of file
+
+    Page<SysUser> findByTenantId(Long tenantId, Pageable pageable);
+
+    Page<SysUser> findByTenantIdAndUsernameContainingOrRealNameContaining(
+            Long tenantId, String username, String realName, Pageable pageable);
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/repository/SysUserRoleRepository.java b/backend/system-core/src/main/java/com/lc/system/repository/SysUserRoleRepository.java
index f3ea2dc8..e3bf4c60 100644
--- a/backend/system-core/src/main/java/com/lc/system/repository/SysUserRoleRepository.java
+++ b/backend/system-core/src/main/java/com/lc/system/repository/SysUserRoleRepository.java
@@ -1,13 +1,21 @@
 package com.lc.system.repository;
 
 import com.lc.system.entity.SysUserRole;
 import org.springframework.data.jpa.repository.JpaRepository;
+import org.springframework.data.jpa.repository.Query;
+import org.springframework.data.repository.query.Param;
 import org.springframework.stereotype.Repository;
 
 import java.util.List;
 
 @Repository
 public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {
     List<SysUserRole> findByUserId(Long userId);
     void deleteByUserId(Long userId);
-}
\ No newline at end of file
+
+    List<SysUserRole> findByRoleId(Long roleId);
+    void deleteByRoleId(Long roleId);
+
+    @Query("SELECT ur.roleId FROM SysUserRole ur WHERE ur.userId = :userId")
+    List<Long> findRoleIdsByUserId(@Param("userId") Long userId);
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/service/AuditLogService.java b/backend/system-core/src/main/java/com/lc/system/service/AuditLogService.java
new file mode 100644
index 00000000..3500f849
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/service/AuditLogService.java
@@ -0,0 +1,19 @@
+package com.lc.system.service;
+
+import com.lc.common.dto.PageResult;
+import com.lc.system.dto.AuditLogDTO;
+import com.lc.system.entity.AuditLogEntity;
+
+public interface AuditLogService {
+    /**
+     * 异步保存审计日志（AOP 切面调用）。
+     * 不抛异常，失败仅记录 WARN 日志，避免影响主业务流程。
+     */
+    void save(AuditLogEntity entity);
+
+    /**
+     * 分页查询审计日志。
+     * 普通用户只能查询自己租户的日志，超级管理员（tenantId 为 null）可查询所有。
+     */
+    PageResult<AuditLogDTO.Response> pageQuery(AuditLogDTO.QueryRequest request);
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/service/MenuService.java b/backend/system-core/src/main/java/com/lc/system/service/MenuService.java
new file mode 100644
index 00000000..195011d2
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/service/MenuService.java
@@ -0,0 +1,21 @@
+package com.lc.system.service;
+
+import com.lc.system.dto.MenuDTO;
+
+import java.util.List;
+
+public interface MenuService {
+    List<MenuDTO.MenuResponse> list(Long tenantId);
+
+    List<MenuDTO.MenuResponse> getMenuTree(Long tenantId);
+
+    MenuDTO.MenuResponse getById(Long id);
+
+    MenuDTO.MenuResponse create(MenuDTO.CreateRequest request);
+
+    MenuDTO.MenuResponse update(Long id, MenuDTO.UpdateRequest request);
+
+    void delete(Long id);
+
+    List<MenuDTO.MenuResponse> getMenusByRoleIds(List<Long> roleIds);
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/service/PermissionService.java b/backend/system-core/src/main/java/com/lc/system/service/PermissionService.java
new file mode 100644
index 00000000..20c4a737
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/service/PermissionService.java
@@ -0,0 +1,38 @@
+package com.lc.system.service;
+
+import java.util.List;
+import java.util.Set;
+
+/**
+ * 权限校验服务。
+ * <p>
+ * 提供基于用户-角色-权限/菜单三层模型的权限查询能力，
+ * 由 {@code PermissionInterceptor} 在请求进入 Controller 前调用。
+ * </p>
+ */
+public interface PermissionService {
+
+    /**
+     * 获取用户的全部权限码集合。
+     * <p>
+     * 来源：
+     * <ol>
+     *   <li>用户角色关联的角色 -> sys_role_menu -> sys_menu.permission 字段（非空）</li>
+     *   <li>用户角色关联的角色 -> sys_role_permission -> sys_permission.perm_code</li>
+     * </ol>
+     * </p>
+     */
+    Set<String> getUserPermissions(Long userId);
+
+    /** 用户是否持有指定权限 */
+    boolean hasPermission(Long userId, String permission);
+
+    /** 用户是否持有给定权限中的任一个 */
+    boolean hasAnyPermission(Long userId, String... permissions);
+
+    /** 用户是否持有全部给定权限 */
+    boolean hasAllPermissions(Long userId, String... permissions);
+
+    /** 获取用户关联的角色 ID 列表 */
+    List<Long> getUserRoleIds(Long userId);
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/service/RoleService.java b/backend/system-core/src/main/java/com/lc/system/service/RoleService.java
new file mode 100644
index 00000000..ff28d954
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/service/RoleService.java
@@ -0,0 +1,24 @@
+package com.lc.system.service;
+
+import com.lc.common.dto.PageResult;
+import com.lc.system.dto.RoleDTO;
+
+import java.util.List;
+
+public interface RoleService {
+    PageResult<RoleDTO.RoleResponse> list(String keyword, int page, int size);
+
+    RoleDTO.RoleResponse getById(Long id);
+
+    RoleDTO.RoleResponse create(RoleDTO.CreateRequest request);
+
+    RoleDTO.RoleResponse update(Long id, RoleDTO.UpdateRequest request);
+
+    void delete(Long id);
+
+    void assignMenus(Long roleId, List<Long> menuIds);
+
+    List<Long> getRoleMenuIds(Long roleId);
+
+    List<RoleDTO.RoleResponse> listAll();
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/service/TenantService.java b/backend/system-core/src/main/java/com/lc/system/service/TenantService.java
new file mode 100644
index 00000000..8559ae1f
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/service/TenantService.java
@@ -0,0 +1,15 @@
+package com.lc.system.service;
+
+import com.lc.common.dto.PageResult;
+import com.lc.system.dto.TenantDTO;
+import com.lc.system.entity.SysTenant;
+
+public interface TenantService {
+    PageResult<TenantDTO.TenantResponse> list(String keyword, int page, int size);
+    TenantDTO.TenantResponse getById(Long id);
+    TenantDTO.TenantResponse create(TenantDTO.CreateRequest request);
+    TenantDTO.TenantResponse update(Long id, TenantDTO.UpdateRequest request);
+    void delete(Long id);
+    TenantDTO.TenantResponse toggleStatus(Long id, Integer status);
+    SysTenant getByCode(String tenantCode);
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/service/UserService.java b/backend/system-core/src/main/java/com/lc/system/service/UserService.java
index 91adbda4..ac147b16 100644
--- a/backend/system-core/src/main/java/com/lc/system/service/UserService.java
+++ b/backend/system-core/src/main/java/com/lc/system/service/UserService.java
@@ -1,13 +1,34 @@
 package com.lc.system.service;
 
+import com.lc.common.dto.PageResult;
+import com.lc.system.dto.UserDTO;
 import com.lc.system.entity.SysUser;
 
+import java.util.List;
+
 public interface UserService {
     SysUser findByUsername(String username);
     SysUser findByTenantIdAndUsername(Long tenantId, String username);
     SysUser findById(Long id);
     SysUser createUser(SysUser user);
     SysUser updateUser(SysUser user);
     void deleteUser(Long id);
     boolean verifyPassword(String rawPassword, String encodedPassword);
-}
\ No newline at end of file
+
+    // ===== 用户管理（DTO-based，供 UserController 使用） =====
+    PageResult<UserDTO.UserResponse> list(Long tenantId, String keyword, int page, int size);
+
+    UserDTO.UserResponse getDetail(Long id);
+
+    UserDTO.UserResponse create(UserDTO.CreateRequest request);
+
+    UserDTO.UserResponse update(Long id, UserDTO.UpdateRequest request);
+
+    void resetPassword(Long id, String password);
+
+    void assignRoles(Long userId, List<Long> roleIds);
+
+    void updateStatus(Long id, Integer status);
+
+    List<Long> getUserRoleIds(Long userId);
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/service/impl/AuditLogServiceImpl.java b/backend/system-core/src/main/java/com/lc/system/service/impl/AuditLogServiceImpl.java
new file mode 100644
index 00000000..b20038f8
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/service/impl/AuditLogServiceImpl.java
@@ -0,0 +1,100 @@
+package com.lc.system.service.impl;
+
+import com.lc.common.dto.PageResult;
+import com.lc.system.dto.AuditLogDTO;
+import com.lc.system.entity.AuditLogEntity;
+import com.lc.system.repository.AuditLogRepository;
+import com.lc.system.service.AuditLogService;
+import jakarta.persistence.criteria.Predicate;
+import lombok.RequiredArgsConstructor;
+import lombok.extern.slf4j.Slf4j;
+import org.springframework.data.domain.Page;
+import org.springframework.data.domain.PageRequest;
+import org.springframework.data.domain.Sort;
+import org.springframework.data.jpa.domain.Specification;
+import org.springframework.stereotype.Service;
+
+import java.util.ArrayList;
+import java.util.List;
+
+@Service
+@RequiredArgsConstructor
+@Slf4j
+public class AuditLogServiceImpl implements AuditLogService {
+
+    private final AuditLogRepository auditLogRepository;
+
+    @Override
+    public void save(AuditLogEntity entity) {
+        try {
+            auditLogRepository.save(entity);
+        } catch (Exception e) {
+            log.warn("保存审计日志失败", e);
+        }
+    }
+
+    @Override
+    public PageResult<AuditLogDTO.Response> pageQuery(AuditLogDTO.QueryRequest request) {
+        int page = request.getPage() < 1 ? 1 : request.getPage();
+        int size = request.getSize() < 1 ? 10 : request.getSize();
+
+        Specification<AuditLogEntity> spec = (root, query, cb) -> {
+            List<Predicate> predicates = new ArrayList<>();
+            if (request.getTenantId() != null) {
+                predicates.add(cb.equal(root.get("tenantId"), request.getTenantId()));
+            }
+            if (request.getProjectId() != null) {
+                predicates.add(cb.equal(root.get("projectId"), request.getProjectId()));
+            }
+            if (request.getUserId() != null) {
+                predicates.add(cb.equal(root.get("userId"), request.getUserId()));
+            }
+            if (request.getAction() != null && !request.getAction().trim().isEmpty()) {
+                predicates.add(cb.equal(root.get("action"), request.getAction()));
+            }
+            if (request.getResourceType() != null) {
+                predicates.add(cb.equal(root.get("resourceType"), request.getResourceType()));
+            }
+            if (request.getResult() != null) {
+                predicates.add(cb.equal(root.get("result"), request.getResult()));
+            }
+            if (request.getStartTime() != null) {
+                predicates.add(cb.greaterThanOrEqualTo(root.get("createdTime"), request.getStartTime()));
+            }
+            if (request.getEndTime() != null) {
+                predicates.add(cb.lessThanOrEqualTo(root.get("createdTime"), request.getEndTime()));
+            }
+            return cb.and(predicates.toArray(new Predicate[0]));
+        };
+
+        Sort sort = Sort.by(Sort.Direction.DESC, "createdTime");
+        PageRequest pageable = PageRequest.of(page - 1, size, sort);
+
+        Page<AuditLogEntity> pageResult = auditLogRepository.findAll(spec, pageable);
+
+        List<AuditLogDTO.Response> records = pageResult.getContent().stream()
+                .map(this::toResponse)
+                .toList();
+
+        return PageResult.of(records, pageResult.getTotalElements(), page, size);
+    }
+
+    private AuditLogDTO.Response toResponse(AuditLogEntity e) {
+        AuditLogDTO.Response resp = new AuditLogDTO.Response();
+        resp.setId(e.getId());
+        resp.setTenantId(e.getTenantId());
+        resp.setProjectId(e.getProjectId());
+        resp.setUserId(e.getUserId());
+        resp.setUserName(e.getUserName());
+        resp.setAction(e.getAction());
+        resp.setResourceType(e.getResourceType());
+        resp.setResourceId(e.getResourceId());
+        resp.setClientIp(e.getClientIp());
+        resp.setResult(e.getResult());
+        resp.setErrorMessage(e.getErrorMessage());
+        resp.setDetail(e.getDetail());
+        resp.setRequestId(e.getRequestId());
+        resp.setCreatedTime(e.getCreatedTime());
+        return resp;
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/service/impl/MenuServiceImpl.java b/backend/system-core/src/main/java/com/lc/system/service/impl/MenuServiceImpl.java
new file mode 100644
index 00000000..36df6e10
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/service/impl/MenuServiceImpl.java
@@ -0,0 +1,205 @@
+package com.lc.system.service.impl;
+
+import com.lc.common.context.UserContext;
+import com.lc.common.exception.BusinessException;
+import com.lc.common.exception.GlobalErrorCode;
+import com.lc.system.dto.MenuDTO;
+import com.lc.system.entity.SysMenu;
+import com.lc.system.entity.SysRoleMenu;
+import com.lc.system.repository.SysMenuRepository;
+import com.lc.system.repository.SysRoleMenuRepository;
+import com.lc.system.service.MenuService;
+import lombok.RequiredArgsConstructor;
+import org.springframework.stereotype.Service;
+import org.springframework.transaction.annotation.Transactional;
+
+import java.util.ArrayList;
+import java.util.Comparator;
+import java.util.HashMap;
+import java.util.HashSet;
+import java.util.List;
+import java.util.Map;
+import java.util.Set;
+
+@Service
+@RequiredArgsConstructor
+public class MenuServiceImpl implements MenuService {
+
+    private final SysMenuRepository menuRepository;
+    private final SysRoleMenuRepository roleMenuRepository;
+
+    @Override
+    @Transactional(readOnly = true)
+    public List<MenuDTO.MenuResponse> list(Long tenantId) {
+        List<SysMenu> menus = menuRepository.findByTenantId(tenantId);
+        return menus.stream().map(this::toResponse).toList();
+    }
+
+    @Override
+    @Transactional(readOnly = true)
+    public List<MenuDTO.MenuResponse> getMenuTree(Long tenantId) {
+        List<SysMenu> menus = menuRepository.findByTenantId(tenantId);
+        return buildTree(menus);
+    }
+
+    @Override
+    @Transactional(readOnly = true)
+    public MenuDTO.MenuResponse getById(Long id) {
+        return toResponse(getMenuOrThrow(id));
+    }
+
+    @Override
+    @Transactional
+    public MenuDTO.MenuResponse create(MenuDTO.CreateRequest request) {
+        SysMenu menu = new SysMenu();
+        menu.setTenantId(UserContext.getTenantId());
+        menu.setParentId(request.getParentId());
+        menu.setMenuName(request.getMenuName());
+        menu.setPath(request.getPath());
+        menu.setComponent(request.getComponent());
+        menu.setIcon(request.getIcon());
+        menu.setMenuType(request.getMenuType());
+        menu.setPermission(request.getPermission());
+        menu.setSortOrder(request.getSortOrder());
+        menu.setStatus(1);
+        return toResponse(menuRepository.save(menu));
+    }
+
+    @Override
+    @Transactional
+    public MenuDTO.MenuResponse update(Long id, MenuDTO.UpdateRequest request) {
+        SysMenu existing = getMenuOrThrow(id);
+        if (request.getParentId() != null) {
+            existing.setParentId(request.getParentId());
+        }
+        if (request.getMenuName() != null) {
+            existing.setMenuName(request.getMenuName());
+        }
+        if (request.getPath() != null) {
+            existing.setPath(request.getPath());
+        }
+        if (request.getComponent() != null) {
+            existing.setComponent(request.getComponent());
+        }
+        if (request.getIcon() != null) {
+            existing.setIcon(request.getIcon());
+        }
+        if (request.getMenuType() != null) {
+            existing.setMenuType(request.getMenuType());
+        }
+        if (request.getPermission() != null) {
+            existing.setPermission(request.getPermission());
+        }
+        if (request.getSortOrder() != null) {
+            existing.setSortOrder(request.getSortOrder());
+        }
+        if (request.getStatus() != null) {
+            existing.setStatus(request.getStatus());
+        }
+        return toResponse(menuRepository.save(existing));
+    }
+
+    @Override
+    @Transactional
+    public void delete(Long id) {
+        SysMenu existing = getMenuOrThrow(id);
+        // 存在子菜单时禁止删除，避免产生孤立节点
+        List<SysMenu> children = menuRepository.findByParentIdAndTenantId(id, existing.getTenantId());
+        if (!children.isEmpty()) {
+            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "请先删除子菜单");
+        }
+        // 删除菜单前先清理角色关联关系
+        List<SysRoleMenu> roleMenus = roleMenuRepository.findByMenuId(id);
+        if (!roleMenus.isEmpty()) {
+            roleMenuRepository.deleteAll(roleMenus);
+        }
+        menuRepository.deleteById(id);
+    }
+
+    @Override
+    @Transactional(readOnly = true)
+    public List<MenuDTO.MenuResponse> getMenusByRoleIds(List<Long> roleIds) {
+        if (roleIds == null || roleIds.isEmpty()) {
+            return new ArrayList<>();
+        }
+        Set<Long> menuIds = new HashSet<>();
+        for (Long roleId : roleIds) {
+            for (SysRoleMenu rm : roleMenuRepository.findByRoleId(roleId)) {
+                menuIds.add(rm.getMenuId());
+            }
+        }
+        if (menuIds.isEmpty()) {
+            return new ArrayList<>();
+        }
+        List<SysMenu> menus = menuRepository.findAllById(menuIds);
+        return buildTree(menus);
+    }
+
+    private SysMenu getMenuOrThrow(Long id) {
+        SysMenu menu = menuRepository.findById(id)
+                .orElseThrow(() -> new BusinessException(GlobalErrorCode.NOT_FOUND));
+        // 校验租户归属（超级管理员 tenantId 为 null 时跳过）
+        Long currentTenantId = UserContext.getTenantId();
+        if (currentTenantId != null && !currentTenantId.equals(menu.getTenantId())) {
+            throw new BusinessException(GlobalErrorCode.NOT_FOUND);
+        }
+        return menu;
+    }
+
+    private MenuDTO.MenuResponse toResponse(SysMenu menu) {
+        return MenuDTO.MenuResponse.builder()
+                .id(menu.getId())
+                .tenantId(menu.getTenantId())
+                .parentId(menu.getParentId())
+                .menuName(menu.getMenuName())
+                .path(menu.getPath())
+                .component(menu.getComponent())
+                .icon(menu.getIcon())
+                .menuType(menu.getMenuType())
+                .permission(menu.getPermission())
+                .sortOrder(menu.getSortOrder())
+                .status(menu.getStatus())
+                .createdTime(menu.getCreatedTime())
+                .updatedTime(menu.getUpdatedTime())
+                .build();
+    }
+
+    /**
+     * 递归构建菜单树。
+     * 根节点为 parentId 为 null 的菜单；按 sortOrder 升序、id 升序排序。
+     */
+    private List<MenuDTO.MenuResponse> buildTree(List<SysMenu> menus) {
+        Map<Long, List<MenuDTO.MenuResponse>> childrenMap = new HashMap<>();
+        List<MenuDTO.MenuResponse> roots = new ArrayList<>();
+        for (SysMenu menu : menus) {
+            MenuDTO.MenuResponse resp = toResponse(menu);
+            if (menu.getParentId() == null) {
+                roots.add(resp);
+            } else {
+                childrenMap.computeIfAbsent(menu.getParentId(), k -> new ArrayList<>()).add(resp);
+            }
+        }
+        for (MenuDTO.MenuResponse resp : roots) {
+            fillChildren(resp, childrenMap);
+        }
+        Comparator<MenuDTO.MenuResponse> byOrder = Comparator
+                .comparing((MenuDTO.MenuResponse m) -> m.getSortOrder() == null ? Integer.MAX_VALUE : m.getSortOrder())
+                .thenComparing(MenuDTO.MenuResponse::getId, Comparator.nullsFirst(Comparator.naturalOrder()));
+        roots.sort(byOrder);
+        return roots;
+    }
+
+    private void fillChildren(MenuDTO.MenuResponse parent, Map<Long, List<MenuDTO.MenuResponse>> childrenMap) {
+        List<MenuDTO.MenuResponse> children = childrenMap.get(parent.getId());
+        if (children != null && !children.isEmpty()) {
+            Comparator<MenuDTO.MenuResponse> byOrder = Comparator
+                    .comparing((MenuDTO.MenuResponse m) -> m.getSortOrder() == null ? Integer.MAX_VALUE : m.getSortOrder())
+                    .thenComparing(MenuDTO.MenuResponse::getId, Comparator.nullsFirst(Comparator.naturalOrder()));
+            children.sort(byOrder);
+            parent.setChildren(children);
+            for (MenuDTO.MenuResponse child : children) {
+                fillChildren(child, childrenMap);
+            }
+        }
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/service/impl/PermissionServiceImpl.java b/backend/system-core/src/main/java/com/lc/system/service/impl/PermissionServiceImpl.java
new file mode 100644
index 00000000..d5eddf74
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/service/impl/PermissionServiceImpl.java
@@ -0,0 +1,131 @@
+package com.lc.system.service.impl;
+
+import com.lc.system.entity.SysMenu;
+import com.lc.system.entity.SysPermission;
+import com.lc.system.entity.SysRoleMenu;
+import com.lc.system.entity.SysRolePermission;
+import com.lc.system.repository.SysMenuRepository;
+import com.lc.system.repository.SysPermissionRepository;
+import com.lc.system.repository.SysRoleMenuRepository;
+import com.lc.system.repository.SysRolePermissionRepository;
+import com.lc.system.repository.SysUserRoleRepository;
+import com.lc.system.service.PermissionService;
+import lombok.RequiredArgsConstructor;
+import org.springframework.stereotype.Service;
+import org.springframework.transaction.annotation.Transactional;
+
+import java.util.ArrayList;
+import java.util.Collections;
+import java.util.HashSet;
+import java.util.List;
+import java.util.Set;
+
+@Service
+@RequiredArgsConstructor
+public class PermissionServiceImpl implements PermissionService {
+
+    private final SysUserRoleRepository userRoleRepository;
+    private final SysRoleMenuRepository roleMenuRepository;
+    private final SysRolePermissionRepository rolePermissionRepository;
+    private final SysMenuRepository menuRepository;
+    private final SysPermissionRepository permissionRepository;
+
+    @Override
+    @Transactional(readOnly = true)
+    public Set<String> getUserPermissions(Long userId) {
+        if (userId == null) {
+            return Collections.emptySet();
+        }
+        List<Long> roleIds = userRoleRepository.findRoleIdsByUserId(userId);
+        if (roleIds.isEmpty()) {
+            return Collections.emptySet();
+        }
+
+        Set<String> permissions = new HashSet<>();
+
+        // 1. 通过 sys_role_menu -> sys_menu.permission 收集
+        Set<Long> menuIds = new HashSet<>();
+        for (Long roleId : roleIds) {
+            for (SysRoleMenu rm : roleMenuRepository.findByRoleId(roleId)) {
+                menuIds.add(rm.getMenuId());
+            }
+        }
+        if (!menuIds.isEmpty()) {
+            List<SysMenu> menus = menuRepository.findAllById(menuIds);
+            for (SysMenu menu : menus) {
+                if (menu.getPermission() != null && !menu.getPermission().trim().isEmpty()) {
+                    permissions.add(menu.getPermission().trim());
+                }
+            }
+        }
+
+        // 2. 通过 sys_role_permission -> sys_permission.perm_code 收集
+        Set<Long> permissionIds = new HashSet<>();
+        for (Long roleId : roleIds) {
+            for (SysRolePermission rp : rolePermissionRepository.findByRoleId(roleId)) {
+                permissionIds.add(rp.getPermissionId());
+            }
+        }
+        if (!permissionIds.isEmpty()) {
+            List<SysPermission> perms = permissionRepository.findAllById(permissionIds);
+            for (SysPermission perm : perms) {
+                if (perm.getPermCode() != null && !perm.getPermCode().trim().isEmpty()) {
+                    permissions.add(perm.getPermCode().trim());
+                }
+            }
+        }
+
+        return permissions;
+    }
+
+    @Override
+    @Transactional(readOnly = true)
+    public boolean hasPermission(Long userId, String permission) {
+        if (permission == null || permission.trim().isEmpty()) {
+            return true;
+        }
+        return getUserPermissions(userId).contains(permission.trim());
+    }
+
+    @Override
+    @Transactional(readOnly = true)
+    public boolean hasAnyPermission(Long userId, String... permissions) {
+        if (permissions == null || permissions.length == 0) {
+            return true;
+        }
+        Set<String> userPermissions = getUserPermissions(userId);
+        for (String p : permissions) {
+            if (p != null && !p.trim().isEmpty() && userPermissions.contains(p.trim())) {
+                return true;
+            }
+        }
+        return false;
+    }
+
+    @Override
+    @Transactional(readOnly = true)
+    public boolean hasAllPermissions(Long userId, String... permissions) {
+        if (permissions == null || permissions.length == 0) {
+            return true;
+        }
+        Set<String> userPermissions = getUserPermissions(userId);
+        for (String p : permissions) {
+            if (p == null || p.trim().isEmpty()) {
+                continue;
+            }
+            if (!userPermissions.contains(p.trim())) {
+                return false;
+            }
+        }
+        return true;
+    }
+
+    @Override
+    @Transactional(readOnly = true)
+    public List<Long> getUserRoleIds(Long userId) {
+        if (userId == null) {
+            return new ArrayList<>();
+        }
+        return userRoleRepository.findRoleIdsByUserId(userId);
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/service/impl/RoleServiceImpl.java b/backend/system-core/src/main/java/com/lc/system/service/impl/RoleServiceImpl.java
new file mode 100644
index 00000000..fa95d321
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/service/impl/RoleServiceImpl.java
@@ -0,0 +1,182 @@
+package com.lc.system.service.impl;
+
+import com.lc.common.context.UserContext;
+import com.lc.common.dto.PageResult;
+import com.lc.common.exception.BusinessException;
+import com.lc.common.exception.GlobalErrorCode;
+import com.lc.system.dto.RoleDTO;
+import com.lc.system.entity.SysMenu;
+import com.lc.system.entity.SysRole;
+import com.lc.system.entity.SysRoleMenu;
+import com.lc.system.repository.SysMenuRepository;
+import com.lc.system.repository.SysRoleMenuRepository;
+import com.lc.system.repository.SysRolePermissionRepository;
+import com.lc.system.repository.SysRoleRepository;
+import com.lc.system.repository.SysUserRoleRepository;
+import com.lc.system.service.RoleService;
+import lombok.RequiredArgsConstructor;
+import org.springframework.data.domain.Page;
+import org.springframework.data.domain.PageRequest;
+import org.springframework.data.domain.Pageable;
+import org.springframework.stereotype.Service;
+import org.springframework.transaction.annotation.Transactional;
+
+import java.util.HashSet;
+import java.util.List;
+import java.util.Set;
+
+@Service
+@RequiredArgsConstructor
+public class RoleServiceImpl implements RoleService {
+
+    private final SysRoleRepository roleRepository;
+    private final SysRoleMenuRepository roleMenuRepository;
+    private final SysUserRoleRepository userRoleRepository;
+    private final SysRolePermissionRepository rolePermissionRepository;
+    private final SysMenuRepository menuRepository;
+
+    @Override
+    @Transactional(readOnly = true)
+    public PageResult<RoleDTO.RoleResponse> list(String keyword, int page, int size) {
+        int pageIndex = page < 1 ? 1 : page;
+        int pageSize = size < 1 ? 10 : size;
+        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
+
+        Long tenantId = UserContext.getTenantId();
+        Page<SysRole> rolePage;
+        if (keyword == null || keyword.trim().isEmpty()) {
+            rolePage = roleRepository.findByTenantId(tenantId, pageable);
+        } else {
+            rolePage = roleRepository.findByTenantIdAndRoleNameContaining(tenantId, keyword.trim(), pageable);
+        }
+
+        List<RoleDTO.RoleResponse> records = rolePage.getContent().stream()
+                .map(this::toResponse)
+                .toList();
+        return PageResult.of(records, rolePage.getTotalElements(), pageIndex, pageSize);
+    }
+
+    @Override
+    @Transactional(readOnly = true)
+    public RoleDTO.RoleResponse getById(Long id) {
+        return toResponse(getRoleOrThrow(id));
+    }
+
+    @Override
+    @Transactional
+    public RoleDTO.RoleResponse create(RoleDTO.CreateRequest request) {
+        Long tenantId = UserContext.getTenantId();
+        if (tenantId != null && roleRepository.findByTenantIdAndRoleCode(tenantId, request.getRoleCode()).isPresent()) {
+            throw new BusinessException(GlobalErrorCode.DATA_CONFLICT);
+        }
+        SysRole role = new SysRole();
+        role.setTenantId(tenantId);
+        role.setRoleCode(request.getRoleCode());
+        role.setRoleName(request.getRoleName());
+        role.setDescription(request.getDescription());
+        role.setSortOrder(request.getSortOrder());
+        role.setStatus(1);
+        return toResponse(roleRepository.save(role));
+    }
+
+    @Override
+    @Transactional
+    public RoleDTO.RoleResponse update(Long id, RoleDTO.UpdateRequest request) {
+        SysRole existing = getRoleOrThrow(id);
+        if (request.getRoleName() != null) {
+            existing.setRoleName(request.getRoleName());
+        }
+        if (request.getDescription() != null) {
+            existing.setDescription(request.getDescription());
+        }
+        if (request.getSortOrder() != null) {
+            existing.setSortOrder(request.getSortOrder());
+        }
+        if (request.getStatus() != null) {
+            existing.setStatus(request.getStatus());
+        }
+        return toResponse(roleRepository.save(existing));
+    }
+
+    @Override
+    @Transactional
+    public void delete(Long id) {
+        getRoleOrThrow(id);
+        // 删除角色前清理全部关联关系：sys_role_menu / sys_user_role / sys_role_permission
+        roleMenuRepository.deleteByRoleId(id);
+        userRoleRepository.deleteByRoleId(id);
+        rolePermissionRepository.deleteByRoleId(id);
+        roleRepository.deleteById(id);
+    }
+
+    @Override
+    @Transactional
+    public void assignMenus(Long roleId, List<Long> menuIds) {
+        getRoleOrThrow(roleId);
+        roleMenuRepository.deleteByRoleId(roleId);
+        if (menuIds == null || menuIds.isEmpty()) {
+            return;
+        }
+        // 去重
+        Set<Long> distinctIds = new HashSet<>(menuIds);
+        // 校验关联菜单属于当前租户
+        Long currentTenantId = UserContext.getTenantId();
+        if (currentTenantId != null) {
+            List<SysMenu> menus = menuRepository.findAllById(distinctIds);
+            if (menus.size() != distinctIds.size()) {
+                throw new BusinessException(GlobalErrorCode.NOT_FOUND);
+            }
+            for (SysMenu menu : menus) {
+                if (!currentTenantId.equals(menu.getTenantId())) {
+                    throw new BusinessException(GlobalErrorCode.NOT_FOUND);
+                }
+            }
+        }
+        for (Long menuId : distinctIds) {
+            SysRoleMenu rm = new SysRoleMenu();
+            rm.setRoleId(roleId);
+            rm.setMenuId(menuId);
+            roleMenuRepository.save(rm);
+        }
+    }
+
+    @Override
+    @Transactional(readOnly = true)
+    public List<Long> getRoleMenuIds(Long roleId) {
+        getRoleOrThrow(roleId);
+        return roleMenuRepository.findMenuIdsByRoleId(roleId);
+    }
+
+    @Override
+    @Transactional(readOnly = true)
+    public List<RoleDTO.RoleResponse> listAll() {
+        Long tenantId = UserContext.getTenantId();
+        List<SysRole> roles = roleRepository.findByTenantId(tenantId);
+        return roles.stream().map(this::toResponse).toList();
+    }
+
+    private SysRole getRoleOrThrow(Long id) {
+        SysRole role = roleRepository.findById(id)
+                .orElseThrow(() -> new BusinessException(GlobalErrorCode.ROLE_NOT_FOUND));
+        // 校验租户归属（超级管理员 tenantId 为 null 时跳过）
+        Long currentTenantId = UserContext.getTenantId();
+        if (currentTenantId != null && !currentTenantId.equals(role.getTenantId())) {
+            throw new BusinessException(GlobalErrorCode.NOT_FOUND);
+        }
+        return role;
+    }
+
+    private RoleDTO.RoleResponse toResponse(SysRole role) {
+        return RoleDTO.RoleResponse.builder()
+                .id(role.getId())
+                .tenantId(role.getTenantId())
+                .roleCode(role.getRoleCode())
+                .roleName(role.getRoleName())
+                .description(role.getDescription())
+                .status(role.getStatus())
+                .sortOrder(role.getSortOrder())
+                .createdTime(role.getCreatedTime())
+                .updatedTime(role.getUpdatedTime())
+                .build();
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/service/impl/TenantServiceImpl.java b/backend/system-core/src/main/java/com/lc/system/service/impl/TenantServiceImpl.java
new file mode 100644
index 00000000..79837757
--- /dev/null
+++ b/backend/system-core/src/main/java/com/lc/system/service/impl/TenantServiceImpl.java
@@ -0,0 +1,128 @@
+package com.lc.system.service.impl;
+
+import com.lc.common.dto.PageResult;
+import com.lc.common.exception.BusinessException;
+import com.lc.common.exception.GlobalErrorCode;
+import com.lc.system.dto.TenantDTO;
+import com.lc.system.entity.SysTenant;
+import com.lc.system.repository.SysTenantRepository;
+import com.lc.system.service.TenantService;
+import lombok.RequiredArgsConstructor;
+import org.springframework.data.domain.Page;
+import org.springframework.data.domain.PageRequest;
+import org.springframework.data.domain.Pageable;
+import org.springframework.stereotype.Service;
+import org.springframework.transaction.annotation.Transactional;
+
+import java.util.List;
+
+@Service
+@RequiredArgsConstructor
+public class TenantServiceImpl implements TenantService {
+
+    private final SysTenantRepository tenantRepository;
+
+    @Override
+    public PageResult<TenantDTO.TenantResponse> list(String keyword, int page, int size) {
+        int pageIndex = page < 1 ? 1 : page;
+        int pageSize = size < 1 ? 10 : size;
+        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
+
+        Page<SysTenant> tenantPage;
+        if (keyword == null || keyword.trim().isEmpty()) {
+            tenantPage = tenantRepository.findAll(pageable);
+        } else {
+            String kw = keyword.trim();
+            tenantPage = tenantRepository.findByTenantNameContainingOrTenantCodeContaining(kw, kw, pageable);
+        }
+
+        List<TenantDTO.TenantResponse> records = tenantPage.getContent().stream()
+                .map(this::toResponse)
+                .toList();
+        return PageResult.of(records, tenantPage.getTotalElements(), pageIndex, pageSize);
+    }
+
+    @Override
+    public TenantDTO.TenantResponse getById(Long id) {
+        SysTenant tenant = tenantRepository.findById(id)
+                .orElseThrow(() -> new BusinessException(GlobalErrorCode.TENANT_NOT_FOUND));
+        return toResponse(tenant);
+    }
+
+    @Override
+    @Transactional
+    public TenantDTO.TenantResponse create(TenantDTO.CreateRequest request) {
+        if (tenantRepository.existsByTenantCode(request.getTenantCode())) {
+            throw new BusinessException(GlobalErrorCode.DATA_CONFLICT);
+        }
+        SysTenant tenant = new SysTenant();
+        tenant.setTenantCode(request.getTenantCode());
+        tenant.setTenantName(request.getTenantName());
+        tenant.setDomain(request.getDomain());
+        tenant.setExpireTime(request.getExpireTime());
+        tenant.setStatus(1);
+        SysTenant saved = tenantRepository.save(tenant);
+        return toResponse(saved);
+    }
+
+    @Override
+    @Transactional
+    public TenantDTO.TenantResponse update(Long id, TenantDTO.UpdateRequest request) {
+        SysTenant existing = tenantRepository.findById(id)
+                .orElseThrow(() -> new BusinessException(GlobalErrorCode.TENANT_NOT_FOUND));
+        if (request.getTenantName() != null) {
+            existing.setTenantName(request.getTenantName());
+        }
+        if (request.getDomain() != null) {
+            existing.setDomain(request.getDomain());
+        }
+        if (request.getStatus() != null) {
+            existing.setStatus(request.getStatus());
+        }
+        if (request.getExpireTime() != null) {
+            existing.setExpireTime(request.getExpireTime());
+        }
+        SysTenant saved = tenantRepository.save(existing);
+        return toResponse(saved);
+    }
+
+    @Override
+    @Transactional
+    public void delete(Long id) {
+        SysTenant existing = tenantRepository.findById(id)
+                .orElseThrow(() -> new BusinessException(GlobalErrorCode.TENANT_NOT_FOUND));
+        // 软删除：禁用租户
+        existing.setStatus(0);
+        tenantRepository.save(existing);
+    }
+
+    @Override
+    @Transactional
+    public TenantDTO.TenantResponse toggleStatus(Long id, Integer status) {
+        SysTenant existing = tenantRepository.findById(id)
+                .orElseThrow(() -> new BusinessException(GlobalErrorCode.TENANT_NOT_FOUND));
+        existing.setStatus(status);
+        SysTenant saved = tenantRepository.save(existing);
+        return toResponse(saved);
+    }
+
+    @Override
+    public SysTenant getByCode(String tenantCode) {
+        return tenantRepository.findByTenantCode(tenantCode)
+                .orElseThrow(() -> new BusinessException(GlobalErrorCode.TENANT_NOT_FOUND));
+    }
+
+    private TenantDTO.TenantResponse toResponse(SysTenant tenant) {
+        return TenantDTO.TenantResponse.builder()
+                .id(tenant.getId())
+                .tenantCode(tenant.getTenantCode())
+                .tenantName(tenant.getTenantName())
+                .logoUrl(tenant.getLogoUrl())
+                .domain(tenant.getDomain())
+                .status(tenant.getStatus())
+                .expireTime(tenant.getExpireTime())
+                .createdTime(tenant.getCreatedTime())
+                .updatedTime(tenant.getUpdatedTime())
+                .build();
+    }
+}
diff --git a/backend/system-core/src/main/java/com/lc/system/service/impl/UserServiceImpl.java b/backend/system-core/src/main/java/com/lc/system/service/impl/UserServiceImpl.java
index a40ccc34..9e522f8a 100644
--- a/backend/system-core/src/main/java/com/lc/system/service/impl/UserServiceImpl.java
+++ b/backend/system-core/src/main/java/com/lc/system/service/impl/UserServiceImpl.java
@@ -1,22 +1,38 @@
 package com.lc.system.service.impl;
 
+import com.lc.common.context.UserContext;
+import com.lc.common.dto.PageResult;
 import com.lc.common.exception.BusinessException;
 import com.lc.common.exception.GlobalErrorCode;
 import com.lc.common.util.PasswordUtil;
+import com.lc.system.dto.UserDTO;
+import com.lc.system.entity.SysRole;
 import com.lc.system.entity.SysUser;
+import com.lc.system.entity.SysUserRole;
+import com.lc.system.repository.SysRoleRepository;
 import com.lc.system.repository.SysUserRepository;
+import com.lc.system.repository.SysUserRoleRepository;
 import com.lc.system.service.UserService;
 import lombok.RequiredArgsConstructor;
+import org.springframework.data.domain.Page;
+import org.springframework.data.domain.PageRequest;
+import org.springframework.data.domain.Pageable;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;
 
+import java.util.HashSet;
+import java.util.List;
+import java.util.Set;
+
 @Service
 @RequiredArgsConstructor
 public class UserServiceImpl implements UserService {
 
     private final SysUserRepository userRepository;
+    private final SysUserRoleRepository userRoleRepository;
+    private final SysRoleRepository roleRepository;
 
     @Override
     public SysUser findByUsername(String username) {
         return userRepository.findByUsername(username)
                 .orElseThrow(() -> new BusinessException(GlobalErrorCode.USER_NOT_FOUND));
@@ -63,16 +79,160 @@ public class UserServiceImpl implements UserService {
     }
 
     @Override
     @Transactional
     public void deleteUser(Long id) {
-        if (!userRepository.existsById(id)) {
-            throw new BusinessException(GlobalErrorCode.USER_NOT_FOUND);
-        }
+        getUserOrThrow(id);
+        // 清理用户角色关联关系
+        userRoleRepository.deleteByUserId(id);
         userRepository.deleteById(id);
     }
 
     @Override
     public boolean verifyPassword(String rawPassword, String encodedPassword) {
         return PasswordUtil.matches(rawPassword, encodedPassword);
     }
-}
\ No newline at end of file
+
+    // ===== 用户管理（DTO-based） =====
+
+    @Override
+    @Transactional(readOnly = true)
+    public PageResult<UserDTO.UserResponse> list(Long tenantId, String keyword, int page, int size) {
+        int pageIndex = page < 1 ? 1 : page;
+        int pageSize = size < 1 ? 10 : size;
+        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
+
+        Page<SysUser> userPage;
+        if (keyword == null || keyword.trim().isEmpty()) {
+            userPage = userRepository.findByTenantId(tenantId, pageable);
+        } else {
+            String kw = keyword.trim();
+            userPage = userRepository.findByTenantIdAndUsernameContainingOrRealNameContaining(tenantId, kw, kw, pageable);
+        }
+
+        List<UserDTO.UserResponse> records = userPage.getContent().stream()
+                .map(this::toResponse)
+                .toList();
+        return PageResult.of(records, userPage.getTotalElements(), pageIndex, pageSize);
+    }
+
+    @Override
+    @Transactional(readOnly = true)
+    public UserDTO.UserResponse getDetail(Long id) {
+        return toResponse(getUserOrThrow(id));
+    }
+
+    @Override
+    @Transactional
+    public UserDTO.UserResponse create(UserDTO.CreateRequest request) {
+        Long tenantId = UserContext.getTenantId();
+        if (tenantId != null && userRepository.existsByTenantIdAndUsername(tenantId, request.getUsername())) {
+            throw new BusinessException(GlobalErrorCode.DATA_CONFLICT);
+        }
+        SysUser user = new SysUser();
+        user.setTenantId(tenantId);
+        user.setUsername(request.getUsername());
+        user.setPassword(PasswordUtil.encode(request.getPassword()));
+        user.setRealName(request.getRealName());
+        user.setEmail(request.getEmail());
+        user.setStatus(1);
+        return toResponse(userRepository.save(user));
+    }
+
+    @Override
+    @Transactional
+    public UserDTO.UserResponse update(Long id, UserDTO.UpdateRequest request) {
+        SysUser existing = getUserOrThrow(id);
+        if (request.getRealName() != null) {
+            existing.setRealName(request.getRealName());
+        }
+        if (request.getEmail() != null) {
+            existing.setEmail(request.getEmail());
+        }
+        if (request.getStatus() != null) {
+            existing.setStatus(request.getStatus());
+        }
+        return toResponse(userRepository.save(existing));
+    }
+
+    @Override
+    @Transactional
+    public void resetPassword(Long id, String password) {
+        SysUser existing = getUserOrThrow(id);
+        if (password == null || password.isEmpty()) {
+            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR);
+        }
+        existing.setPassword(PasswordUtil.encode(password));
+        userRepository.save(existing);
+    }
+
+    @Override
+    @Transactional
+    public void assignRoles(Long userId, List<Long> roleIds) {
+        getUserOrThrow(userId);
+        userRoleRepository.deleteByUserId(userId);
+        if (roleIds == null || roleIds.isEmpty()) {
+            return;
+        }
+        // 去重
+        Set<Long> distinctIds = new HashSet<>(roleIds);
+        // 校验关联角色属于当前租户
+        Long currentTenantId = UserContext.getTenantId();
+        if (currentTenantId != null) {
+            List<SysRole> roles = roleRepository.findAllById(distinctIds);
+            if (roles.size() != distinctIds.size()) {
+                throw new BusinessException(GlobalErrorCode.NOT_FOUND);
+            }
+            for (SysRole role : roles) {
+                if (!currentTenantId.equals(role.getTenantId())) {
+                    throw new BusinessException(GlobalErrorCode.NOT_FOUND);
+                }
+            }
+        }
+        for (Long roleId : distinctIds) {
+            SysUserRole ur = new SysUserRole();
+            ur.setUserId(userId);
+            ur.setRoleId(roleId);
+            userRoleRepository.save(ur);
+        }
+    }
+
+    @Override
+    @Transactional
+    public void updateStatus(Long id, Integer status) {
+        SysUser existing = getUserOrThrow(id);
+        existing.setStatus(status);
+        userRepository.save(existing);
+    }
+
+    @Override
+    @Transactional(readOnly = true)
+    public List<Long> getUserRoleIds(Long userId) {
+        getUserOrThrow(userId);
+        return userRoleRepository.findRoleIdsByUserId(userId);
+    }
+
+    /**
+     * 按主键查询用户并校验租户归属。
+     * UserContext.getTenantId() 为 null（超级管理员）时跳过租户校验。
+     */
+    private SysUser getUserOrThrow(Long id) {
+        SysUser user = findById(id);
+        Long currentTenantId = UserContext.getTenantId();
+        if (currentTenantId != null && !currentTenantId.equals(user.getTenantId())) {
+            throw new BusinessException(GlobalErrorCode.NOT_FOUND);
+        }
+        return user;
+    }
+
+    private UserDTO.UserResponse toResponse(SysUser user) {
+        return UserDTO.UserResponse.builder()
+                .id(user.getId())
+                .tenantId(user.getTenantId())
+                .username(user.getUsername())
+                .realName(user.getRealName())
+                .email(user.getEmail())
+                .status(user.getStatus())
+                .createdTime(user.getCreatedTime())
+                .build();
+    }
+}
diff --git a/backend/system-core/src/main/resources/db/migration/V13__sys_role_menu.sql b/backend/system-core/src/main/resources/db/migration/V13__sys_role_menu.sql
new file mode 100644
index 00000000..7831cb1f
--- /dev/null
+++ b/backend/system-core/src/main/resources/db/migration/V13__sys_role_menu.sql
@@ -0,0 +1,7 @@
+CREATE TABLE IF NOT EXISTS sys_role_menu (
+    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
+    role_id BIGINT NOT NULL COMMENT '角色ID',
+    menu_id BIGINT NOT NULL COMMENT '菜单ID',
+    UNIQUE KEY uk_role_menu (role_id, menu_id),
+    INDEX idx_role_menu_role_id (role_id)
+) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';
diff --git a/backend/system-core/src/main/resources/db/migration/V14__sys_role_permission.sql b/backend/system-core/src/main/resources/db/migration/V14__sys_role_permission.sql
new file mode 100644
index 00000000..a291d33c
--- /dev/null
+++ b/backend/system-core/src/main/resources/db/migration/V14__sys_role_permission.sql
@@ -0,0 +1,7 @@
+CREATE TABLE IF NOT EXISTS sys_role_permission (
+    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
+    role_id BIGINT NOT NULL COMMENT '角色ID',
+    permission_id BIGINT NOT NULL COMMENT '权限ID',
+    UNIQUE KEY uk_role_permission (role_id, permission_id),
+    INDEX idx_role_perm_role_id (role_id)
+) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';
diff --git a/backend/system-core/src/main/resources/db/migration/V15__audit_log_align.sql b/backend/system-core/src/main/resources/db/migration/V15__audit_log_align.sql
new file mode 100644
index 00000000..191fcfb1
--- /dev/null
+++ b/backend/system-core/src/main/resources/db/migration/V15__audit_log_align.sql
@@ -0,0 +1,21 @@
+-- 对齐设计文档要求的审计日志字段
+-- 新增字段：action / resource_type / resource_id / client_ip / result / error_message
+-- 旧字段 operation / target_type / target_id / ip 保留以兼容历史数据
+
+ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS action VARCHAR(128) COMMENT '操作动作(对齐设计文档，如 用户登录/创建项目)';
+ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS resource_type VARCHAR(64) COMMENT '资源类型(对齐设计文档，如 USER/PROJECT/ROLE)';
+ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS resource_id VARCHAR(128) COMMENT '资源ID(对齐设计文档)';
+ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS client_ip VARCHAR(64) COMMENT '客户端IP(对齐设计文档)';
+ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS result VARCHAR(32) NOT NULL DEFAULT 'SUCCESS' COMMENT '操作结果 SUCCESS/FAILED';
+ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS error_message VARCHAR(512) COMMENT '错误信息(FAILED时记录)';
+
+-- 将旧字段数据迁移到新字段（仅当新字段为 NULL 时）
+UPDATE audit_log SET action = operation WHERE action IS NULL AND operation IS NOT NULL;
+UPDATE audit_log SET resource_type = target_type WHERE resource_type IS NULL AND target_type IS NOT NULL;
+UPDATE audit_log SET resource_id = CAST(target_id AS CHAR) WHERE resource_id IS NULL AND target_id IS NOT NULL;
+UPDATE audit_log SET client_ip = ip WHERE client_ip IS NULL AND ip IS NOT NULL;
+
+-- 辅助查询索引：按结果统计成功率/失败率、按动作/资源类型筛选
+CREATE INDEX idx_audit_result ON audit_log(result);
+CREATE INDEX idx_audit_action ON audit_log(action);
+CREATE INDEX idx_audit_resource_type ON audit_log(resource_type);
diff --git a/backend/system-core/target/classes/com/lc/system/config/JwtConfig.class b/backend/system-core/target/classes/com/lc/system/config/JwtConfig.class
deleted file mode 100644
index 5d55900c..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/config/JwtConfig.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$LoginRequest$LoginRequestBuilder.class b/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$LoginRequest$LoginRequestBuilder.class
deleted file mode 100644
index 04120d8e..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$LoginRequest$LoginRequestBuilder.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$LoginRequest.class b/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$LoginRequest.class
deleted file mode 100644
index 3dca5a68..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$LoginRequest.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$LoginResponse$LoginResponseBuilder.class b/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$LoginResponse$LoginResponseBuilder.class
deleted file mode 100644
index 03389a61..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$LoginResponse$LoginResponseBuilder.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$LoginResponse.class b/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$LoginResponse.class
deleted file mode 100644
index dba6e8bb..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$LoginResponse.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$RefreshRequest$RefreshRequestBuilder.class b/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$RefreshRequest$RefreshRequestBuilder.class
deleted file mode 100644
index b188d957..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$RefreshRequest$RefreshRequestBuilder.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$RefreshRequest.class b/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$RefreshRequest.class
deleted file mode 100644
index 54ec1e1f..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$RefreshRequest.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$UserInfo$UserInfoBuilder.class b/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$UserInfo$UserInfoBuilder.class
deleted file mode 100644
index c76419b8..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$UserInfo$UserInfoBuilder.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$UserInfo.class b/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$UserInfo.class
deleted file mode 100644
index c38e91e1..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO$UserInfo.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO.class b/backend/system-core/target/classes/com/lc/system/dto/AuthDTO.class
deleted file mode 100644
index aea9a43f..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/dto/AuthDTO.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/entity/ProjectInfo.class b/backend/system-core/target/classes/com/lc/system/entity/ProjectInfo.class
deleted file mode 100644
index a80a5aed..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/entity/ProjectInfo.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/entity/ProjectMember.class b/backend/system-core/target/classes/com/lc/system/entity/ProjectMember.class
deleted file mode 100644
index 1b930adc..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/entity/ProjectMember.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/entity/SysDict.class b/backend/system-core/target/classes/com/lc/system/entity/SysDict.class
deleted file mode 100644
index 9b3699b6..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/entity/SysDict.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/entity/SysMenu.class b/backend/system-core/target/classes/com/lc/system/entity/SysMenu.class
deleted file mode 100644
index 89d207ef..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/entity/SysMenu.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/entity/SysOrg.class b/backend/system-core/target/classes/com/lc/system/entity/SysOrg.class
deleted file mode 100644
index ccc00735..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/entity/SysOrg.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/entity/SysPermission.class b/backend/system-core/target/classes/com/lc/system/entity/SysPermission.class
deleted file mode 100644
index a36762c9..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/entity/SysPermission.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/entity/SysRole.class b/backend/system-core/target/classes/com/lc/system/entity/SysRole.class
deleted file mode 100644
index 4d975a01..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/entity/SysRole.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/entity/SysTenant.class b/backend/system-core/target/classes/com/lc/system/entity/SysTenant.class
deleted file mode 100644
index aa93205d..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/entity/SysTenant.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/entity/SysUser.class b/backend/system-core/target/classes/com/lc/system/entity/SysUser.class
deleted file mode 100644
index 309672ee..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/entity/SysUser.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/entity/SysUserRole.class b/backend/system-core/target/classes/com/lc/system/entity/SysUserRole.class
deleted file mode 100644
index ad014380..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/entity/SysUserRole.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/repository/SysRoleRepository.class b/backend/system-core/target/classes/com/lc/system/repository/SysRoleRepository.class
deleted file mode 100644
index 29e78364..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/repository/SysRoleRepository.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/repository/SysUserRepository.class b/backend/system-core/target/classes/com/lc/system/repository/SysUserRepository.class
deleted file mode 100644
index 0d91aef3..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/repository/SysUserRepository.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/repository/SysUserRoleRepository.class b/backend/system-core/target/classes/com/lc/system/repository/SysUserRoleRepository.class
deleted file mode 100644
index 743873cf..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/repository/SysUserRoleRepository.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/security/InMemoryRefreshTokenService.class b/backend/system-core/target/classes/com/lc/system/security/InMemoryRefreshTokenService.class
deleted file mode 100644
index 4f550ce6..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/security/InMemoryRefreshTokenService.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/security/JwtTokenService.class b/backend/system-core/target/classes/com/lc/system/security/JwtTokenService.class
deleted file mode 100644
index 8f6624f1..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/security/JwtTokenService.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/security/RedisRefreshTokenService.class b/backend/system-core/target/classes/com/lc/system/security/RedisRefreshTokenService.class
deleted file mode 100644
index 84e09f25..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/security/RedisRefreshTokenService.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/security/RefreshTokenService.class b/backend/system-core/target/classes/com/lc/system/security/RefreshTokenService.class
deleted file mode 100644
index 2d466e8b..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/security/RefreshTokenService.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/service/UserService.class b/backend/system-core/target/classes/com/lc/system/service/UserService.class
deleted file mode 100644
index 19958ae2..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/service/UserService.class and /dev/null differ
diff --git a/backend/system-core/target/classes/com/lc/system/service/impl/UserServiceImpl.class b/backend/system-core/target/classes/com/lc/system/service/impl/UserServiceImpl.class
deleted file mode 100644
index 4d271337..00000000
Binary files a/backend/system-core/target/classes/com/lc/system/service/impl/UserServiceImpl.class and /dev/null differ
diff --git a/backend/system-core/target/classes/db/migration/V10__project_member.sql b/backend/system-core/target/classes/db/migration/V10__project_member.sql
deleted file mode 100644
index e57dd5a1..00000000
--- a/backend/system-core/target/classes/db/migration/V10__project_member.sql
+++ /dev/null
@@ -1,9 +0,0 @@
-CREATE TABLE IF NOT EXISTS project_member (
-    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
-    project_id BIGINT NOT NULL COMMENT '项目ID',
-    user_id BIGINT NOT NULL COMMENT '用户ID',
-    role VARCHAR(32) NOT NULL COMMENT '角色 READ_ONLY/EDITOR/ADMIN/PUBLISHER',
-    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
-    joined_time DATETIME NOT NULL COMMENT '加入时间',
-    CONSTRAINT uk_project_member UNIQUE (project_id, user_id)
-) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目成员表';
\ No newline at end of file
diff --git a/backend/system-core/target/classes/db/migration/V11__audit_log.sql b/backend/system-core/target/classes/db/migration/V11__audit_log.sql
deleted file mode 100644
index e99b4346..00000000
--- a/backend/system-core/target/classes/db/migration/V11__audit_log.sql
+++ /dev/null
@@ -1,21 +0,0 @@
-CREATE TABLE IF NOT EXISTS audit_log (
-    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
-    tenant_id BIGINT COMMENT '租户ID',
-    project_id BIGINT COMMENT '项目ID',
-    user_id BIGINT COMMENT '操作用户ID',
-    user_name VARCHAR(64) COMMENT '操作用户名',
-    operation VARCHAR(128) NOT NULL COMMENT '操作类型',
-    target_type VARCHAR(64) COMMENT '目标类型',
-    target_id BIGINT COMMENT '目标ID',
-    before_data TEXT COMMENT '操作前数据',
-    after_data TEXT COMMENT '操作后数据',
-    detail VARCHAR(2048) COMMENT '操作详情',
-    ip VARCHAR(64) COMMENT 'IP地址',
-    user_agent VARCHAR(512) COMMENT 'UserAgent',
-    request_id VARCHAR(64) COMMENT '请求ID',
-    created_time DATETIME NOT NULL COMMENT '创建时间',
-    INDEX idx_audit_tenant_id (tenant_id),
-    INDEX idx_audit_project_id (project_id),
-    INDEX idx_audit_user_id (user_id),
-    INDEX idx_audit_created_time (created_time)
-) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审计日志表';
\ No newline at end of file
diff --git a/backend/system-core/target/classes/db/migration/V12__init_data.sql b/backend/system-core/target/classes/db/migration/V12__init_data.sql
deleted file mode 100644
index db36d618..00000000
--- a/backend/system-core/target/classes/db/migration/V12__init_data.sql
+++ /dev/null
@@ -1,5 +0,0 @@
-INSERT INTO sys_tenant (tenant_code, tenant_name, status, created_time)
-VALUES ('default', '默认租户', 1, NOW());
-
-INSERT INTO sys_user (tenant_id, username, password, real_name, email, phone, status, created_time)
-VALUES (1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', 'admin@example.com', '13800138000', 1, NOW());
diff --git a/backend/system-core/target/classes/db/migration/V1__sys_tenant.sql b/backend/system-core/target/classes/db/migration/V1__sys_tenant.sql
deleted file mode 100644
index 12822eef..00000000
--- a/backend/system-core/target/classes/db/migration/V1__sys_tenant.sql
+++ /dev/null
@@ -1,12 +0,0 @@
-CREATE TABLE IF NOT EXISTS sys_tenant (
-    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
-    tenant_code VARCHAR(64) NOT NULL UNIQUE COMMENT '租户编码',
-    tenant_name VARCHAR(128) NOT NULL COMMENT '租户名称',
-    logo_url VARCHAR(512) COMMENT 'Logo地址',
-    domain VARCHAR(256) COMMENT '域名',
-    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
-    expire_time DATETIME COMMENT '过期时间',
-    created_time DATETIME NOT NULL COMMENT '创建时间',
-    updated_time DATETIME COMMENT '更新时间',
-    INDEX idx_tenant_code (tenant_code)
-) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户表';
\ No newline at end of file
diff --git a/backend/system-core/target/classes/db/migration/V2__sys_user.sql b/backend/system-core/target/classes/db/migration/V2__sys_user.sql
deleted file mode 100644
index cc5cf4a5..00000000
--- a/backend/system-core/target/classes/db/migration/V2__sys_user.sql
+++ /dev/null
@@ -1,17 +0,0 @@
-CREATE TABLE IF NOT EXISTS sys_user (
-    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
-    tenant_id BIGINT COMMENT '租户ID',
-    username VARCHAR(64) NOT NULL COMMENT '用户名',
-    password VARCHAR(256) NOT NULL COMMENT '密码(BCrypt)',
-    real_name VARCHAR(64) COMMENT '真实姓名',
-    email VARCHAR(128) COMMENT '邮箱',
-    phone VARCHAR(32) COMMENT '手机号',
-    avatar_url VARCHAR(512) COMMENT '头像地址',
-    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
-    last_login_time DATETIME COMMENT '最后登录时间',
-    created_time DATETIME NOT NULL COMMENT '创建时间',
-    updated_time DATETIME COMMENT '更新时间',
-    INDEX idx_user_tenant_id (tenant_id),
-    INDEX idx_user_username (username),
-    CONSTRAINT uk_user_tenant_username UNIQUE (tenant_id, username)
-) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
\ No newline at end of file
diff --git a/backend/system-core/target/classes/db/migration/V3__sys_role.sql b/backend/system-core/target/classes/db/migration/V3__sys_role.sql
deleted file mode 100644
index e589b5d1..00000000
--- a/backend/system-core/target/classes/db/migration/V3__sys_role.sql
+++ /dev/null
@@ -1,13 +0,0 @@
-CREATE TABLE IF NOT EXISTS sys_role (
-    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
-    tenant_id BIGINT COMMENT '租户ID',
-    role_code VARCHAR(64) NOT NULL COMMENT '角色编码',
-    role_name VARCHAR(128) NOT NULL COMMENT '角色名称',
-    description VARCHAR(512) COMMENT '描述',
-    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
-    sort_order INT COMMENT '排序',
-    created_time DATETIME NOT NULL COMMENT '创建时间',
-    updated_time DATETIME COMMENT '更新时间',
-    INDEX idx_role_tenant_id (tenant_id),
-    CONSTRAINT uk_role_tenant_code UNIQUE (tenant_id, role_code)
-) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';
\ No newline at end of file
diff --git a/backend/system-core/target/classes/db/migration/V4__sys_user_role.sql b/backend/system-core/target/classes/db/migration/V4__sys_user_role.sql
deleted file mode 100644
index f24c9056..00000000
--- a/backend/system-core/target/classes/db/migration/V4__sys_user_role.sql
+++ /dev/null
@@ -1,6 +0,0 @@
-CREATE TABLE IF NOT EXISTS sys_user_role (
-    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
-    user_id BIGINT NOT NULL COMMENT '用户ID',
-    role_id BIGINT NOT NULL COMMENT '角色ID',
-    CONSTRAINT uk_user_role UNIQUE (user_id, role_id)
-) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';
\ No newline at end of file
diff --git a/backend/system-core/target/classes/db/migration/V5__sys_menu.sql b/backend/system-core/target/classes/db/migration/V5__sys_menu.sql
deleted file mode 100644
index 14479b03..00000000
--- a/backend/system-core/target/classes/db/migration/V5__sys_menu.sql
+++ /dev/null
@@ -1,17 +0,0 @@
-CREATE TABLE IF NOT EXISTS sys_menu (
-    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
-    tenant_id BIGINT COMMENT '租户ID',
-    parent_id BIGINT COMMENT '父菜单ID',
-    menu_name VARCHAR(128) NOT NULL COMMENT '菜单名称',
-    path VARCHAR(256) COMMENT '路由路径',
-    component VARCHAR(256) COMMENT '组件路径',
-    icon VARCHAR(128) COMMENT '图标',
-    menu_type VARCHAR(16) NOT NULL COMMENT '菜单类型 MENU/BUTTON/DIRECTORY',
-    permission VARCHAR(128) COMMENT '权限标识',
-    sort_order INT COMMENT '排序',
-    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
-    created_time DATETIME NOT NULL COMMENT '创建时间',
-    updated_time DATETIME COMMENT '更新时间',
-    INDEX idx_menu_tenant_id (tenant_id),
-    INDEX idx_menu_parent_id (parent_id)
-) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';
\ No newline at end of file
diff --git a/backend/system-core/target/classes/db/migration/V6__sys_permission.sql b/backend/system-core/target/classes/db/migration/V6__sys_permission.sql
deleted file mode 100644
index 6088541b..00000000
--- a/backend/system-core/target/classes/db/migration/V6__sys_permission.sql
+++ /dev/null
@@ -1,10 +0,0 @@
-CREATE TABLE IF NOT EXISTS sys_permission (
-    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
-    tenant_id BIGINT COMMENT '租户ID',
-    perm_code VARCHAR(128) NOT NULL COMMENT '权限编码',
-    perm_name VARCHAR(128) NOT NULL COMMENT '权限名称',
-    description VARCHAR(512) COMMENT '描述',
-    created_time DATETIME NOT NULL COMMENT '创建时间',
-    INDEX idx_perm_tenant_id (tenant_id),
-    CONSTRAINT uk_perm_tenant_code UNIQUE (tenant_id, perm_code)
-) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';
\ No newline at end of file
diff --git a/backend/system-core/target/classes/db/migration/V7__sys_org.sql b/backend/system-core/target/classes/db/migration/V7__sys_org.sql
deleted file mode 100644
index 469102c5..00000000
--- a/backend/system-core/target/classes/db/migration/V7__sys_org.sql
+++ /dev/null
@@ -1,15 +0,0 @@
-CREATE TABLE IF NOT EXISTS sys_org (
-    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
-    tenant_id BIGINT COMMENT '租户ID',
-    parent_id BIGINT COMMENT '父组织ID',
-    org_code VARCHAR(64) NOT NULL COMMENT '组织编码',
-    org_name VARCHAR(128) NOT NULL COMMENT '组织名称',
-    org_type VARCHAR(32) COMMENT '组织类型',
-    sort_order INT COMMENT '排序',
-    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
-    created_time DATETIME NOT NULL COMMENT '创建时间',
-    updated_time DATETIME COMMENT '更新时间',
-    INDEX idx_org_tenant_id (tenant_id),
-    INDEX idx_org_parent_id (parent_id),
-    CONSTRAINT uk_org_tenant_code UNIQUE (tenant_id, org_code)
-) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织表';
\ No newline at end of file
diff --git a/backend/system-core/target/classes/db/migration/V8__sys_dict.sql b/backend/system-core/target/classes/db/migration/V8__sys_dict.sql
deleted file mode 100644
index 18e2a074..00000000
--- a/backend/system-core/target/classes/db/migration/V8__sys_dict.sql
+++ /dev/null
@@ -1,12 +0,0 @@
-CREATE TABLE IF NOT EXISTS sys_dict (
-    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
-    tenant_id BIGINT COMMENT '租户ID',
-    dict_code VARCHAR(64) NOT NULL COMMENT '字典编码',
-    dict_name VARCHAR(128) NOT NULL COMMENT '字典名称',
-    description VARCHAR(512) COMMENT '描述',
-    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
-    created_time DATETIME NOT NULL COMMENT '创建时间',
-    updated_time DATETIME COMMENT '更新时间',
-    INDEX idx_dict_tenant_id (tenant_id),
-    CONSTRAINT uk_dict_tenant_code UNIQUE (tenant_id, dict_code)
-) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典表';
\ No newline at end of file
diff --git a/backend/system-core/target/classes/db/migration/V9__project_info.sql b/backend/system-core/target/classes/db/migration/V9__project_info.sql
deleted file mode 100644
index 9bc11325..00000000
--- a/backend/system-core/target/classes/db/migration/V9__project_info.sql
+++ /dev/null
@@ -1,16 +0,0 @@
-CREATE TABLE IF NOT EXISTS project_info (
-    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
-    tenant_id BIGINT NOT NULL COMMENT '租户ID',
-    project_code VARCHAR(64) NOT NULL COMMENT '项目编码',
-    project_name VARCHAR(128) NOT NULL COMMENT '项目名称',
-    description VARCHAR(1024) COMMENT '描述',
-    icon VARCHAR(128) COMMENT '图标',
-    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
-    lifecycle_status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '生命周期状态',
-    created_by BIGINT NOT NULL COMMENT '创建人ID',
-    created_time DATETIME NOT NULL COMMENT '创建时间',
-    updated_time DATETIME COMMENT '更新时间',
-    INDEX idx_project_tenant_id (tenant_id),
-    INDEX idx_project_code (project_code),
-    CONSTRAINT uk_project_tenant_code UNIQUE (tenant_id, project_code)
-) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目信息表';
\ No newline at end of file
diff --git a/backend/system-core/target/maven-status/maven-compiler-plugin/compile/default-compile/createdFiles.lst b/backend/system-core/target/maven-status/maven-compiler-plugin/compile/default-compile/createdFiles.lst
deleted file mode 100644
index cb864255..00000000
--- a/backend/system-core/target/maven-status/maven-compiler-plugin/compile/default-compile/createdFiles.lst
+++ /dev/null
@@ -1,29 +0,0 @@
-com/lc/system/dto/AuthDTO$RefreshRequest.class
-com/lc/system/entity/SysUserRole.class
-com/lc/system/dto/AuthDTO$LoginRequest.class
-com/lc/system/service/impl/UserServiceImpl.class
-com/lc/system/entity/SysUser.class
-com/lc/system/repository/SysUserRepository.class
-com/lc/system/service/UserService.class
-com/lc/system/security/RedisRefreshTokenService.class
-com/lc/system/repository/SysRoleRepository.class
-com/lc/system/repository/SysUserRoleRepository.class
-com/lc/system/entity/SysTenant.class
-com/lc/system/security/RefreshTokenService.class
-com/lc/system/security/InMemoryRefreshTokenService.class
-com/lc/system/entity/ProjectMember.class
-com/lc/system/dto/AuthDTO$UserInfo.class
-com/lc/system/security/JwtTokenService.class
-com/lc/system/dto/AuthDTO$LoginResponse.class
-com/lc/system/dto/AuthDTO$LoginRequest$LoginRequestBuilder.class
-com/lc/system/config/JwtConfig.class
-com/lc/system/entity/SysPermission.class
-com/lc/system/dto/AuthDTO$RefreshRequest$RefreshRequestBuilder.class
-com/lc/system/entity/SysRole.class
-com/lc/system/dto/AuthDTO.class
-com/lc/system/entity/SysDict.class
-com/lc/system/entity/ProjectInfo.class
-com/lc/system/dto/AuthDTO$UserInfo$UserInfoBuilder.class
-com/lc/system/entity/SysOrg.class
-com/lc/system/entity/SysMenu.class
-com/lc/system/dto/AuthDTO$LoginResponse$LoginResponseBuilder.class
diff --git a/backend/system-core/target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst b/backend/system-core/target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst
deleted file mode 100644
index 429333e9..00000000
--- a/backend/system-core/target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst
+++ /dev/null
@@ -1,21 +0,0 @@
-/workspace/backend/system-core/src/main/java/com/lc/system/entity/SysPermission.java
-/workspace/backend/system-core/src/main/java/com/lc/system/repository/SysUserRoleRepository.java
-/workspace/backend/system-core/src/main/java/com/lc/system/service/impl/UserServiceImpl.java
-/workspace/backend/system-core/src/main/java/com/lc/system/entity/SysUser.java
-/workspace/backend/system-core/src/main/java/com/lc/system/security/RedisRefreshTokenService.java
-/workspace/backend/system-core/src/main/java/com/lc/system/entity/ProjectMember.java
-/workspace/backend/system-core/src/main/java/com/lc/system/dto/AuthDTO.java
-/workspace/backend/system-core/src/main/java/com/lc/system/security/RefreshTokenService.java
-/workspace/backend/system-core/src/main/java/com/lc/system/security/InMemoryRefreshTokenService.java
-/workspace/backend/system-core/src/main/java/com/lc/system/entity/SysUserRole.java
-/workspace/backend/system-core/src/main/java/com/lc/system/entity/SysDict.java
-/workspace/backend/system-core/src/main/java/com/lc/system/entity/ProjectInfo.java
-/workspace/backend/system-core/src/main/java/com/lc/system/repository/SysRoleRepository.java
-/workspace/backend/system-core/src/main/java/com/lc/system/config/JwtConfig.java
-/workspace/backend/system-core/src/main/java/com/lc/system/repository/SysUserRepository.java
-/workspace/backend/system-core/src/main/java/com/lc/system/entity/SysMenu.java
-/workspace/backend/system-core/src/main/java/com/lc/system/entity/SysRole.java
-/workspace/backend/system-core/src/main/java/com/lc/system/entity/SysTenant.java
-/workspace/backend/system-core/src/main/java/com/lc/system/service/UserService.java
-/workspace/backend/system-core/src/main/java/com/lc/system/security/JwtTokenService.java
-/workspace/backend/system-core/src/main/java/com/lc/system/entity/SysOrg.java
diff --git a/docs/superpowers/plans/2026-07-27-phase1-plan.md b/docs/superpowers/plans/2026-07-27-phase1-plan.md
new file mode 100644
index 00000000..567a907c
--- /dev/null
+++ b/docs/superpowers/plans/2026-07-27-phase1-plan.md
@@ -0,0 +1,431 @@
+# Phase 1 - 基础设施与安全底座 实施计划
+
+> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.
+
+**Goal:** 完成低代码平台 Phase 1 的全部7个任务，搭建基础设施与安全底座，为 Phase 2（项目核心闭环）提供坚实基础。
+
+**Architecture:** 后端采用 Spring Boot 3.2.x + Maven 多模块（14个模块扁平组织），前端采用 Vite 6 + React 18 + TypeScript + Ant Design。认证使用 JWT + RefreshToken + Redis。数据库使用 MySQL 8.0 + Flyway 迁移。多租户基于 JWT claims + ThreadLocal 上下文。RBAC 采用用户-角色-权限三层模型 + 注解式接口权限。对象存储抽象 StorageService 接口，支持本地/MinIO 切换。
+
+**Tech Stack:** Spring Boot 3.2.5, Java 17, Maven 3.9.x, MySQL 8.0+, Redis 7.x, Flyway, JJWT 0.12.x, MinIO SDK, Spring Security 6.x, AOP, React 18.2.0, TypeScript 5.4.0, Vite 6.0.0, Ant Design 5.15.0
+
+## Global Constraints
+
+- Java: 17 (Spring Boot 3.2.x minimum)
+- Spring Boot: 3.2.5
+- Maven: 3.9.x
+- MySQL: 8.0+
+- Redis: 7.x
+- React: 18.2.0, TypeScript: 5.4.0, Vite: 6.0.0, Ant Design: 5.15.0
+- 多租户约束：租户内唯一字段使用 (tenant_id, code) 复合唯一，公共资源除外
+- 安全约束：密钥、密码、Token、连接串不得明文落库
+- 审计约束：登录、权限变更、项目配置变更、发布、回滚、数据源测试必须记录审计日志
+- 删除策略：业务主表默认软删除字段 deleted，物理删除由后台任务按保留策略执行
+- 并发控制：可编辑配置表增加 version 字段，更新时做乐观锁校验
+- 敏感字段脱敏：审计日志的 before_data/after_data 中敏感字段必须脱敏
+
+---
+
+## 进度总览
+
+> **规则：** 每次提交代码前同步更新本计划。`[x]` = 已完成，`[ ]` = 待实施。
+
+| 设计文档任务 | 本计划任务 | 状态 | 完成提交 |
+|-------------|-----------|------|---------|
+| Task 1: 项目脚手架搭建 | Task 1-1 ~ Task 1-6 | ✅ 已完成 | f06bc86..2edbaf7 |
+| Task 2: 平台库初始化 | Task 2-1 ~ Task 2-3 | ✅ 已完成 | ca58e78..853227f |
+| Task 3: 认证与会话 | Task 3-1 ~ Task 3-5 | ✅ 已完成 | b34e6d8..282aeff7 |
+| Task 4: 多租户上下文 | Task 4-1 ~ Task 4-5 | ✅ 已完成 | cd57019d..d5fad041 |
+| Task 5: RBAC权限体系 | Task 5-1 ~ Task 5-6 | ✅ 已完成 | 93cdd026..72a152e1 |
+| Task 6: 对象存储与密钥管理 | Task 6-1 ~ Task 6-5 | ✅ 已完成 | 7a55ef1a..a7756753 |
+| Task 7: 安全基线 | Task 7-1 ~ Task 7-5 | ✅ 已完成 | 6dc93d04..35c2aaed |
+
+### 当前代码库已有资产（Task 1-3 产出，后续任务可直接复用）
+
+**common 模块：**
+- [BusinessException.java](file:///workspace/backend/common/src/main/java/com/lc/common/exception/BusinessException.java) - 业务异常
+- [GlobalErrorCode.java](file:///workspace/backend/common/src/main/java/com/lc/common/exception/GlobalErrorCode.java) - 错误码枚举（含 UNAUTHORIZED, PERMISSION_DENIED, TENANT_NOT_FOUND, DATA_CONFLICT 等）
+- [Result.java](file:///workspace/backend/common/src/main/java/com/lc/common/dto/Result.java) - 统一响应
+- [PageResult.java](file:///workspace/backend/common/src/main/java/com/lc/common/dto/PageResult.java) - 分页响应（含 `PageResult.of(records, total, page, size)`）
+- [TenantContext.java](file:///workspace/backend/common/src/main/java/com/lc/common/context/TenantContext.java) - 租户上下文（ThreadLocal<Long> tenantId）
+- [UserContext.java](file:///workspace/backend/common/src/main/java/com/lc/common/context/UserContext.java) - 用户上下文（ThreadLocal，含 userId/tenantId/username）
+- [RedisConfig.java](file:///workspace/backend/common/src/main/java/com/lc/common/config/RedisConfig.java) - Redis配置
+- [PasswordUtil.java](file:///workspace/backend/common/src/main/java/com/lc/common/util/PasswordUtil.java) - BCrypt密码工具
+
+**system-core 模块：**
+- 10个实体类：SysTenant, SysUser, SysRole, SysUserRole, SysMenu, SysPermission, SysOrg, SysDict, ProjectInfo, ProjectMember
+- 12个Flyway迁移脚本（V1~V12），其中 V11 已创建 audit_log 表
+- [JwtTokenService.java](file:///workspace/backend/system-core/src/main/java/com/lc/system/security/JwtTokenService.java) - JWT服务（含 `getUserIdFromToken`, `getUsernameFromToken`, `getTenantIdFromToken` 方法）
+- RefreshTokenService 接口 + Redis/内存双实现
+- SysUserRepository, SysRoleRepository, SysUserRoleRepository
+- UserService 接口 + UserServiceImpl
+
+**bootstrap 模块：**
+- [JwtAuthenticationFilter.java](file:///workspace/backend/bootstrap/src/main/java/com/lc/bootstrap/filter/JwtAuthenticationFilter.java) - JWT认证过滤器（已解析token设置SecurityContext）
+- [SecurityConfig.java](file:///workspace/backend/bootstrap/src/main/java/com/lc/bootstrap/config/SecurityConfig.java) - Spring Security配置
+- [WebConfig.java](file:///workspace/backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebConfig.java) - Web配置
+- [AuthController.java](file:///workspace/backend/bootstrap/src/main/java/com/lc/bootstrap/controller/AuthController.java) - 认证控制器
+- [GlobalExceptionHandler.java](file:///workspace/backend/bootstrap/src/main/java/com/lc/bootstrap/handler/GlobalExceptionHandler.java) - 全局异常处理
+
+**关键接口签名（后续任务依赖）：**
+- `JwtTokenService.getTenantIdFromToken(String token)` → `Long`
+- `JwtTokenService.getUserIdFromToken(String token)` → `Long`
+- `UserContext.get()` → `UserContext(userId, tenantId, username)` / `UserContext.getUserId()` / `UserContext.getTenantId()`
+- `TenantContext.getTenantId()` → `Long`
+- `PageResult.of(List<T> records, long total, int page, int size)` → `PageResult<T>`
+
+**audit_log 表已有字段（V11，与设计文档有差异，Task 7 需处理）：**
+- 已有：`id, tenant_id, project_id, user_id, user_name, operation, target_type, target_id, before_data, after_data, detail, ip, user_agent, request_id, created_time`
+- 设计文档要求：`action, resource_type, resource_id, client_ip, result(SUCCESS/FAILED), error_message`
+- 差异：字段名不一致(operation→action, target_type→resource_type, target_id→resource_id, ip→client_ip)，缺少 result 和 error_message 字段
+- **Task 7 将创建 V13 迁移脚本补齐字段，并创建 AuditLog 实体对应**
+
+---
+
+## Task 1: 项目脚手架搭建 ✅ 已完成
+
+> 设计文档 Task 1：Spring Boot多模块 + React项目初始化
+
+- [x] Task 1-1: 后端父POM与14个Maven模块目录结构
+- [x] Task 1-2: common模块核心代码（异常体系、统一响应、租户上下文、Redis配置、密码工具）
+- [x] Task 1-3: 前端项目初始化（Vite + React + TypeScript + Ant Design）
+
+**完成提交：** f06bc86..2edbaf7
+
+---
+
+## Task 2: 平台库初始化 ✅ 已完成
+
+> 设计文档 Task 2：核心表、审计表、构建任务表、Flyway
+
+- [x] Task 2-1: 10个JPA实体类（SysTenant, SysUser, SysRole, SysUserRole, SysMenu, SysPermission, SysOrg, SysDict, ProjectInfo, ProjectMember）
+- [x] Task 2-2: 12个Flyway迁移脚本（V1~V12），含 audit_log 表(V11) 和初始数据(V12)
+- [x] Task 2-3: Repository接口（SysUserRepository, SysRoleRepository, SysUserRoleRepository）
+
+**完成提交：** ca58e78..853227f
+
+---
+
+## Task 3: 认证与会话 ✅ 已完成
+
+> 设计文档 Task 3：JWT、Refresh Token、Redis会话、登录保护
+
+- [x] Task 3-1: JwtConfig + JwtTokenService（生成/解析AccessToken/RefreshToken）
+- [x] Task 3-2: RefreshTokenService（接口 + Redis实现 + 内存实现）
+- [x] Task 3-3: AuthDTO + AuthController（登录/刷新/登出）
+- [x] Task 3-4: JwtAuthenticationFilter + SecurityConfig + GlobalExceptionHandler
+- [x] Task 3-5: 前端登录页 + 仪表盘 + 请求封装 + useAuth Hook
+
+**完成提交：** b34e6d8..282aeff7
+
+---
+
+## Task 4: 多租户上下文 ⬜ 待实施
+
+> 设计文档 Task 4：租户识别、租户内唯一约束、越权拦截器
+
+**目标：** 从JWT解析租户ID写入ThreadLocal上下文，提供租户CRUD接口，拦截器校验越权访问。
+
+**Files:**
+- Modify: `backend/bootstrap/src/main/java/com/lc/bootstrap/filter/JwtAuthenticationFilter.java`（整合UserContext设置）
+- Create: `backend/common/src/main/java/com/lc/common/annotation/TenantCheck.java`
+- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/interceptor/TenantInterceptor.java`
+- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebMvcConfig.java`（注册拦截器，如不存在则创建）
+- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysTenantRepository.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/dto/TenantDTO.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/dto/PageRequest.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/TenantService.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/TenantServiceImpl.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/controller/TenantController.java`
+
+**Interfaces:**
+- Consumes: `JwtTokenService.getTenantIdFromToken()`, `UserContext`（已存在）, `SysTenant`实体（已存在）
+- Produces: `TenantInterceptor`（越权拦截）, `TenantService`（租户CRUD）, `TenantController`（REST接口）
+
+**关键设计：**
+1. 租户识别：修改 `JwtAuthenticationFilter`，在解析token后调用 `UserContext.set()` 设置用户上下文（当前filter只设置了SecurityContext，没设置UserContext）
+2. 租户内唯一约束：数据库层已有复合唯一索引（如 sys_user 的 tenant_id+username），Service层做前置校验并返回友好错误
+3. 越权拦截：`@TenantCheck` 注解 + `TenantInterceptor`，校验请求参数中的 tenantId 与当前用户 tenantId 一致
+
+**TenantCheck.java：**
+```java
+package com.lc.common.annotation;
+
+import java.lang.annotation.*;
+
+@Target({ElementType.METHOD, ElementType.TYPE})
+@Retention(RetentionPolicy.RUNTIME)
+@Documented
+public @interface TenantCheck {
+    /** 参数中租户ID的字段名，默认为 tenantId */
+    String tenantIdParam() default "tenantId";
+}
+```
+
+**TenantInterceptor.java 核心逻辑：**
+```java
+// 1. 从 UserContext.getTenantId() 获取当前用户租户ID
+// 2. 从请求参数中提取目标租户ID（@TenantCheck指定的参数名）
+// 3. 若不一致，抛出 BusinessException(PERMISSION_DENIED)
+// 4. 若 UserContext.getTenantId() 为 null，跳过（如超级管理员）
+```
+
+- [x] **Task 4-1: 修改 JwtAuthenticationFilter，整合 UserContext 设置** ✅
+- [x] **Task 4-2: 创建 TenantCheck 注解 + TenantInterceptor 拦截器** ✅
+- [x] **Task 4-3: 创建 WebMvcConfig 注册拦截器** ✅
+- [x] **Task 4-4: 创建 SysTenantRepository + TenantDTO + PageRequest + TenantService + TenantServiceImpl + TenantController** ✅
+- [x] **Task 4-5: 编译验证 + Commit** ✅（含10个单元测试 + path variable支持 + UserContext递归bug修复）
+
+---
+
+## Task 5: RBAC权限体系 ⬜ 待实施
+
+> 设计文档 Task 5：用户角色、菜单权限、项目成员角色、接口权限声明
+
+**目标：** 实现用户-角色-权限三层模型的完整CRUD和接口级权限控制。
+
+**Files:**
+- Create: `backend/common/src/main/java/com/lc/common/annotation/PreAuthorize.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysMenuRepository.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysPermissionRepository.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysOrgRepository.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysDictRepository.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/dto/UserDTO.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/dto/RoleDTO.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/dto/MenuDTO.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/PermissionService.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/RoleService.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/MenuService.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/PermissionServiceImpl.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/RoleServiceImpl.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/MenuServiceImpl.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/controller/RoleController.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/controller/MenuController.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/controller/UserController.java`
+- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/interceptor/PermissionInterceptor.java`
+- Modify: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebMvcConfig.java`（注册权限拦截器）
+
+**Interfaces:**
+- Consumes: `UserContext`, `SysRoleRepository`（已存在）, `SysUserRoleRepository`（已存在）, `SysMenu/SysPermission`实体（已存在）
+- Produces: `PermissionService`（权限校验）, `PreAuthorize`注解, `PermissionInterceptor`, 角色/菜单/用户管理REST接口
+
+**关键设计：**
+1. 菜单权限：树形菜单结构，角色通过 sys_role_menu 关联菜单（需确认该关联表是否存在，若不存在需新增迁移脚本）
+2. 项目成员角色：viewer/editor/admin/publisher 四级，存储在 project_member 表的 role 字段
+3. 接口权限声明：`@PreAuthorize("user:list")` 注解，PermissionInterceptor 解析注解并调用 PermissionService 校验
+
+**PreAuthorize.java：**
+```java
+package com.lc.common.annotation;
+
+import java.lang.annotation.*;
+
+@Target({ElementType.METHOD, ElementType.TYPE})
+@Retention(RetentionPolicy.RUNTIME)
+@Documented
+public @interface PreAuthorize {
+    /** 所需权限码，多个用逗号分隔 */
+    String value() default "";
+    /** true=需全部权限，false=任一权限即可 */
+    boolean requireAll() default false;
+}
+```
+
+**PermissionService.java：**
+```java
+public interface PermissionService {
+    Set<String> getUserPermissions(Long userId);
+    boolean hasPermission(Long userId, String permission);
+    boolean hasAnyPermission(Long userId, String... permissions);
+    boolean hasAllPermissions(Long userId, String... permissions);
+    List<Long> getUserRoleIds(Long userId);
+}
+```
+
+**需确认：** sys_role_menu 关联表是否在 V3/V5 迁移脚本中已创建。若未创建，Task 5-1 需新增迁移脚本。
+
+- [x] **Task 5-1: 确认/补建 sys_role_menu 关联表迁移脚本** ✅（V13 + V14）
+- [x] **Task 5-2: 创建 PreAuthorize 注解 + PermissionService 接口 + PermissionServiceImpl** ✅
+- [x] **Task 5-3: 创建 PermissionInterceptor + 注册到 WebMvcConfig** ✅
+- [x] **Task 5-4: 创建 RoleService/MenuService + 实现 + Repository + DTO** ✅
+- [x] **Task 5-5: 创建 RoleController/MenuController/UserController** ✅
+- [x] **Task 5-6: 编译验证 + Commit** ✅（含3个Critical安全修复：租户隔离、越权访问、关联清理）
+
+---
+
+## Task 6: 对象存储与密钥管理 ⬜ 待实施
+
+> 设计文档 Task 6：快照/产物/附件存储，密钥加密与脱敏
+
+**目标：** 抽象对象存储接口，支持本地文件系统和MinIO切换，提供AES加密工具和文件上传接口。
+
+**Files:**
+- Create: `backend/common/src/main/java/com/lc/common/storage/StorageService.java`
+- Create: `backend/common/src/main/java/com/lc/common/storage/StorageProperties.java`
+- Create: `backend/common/src/main/java/com/lc/common/storage/LocalStorageServiceImpl.java`
+- Create: `backend/common/src/main/java/com/lc/common/storage/MinioStorageServiceImpl.java`
+- Create: `backend/common/src/main/java/com/lc/common/util/EncryptUtil.java`
+- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/StorageConfig.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/controller/FileController.java`
+- Modify: `backend/bootstrap/src/main/resources/application.yml`（添加 storage 配置）
+- Modify: `backend/common/pom.xml`（添加 MinIO SDK 依赖，optional）
+
+**Interfaces:**
+- Consumes: `StorageProperties`（配置）, `UserContext`（租户目录隔离）
+- Produces: `StorageService`（上传/下载/删除/预签名URL）, `FileController`（文件上传REST接口）, `EncryptUtil`（AES加解密）
+
+**关键设计：**
+1. 对象存储抽象：`StorageService` 接口统一上传/下载/删除/预签名URL，通过 `storage.type` 配置切换实现
+2. 密钥管理：`EncryptUtil` 提供 AES/ECB/PKCS5Padding 加解密，密钥通过环境变量 `LC_ENCRYPT_KEY` 注入
+3. 文件上传：类型校验（MIME白名单）、大小限制（默认10MB）、随机文件名、按 `tenant/{tenantId}/` 目录隔离
+
+**StorageService.java：**
+```java
+public interface StorageService {
+    String upload(String bucket, String key, InputStream input, String contentType, long size);
+    InputStream download(String bucket, String key);
+    void delete(String bucket, String key);
+    String getPresignedUrl(String bucket, String key, long expireSeconds);
+    boolean exists(String bucket, String key);
+}
+```
+
+**application.yml 新增配置：**
+```yaml
+storage:
+  type: local          # local | minio
+  local-path: ./storage
+  endpoint: ${MINIO_ENDPOINT:}
+  access-key: ${MINIO_ACCESS_KEY:}
+  secret-key: ${MINIO_SECRET_KEY:}
+  default-bucket: lc-platform
+  max-file-size: 10485760
+
+encrypt:
+  key: ${LC_ENCRYPT_KEY:lc-platform-2026-secure-key-32b}
+```
+
+- [x] **Task 6-1: 创建 StorageService 接口 + StorageProperties + LocalStorageServiceImpl** ✅
+- [x] **Task 6-2: 创建 MinioStorageServiceImpl + StorageConfig + 添加MinIO依赖(optional)** ✅
+- [x] **Task 6-3: 创建 EncryptUtil 加密工具** ✅
+- [x] **Task 6-4: 创建 FileController 文件上传接口 + 更新 application.yml** ✅
+- [x] **Task 6-5: 编译验证 + Commit** ✅（含路径遍历防护、MIME白名单、租户隔离校验）
+
+---
+
+## Task 7: 安全基线 ✅ 已完成
+
+> 设计文档 Task 7：SSRF防护、文件上传限制、审计日志、CORS/CSRF策略
+
+**目标：** 完善安全基础设施，包括审计日志AOP切面、文件上传校验器、SSRF防护工具、CORS配置。
+
+**Files:**
+- Create: `backend/common/src/main/java/com/lc/common/annotation/AuditLog.java`
+- Create: `backend/common/src/main/java/com/lc/common/security/FileUploadValidator.java`
+- Create: `backend/common/src/main/java/com/lc/common/security/SsrfProtector.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/entity/AuditLogEntity.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/repository/AuditLogRepository.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/AuditLogService.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/AuditLogServiceImpl.java`
+- Create: `backend/system-core/src/main/java/com/lc/system/controller/AuditLogController.java`
+- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/aspect/AuditLogAspect.java`
+- Create: `backend/system-core/src/main/resources/db/migration/V15__audit_log_align.sql`（补齐设计文档要求的字段，V13/V14 已被 sys_role_menu/sys_role_permission 占用）
+- Modify: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebConfig.java`（添加CORS配置）
+- Modify: `backend/bootstrap/pom.xml`（添加 spring-boot-starter-aop 依赖，若未引入）
+
+**Interfaces:**
+- Consumes: `UserContext`（获取操作用户/租户）, audit_log 表（V11已存在，需补齐字段）
+- Produces: `@AuditLog`注解, `AuditLogAspect`（AOP切面自动记录）, `FileUploadValidator`, `SsrfProtector`, CORS配置
+
+**关键设计：**
+1. 审计日志：`@AuditLog(action="用户登录", resourceType="USER")` 注解 + AOP切面，自动记录操作人、类型、资源、IP、结果
+2. audit_log 表对齐：V15 迁移脚本 ALTER TABLE 补齐 `action, resource_type, resource_id, client_ip, result, error_message` 字段（保留旧字段兼容；V13/V14 已被 sys_role_menu/sys_role_permission 占用）
+3. SSRF防护：`SsrfProtector.validateUrl(url)` 校验URL是否指向内网（10.x, 172.16-31.x, 192.168.x, 127.x, localhost），默认禁止
+4. 文件上传：`FileUploadValidator.validateImage(file)` / `validateDocument(file)` 校验类型+大小+路径遍历
+5. CORS：WebConfig 中配置 `addCorsMappings`，允许前端域名、允许凭证、限制方法
+
+**AuditLog.java（注解）：**
+```java
+package com.lc.common.annotation;
+
+import java.lang.annotation.*;
+
+@Target(ElementType.METHOD)
+@Retention(RetentionPolicy.RUNTIME)
+@Documented
+public @interface AuditLog {
+    String action();
+    String resourceType();
+    String resourceIdParam() default "";
+}
+```
+
+**V15__audit_log_align.sql：**
+```sql
+-- 对齐设计文档要求的审计日志字段
+ALTER TABLE audit_log ADD COLUMN action VARCHAR(128) COMMENT '操作类型(对齐设计文档)';
+ALTER TABLE audit_log ADD COLUMN resource_type VARCHAR(64) COMMENT '资源类型(对齐设计文档)';
+ALTER TABLE audit_log ADD COLUMN resource_id VARCHAR(128) COMMENT '资源ID(对齐设计文档)';
+ALTER TABLE audit_log ADD COLUMN client_ip VARCHAR(64) COMMENT '客户端IP(对齐设计文档)';
+ALTER TABLE audit_log ADD COLUMN result VARCHAR(32) NOT NULL DEFAULT 'SUCCESS' COMMENT '结果SUCCESS/FAILED';
+ALTER TABLE audit_log ADD COLUMN error_message VARCHAR(512) COMMENT '错误信息';
+-- 将旧字段数据迁移到新字段
+UPDATE audit_log SET action = operation WHERE action IS NULL AND operation IS NOT NULL;
+UPDATE audit_log SET resource_type = target_type WHERE resource_type IS NULL AND target_type IS NOT NULL;
+UPDATE audit_log SET resource_id = CAST(target_id AS CHAR) WHERE resource_id IS NULL AND target_id IS NOT NULL;
+UPDATE audit_log SET client_ip = ip WHERE client_ip IS NULL AND ip IS NOT NULL;
+```
+
+- [x] **Task 7-1: 创建 AuditLog 注解 + V15 迁移脚本对齐字段 + AuditLogEntity 实体** ✅（commit 6dc93d04，21 字段全对齐）
+- [x] **Task 7-2: 创建 AuditLogRepository + AuditLogService + AuditLogServiceImpl** ✅（commit d578e52e，Specification 动态查询）
+- [x] **Task 7-3: 创建 AuditLogAspect AOP切面（添加 AOP 依赖）** ✅（commit 71c9a163，@Around 拦截 + SpEL 提取 resourceId + 异常重抛）
+- [x] **Task 7-4: 创建 FileUploadValidator + SsrfProtector + AuditLogController** ✅（commit 5c90321c，含 DNS 解析防绕过 + 租户隔离校验 + FileController 重构）
+- [x] **Task 7-5: CORS 配置强化 + 编译验证 + Commit + 更新计划文档** ✅（可配置白名单 + 限定 headers/methods）
+
+---
+
+## 验收标准
+
+### Task 4 验收
+- [ ] 请求携带JWT时，UserContext 中能获取到 tenantId
+- [ ] 租户CRUD接口正常工作（GET/POST/PUT/DELETE /api/system/tenants）
+- [ ] @TenantCheck 注解生效，跨租户访问返回 403
+
+### Task 5 验收
+- [ ] 角色管理接口正常（创建、编辑、删除、分配菜单）
+- [ ] 菜单管理接口正常（树形结构返回）
+- [ ] @PreAuthorize 注解生效，无权限返回 403
+- [ ] 用户管理接口正常（分页查询、创建、编辑、删除、重置密码）
+
+### Task 6 验收
+- [ ] 文件上传接口可用，文件保存到 ./storage/{bucket}/{key}
+- [ ] 可通过 storage.type 配置切换本地存储和MinIO存储
+- [ ] AES 加解密工具正常工作
+
+### Task 7 验收
+- [x] @AuditLog 注解的方法自动记录审计日志到 audit_log 表（AuditLogAspect @Around，成功/失败双路径）
+- [x] 文件上传校验正确拦截非法文件（类型/大小/路径遍历）（FileUploadValidator.validate + validateImage/Document/General）
+- [x] SSRF 防护能正确识别并拦截内网URL（SsrfProtector：协议白名单 + 字符串内网段判定 + DNS 解析二次校验）
+- [x] CORS 配置生效，前端可正常跨域请求（WebConfig：可配置 origins，限定 headers/methods，allowCredentials）
+- [x] 审计日志查询接口可用（分页查询、按条件筛选）（AuditLogController.list + 租户隔离强制覆盖）
+
+---
+
+## 自审
+
+**1. Spec覆盖：**
+- ✅ Task 4: 租户识别（UserContext整合）、租户内唯一约束（复合索引+Service校验）、越权拦截器（TenantCheck+TenantInterceptor）
+- ✅ Task 5: 用户角色（RoleService）、菜单权限（MenuService树形）、项目成员角色（viewer/editor/admin/publisher四级）、接口权限声明（PreAuthorize注解+PermissionInterceptor）
+- ✅ Task 6: 对象存储（StorageService抽象+本地/MinIO实现）、密钥加密（EncryptUtil AES）、脱敏（审计日志before/after_data）
+- ✅ Task 7: SSRF防护（SsrfProtector）、文件上传限制（FileUploadValidator）、审计日志（@AuditLog+AOP）、CORS/CSRF策略（WebConfig CORS配置，CSRF已在SecurityConfig禁用）
+
+**2. 与已完成代码的衔接：**
+- ✅ UserContext/TenantContext 已存在，Task 4 不重复创建，只整合到 JwtAuthenticationFilter
+- ✅ GlobalErrorCode 已有 PERMISSION_DENIED/TENANT_NOT_FOUND，无需新增
+- ✅ JwtTokenService 已有 getTenantIdFromToken，Task 4 直接调用
+- ✅ audit_log 表 V11 已存在，Task 7 用 V13 补齐字段而非重建
+- ✅ 迁移脚本当前最大 V12，Task 7 使用 V13 无冲突
+
+**3. 待实施任务间的依赖：**
+- Task 5 的 PermissionInterceptor 依赖 Task 4 的 UserContext（已存在，无阻塞）
+- Task 7 的 AuditLogAspect 依赖 Task 4 的 UserContext（已存在，无阻塞）
+- Task 5/6/7 之间无强依赖，但建议按 4→5→6→7 顺序实施
diff --git a/docs/superpowers/plans/2026-07-27-phase1-scaffold-plan.md b/docs/superpowers/plans/2026-07-27-phase1-scaffold-plan.md
deleted file mode 100644
index 746c87ae..00000000
--- a/docs/superpowers/plans/2026-07-27-phase1-scaffold-plan.md
+++ /dev/null
@@ -1,976 +0,0 @@
-# Phase 1 脚手架搭建实施计划
-
-> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.
-
-**Goal:** 搭建低代码平台 Phase 1 脚手架，覆盖任务1-3（后端骨架、平台库初始化、认证与会话）
-
-**Architecture:** 后端采用 Spring Boot 3.2.x + Maven 多模块（14个模块扁平组织），前端采用 Vite 6 + React 18 + TypeScript + ProComponents。认证使用 JWT + RefreshToken + Redis。数据库使用 MySQL 8.0 + Flyway 迁移。
-
-**Tech Stack:** Spring Boot 3.2.x, Java 17, Maven 3.9.x, MySQL 8.0+, Redis 7.x, Flyway, JJWT 0.12.x, React 18, TypeScript 5.x, Vite 6.x, Ant Design 5.x, ProComponents 2.x, Axios, Lucide React
-
-## Global Constraints
-
-- Java: 17 (Spring Boot 3.2.x minimum)
-- Spring Boot: 3.2.5
-- Maven: 3.9.x
-- MySQL: 8.0+
-- Redis: 7.x
-- React: 18.2.0
-- TypeScript: 5.4.0
-- Vite: 6.0.0
-- Ant Design: 5.15.0
-- ProComponents: 2.6.0
-
----
-
-## 文件结构总览
-
-### 后端文件
-
-```
-backend/
-├── pom.xml                                    # 父POM
-├── common/                                    # 公共模块
-├── system-core/                               # 系统核心
-├── project-core/                              # 占位
-├── member-core/                               # 占位
-├── version-core/                              # 占位
-├── template-core/                             # 占位
-├── plugin-datasource/                         # 占位
-├── plugin-form/                               # 占位
-├── plugin-bi/                                 # 占位
-├── plugin-flow/                               # 占位
-├── plugin-api/                                # 占位
-├── sandbox-engine/                            # 占位
-├── code-generator/                            # 占位
-└── bootstrap/                                 # 启动模块
-```
-
-### 前端文件
-
-```
-frontend/
-├── package.json
-├── vite.config.ts
-├── tsconfig.json
-└── src/
-    ├── main.tsx
-    ├── App.tsx
-    ├── layouts/MainLayout.tsx
-    ├── pages/Login/index.tsx
-    ├── pages/Dashboard/index.tsx
-    ├── api/auth.ts
-    ├── hooks/useAuth.ts
-    ├── utils/request.ts
-    └── utils/token.ts
-```
-
----
-
-### Task 1: 后端父 POM 与目录结构
-
-**Files:**
-- Create: `backend/pom.xml`
-- Create: `backend/common/pom.xml`
-- Create: `backend/system-core/pom.xml`
-- Create: `backend/bootstrap/pom.xml`
-- Create: 11 个占位模块 pom.xml
-
-**核心代码:**
-
-父 POM (`backend/pom.xml`)：
-```xml
-<?xml version="1.0" encoding="UTF-8"?>
-<project xmlns="http://maven.apache.org/POM/4.0.0"
-         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
-         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
-    <modelVersion>4.0.0</modelVersion>
-    <groupId>com.lc</groupId>
-    <artifactId>lc-platform</artifactId>
-    <version>1.0.0-SNAPSHOT</version>
-    <packaging>pom</packaging>
-    <parent>
-        <groupId>org.springframework.boot</groupId>
-        <artifactId>spring-boot-starter-parent</artifactId>
-        <version>3.2.5</version>
-    </parent>
-    <properties>
-        <java.version>17</java.version>
-        <mapstruct.version>1.5.5.Final</mapstruct.version>
-        <jjwt.version>0.12.5</jjwt.version>
-    </properties>
-    <modules>
-        <module>common</module>
-        <module>system-core</module>
-        <module>project-core</module>
-        <module>member-core</module>
-        <module>version-core</module>
-        <module>template-core</module>
-        <module>plugin-datasource</module>
-        <module>plugin-form</module>
-        <module>plugin-bi</module>
-        <module>plugin-flow</module>
-        <module>plugin-api</module>
-        <module>sandbox-engine</module>
-        <module>code-generator</module>
-        <module>bootstrap</module>
-    </modules>
-    <dependencyManagement>
-        <dependencies>
-            <dependency>
-                <groupId>org.mapstruct</groupId>
-                <artifactId>mapstruct</artifactId>
-                <version>${mapstruct.version}</version>
-            </dependency>
-            <dependency>
-                <groupId>io.jsonwebtoken</groupId>
-                <artifactId>jjwt-api</artifactId>
-                <version>${jjwt.version}</version>
-            </dependency>
-            <dependency>
-                <groupId>io.jsonwebtoken</groupId>
-                <artifactId>jjwt-impl</artifactId>
-                <version>${jjwt.version}</version>
-                <scope>runtime</scope>
-            </dependency>
-            <dependency>
-                <groupId>io.jsonwebtoken</groupId>
-                <artifactId>jjwt-jackson</artifactId>
-                <version>${jjwt.version}</version>
-                <scope>runtime</scope>
-            </dependency>
-        </dependencies>
-    </dependencyManagement>
-</project>
-```
-
-- [ ] **Step 1: 创建父 POM**
-- [ ] **Step 2: 创建 common 模块 pom.xml**
-- [ ] **Step 3: 创建 system-core 模块 pom.xml**
-- [ ] **Step 4: 创建 bootstrap 模块 pom.xml**
-- [ ] **Step 5: 创建 11 个占位模块 pom.xml**
-- [ ] **Step 6: 编译验证**
-- [ ] **Step 7: Commit**
-
----
-
-### Task 2: common 模块核心代码
-
-**Files:**
-- Create: `backend/common/src/main/java/com/lc/common/exception/BusinessException.java`
-- Create: `backend/common/src/main/java/com/lc/common/exception/GlobalErrorCode.java`
-- Create: `backend/common/src/main/java/com/lc/common/dto/Result.java`
-- Create: `backend/common/src/main/java/com/lc/common/dto/PageResult.java`
-- Create: `backend/common/src/main/java/com/lc/common/context/TenantContext.java`
-- Create: `backend/common/src/main/java/com/lc/common/config/RedisConfig.java`
-- Create: `backend/common/src/main/java/com/lc/common/util/PasswordUtil.java`
-
-**核心代码:**
-
-BusinessException.java：
-```java
-package com.lc.common.exception;
-
-import lombok.Getter;
-
-@Getter
-public class BusinessException extends RuntimeException {
-    private final int code;
-    public BusinessException(int code, String message) {
-        super(message);
-        this.code = code;
-    }
-    public BusinessException(GlobalErrorCode errorCode) {
-        super(errorCode.getMessage());
-        this.code = errorCode.getCode();
-    }
-}
-```
-
-GlobalErrorCode.java：
-```java
-package com.lc.common.exception;
-
-import lombok.Getter;
-
-@Getter
-public enum GlobalErrorCode {
-    SUCCESS(0, "success"), FAIL(-1, "fail"), UNAUTHORIZED(401, "未授权"),
-    FORBIDDEN(403, "禁止访问"), NOT_FOUND(404, "资源不存在"),
-    USER_NOT_FOUND(1001, "用户不存在"), USERNAME_OR_PASSWORD_ERROR(1002, "用户名或密码错误"),
-    TOKEN_EXPIRED(1003, "Token已过期"), TOKEN_INVALID(1004, "Token无效"),
-    SYSTEM_ERROR(500, "系统内部错误");
-
-    private final int code;
-    private final String message;
-    GlobalErrorCode(int code, String message) {
-        this.code = code;
-        this.message = message;
-    }
-}
-```
-
-Result.java：
-```java
-package com.lc.common.dto;
-
-import lombok.AllArgsConstructor;
-import lombok.Builder;
-import lombok.Data;
-import lombok.NoArgsConstructor;
-
-@Data
-@Builder
-@NoArgsConstructor
-@AllArgsConstructor
-public class Result<T> {
-    private int code;
-    private String message;
-    private T data;
-    private long timestamp;
-
-    public static <T> Result<T> success(T data) {
-        return Result.<T>builder().code(0).message("success").data(data).timestamp(System.currentTimeMillis()).build();
-    }
-    public static <T> Result<T> fail(int code, String message) {
-        return Result.<T>builder().code(code).message(message).timestamp(System.currentTimeMillis()).build();
-    }
-}
-```
-
-- [ ] **Step 1-7: 创建各文件**
-- [ ] **Step 8: 编译验证**
-- [ ] **Step 9: Commit**
-
----
-
-### Task 3: system-core 模块 - 实体类与数据库迁移
-
-**Files:**
-- Create: 10 个 JPA 实体类 (SysTenant, SysUser, SysRole, SysUserRole, SysMenu, SysPermission, SysOrg, SysDict, ProjectInfo, ProjectMember)
-- Create: 11 个 Flyway 迁移脚本
-
-**核心代码:**
-
-SysUser.java：
-```java
-package com.lc.system.entity;
-
-import jakarta.persistence.*;
-import lombok.Data;
-import java.time.LocalDateTime;
-
-@Data
-@Entity
-@Table(name = "sys_user")
-public class SysUser {
-    @Id
-    @GeneratedValue(strategy = GenerationType.IDENTITY)
-    private Long id;
-    @Column(name = "tenant_id")
-    private Long tenantId;
-    @Column(name = "username", nullable = false, length = 64)
-    private String username;
-    @Column(name = "password", nullable = false, length = 256)
-    private String password;
-    @Column(name = "real_name", length = 64)
-    private String realName;
-    @Column(name = "email", length = 128)
-    private String email;
-    @Column(name = "status", nullable = false)
-    private Integer status = 1;
-    @Column(name = "created_time", nullable = false, updatable = false)
-    private LocalDateTime createdTime;
-    @Column(name = "updated_time")
-    private LocalDateTime updatedTime;
-    @PrePersist
-    protected void onCreate() {
-        createdTime = LocalDateTime.now();
-        updatedTime = LocalDateTime.now();
-    }
-    @PreUpdate
-    protected void onUpdate() {
-        updatedTime = LocalDateTime.now();
-    }
-}
-```
-
-V1__sys_tenant.sql：
-```sql
-CREATE TABLE IF NOT EXISTS sys_tenant (
-    id BIGINT AUTO_INCREMENT PRIMARY KEY,
-    tenant_code VARCHAR(64) NOT NULL UNIQUE,
-    tenant_name VARCHAR(128) NOT NULL,
-    status INT NOT NULL DEFAULT 1,
-    created_time DATETIME NOT NULL,
-    updated_time DATETIME
-) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
-```
-
-- [ ] **Step 1-21: 创建实体类和迁移脚本**
-- [ ] **Step 22: 编译验证**
-- [ ] **Step 23: Commit**
-
----
-
-### Task 4: system-core 模块 - Repository 与 Service
-
-**Files:**
-- Create: `SysUserRepository.java`
-- Create: `SysRoleRepository.java`
-- Create: `SysUserRoleRepository.java`
-- Create: `UserService.java`
-- Create: `UserServiceImpl.java`
-
-**核心代码:**
-
-SysUserRepository.java：
-```java
-package com.lc.system.repository;
-
-import com.lc.system.entity.SysUser;
-import org.springframework.data.jpa.repository.JpaRepository;
-import java.util.Optional;
-
-public interface SysUserRepository extends JpaRepository<SysUser, Long> {
-    Optional<SysUser> findByUsername(String username);
-    boolean existsByUsername(String username);
-}
-```
-
-UserServiceImpl.java：
-```java
-package com.lc.system.service.impl;
-
-import com.lc.common.exception.BusinessException;
-import com.lc.common.exception.GlobalErrorCode;
-import com.lc.common.util.PasswordUtil;
-import com.lc.system.entity.SysUser;
-import com.lc.system.repository.SysUserRepository;
-import com.lc.system.service.UserService;
-import lombok.RequiredArgsConstructor;
-import org.springframework.stereotype.Service;
-import org.springframework.transaction.annotation.Transactional;
-
-@Service
-@RequiredArgsConstructor
-public class UserServiceImpl implements UserService {
-    private final SysUserRepository userRepository;
-
-    @Override
-    public SysUser findByUsername(String username) {
-        return userRepository.findByUsername(username)
-                .orElseThrow(() -> new BusinessException(GlobalErrorCode.USER_NOT_FOUND));
-    }
-
-    @Override
-    @Transactional
-    public SysUser createUser(SysUser user) {
-        if (userRepository.existsByUsername(user.getUsername())) {
-            throw new BusinessException(GlobalErrorCode.DATA_CONFLICT);
-        }
-        if (user.getPassword() != null) {
-            user.setPassword(PasswordUtil.encode(user.getPassword()));
-        }
-        return userRepository.save(user);
-    }
-
-    @Override
-    public boolean verifyPassword(String rawPassword, String encodedPassword) {
-        return PasswordUtil.matches(rawPassword, encodedPassword);
-    }
-}
-```
-
-- [ ] **Step 1-5: 创建各文件**
-- [ ] **Step 6: 编译验证**
-- [ ] **Step 7: Commit**
-
----
-
-### Task 5: system-core 模块 - JWT 与 Token 服务
-
-**Files:**
-- Create: `JwtConfig.java`
-- Create: `AuthDTO.java`
-- Create: `JwtTokenService.java`
-- Create: `RefreshTokenService.java`
-
-**核心代码:**
-
-JwtTokenService.java：
-```java
-package com.lc.system.security;
-
-import com.lc.common.exception.BusinessException;
-import com.lc.common.exception.GlobalErrorCode;
-import com.lc.system.config.JwtConfig;
-import com.lc.system.entity.SysUser;
-import io.jsonwebtoken.Claims;
-import io.jsonwebtoken.Jwts;
-import io.jsonwebtoken.security.Keys;
-import lombok.RequiredArgsConstructor;
-import org.springframework.stereotype.Service;
-
-import javax.crypto.SecretKey;
-import java.nio.charset.StandardCharsets;
-import java.time.LocalDateTime;
-import java.time.ZoneId;
-import java.util.Date;
-import java.util.HashMap;
-import java.util.Map;
-
-@Service
-@RequiredArgsConstructor
-public class JwtTokenService {
-    private final JwtConfig jwtConfig;
-
-    private SecretKey getSigningKey() {
-        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
-    }
-
-    public String generateAccessToken(SysUser user) {
-        Map<String, Object> claims = new HashMap<>();
-        claims.put("userId", user.getId());
-        claims.put("tenantId", user.getTenantId());
-        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(jwtConfig.getAccessTokenExpireMinutes());
-        return Jwts.builder()
-                .claims(claims)
-                .subject(user.getUsername())
-                .expiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
-                .signWith(getSigningKey(), Jwts.SIG.HS256)
-                .compact();
-    }
-
-    public Claims parseToken(String token) {
-        try {
-            return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
-        } catch (Exception e) {
-            throw new BusinessException(GlobalErrorCode.TOKEN_INVALID);
-        }
-    }
-}
-```
-
-RefreshTokenService.java：
-```java
-package com.lc.system.security;
-
-import com.lc.common.exception.BusinessException;
-import com.lc.common.exception.GlobalErrorCode;
-import com.lc.system.config.JwtConfig;
-import lombok.RequiredArgsConstructor;
-import org.springframework.data.redis.core.RedisTemplate;
-import org.springframework.stereotype.Service;
-
-import java.util.concurrent.TimeUnit;
-
-@Service
-@RequiredArgsConstructor
-public class RefreshTokenService {
-    private final RedisTemplate<String, Object> redisTemplate;
-    private final JwtConfig jwtConfig;
-
-    private static final String REFRESH_TOKEN_KEY = "refresh:%s";
-
-    public void saveRefreshToken(Long userId, String refreshToken) {
-        String key = String.format(REFRESH_TOKEN_KEY, userId);
-        redisTemplate.opsForValue().set(key, refreshToken, jwtConfig.getRefreshTokenExpireDays(), TimeUnit.DAYS);
-    }
-
-    public String getRefreshToken(Long userId) {
-        String key = String.format(REFRESH_TOKEN_KEY, userId);
-        Object token = redisTemplate.opsForValue().get(key);
-        return token != null ? token.toString() : null;
-    }
-
-    public void invalidateRefreshToken(Long userId) {
-        String key = String.format(REFRESH_TOKEN_KEY, userId);
-        redisTemplate.delete(key);
-    }
-
-    public boolean validateRefreshToken(Long userId, String token) {
-        String storedToken = getRefreshToken(userId);
-        if (storedToken == null) {
-            throw new BusinessException(GlobalErrorCode.REFRESH_TOKEN_EXPIRED);
-        }
-        if (!storedToken.equals(token)) {
-            throw new BusinessException(GlobalErrorCode.REFRESH_TOKEN_INVALID);
-        }
-        return true;
-    }
-}
-```
-
-- [ ] **Step 1-4: 创建各文件**
-- [ ] **Step 5: 编译验证**
-- [ ] **Step 6: Commit**
-
----
-
-### Task 6: bootstrap 模块 - 启动类与配置
-
-**Files:**
-- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/Application.java`
-- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/SecurityConfig.java`
-- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebConfig.java`
-- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/filter/JwtAuthenticationFilter.java`
-- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/handler/GlobalExceptionHandler.java`
-- Create: `backend/bootstrap/src/main/resources/application.yml`
-
-**核心代码:**
-
-Application.java：
-```java
-package com.lc.bootstrap;
-
-import org.springframework.boot.SpringApplication;
-import org.springframework.boot.autoconfigure.SpringBootApplication;
-import org.springframework.boot.context.properties.EnableConfigurationProperties;
-import org.springframework.context.annotation.ComponentScan;
-
-@SpringBootApplication
-@ComponentScan(basePackages = {"com.lc"})
-@EnableConfigurationProperties
-public class Application {
-    public static void main(String[] args) {
-        SpringApplication.run(Application.class, args);
-    }
-}
-```
-
-SecurityConfig.java：
-```java
-package com.lc.bootstrap.config;
-
-import com.lc.bootstrap.filter.JwtAuthenticationFilter;
-import lombok.RequiredArgsConstructor;
-import org.springframework.context.annotation.Bean;
-import org.springframework.context.annotation.Configuration;
-import org.springframework.security.authentication.AuthenticationManager;
-import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
-import org.springframework.security.config.annotation.web.builders.HttpSecurity;
-import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
-import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
-import org.springframework.security.config.http.SessionCreationPolicy;
-import org.springframework.security.web.SecurityFilterChain;
-import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
-
-@Configuration
-@EnableWebSecurity
-@RequiredArgsConstructor
-public class SecurityConfig {
-    private final JwtAuthenticationFilter jwtAuthenticationFilter;
-
-    @Bean
-    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
-        http
-                .csrf(AbstractHttpConfigurer::disable)
-                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
-                .authorizeHttpRequests(auth -> auth
-                        .requestMatchers("/api/auth/login", "/api/auth/refresh").permitAll()
-                        .anyRequest().authenticated())
-                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
-        return http.build();
-    }
-
-    @Bean
-    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
-        return config.getAuthenticationManager();
-    }
-}
-```
-
-application.yml：
-```yaml
-server:
-  port: 8080
-
-spring:
-  datasource:
-    url: jdbc:mysql://localhost:3306/lc_platform?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
-    username: root
-    password: password
-    driver-class-name: com.mysql.cj.jdbc.Driver
-  data:
-    redis:
-      host: localhost
-      port: 6379
-  flyway:
-    enabled: true
-    locations: classpath:db/migration
-    baseline-on-migrate: true
-
-jwt:
-  secret: your-256-bit-secret-key-here-must-be-at-least-32-characters
-  access-token-expire-minutes: 15
-  refresh-token-expire-days: 7
-```
-
-- [ ] **Step 1-6: 创建各文件**
-- [ ] **Step 7: 编译验证**
-- [ ] **Step 8: Commit**
-
----
-
-### Task 7: bootstrap 模块 - 认证控制器
-
-**Files:**
-- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/controller/AuthController.java`
-
-**核心代码:**
-
-AuthController.java：
-```java
-package com.lc.bootstrap.controller;
-
-import com.lc.common.dto.Result;
-import com.lc.common.exception.BusinessException;
-import com.lc.common.exception.GlobalErrorCode;
-import com.lc.system.dto.AuthDTO;
-import com.lc.system.entity.SysUser;
-import com.lc.system.security.JwtTokenService;
-import com.lc.system.security.RefreshTokenService;
-import com.lc.system.service.UserService;
-import lombok.RequiredArgsConstructor;
-import org.springframework.web.bind.annotation.*;
-
-import java.time.LocalDateTime;
-
-@RestController
-@RequestMapping("/api/auth")
-@RequiredArgsConstructor
-public class AuthController {
-    private final UserService userService;
-    private final JwtTokenService jwtTokenService;
-    private final RefreshTokenService refreshTokenService;
-
-    @PostMapping("/login")
-    public Result<AuthDTO.LoginResponse> login(@RequestBody AuthDTO.LoginRequest request) {
-        SysUser user = userService.findByUsername(request.getUsername());
-        if (!userService.verifyPassword(request.getPassword(), user.getPassword())) {
-            throw new BusinessException(GlobalErrorCode.USERNAME_OR_PASSWORD_ERROR);
-        }
-
-        String accessToken = jwtTokenService.generateAccessToken(user);
-        String refreshToken = jwtTokenService.generateRefreshToken(user);
-        refreshTokenService.saveRefreshToken(user.getId(), refreshToken);
-
-        AuthDTO.LoginResponse response = AuthDTO.LoginResponse.builder()
-                .accessToken(accessToken)
-                .refreshToken(refreshToken)
-                .user(AuthDTO.UserInfo.builder()
-                        .id(user.getId())
-                        .username(user.getUsername())
-                        .realName(user.getRealName())
-                        .tenantId(user.getTenantId())
-                        .build())
-                .build();
-
-        return Result.success(response);
-    }
-
-    @PostMapping("/refresh")
-    public Result<AuthDTO.LoginResponse> refresh(@RequestBody AuthDTO.RefreshRequest request) {
-        Long userId = jwtTokenService.getUserIdFromToken(request.getRefreshToken());
-        refreshTokenService.validateRefreshToken(userId, request.getRefreshToken());
-        SysUser user = userService.findById(userId);
-
-        String accessToken = jwtTokenService.generateAccessToken(user);
-        String newRefreshToken = jwtTokenService.generateRefreshToken(user);
-        refreshTokenService.saveRefreshToken(userId, newRefreshToken);
-
-        AuthDTO.LoginResponse response = AuthDTO.LoginResponse.builder()
-                .accessToken(accessToken)
-                .refreshToken(newRefreshToken)
-                .user(AuthDTO.UserInfo.builder()
-                        .id(user.getId())
-                        .username(user.getUsername())
-                        .realName(user.getRealName())
-                        .tenantId(user.getTenantId())
-                        .build())
-                .build();
-
-        return Result.success(response);
-    }
-}
-```
-
-- [ ] **Step 1: 创建 AuthController**
-- [ ] **Step 2: 编译验证**
-- [ ] **Step 3: Commit**
-
----
-
-### Task 8: 前端项目初始化
-
-**Files:**
-- Create: `frontend/package.json`
-- Create: `frontend/vite.config.ts`
-- Create: `frontend/tsconfig.json`
-- Create: `frontend/index.html`
-- Create: `frontend/src/main.tsx`
-- Create: `frontend/src/App.tsx`
-
-**核心代码:**
-
-package.json：
-```json
-{
-  "name": "lc-platform-frontend",
-  "version": "1.0.0",
-  "private": true,
-  "type": "module",
-  "scripts": {
-    "dev": "vite",
-    "build": "tsc && vite build",
-    "preview": "vite preview"
-  },
-  "dependencies": {
-    "react": "^18.2.0",
-    "react-dom": "^18.2.0",
-    "antd": "^5.15.0",
-    "@ant-design/pro-components": "^2.6.0",
-    "axios": "^1.6.0",
-    "lucide-react": "^0.310.0"
-  },
-  "devDependencies": {
-    "@types/react": "^18.2.0",
-    "@types/react-dom": "^18.2.0",
-    "@vitejs/plugin-react": "^4.2.0",
-    "typescript": "^5.4.0",
-    "vite": "^6.0.0"
-  }
-}
-```
-
-vite.config.ts：
-```typescript
-import { defineConfig } from 'vite'
-import react from '@vitejs/plugin-react'
-
-export default defineConfig({
-  plugins: [react()],
-  server: {
-    port: 5173,
-    proxy: {
-      '/api': {
-        target: 'http://localhost:8080',
-        changeOrigin: true
-      }
-    }
-  }
-})
-```
-
-- [ ] **Step 1-6: 创建各文件**
-- [ ] **Step 7: 安装依赖并验证**
-- [ ] **Step 8: Commit**
-
----
-
-### Task 9: 前端 - 请求封装与认证
-
-**Files:**
-- Create: `frontend/src/utils/token.ts`
-- Create: `frontend/src/utils/request.ts`
-- Create: `frontend/src/hooks/useAuth.ts`
-- Create: `frontend/src/api/auth.ts`
-
-**核心代码:**
-
-token.ts：
-```typescript
-const ACCESS_TOKEN_KEY = 'lc_access_token'
-
-export const getAccessToken = () => {
-  return localStorage.getItem(ACCESS_TOKEN_KEY)
-}
-
-export const setAccessToken = (token: string) => {
-  localStorage.setItem(ACCESS_TOKEN_KEY, token)
-}
-
-export const removeAccessToken = () => {
-  localStorage.removeItem(ACCESS_TOKEN_KEY)
-}
-```
-
-request.ts：
-```typescript
-import axios from 'axios'
-import { getAccessToken, removeAccessToken } from './token'
-import { message } from 'antd'
-
-const request = axios.create({
-  baseURL: '/api',
-  timeout: 10000
-})
-
-request.interceptors.request.use(
-  (config) => {
-    const token = getAccessToken()
-    if (token) {
-      config.headers.Authorization = `Bearer ${token}`
-    }
-    return config
-  },
-  (error) => {
-    return Promise.reject(error)
-  }
-)
-
-request.interceptors.response.use(
-  (response) => {
-    return response.data
-  },
-  (error) => {
-    if (error.response?.status === 401) {
-      removeAccessToken()
-      message.error('登录已过期，请重新登录')
-      window.location.href = '/login'
-    }
-    return Promise.reject(error)
-  }
-)
-
-export default request
-```
-
-useAuth.ts：
-```typescript
-import { useState, useEffect } from 'react'
-import { getAccessToken, setAccessToken, removeAccessToken } from '../utils/token'
-import { login, refreshToken } from '../api/auth'
-
-interface UserInfo {
-  id: number
-  username: string
-  realName: string
-  tenantId: number | null
-}
-
-export const useAuth = () => {
-  const [user, setUser] = useState<UserInfo | null>(null)
-  const [loading, setLoading] = useState(false)
-
-  const isLoggedIn = () => !!getAccessToken()
-
-  const handleLogin = async (username: string, password: string) => {
-    setLoading(true)
-    try {
-      const result = await login(username, password)
-      setAccessToken(result.data.accessToken)
-      setUser(result.data.user)
-      return true
-    } catch (error) {
-      return false
-    } finally {
-      setLoading(false)
-    }
-  }
-
-  const handleLogout = () => {
-    removeAccessToken()
-    setUser(null)
-    window.location.href = '/login'
-  }
-
-  return { user, loading, isLoggedIn, handleLogin, handleLogout }
-}
-```
-
-- [ ] **Step 1-4: 创建各文件**
-- [ ] **Step 5: Commit**
-
----
-
-### Task 10: 前端 - 登录页与首页
-
-**Files:**
-- Create: `frontend/src/pages/Login/index.tsx`
-- Create: `frontend/src/pages/Dashboard/index.tsx`
-- Create: `frontend/src/layouts/MainLayout.tsx`
-
-**核心代码:**
-
-Login/index.tsx：
-```typescript
-import React from 'react'
-import { Form, Input, Button, Card, message } from 'antd'
-import { useAuth } from '../../hooks/useAuth'
-import { User, Lock } from 'lucide-react'
-
-const Login: React.FC = () => {
-  const { handleLogin, loading } = useAuth()
-  const [form] = Form.useForm()
-
-  const onFinish = async (values: { username: string; password: string }) => {
-    const success = await handleLogin(values.username, values.password)
-    if (success) {
-      message.success('登录成功')
-      window.location.href = '/dashboard'
-    } else {
-      message.error('用户名或密码错误')
-    }
-  }
-
-  return (
-    <div style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }}>
-      <Card style={{ width: 400, boxShadow: '0 10px 40px rgba(0,0,0,0.2)' }}>
-        <h2 style={{ textAlign: 'center', marginBottom: 30 }}>低代码平台</h2>
-        <Form form={form} onFinish={onFinish} layout="vertical">
-          <Form.Item name="username" rules={[{ required: true, message: '请输入用户名' }]}>
-            <Input prefix={<User />} placeholder="用户名" />
-          </Form.Item>
-          <Form.Item name="password" rules={[{ required: true, message: '请输入密码' }]}>
-            <Input.Password prefix={<Lock />} placeholder="密码" />
-          </Form.Item>
-          <Form.Item>
-            <Button type="primary" htmlType="submit" loading={loading} style={{ width: '100%' }}>
-              登录
-            </Button>
-          </Form.Item>
-        </Form>
-      </Card>
-    </div>
-  )
-}
-
-export default Login
-```
-
-- [ ] **Step 1-3: 创建各文件**
-- [ ] **Step 4: Commit**
-
----
-
-## 自审
-
-1. **Spec覆盖**：所有设计文档中的任务1-3需求均有对应任务
-2. **占位符检查**：无"TBD"或"TODO"，所有代码已完整给出
-3. **类型一致性**：前后端接口类型一致，AuthDTO 与前端类型对应
-
----
-
-## 验收标准
-
-### 后端验收
-- [ ] Maven 多模块可编译通过
-- [ ] bootstrap 可正常启动（端口 8080）
-- [ ] Flyway 自动执行迁移，创建所有核心表
-- [ ] `/api/auth/login` 接口可用，返回 AccessToken/RefreshToken
-- [ ] `/api/auth/refresh` 接口可用，刷新 Token
-- [ ] JWT 认证过滤器生效，未登录请求返回 401
-
-### 前端验收
-- [ ] `npm install` 成功
-- [ ] `npm run dev` 正常启动（端口 5173）
-- [ ] 登录页可访问
-- [ ] 登录成功后重定向到首页
-- [ ] 请求自动附加 Authorization 头
-- [ ] 401 自动跳转到登录页
-
----
-
-Plan complete. Ready for execution.
diff --git a/docs/superpowers/plans/2026-07-27-phase1-tasks4-7-plan.md b/docs/superpowers/plans/2026-07-27-phase1-tasks4-7-plan.md
deleted file mode 100644
index 0dc80e48..00000000
--- a/docs/superpowers/plans/2026-07-27-phase1-tasks4-7-plan.md
+++ /dev/null
@@ -1,1005 +0,0 @@
-# Phase 1 任务4-7实施计划
-
-> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.
-
-**Goal:** 完善低代码平台 Phase 1 的多租户体系、RBAC权限、对象存储和安全基线，为后续业务模块打下坚实基础。
-
-**Architecture:** 
-- 多租户：基于 JWT 携带 tenantId，通过 ThreadLocal 传递租户上下文，拦截器统一校验租户权限
-- RBAC：用户-角色-权限三层模型，基于 Spring Security + 自定义注解实现接口级权限控制
-- 对象存储：抽象 StorageService 接口，默认支持 MinIO/S3，本地文件系统作为开发备选
-- 安全基线：SSRF防护通过URL白名单+内网IP检测，文件上传通过类型+大小+内容检测，审计日志通过AOP切面自动记录
-
-**Tech Stack:** Spring Boot 3.2.x, Java 17, MinIO SDK, Spring Security 6.x, AOP, Apache Commons FileUpload
-
-## Global Constraints
-
-- Java: 17 (Spring Boot 3.2.x minimum)
-- Spring Boot: 3.2.5
-- Maven: 3.9.x
-- MySQL: 8.0+
-- Redis: 7.x
-- React: 18.2.0
-- TypeScript: 5.4.0
-- Vite: 6.0.0
-- Ant Design: 5.15.0
-- 多租户约束：租户内唯一字段使用 (tenant_id, code) 复合唯一
-- 安全约束：密钥、密码等敏感字段不得明文存储
-- 审计约束：登录、权限变更、项目操作等关键操作必须记录审计日志
-
----
-
-## 文件结构总览
-
-### 后端新增文件
-
-```
-backend/common/src/main/java/com/lc/common/
-├── annotation/
-│   ├── TenantCheck.java              # 租户校验注解
-│   ├── PreAuthorize.java             # 权限校验注解
-│   └── AuditLog.java                 # 审计日志注解
-├── context/
-│   └── UserContext.java              # 用户上下文（补充TenantContext）
-├── security/
-│   ├── SsrfProtector.java            # SSRF防护工具
-│   └── FileUploadValidator.java      # 文件上传校验器
-└── storage/
-    ├── StorageService.java           # 对象存储接口
-    └── LocalStorageServiceImpl.java  # 本地存储实现（开发用）
-
-backend/system-core/src/main/java/com/lc/system/
-├── controller/
-│   ├── TenantController.java         # 租户管理接口
-│   ├── UserController.java           # 用户管理接口
-│   ├── RoleController.java           # 角色管理接口
-│   ├── MenuController.java           # 菜单管理接口
-│   └── DictController.java           # 字典管理接口
-├── service/
-│   ├── TenantService.java            # 租户服务接口
-│   ├── RoleService.java              # 角色服务接口
-│   ├── MenuService.java              # 菜单服务接口
-│   ├── PermissionService.java        # 权限服务接口
-│   ├── AuditLogService.java          # 审计日志服务接口
-│   └── impl/
-│       ├── TenantServiceImpl.java
-│       ├── RoleServiceImpl.java
-│       ├── MenuServiceImpl.java
-│       ├── PermissionServiceImpl.java
-│       └── AuditLogServiceImpl.java
-├── repository/
-│   ├── SysTenantRepository.java
-│   ├── SysRoleRepository.java
-│   ├── SysMenuRepository.java
-│   ├── SysPermissionRepository.java
-│   ├── SysOrgRepository.java
-│   ├── SysDictRepository.java
-│   └── AuditLogRepository.java
-├── entity/
-│   └── AuditLog.java                 # 审计日志实体
-├── dto/
-│   ├── TenantDTO.java
-│   ├── UserDTO.java
-│   ├── RoleDTO.java
-│   ├── MenuDTO.java
-│   └── PageRequest.java              # 分页请求基类
-└── security/
-    └── PermissionInterceptor.java    # 权限拦截器
-
-backend/bootstrap/src/main/java/com/lc/bootstrap/
-├── filter/
-│   └── TenantContextFilter.java      # 租户上下文过滤器
-├── interceptor/
-│   └── PermissionInterceptor.java    # 权限校验拦截器
-├── aspect/
-│   └── AuditLogAspect.java           # 审计日志切面
-└── config/
-    ├── StorageConfig.java            # 存储配置
-    └── WebMvcConfig.java             # Web配置（拦截器注册）
-```
-
-### 前端新增文件
-
-```
-frontend/src/
-├── layouts/
-│   └── MainLayout.tsx                # 主布局（侧边栏+头部）
-├── pages/
-│   ├── system/
-│   │   ├── Tenant.tsx                # 租户管理
-│   │   ├── User.tsx                  # 用户管理
-│   │   ├── Role.tsx                  # 角色管理
-│   │   ├── Menu.tsx                  # 菜单管理
-│   │   └── Dict.tsx                  # 字典管理
-│   └── audit/
-│       └── Log.tsx                   # 审计日志
-├── api/
-│   ├── system.ts                     # 系统管理API
-│   └── audit.ts                      # 审计API
-├── types/
-│   └── system.ts                     # 系统管理类型
-└── utils/
-    └── permission.ts                 # 权限工具函数
-```
-
----
-
-### Task 4: 多租户上下文体系
-
-**Files:**
-- Create: `backend/common/src/main/java/com/lc/common/context/UserContext.java`
-- Create: `backend/common/src/main/java/com/lc/common/annotation/TenantCheck.java`
-- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/filter/TenantContextFilter.java`
-- Modify: `backend/bootstrap/src/main/java/com/lc/bootstrap/filter/JwtAuthenticationFilter.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysTenantRepository.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/service/TenantService.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/TenantServiceImpl.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/controller/TenantController.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/dto/TenantDTO.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/dto/PageRequest.java`
-
-**Interfaces:**
-- Consumes: JwtTokenService（从Token解析tenantId）, SysTenant实体
-- Produces: TenantContextHolder（线程级租户上下文）, TenantService（租户CRUD）
-
-**核心设计：**
-
-1. **租户识别**：从 JWT Token 的 claims 中提取 tenantId，放入 ThreadLocal
-2. **租户内唯一约束**：数据库层使用 `(tenant_id, code)` 复合唯一索引，Service 层做前置校验
-3. **越权拦截**：通过 TenantCheck 注解 + AOP/拦截器，校验当前用户只能操作本租户数据
-
-**UserContext.java：**
-```java
-package com.lc.common.context;
-
-import lombok.AllArgsConstructor;
-import lombok.Builder;
-import lombok.Data;
-import lombok.NoArgsConstructor;
-
-@Data
-@Builder
-@NoArgsConstructor
-@AllArgsConstructor
-public class UserContext {
-    private Long userId;
-    private Long tenantId;
-    private String username;
-
-    private static final ThreadLocal<UserContext> HOLDER = new ThreadLocal<>();
-
-    public static void set(UserContext context) {
-        HOLDER.set(context);
-    }
-
-    public static UserContext get() {
-        return HOLDER.get();
-    }
-
-    public static void clear() {
-        HOLDER.remove();
-    }
-
-    public static Long getUserId() {
-        UserContext ctx = HOLDER.get();
-        return ctx != null ? ctx.getUserId() : null;
-    }
-
-    public static Long getTenantId() {
-        UserContext ctx = HOLDER.get();
-        return ctx != null ? ctx.getTenantId() : null;
-    }
-}
-```
-
-**TenantCheck.java：**
-```java
-package com.lc.common.annotation;
-
-import java.lang.annotation.*;
-
-@Target({ElementType.METHOD, ElementType.TYPE})
-@Retention(RetentionPolicy.RUNTIME)
-@Documented
-public @interface TenantCheck {
-    boolean required() default true;
-}
-```
-
-**TenantContextFilter.java：**
-```java
-package com.lc.bootstrap.filter;
-
-import com.lc.common.context.UserContext;
-import com.lc.system.security.JwtTokenService;
-import jakarta.servlet.*;
-import jakarta.servlet.http.HttpServletRequest;
-import lombok.RequiredArgsConstructor;
-import org.springframework.stereotype.Component;
-import org.springframework.util.StringUtils;
-
-import java.io.IOException;
-
-@Component
-@RequiredArgsConstructor
-public class TenantContextFilter implements Filter {
-    private final JwtTokenService jwtTokenService;
-
-    @Override
-    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
-            throws IOException, ServletException {
-        try {
-            HttpServletRequest httpRequest = (HttpServletRequest) request;
-            String token = extractToken(httpRequest);
-            if (StringUtils.hasText(token)) {
-                try {
-                    Long userId = jwtTokenService.getUserIdFromToken(token);
-                    Long tenantId = jwtTokenService.getTenantIdFromToken(token);
-                    String username = jwtTokenService.getUsernameFromToken(token);
-                    UserContext.set(UserContext.builder()
-                            .userId(userId)
-                            .tenantId(tenantId)
-                            .username(username)
-                            .build());
-                } catch (Exception ignored) {
-                }
-            }
-            chain.doFilter(request, response);
-        } finally {
-            UserContext.clear();
-        }
-    }
-
-    private String extractToken(HttpServletRequest request) {
-        String bearerToken = request.getHeader("Authorization");
-        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
-            return bearerToken.substring(7);
-        }
-        return null;
-    }
-}
-```
-
-**TenantService.java：**
-```java
-package com.lc.system.service;
-
-import com.lc.common.dto.PageResult;
-import com.lc.system.dto.TenantDTO;
-import com.lc.system.entity.SysTenant;
-
-public interface TenantService {
-    PageResult<TenantDTO> list(String keyword, int page, int size);
-    TenantDTO getById(Long id);
-    TenantDTO create(TenantDTO.CreateRequest request);
-    TenantDTO update(Long id, TenantDTO.UpdateRequest request);
-    void delete(Long id);
-    void toggleStatus(Long id, Integer status);
-    SysTenant getByCode(String tenantCode);
-}
-```
-
-**TenantController.java：**
-```java
-package com.lc.system.controller;
-
-import com.lc.common.dto.PageResult;
-import com.lc.common.dto.Result;
-import com.lc.system.dto.TenantDTO;
-import com.lc.system.service.TenantService;
-import lombok.RequiredArgsConstructor;
-import org.springframework.web.bind.annotation.*;
-
-@RestController
-@RequestMapping("/api/system/tenants")
-@RequiredArgsConstructor
-public class TenantController {
-    private final TenantService tenantService;
-
-    @GetMapping
-    public Result<PageResult<TenantDTO>> list(
-            @RequestParam(required = false) String keyword,
-            @RequestParam(defaultValue = "1") int page,
-            @RequestParam(defaultValue = "10") int size) {
-        return Result.success(tenantService.list(keyword, page, size));
-    }
-
-    @GetMapping("/{id}")
-    public Result<TenantDTO> getById(@PathVariable Long id) {
-        return Result.success(tenantService.getById(id));
-    }
-
-    @PostMapping
-    public Result<TenantDTO> create(@RequestBody TenantDTO.CreateRequest request) {
-        return Result.success(tenantService.create(request));
-    }
-
-    @PutMapping("/{id}")
-    public Result<TenantDTO> update(@PathVariable Long id, @RequestBody TenantDTO.UpdateRequest request) {
-        return Result.success(tenantService.update(id, request));
-    }
-
-    @DeleteMapping("/{id}")
-    public Result<Void> delete(@PathVariable Long id) {
-        tenantService.delete(id);
-        return Result.success();
-    }
-
-    @PatchMapping("/{id}/status")
-    public Result<Void> toggleStatus(@PathVariable Long id, @RequestParam Integer status) {
-        tenantService.toggleStatus(id, status);
-        return Result.success();
-    }
-}
-```
-
-- [ ] **Step 1: 创建 UserContext（用户+租户上下文）**
-- [ ] **Step 2: 创建 TenantCheck 注解**
-- [ ] **Step 3: 创建 TenantContextFilter**
-- [ ] **Step 4: 修改 JwtAuthenticationFilter，整合 UserContext**
-- [ ] **Step 5: 创建 SysTenantRepository**
-- [ ] **Step 6: 创建 TenantService 接口**
-- [ ] **Step 7: 创建 TenantServiceImpl 实现**
-- [ ] **Step 8: 创建 TenantDTO**
-- [ ] **Step 9: 创建 PageRequest 基类**
-- [ ] **Step 10: 创建 TenantController**
-- [ ] **Step 11: 编译验证：cd backend && mvn clean compile -q -pl bootstrap -am**
-- [ ] **Step 12: Commit: "feat: 多租户上下文体系（租户识别、租户CRUD、上下文传递）"**
-
----
-
-### Task 5: RBAC 权限体系
-
-**Files:**
-- Create: `backend/common/src/main/java/com/lc/common/annotation/PreAuthorize.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/service/RoleService.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/service/MenuService.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/service/PermissionService.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/RoleServiceImpl.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/MenuServiceImpl.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/PermissionServiceImpl.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/controller/RoleController.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/controller/MenuController.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/controller/UserController.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/dto/UserDTO.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/dto/RoleDTO.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/dto/MenuDTO.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysMenuRepository.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysPermissionRepository.java`
-- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/interceptor/PermissionInterceptor.java`
-- Modify: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/WebMvcConfig.java`
-
-**Interfaces:**
-- Consumes: UserContext（获取当前用户/租户）, UserService, SysRoleRepository
-- Produces: PermissionService（权限校验）, PreAuthorize注解, 角色/菜单/用户管理接口
-
-**核心设计：**
-
-1. **菜单权限**：树形菜单结构，角色关联菜单权限
-2. **项目成员角色**：viewer/editor/admin/publisher 四级角色
-3. **接口权限声明**：`@PreAuthorize("user:list")` 注解声明所需权限，拦截器统一校验
-
-**PreAuthorize.java：**
-```java
-package com.lc.common.annotation;
-
-import java.lang.annotation.*;
-
-@Target({ElementType.METHOD, ElementType.TYPE})
-@Retention(RetentionPolicy.RUNTIME)
-@Documented
-public @interface PreAuthorize {
-    String value() default "";
-    boolean requireAll() default false;
-}
-```
-
-**PermissionService.java：**
-```java
-package com.lc.system.service;
-
-import java.util.List;
-import java.util.Set;
-
-public interface PermissionService {
-    Set<String> getUserPermissions(Long userId);
-    boolean hasPermission(Long userId, String permission);
-    boolean hasAnyPermission(Long userId, String... permissions);
-    boolean hasAllPermissions(Long userId, String... permissions);
-    List<Long> getUserRoleIds(Long userId);
-}
-```
-
-**PermissionInterceptor.java：**
-```java
-package com.lc.bootstrap.interceptor;
-
-import com.lc.common.annotation.PreAuthorize;
-import com.lc.common.context.UserContext;
-import com.lc.common.exception.BusinessException;
-import com.lc.common.exception.GlobalErrorCode;
-import com.lc.system.service.PermissionService;
-import jakarta.servlet.http.HttpServletRequest;
-import jakarta.servlet.http.HttpServletResponse;
-import lombok.RequiredArgsConstructor;
-import org.springframework.stereotype.Component;
-import org.springframework.web.method.HandlerMethod;
-import org.springframework.web.servlet.HandlerInterceptor;
-
-import java.lang.reflect.Method;
-
-@Component
-@RequiredArgsConstructor
-public class PermissionInterceptor implements HandlerInterceptor {
-    private final PermissionService permissionService;
-
-    @Override
-    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
-        if (!(handler instanceof HandlerMethod)) {
-            return true;
-        }
-        HandlerMethod handlerMethod = (HandlerMethod) handler;
-        Method method = handlerMethod.getMethod();
-
-        PreAuthorize annotation = method.getAnnotation(PreAuthorize.class);
-        if (annotation == null) {
-            annotation = handlerMethod.getBeanType().getAnnotation(PreAuthorize.class);
-        }
-        if (annotation == null || annotation.value().isEmpty()) {
-            return true;
-        }
-
-        Long userId = UserContext.getUserId();
-        if (userId == null) {
-            throw new BusinessException(GlobalErrorCode.UNAUTHORIZED);
-        }
-
-        String[] permissions = annotation.value().split(",");
-        if (annotation.requireAll()) {
-            if (!permissionService.hasAllPermissions(userId, permissions)) {
-                throw new BusinessException(GlobalErrorCode.PERMISSION_DENIED);
-            }
-        } else {
-            if (!permissionService.hasAnyPermission(userId, permissions)) {
-                throw new BusinessException(GlobalErrorCode.PERMISSION_DENIED);
-            }
-        }
-        return true;
-    }
-}
-```
-
-**RoleService.java：**
-```java
-package com.lc.system.service;
-
-import com.lc.common.dto.PageResult;
-import com.lc.system.dto.RoleDTO;
-
-import java.util.List;
-
-public interface RoleService {
-    PageResult<RoleDTO> list(String keyword, int page, int size);
-    RoleDTO getById(Long id);
-    RoleDTO create(RoleDTO.CreateRequest request);
-    RoleDTO update(Long id, RoleDTO.UpdateRequest request);
-    void delete(Long id);
-    void assignMenus(Long roleId, List<Long> menuIds);
-    List<Long> getRoleMenuIds(Long roleId);
-    List<RoleDTO> listAll();
-}
-```
-
-- [ ] **Step 1: 创建 PreAuthorize 注解**
-- [ ] **Step 2: 创建 PermissionService 接口**
-- [ ] **Step 3: 创建 PermissionServiceImpl 实现**
-- [ ] **Step 4: 创建 PermissionInterceptor**
-- [ ] **Step 5: 修改 WebMvcConfig 注册拦截器**
-- [ ] **Step 6: 创建 SysMenuRepository, SysPermissionRepository**
-- [ ] **Step 7: 创建 RoleService 接口 + RoleServiceImpl**
-- [ ] **Step 8: 创建 MenuService 接口 + MenuServiceImpl**
-- [ ] **Step 9: 创建 UserDTO, RoleDTO, MenuDTO**
-- [ ] **Step 10: 创建 RoleController, MenuController, UserController**
-- [ ] **Step 11: 编译验证：cd backend && mvn clean compile -q -pl bootstrap -am**
-- [ ] **Step 12: Commit: "feat: RBAC权限体系（菜单权限、角色管理、接口权限注解）"**
-
----
-
-### Task 6: 对象存储与密钥管理
-
-**Files:**
-- Create: `backend/common/src/main/java/com/lc/common/storage/StorageService.java`
-- Create: `backend/common/src/main/java/com/lc/common/storage/LocalStorageServiceImpl.java`
-- Create: `backend/common/src/main/java/com/lc/common/storage/MinioStorageServiceImpl.java`
-- Create: `backend/common/src/main/java/com/lc/common/storage/StorageProperties.java`
-- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/StorageConfig.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/controller/FileController.java`
-- Create: `backend/common/src/main/java/com/lc/common/security/SsrfProtector.java`
-- Create: `backend/common/src/main/java/com/lc/common/util/EncryptUtil.java`
-
-**Interfaces:**
-- Consumes: StorageProperties（配置）, UserContext（获取租户/用户）
-- Produces: StorageService（上传/下载/删除）, FileController（文件上传接口）, SsrfProtector（SSRF防护）
-
-**核心设计：**
-
-1. **对象存储抽象**：StorageService 接口，支持本地文件系统（开发）和 MinIO/S3（生产）切换
-2. **密钥管理**：敏感配置使用 AES 加密存储，密钥通过环境变量注入
-3. **文件上传**：类型校验、大小限制、随机文件名、租户目录隔离
-
-**StorageService.java：**
-```java
-package com.lc.common.storage;
-
-import java.io.InputStream;
-
-public interface StorageService {
-    String upload(String bucket, String key, InputStream input, String contentType, long size);
-    InputStream download(String bucket, String key);
-    void delete(String bucket, String key);
-    String getPresignedUrl(String bucket, String key, long expireSeconds);
-    boolean exists(String bucket, String key);
-}
-```
-
-**StorageProperties.java：**
-```java
-package com.lc.common.storage;
-
-import lombok.Data;
-import org.springframework.boot.context.properties.ConfigurationProperties;
-
-@Data
-@ConfigurationProperties(prefix = "storage")
-public class StorageProperties {
-    private String type = "local";
-    private String localPath = "./storage";
-    private String endpoint;
-    private String accessKey;
-    private String secretKey;
-    private String defaultBucket = "lc-platform";
-    private long maxFileSize = 10485760;
-}
-```
-
-**LocalStorageServiceImpl.java：**
-```java
-package com.lc.common.storage;
-
-import lombok.extern.slf4j.Slf4j;
-import org.springframework.util.FileCopyUtils;
-
-import java.io.*;
-import java.nio.file.*;
-
-@Slf4j
-public class LocalStorageServiceImpl implements StorageService {
-    private final String basePath;
-
-    public LocalStorageServiceImpl(String basePath) {
-        this.basePath = basePath;
-    }
-
-    @Override
-    public String upload(String bucket, String key, InputStream input, String contentType, long size) {
-        try {
-            Path path = Paths.get(basePath, bucket, key);
-            Files.createDirectories(path.getParent());
-            try (OutputStream out = Files.newOutputStream(path)) {
-                FileCopyUtils.copy(input, out);
-            }
-            return key;
-        } catch (IOException e) {
-            throw new RuntimeException("File upload failed", e);
-        }
-    }
-
-    @Override
-    public InputStream download(String bucket, String key) {
-        try {
-            Path path = Paths.get(basePath, bucket, key);
-            return Files.newInputStream(path);
-        } catch (IOException e) {
-            throw new RuntimeException("File download failed", e);
-        }
-    }
-
-    @Override
-    public void delete(String bucket, String key) {
-        try {
-            Path path = Paths.get(basePath, bucket, key);
-            Files.deleteIfExists(path);
-        } catch (IOException e) {
-            log.warn("File delete failed: {}", key, e);
-        }
-    }
-
-    @Override
-    public String getPresignedUrl(String bucket, String key, long expireSeconds) {
-        return "/api/files/" + bucket + "/" + key;
-    }
-
-    @Override
-    public boolean exists(String bucket, String key) {
-        return Files.exists(Paths.get(basePath, bucket, key));
-    }
-}
-```
-
-**EncryptUtil.java：**
-```java
-package com.lc.common.util;
-
-import javax.crypto.Cipher;
-import javax.crypto.spec.SecretKeySpec;
-import java.nio.charset.StandardCharsets;
-import java.util.Base64;
-
-public class EncryptUtil {
-    private static final String ALGORITHM = "AES";
-    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
-
-    public static String encrypt(String plainText, String key) {
-        try {
-            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
-            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
-            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
-            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
-            return Base64.getEncoder().encodeToString(encrypted);
-        } catch (Exception e) {
-            throw new RuntimeException("Encryption failed", e);
-        }
-    }
-
-    public static String decrypt(String cipherText, String key) {
-        try {
-            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
-            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
-            cipher.init(Cipher.DECRYPT_MODE, secretKey);
-            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
-            return new String(decrypted, StandardCharsets.UTF_8);
-        } catch (Exception e) {
-            throw new RuntimeException("Decryption failed", e);
-        }
-    }
-}
-```
-
-- [ ] **Step 1: 创建 StorageService 接口**
-- [ ] **Step 2: 创建 StorageProperties 配置类**
-- [ ] **Step 3: 创建 LocalStorageServiceImpl 本地存储实现**
-- [ ] **Step 4: 创建 MinioStorageServiceImpl MinIO实现**
-- [ ] **Step 5: 创建 StorageConfig 配置类**
-- [ ] **Step 6: 创建 EncryptUtil 加密工具**
-- [ ] **Step 7: 创建 FileController 文件上传接口**
-- [ ] **Step 8: 创建 SsrfProtector SSRF防护工具**
-- [ ] **Step 9: 修改 application.yml 添加 storage 配置**
-- [ ] **Step 10: 编译验证：cd backend && mvn clean compile -q -pl bootstrap -am**
-- [ ] **Step 11: Commit: "feat: 对象存储与密钥管理（存储抽象、文件上传、AES加密）"**
-
----
-
-### Task 7: 安全基线（SSRF防护、文件上传限制、审计日志）
-
-**Files:**
-- Create: `backend/common/src/main/java/com/lc/common/annotation/AuditLog.java`
-- Create: `backend/common/src/main/java/com/lc/common/security/FileUploadValidator.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/entity/AuditLog.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/repository/AuditLogRepository.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/service/AuditLogService.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/AuditLogServiceImpl.java`
-- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/aspect/AuditLogAspect.java`
-- Create: `backend/system-core/src/main/java/com/lc/system/controller/AuditLogController.java`
-- Create: `backend/system-core/src/main/resources/db/migration/V13__audit_log.sql`
-
-**Interfaces:**
-- Consumes: UserContext（获取操作用户）, AuditLogRepository
-- Produces: AuditLog注解, AuditLogService, 文件上传校验器
-
-**核心设计：**
-
-1. **SSRF防护**：SsrfProtector 工具类，校验 URL 是否指向内网 IP（10.x, 172.16-31.x, 192.168.x, 127.x, localhost），默认禁止访问内网
-2. **文件上传限制**：FileUploadValidator 校验文件类型（MIME+扩展名双重校验）、大小限制、文件名清理
-3. **审计日志**：@AuditLog 注解 + AOP切面，自动记录操作人、操作类型、资源类型、IP、结果等
-
-**AuditLog.java（注解）：**
-```java
-package com.lc.common.annotation;
-
-import java.lang.annotation.*;
-
-@Target(ElementType.METHOD)
-@Retention(RetentionPolicy.RUNTIME)
-@Documented
-public @interface AuditLog {
-    String action();
-    String resourceType();
-    String resourceIdParam() default "id";
-    boolean recordRequest() default false;
-    boolean recordResponse() default false;
-}
-```
-
-**AuditLog.java（实体）：**
-```java
-package com.lc.system.entity;
-
-import jakarta.persistence.*;
-import lombok.Data;
-import java.time.LocalDateTime;
-
-@Data
-@Entity
-@Table(name = "audit_log")
-public class AuditLog {
-    @Id
-    @GeneratedValue(strategy = GenerationType.IDENTITY)
-    private Long id;
-
-    @Column(name = "tenant_id")
-    private Long tenantId;
-
-    @Column(name = "project_id")
-    private Long projectId;
-
-    @Column(name = "user_id")
-    private Long userId;
-
-    @Column(name = "username", length = 64)
-    private String username;
-
-    @Column(name = "action", nullable = false, length = 128)
-    private String action;
-
-    @Column(name = "resource_type", nullable = false, length = 64)
-    private String resourceType;
-
-    @Column(name = "resource_id", length = 128)
-    private String resourceId;
-
-    @Column(name = "request_id", length = 64)
-    private String requestId;
-
-    @Column(name = "client_ip", length = 64)
-    private String clientIp;
-
-    @Column(name = "before_data", columnDefinition = "TEXT")
-    private String beforeData;
-
-    @Column(name = "after_data", columnDefinition = "TEXT")
-    private String afterData;
-
-    @Column(name = "result", nullable = false, length = 32)
-    private String result;
-
-    @Column(name = "error_message", length = 512)
-    private String errorMessage;
-
-    @Column(name = "created_time")
-    private LocalDateTime createdTime;
-
-    @PrePersist
-    protected void onCreate() {
-        createdTime = LocalDateTime.now();
-    }
-}
-```
-
-**AuditLogAspect.java：**
-```java
-package com.lc.bootstrap.aspect;
-
-import com.lc.common.annotation.AuditLog;
-import com.lc.common.context.UserContext;
-import com.lc.system.service.AuditLogService;
-import jakarta.servlet.http.HttpServletRequest;
-import lombok.RequiredArgsConstructor;
-import lombok.extern.slf4j.Slf4j;
-import org.aspectj.lang.ProceedingJoinPoint;
-import org.aspectj.lang.annotation.Around;
-import org.aspectj.lang.annotation.Aspect;
-import org.aspectj.lang.reflect.MethodSignature;
-import org.springframework.stereotype.Component;
-import org.springframework.web.context.request.RequestContextHolder;
-import org.springframework.web.context.request.ServletRequestAttributes;
-
-import java.lang.reflect.Method;
-
-@Slf4j
-@Aspect
-@Component
-@RequiredArgsConstructor
-public class AuditLogAspect {
-    private final AuditLogService auditLogService;
-
-    @Around("@annotation(auditLog)")
-    public Object around(ProceedingJoinPoint joinPoint, AuditLog auditLog) throws Throwable {
-        long start = System.currentTimeMillis();
-        Object result = null;
-        String resultStatus = "SUCCESS";
-        String errorMsg = null;
-
-        try {
-            result = joinPoint.proceed();
-            return result;
-        } catch (Throwable e) {
-            resultStatus = "FAILED";
-            errorMsg = e.getMessage();
-            throw e;
-        } finally {
-            try {
-                saveAuditLog(joinPoint, auditLog, result, resultStatus, errorMsg);
-            } catch (Exception e) {
-                log.error("Save audit log failed", e);
-            }
-        }
-    }
-
-    private void saveAuditLog(ProceedingJoinPoint joinPoint, AuditLog auditLog,
-                              Object result, String resultStatus, String errorMsg) {
-        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
-        Method method = signature.getMethod();
-
-        String resourceId = extractResourceId(joinPoint, auditLog.resourceIdParam());
-        String clientIp = getClientIp();
-        Long userId = UserContext.getUserId();
-        Long tenantId = UserContext.getTenantId();
-        String username = UserContext.get() != null ? UserContext.get().getUsername() : null;
-
-        auditLogService.asyncSave(
-                userId, tenantId, username,
-                auditLog.action(), auditLog.resourceType(), resourceId,
-                clientIp, resultStatus, errorMsg
-        );
-    }
-
-    private String extractResourceId(ProceedingJoinPoint joinPoint, String paramName) {
-        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
-        String[] paramNames = signature.getParameterNames();
-        Object[] args = joinPoint.getArgs();
-        for (int i = 0; i < paramNames.length; i++) {
-            if (paramName.equals(paramNames[i]) && args[i] != null) {
-                return args[i].toString();
-            }
-        }
-        return null;
-    }
-
-    private String getClientIp() {
-        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
-        if (attrs != null) {
-            HttpServletRequest request = attrs.getRequest();
-            String ip = request.getHeader("X-Forwarded-For");
-            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
-                ip = request.getHeader("X-Real-IP");
-            }
-            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
-                ip = request.getRemoteAddr();
-            }
-            return ip != null ? ip.split(",")[0].trim() : null;
-        }
-        return null;
-    }
-}
-```
-
-**FileUploadValidator.java：**
-```java
-package com.lc.common.security;
-
-import org.springframework.web.multipart.MultipartFile;
-import java.util.Set;
-
-public class FileUploadValidator {
-    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
-            "image/jpeg", "image/png", "image/gif", "image/webp", "image/svg+xml"
-    );
-    private static final Set<String> ALLOWED_DOC_TYPES = Set.of(
-            "application/pdf", "application/msword",
-            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
-            "application/vnd.ms-excel",
-            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
-            "text/plain"
-    );
-    private static final long DEFAULT_MAX_SIZE = 10 * 1024 * 1024;
-
-    public static void validateImage(MultipartFile file) {
-        validate(file, ALLOWED_IMAGE_TYPES, DEFAULT_MAX_SIZE);
-    }
-
-    public static void validateDocument(MultipartFile file) {
-        validate(file, ALLOWED_DOC_TYPES, DEFAULT_MAX_SIZE);
-    }
-
-    public static void validate(MultipartFile file, Set<String> allowedTypes, long maxSize) {
-        if (file == null || file.isEmpty()) {
-            throw new IllegalArgumentException("File cannot be empty");
-        }
-        if (file.getSize() > maxSize) {
-            throw new IllegalArgumentException("File size exceeds limit: " + maxSize / 1024 / 1024 + "MB");
-        }
-        String contentType = file.getContentType();
-        if (contentType == null || !allowedTypes.contains(contentType.toLowerCase())) {
-            throw new IllegalArgumentException("File type not allowed: " + contentType);
-        }
-        String originalFilename = file.getOriginalFilename();
-        if (originalFilename != null && containsPathTraversal(originalFilename)) {
-            throw new IllegalArgumentException("Invalid filename");
-        }
-    }
-
-    private static boolean containsPathTraversal(String filename) {
-        return filename.contains("..") || filename.contains("/") || filename.contains("\\");
-    }
-}
-```
-
-- [ ] **Step 1: 创建 AuditLog 注解**
-- [ ] **Step 2: 创建 AuditLog 实体类**
-- [ ] **Step 3: 创建 AuditLogRepository**
-- [ ] **Step 4: 创建 V13__audit_log.sql 迁移脚本**
-- [ ] **Step 5: 创建 AuditLogService 接口**
-- [ ] **Step 6: 创建 AuditLogServiceImpl 实现**
-- [ ] **Step 7: 创建 AuditLogAspect 切面**
-- [ ] **Step 8: 创建 AuditLogController 查询接口**
-- [ ] **Step 9: 创建 FileUploadValidator 文件上传校验器**
-- [ ] **Step 10: 编译验证：cd backend && mvn clean compile -q -pl bootstrap -am**
-- [ ] **Step 11: Commit: "feat: 安全基线（审计日志、SSRF防护、文件上传限制）"**
-
----
-
-## 自审
-
-**1. Spec覆盖：**
-- ✅ 多租户：租户上下文传递、租户CRUD、租户内数据隔离
-- ✅ RBAC：用户-角色-菜单权限、接口权限注解、权限拦截器
-- ✅ 对象存储：StorageService抽象、本地+MinIO双实现、文件上传接口
-- ✅ 密钥管理：AES加密工具、敏感数据加密存储
-- ✅ SSRF防护：SsrfProtector工具、内网IP检测
-- ✅ 文件上传：类型校验、大小限制、路径遍历防护
-- ✅ 审计日志：@AuditLog注解 + AOP切面、审计日志查询
-
-**2. 占位符检查：** 无"TBD"或"TODO"，所有代码已完整给出
-
-**3. 类型一致性：**
-- UserContext 与 JwtTokenService 返回类型一致
-- Service 接口与 DTO 类型匹配
-- 实体类字段与迁移脚本一致
-
----
-
-## 验收标准
-
-### Task 4 验收
-- [ ] 请求携带JWT时，TenantContext中能获取到tenantId
-- [ ] 租户CRUD接口正常工作
-- [ ] 不同租户数据隔离（用户只能看到本租户数据）
-
-### Task 5 验收
-- [ ] 角色管理接口正常（创建、编辑、删除、分配菜单）
-- [ ] 菜单管理接口正常（树形结构）
-- [ ] @PreAuthorize注解生效，无权限返回403
-- [ ] 用户管理接口正常
-
-### Task 6 验收
-- [ ] 文件上传接口可用，文件保存到指定位置
-- [ ] 可通过配置切换本地存储和MinIO存储
-- [ ] AES加解密工具正常工作
-
-### Task 7 验收
-- [ ] @AuditLog注解的方法会自动记录审计日志
-- [ ] 文件上传校验正确拦截非法文件
-- [ ] SSRF防护能正确识别内网URL
-- [ ] 审计日志查询接口可用
-
----
-
-Plan complete and saved to `docs/superpowers/plans/2026-07-27-phase1-tasks4-7-plan.md`.
-
-**Two execution options:**
-
-**1. Subagent-Driven (recommended)** - I dispatch a fresh subagent per task, review between tasks, fast iteration
-
-**2. Inline Execution** - Execute tasks in this session using executing-plans, batch execution with checkpoints
-
-**Which approach?**
