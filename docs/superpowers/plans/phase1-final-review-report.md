# Phase 1 最终代码审查报告

## 概览

- **审查范围**：分支 `feature/phase1-security-rbac`，merge base `1eed1722` → HEAD `c28205bb`，共 16 commits、131 files changed（4141 insertions / 2914 deletions，其中约 60% 删除为清理 `target/` 构建产物）
- **审查方法**：通读 review-package.md 全量 diff + 读取 9 个关键源文件交叉验证 + Grep 确认注解覆盖范围
- **总体评估**：**NEEDS_REWORK**
- **Critical 发现数**：6
- **Important 发现数**：10
- **Minor 发现数**：9

### 评估理由

代码结构清晰、分层合理、编译可通过，但存在 **1 条可被实际利用的认证/授权绕过漏洞（C1）**、**1 条未授权租户管理接口（C2）**，以及 **Global Constraints 中"审计/软删除/乐观锁"三条强约束几乎完全未落地**（C4/C5/C6）。在修复 C1~C4 之前不建议合并；C5/C6 可作为合并后的紧 follow-up，但严格按 spec 也应阻断合并。

---

## Critical 发现（必须修复）

### C1: JWT Access/Refresh Token 类型未区分，RefreshToken 可作为 AccessToken 使用导致跨租户越权

- **文件**：
  - `backend/system-core/src/main/java/com/lc/system/security/JwtTokenService.java:32-59`（生成 access/refresh token，无 `type`/`token_type` claim 区分）
  - `backend/bootstrap/src/main/java/com/lc/bootstrap/filter/JwtAuthenticationFilter.java:33-46`（解析 token 时不校验 token 类型）
- **问题**：`generateAccessToken` 写入 claims `{userId, username, tenantId}`，`generateRefreshToken` 只写入 `{userId, username}`（**无 tenantId**）。`JwtAuthenticationFilter` 对 Bearer token 只做签名校验，不校验 token 类型。攻击者可将自己的 RefreshToken 作为 AccessToken 放入 `Authorization: Bearer` 头：
  1. `getTenantIdFromToken()` 返回 `null`（refresh token 无 tenantId claim）
  2. `UserContext.tenantId = null`
  3. 所有 Service 的租户校验形如 `if (currentTenantId != null && !currentTenantId.equals(...))` 全部被跳过
  4. `TenantInterceptor` 中 `if (currentTenantId == null) return true;` 也跳过
- **影响**：**认证/授权绕过 + 租户隔离全面破坏**。任何持有 7 天有效期 RefreshToken 的普通用户，可将其作为 AccessToken 使用，获得"超级管理员"身份（tenantId=null），进而：
  - 通过 `GET /api/system/users/{anyId}` 读取任意租户用户详情
  - 通过 `PUT /api/system/users/{anyId}/password` 重置任意用户密码（若持有 `system:user:reset` 权限）
  - 通过 `TenantController`（无 `@PreAuthorize`）CRUD 任意租户
- **建议修复**：
  1. 在 access/refresh token 中分别加 claim `type=access` / `type=refresh`
  2. `JwtAuthenticationFilter` 解析后校验 `type == access`，否则拒绝
  3. `AuthController.refresh` 校验 `type == refresh`
  4. 同时修复 refresh token 缺失 tenantId 的问题（或显式拒绝 tenantId=null 的 access token）
- **spec 违反**：Global Constraints "安全：密钥、密码、Token、连接串不得明文落库"（Token 完整性）；Task 3 "认证与会话" 隐含 access/refresh 不可互换

### C2: TenantController 全部接口缺失 @PreAuthorize，任何认证用户可 CRUD 任意租户

- **文件**：`backend/system-core/src/main/java/com/lc/system/controller/TenantController.java:1-52`（整文件无 `@PreAuthorize`，也无 `@TenantCheck`）
- **问题**：`TenantController` 的 `create`/`update`/`delete`/`toggleStatus` 均无任何权限注解。Grep 确认 `@PreAuthorize` 只覆盖 UserController/MenuController/RoleController/AuditLogController，TenantController 完全裸露。任何通过 JWT 认证的普通用户均可：
  - `DELETE /api/system/tenants/{id}` → `TenantServiceImpl.delete` 仅 `setStatus(0)`，可禁用任意租户（含自己的租户）造成 DoS
  - `PATCH /api/system/tenants/{id}/status?status=1` → 重新启用已禁用租户
  - `POST /api/system/tenants` → 创建新租户
- **影响**：**授权绕过 + DoS 风险**。普通用户可禁用任意租户，导致该租户所有用户无法正常使用系统（虽然 login 不校验 tenant status，但业务接口可能受影响）。
- **建议修复**：为 TenantController 全部方法添加 `@PreAuthorize("system:tenant:*")` 或类级注解，并引入"超级管理员专用"权限码（仅超级管理员可管理租户）。
- **spec 违反**：Task 4 "租户CRUD接口"；Global Constraints "审计：权限变更...必须可记录审计日志"（这里连权限校验都没有）

### C3: 登录使用全局 findByUsername，跨租户同用户名导致登录串号

- **文件**：
  - `backend/bootstrap/src/main/java/com/lc/bootstrap/controller/AuthController.java:24`（`userService.findByUsername(request.getUsername())`）
  - `backend/system-core/src/main/java/com/lc/system/service/impl/UserServiceImpl.java:14-16`（`userRepository.findByUsername(username)`，全局查询）
  - `backend/system-core/src/main/resources/db/migration/V2__sys_user.sql:16`（`UNIQUE (tenant_id, username)` 复合唯一，username 全局可重复）
- **问题**：登录按 username 全局查询，但 V2 schema 允许 `(tenant_id, username)` 复合唯一，即不同租户可有同 username。`findByUsername` 返回 `Optional`，会命中**第一个匹配**（取决于 DB 实现可能不稳定）。若 tenant A 和 tenant B 都有 `admin` 用户，tenant B 的 admin 登录时可能命中 tenant A 的 admin 记录，密码校验通过后获得 tenant A 的 token。
- **影响**：**租户隔离破坏 + 认证串号**。当多租户场景出现重名用户时，登录身份归属不可控。
- **建议修复**：
  1. 登录接口增加 `tenantCode` 或 `tenantId` 参数
  2. 改用 `findByTenantIdAndUsername(tenantId, username)`
  3. 或在 username 体系改为全局唯一（与现有复合唯一约束冲突，需 spec 决策）
- **spec 违反**：Global Constraints "多租户：租户内唯一字段使用 (tenant_id, code) 复合唯一"；Task 3 "认证与会话"

### C4: @AuditLog 仅覆盖 1 个只读接口，Global Constraints 要求的审计操作几乎全部缺失

- **文件**：
  - `backend/common/src/main/java/com/lc/common/annotation/AuditLog.java`（注解定义）
  - `backend/bootstrap/src/main/java/com/lc/bootstrap/aspect/AuditLogAspect.java`（切面实现，正确）
  - **缺失点**（Grep 确认全仓库 `@AuditLog` 仅出现在 `AuditLogController.java:30` 一处）：
    - `AuthController.login/refresh/logout` — **登录** 未审计
    - `UserController.create/update/delete/resetPassword/assignRoles` — **权限变更** 未审计
    - `RoleController.create/update/delete/assignMenus` — **权限变更** 未审计
    - `TenantController.create/update/delete/toggleStatus` — **项目配置变更** 未审计
    - `FileController.upload/delete` — 未审计
- **问题**：Global Constraints 明确要求"登录、权限变更、项目配置变更、发布、回滚、数据源测试必须可记录审计日志"。当前 `@AuditLog` 只标注在 `AuditLogController.list`（查询审计日志的只读接口），所有写操作均未审计。AuditLogAspect 机制本身实现正确（异常重抛、SpEL 提取 resourceId），但**没有任何业务方法使用它**。
- **影响**：**审计约束完全未满足**。安全事件无法追溯，合规性失败。
- **建议修复**：在上述所有写操作方法上添加 `@AuditLog(action=..., resourceType=..., resourceIdParam="#id")`。Login 的 resourceIdParam 可用 `#request.username`。
- **spec 违反**：Global Constraints "审计：登录、权限变更、项目配置变更、发布、回滚、数据源测试必须可记录审计日志"；Task 7 "审计日志"

### C5: 业务主表无软删除字段，User/Role/Menu 删除均为物理 deleteById

- **文件**：
  - `backend/system-core/src/main/java/com/lc/system/entity/SysUser.java`（无 `deleted` 字段）
  - `backend/system-core/src/main/java/com/lc/system/entity/SysRole.java`（无 `deleted` 字段）
  - `backend/system-core/src/main/java/com/lc/system/entity/SysMenu.java`（无 `deleted` 字段）
  - `backend/system-core/src/main/java/com/lc/system/service/impl/UserServiceImpl.java:84`（`userRepository.deleteById(id)` 物理删除）
  - `backend/system-core/src/main/java/com/lc/system/service/impl/RoleServiceImpl.java:51`（`roleRepository.deleteById(id)` 物理删除）
  - `backend/system-core/src/main/java/com/lc/system/service/impl/MenuServiceImpl.java:70`（`menuRepository.deleteById(id)` 物理删除）
  - V2/V3/V5 迁移脚本均无 `deleted` 列
- **问题**：Global Constraints 明确"业务主表默认软删除字段 deleted，物理删除由后台任务按保留策略执行"。当前 SysUser/SysRole/SysMenu 实体和表结构均无 `deleted` 字段，且 delete 接口直接 `deleteById` 物理删除。仅 `TenantServiceImpl.delete` 用 `setStatus(0)` 做了软删除（但字段名不叫 `deleted`，策略也不统一）。
- **影响**：**数据丢失风险**。误删用户/角色/菜单后数据不可恢复；审计日志中的 `target_id` 引用将变成悬空指针。
- **建议修复**：
  1. 各业务表加 `deleted TINYINT NOT NULL DEFAULT 0` 列（V16 迁移）
  2. 实体加 `@Column(name="deleted") private Integer deleted = 0;` + `@Where(clause="deleted=0")`（Hibernate 6 用 `@SQLRestriction`）
  3. delete 方法改为 `setDeleted(1)` + save
  4. Repository 查询自动过滤 deleted=0
- **spec 违反**：Global Constraints "删除策略：业务主表默认软删除字段 deleted"

### C6: 可编辑配置表无 version 字段，乐观锁完全未实现

- **文件**：
  - `backend/system-core/src/main/java/com/lc/system/entity/SysUser.java`（无 `version` 字段）
  - `backend/system-core/src/main/java/com/lc/system/entity/SysRole.java`（无 `version` 字段）
  - `backend/system-core/src/main/java/com/lc/system/entity/SysMenu.java`（无 `version` 字段）
  - `backend/system-core/src/main/java/com/lc/system/entity/SysTenant.java`（无 `version` 字段）
  - 各 ServiceImpl 的 update 方法均无 version 校验
- **问题**：Global Constraints 明确"可编辑配置表增加 version 字段，更新时做乐观锁校验"。当前所有可编辑实体（SysUser/SysRole/SysMenu/SysTenant）均无 `@Version` 字段，update 方法为简单 read-modify-write，无并发控制。两个管理员同时编辑同一角色时，后提交者覆盖前者的修改且无感知。
- **影响**：**数据一致性风险**。并发更新导致 silently lost update。
- **建议修复**：
  1. 实体加 `@Version private Long version;`
  2. 表加 `version BIGINT NOT NULL DEFAULT 0` 列
  3. DTO.UpdateRequest 加 `version` 字段，update 时由 JPA 自动校验（ObjectOptimisticLockingFailureException → 转 DATA_CONFLICT）
- **spec 违反**：Global Constraints "并发控制：可编辑配置表增加 version 字段，更新时做乐观锁校验"

---

## Important 发现（建议修复）

### I1: AuthController.logout 使用 @RequestAttribute("userId")，但该属性从未被设置 → logout 接口运行时必失败

- **文件**：`backend/bootstrap/src/main/java/com/lc/bootstrap/controller/AuthController.java:76`
- **问题**：`logout(@RequestAttribute("userId") Long userId)` 期望从 request attribute 取 `userId`，但 Grep 确认全仓库无任何 `request.setAttribute("userId", ...)`。`JwtAuthenticationFilter` 只设置了 `SecurityContextHolder`、`UserContext`（ThreadLocal）、`TenantContext`（ThreadLocal），未设置 request attribute。
- **影响**：调用 `POST /api/auth/logout` 会抛 `MissingRequestAttributeException`（400）。logout 功能完全不可用。
- **建议修复**：改为 `@AuthenticationPrincipal Long userId`（JwtAuthenticationFilter 已将 userId 设为 principal），或从 `UserContext.getUserId()` 取。

### I2: application.yml 硬编码 JWT secret 与 DB 密码

- **文件**：`backend/bootstrap/src/main/resources/application.yml:8,20,39`
- **问题**：
  - `password: password`（DB 密码明文，无环境变量绑定）
  - `jwt.secret: your-256-bit-secret-key-here-must-be-at-least-32-characters`（JWT 密钥明文，无环境变量绑定）
  - `encrypt.key: ${LC_ENCRYPT_KEY:lc-platform-2026-secure-key-32bx}`（有 env 绑定，但**有硬编码 fallback**，生产环境若未设环境变量会使用弱默认值）
- **影响**：任何能读取代码仓库的人可伪造 JWT token、解密加密数据。虽然 Global Constraints 的"不得明文落库"指数据库存储，但配置文件中的硬编码密钥同样是安全隐患。
- **建议修复**：`password: ${DB_PASSWORD:}`、`secret: ${JWT_SECRET:}`、`key: ${LC_ENCRYPT_KEY:}`（去掉 fallback，启动时强制要求环境变量）。

### I3: EncryptUtil 使用 AES/ECB 模式，密码学不安全

- **文件**：`backend/common/src/main/java/com/lc/common/util/EncryptUtil.java:14,24-31`
- **问题**：`TRANSFORMATION = "AES/ECB/PKCS5Padding"`。ECB 模式下相同明文块产生相同密文块，无法掩盖明文模式，密码学上已被弃用。
- **影响**：若用于加密数据库中重复出现的字段（如多个数据源相同密码），密文相同会泄露信息。
- **建议修复**：改为 `AES/GCM/NoPadding` 或 `AES/CBC/PKCS5Padding` + 随机 IV（IV 随密文一起存储）。

### I4: PermissionInterceptor 无超级管理员旁路，且 V12 未 seed 任何角色/权限 → 系统开箱不可用

- **文件**：
  - `backend/bootstrap/src/main/java/com/lc/bootstrap/interceptor/PermissionInterceptor.java:36-40`（`userId == null` 抛 UNAUTHORIZED，无 super admin bypass）
  - `backend/system-core/src/main/resources/db/migration/V12__init_data.sql`（只 seed 1 个 tenant + 1 个 user，无 role/permission/menu 数据）
- **问题**：V12 只创建了 `admin` 用户（tenant_id=1），未分配任何角色或权限。该用户登录后：
  - 访问 `/api/system/users` → `@PreAuthorize("system:user:list")` → `PermissionServiceImpl.getUserPermissions` 返回空集 → PERMISSION_DENIED
  - 所有 `@PreAuthorize` 接口均不可访问
  - 仅 `TenantController`（无 @PreAuthorize）可访问，但那是 C2 的漏洞
- **影响**：**系统开箱不可用**。管理员登录后无法管理用户/角色/菜单，形成死锁（没有权限创建权限）。
- **建议修复**：
  1. V12 补 seed：为 default tenant 创建 `tenant_admin` 角色 + 全部 `system:*` 权限 + admin 用户关联该角色
  2. 或在 PermissionInterceptor 增加 super admin 旁路（如 userId == 1 或 tenantId == null 时放行）

### I5: 登录未校验租户状态与用户状态，禁用账户仍可登录

- **文件**：`backend/bootstrap/src/main/java/com/lc/bootstrap/controller/AuthController.java:22-27`
- **问题**：login 只校验密码，未校验 `user.getStatus() == 1` 和 `user.getTenant().getStatus() == 1`。被禁用的用户、被禁用租户下的用户仍可登录获取 token。
- **影响**：禁用账户无法即时生效，安全封禁失效。
- **建议修复**：login 中增加 `if (user.getStatus() != 1) throw new BusinessException(...)`，并查询 tenant 校验 status。

### I6: AuditLogAspect 未填充 before_data / after_data，状态变更不可追溯

- **文件**：`backend/bootstrap/src/main/java/com/lc/bootstrap/aspect/AuditLogAspect.java:60-77`（buildEntity 未设置 beforeData/afterData）
- **问题**：`AuditLogEntity` 有 `beforeData`/`afterData` 字段，V11 表也有对应列，但 AuditLogAspect 的 `buildEntity` 只设置了 action/resourceType/resourceId/result/errorMessage，**未捕获操作前后的数据快照**。Global Constraints 要求"审计日志 before_data/after_data 中敏感字段必须脱敏"，但当前 before/after 恒为 null，脱敏逻辑也无从谈起。
- **影响**：审计日志只有"谁做了什么"，没有"改了什么"，无法满足合规审计要求。
- **建议修复**：
  1. 在 update/delete 类方法中，before data 通过 `@AuditLog` 的 SpEL 或在 Service 层主动调用 `AuditLogService.save` 时传入
  2. 实现 `AuditLogContext` 工具类，Service 在 update 前 fetch old entity、update 后 fetch new entity，写入审计
  3. 实现脱敏过滤器（如 `MaskingUtil`），对 password/token/secret 字段替换为 `***`

### I7: @TenantCheck 注解全仓库零生产使用，TenantInterceptor 实际为死代码

- **文件**：
  - `backend/common/src/main/java/com/lc/common/annotation/TenantCheck.java`
  - `backend/bootstrap/src/main/java/com/lc/bootstrap/interceptor/TenantInterceptor.java`
- **问题**：Grep 确认 `@TenantCheck` 仅出现在 `TenantInterceptorTest.java`（测试用 Controller），**生产代码中无任何方法标注 `@TenantCheck`**。Task 4 的核心交付物"越权拦截器"在运行时不会触发任何校验。当前租户隔离完全依赖 Service 层的 `getUserOrThrow`/`getRoleOrThrow`/`getMenuOrThrow` 中的 `if (!currentTenantId.equals(...))` 判断，缺乏 defense-in-depth。
- **影响**：一旦某个 Service 方法遗漏租户校验（如未来新增的接口），将直接导致跨租户数据泄露，无拦截器兜底。
- **建议修复**：至少在所有 `@RequestBody` 包含 tenantId 的 Controller 方法上标注 `@TenantCheck`，或在 list 类接口上标注以做第二层校验。

### I8: JwtAuthenticationFilter catch 块静默吞异常，无任何日志

- **文件**：`backend/bootstrap/src/main/java/com/lc/bootstrap/filter/JwtAuthenticationFilter.java:46-50`
- **问题**：`catch (Exception e) { SecurityContextHolder.clearContext(); UserContext.clear(); TenantContext.clear(); }` — 无 `log.warn`/`log.debug`。Token 解析失败（过期、签名错误、格式错误）全部静默，安全排查时无线索。
- **影响**：安全事件难以追溯；调试困难。
- **建议修复**：加 `log.warn("JWT parse failed: {}", e.getMessage());`（注意不要打印完整 token）。

### I9: AuditLogServiceImpl.save 吞掉所有异常，审计失败完全无感知

- **文件**：`backend/system-core/src/main/java/com/lc/system/service/impl/AuditLogServiceImpl.java:18-23`
- **问题**：`try { auditLogRepository.save(entity); } catch (Exception e) { log.warn("保存审计日志失败", e); }`。虽然接口注释说"不抛异常，失败仅记录 WARN 日志"，但这意味着审计日志可能整批丢失而运维无感知。对于合规要求的审计场景，应有告警机制。
- **影响**：审计日志可能 silently 丢失。
- **建议修复**：至少提升为 `log.error` + 接入告警；或对关键审计操作（登录、权限变更）失败时 fallback 写入本地文件/消息队列。

### I10: FileController 无 @PreAuthorize，任何认证用户可上传文件

- **文件**：`backend/system-core/src/main/java/com/lc/system/controller/FileController.java:1-132`
- **问题**：`upload`/`download`/`delete` 均无 `@PreAuthorize`。虽然 `validateTenantAccess` 做了租户隔离（按 key 前缀 `tenant/{tenantId}/` 校验），但无权限码控制。任何认证用户都能上传任意数量文件（仅受 10MB 单文件限制），无频率限制。
- **影响**：存储滥用风险；任何用户可上传文件占用存储。
- **建议修复**：添加 `@PreAuthorize("file:upload")` / `@PreAuthorize("file:delete")` 等权限码，并在 V12 seed 中授予普通用户 `file:upload`。

---

## Minor 发现（记录备查）

- **M1**：`AuditLogEntity` 同时保留旧字段（operation/target_type/target_id/ip）和新字段（action/resource_type/resource_id/client_ip），V15 迁移做了数据迁移但实体未废弃旧字段，存在数据冗余与混淆。
- **M2**：`UserDTO.CreateRequest` 包含 `tenantId` 字段，但 `UserServiceImpl.create` 完全忽略它（用 `UserContext.getTenantId()`），API 契约与实现不一致，易误导调用方。
- **M3**：删除策略不统一：`TenantServiceImpl.delete` 软删除（setStatus(0)），`UserServiceImpl.deleteUser`/`RoleServiceImpl.delete`/`MenuServiceImpl.delete` 物理删除。
- **M4**：`LocalStorageServiceImpl.getPresignedUrl` 直接返回 `/api/files/{bucket}/{key}`，并非真正的预签名 URL（无时效、无签名），与接口语义不符。
- **M5**：`FileController.download` 固定返回 `MediaType.APPLICATION_OCTET_STREAM`，未保留原始 Content-Type，图片/PDF 等无法在线预览。
- **M6**：`AuditLogAspect` 使用 `StandardEvaluationContext`（功能全但更危险），SpEL 表达式虽来自注解（可信），但建议改用 `SimpleEvaluationContext` 做 defense-in-depth。
- **M7**：`PermissionServiceImpl.getUserPermissions` 每次请求都查 5 张表（user_role/role_menu/role_permission/menu/permission），无缓存，高并发下 DB 压力大。建议加 `@Cacheable` 或本地缓存。
- **M8**：`V13__sys_role_menu.sql`/`V14__sys_role_permission.sql` 关联表无 `tenant_id` 列，依赖 `role_id` 间接隔离。当前可接受，但若未来允许跨租户角色共享需重新评估。
- **M9**：`application.yml` 中 `cors.allowed-origins` 默认包含 `http://localhost:3000`（React dev 默认端口之一），生产环境应通过 profile 或环境变量覆盖。

---

## ⚠️ 无法从 diff 验证的疑点

1. **应用是否能成功启动**：V15 使用 `ALTER TABLE ... ADD COLUMN IF NOT EXISTS`（MySQL 8.0+ 语法）+ `UPDATE` 数据迁移。若生产 MySQL 版本 < 8.0 或 audit_log 表已有数据，迁移行为需运行时验证。Flyway baseline-on-migrate=true 在已有数据库上的行为也需确认。
2. **PermissionInterceptor 在 token 缺失时的实际 HTTP 状态码**：抛 `BusinessException(UNAUTHORIZED)` 由 `GlobalExceptionHandler` 处理，但 `handleAuthenticationException` 与 `handleBusinessException` 的优先级/状态码映射需运行时确认（BusinessException 未标注 `@ResponseStatus`，可能返回 200 + 业务码，而非 401）。
3. **前端登录流程端到端**：Task 3 标注完成，但本次 diff 未包含前端变更，无法验证 token 存储/刷新/登出流程是否与后端一致。
4. **Redis 连接与 RefreshToken 存储**：`RedisRefreshTokenService` 依赖 `RedisTemplate<String, Object>`，需确认 RedisConfig 的序列化方式（review-package 中 RedisConfig.class 被删除但源码未在 diff 中展示，推测为已有资产）。
5. **超级管理员账户是否存在**：V12 只 seed 了 `tenant_id=1` 的 admin，无 `tenant_id=NULL` 的超管。代码中大量 `if (tenantId == null) skip` 的超管旁路逻辑目前无任何用户能触发（除非利用 C1 漏洞）。
6. **MinIO SDK optional 依赖在 type=minio 时是否可加载**：`StorageConfig` 用反射加载 `MinioStorageServiceImpl`，common 模块 pom 中 minio 为 `<optional>true</optional>`。若 bootstrap 未显式引入 minio 依赖，type=minio 时会 `ClassNotFoundException`。需确认 bootstrap pom 是否传递引入。

---

## 总结

Phase 1 后端代码在**架构分层、模块组织、DTO 设计、AOP/拦截器机制实现**层面质量较好——`AuditLogAspect` 的异常重抛、SpEL resourceId 提取、`TenantInterceptor` 的 query+path variable 双提取、`SsrfProtector` 的 DNS 二次校验、`LocalStorageServiceImpl` 的 `normalize()` 路径遍历防护，都体现了对常见安全坑的认知。编译可通过，10 个 TenantInterceptor 单元测试覆盖了边界场景。

但存在**3 条必须修复的安全漏洞**（C1 token 类型混淆导致越权、C2 租户管理无授权、C3 跨租户登录串号）和**3 条 Global Constraints 强约束几乎完全未落地**（C4 审计日志覆盖率 1/N、C5 软删除零实现、C6 乐观锁零实现）。其中 C1 是最严重的——任何普通用户用 RefreshToken 当 AccessToken 即可获得 tenantId=null 的超管身份，绕过所有租户隔离，且该攻击路径无需任何权限前提。C4 的"审计机制实现了但没人用"是典型的"基础设施就绪但业务未接入"，spec 合规性失败。

**合并建议**：**NEEDS_REWORK，不建议立即合并**。合并前必须至少修复 C1（token 类型校验）、C2（TenantController 加权限）、C3（登录加租户维度）、C4（关键操作补 @AuditLog）。C5/C6 涉及表结构变更与全局数据访问层改造，工作量较大，可作为合并后的紧 follow-up（建议在同一 sprint 内完成），但严格按 spec 也应阻断合并。I1（logout 不可用）虽非安全问题，但属于"接口完全不可用"的硬 bug，建议一并修复。
