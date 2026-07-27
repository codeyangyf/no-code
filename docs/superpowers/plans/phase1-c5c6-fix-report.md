# Phase 1 C5 (软删除) + C6 (乐观锁) 修复报告

- 修复日期：2026-07-27
- 分支：`feature/phase1-security-rbac`
- Commit Hash：`550dceb4`
- 修复范围：Global Constraint C5（业务主表默认软删除字段 deleted）+ C6（可编辑配置表 version 字段乐观锁校验）

---

## 1. 修改的文件清单

### 新增（2 个 Flyway 迁移脚本）
| 文件 | 作用 |
| --- | --- |
| `backend/system-core/src/main/resources/db/migration/V17__add_soft_delete.sql` | 为 4 张业务主表添加 `deleted` 列 + 索引 |
| `backend/system-core/src/main/resources/db/migration/V18__add_optimistic_lock.sql` | 为 4 张可编辑配置表添加 `version` 列 |

### 实体（4 个）
| 文件 | 改动 |
| --- | --- |
| `backend/system-core/src/main/java/com/lc/system/entity/SysTenant.java` | 类级 `@SQLRestriction("deleted = 0")` + `deleted` / `version` 字段 |
| `backend/system-core/src/main/java/com/lc/system/entity/SysUser.java` | 同上 |
| `backend/system-core/src/main/java/com/lc/system/entity/SysRole.java` | 同上 |
| `backend/system-core/src/main/java/com/lc/system/entity/SysMenu.java` | 同上 |

### DTO（4 个）
| 文件 | 改动 |
| --- | --- |
| `backend/system-core/src/main/java/com/lc/system/dto/TenantDTO.java` | `UpdateRequest` + `TenantResponse` 加 `version` |
| `backend/system-core/src/main/java/com/lc/system/dto/UserDTO.java` | `UpdateRequest` + `UserResponse` 加 `version` |
| `backend/system-core/src/main/java/com/lc/system/dto/RoleDTO.java` | `UpdateRequest` + `RoleResponse` 加 `version` |
| `backend/system-core/src/main/java/com/lc/system/dto/MenuDTO.java` | `UpdateRequest` + `MenuResponse` 加 `version` |

### ServiceImpl（4 个）
| 文件 | 改动 |
| --- | --- |
| `backend/system-core/src/main/java/com/lc/system/service/impl/UserServiceImpl.java` | `deleteUser` 软删除；`update` 显式 version 校验；`toResponse` 加 version |
| `backend/system-core/src/main/java/com/lc/system/service/impl/RoleServiceImpl.java` | `delete` 软删除；`update` 显式 version 校验；`toResponse` 加 version |
| `backend/system-core/src/main/java/com/lc/system/service/impl/MenuServiceImpl.java` | `delete` 软删除；`update` 显式 version 校验；`toResponse` 加 version |
| `backend/system-core/src/main/java/com/lc/system/service/impl/TenantServiceImpl.java` | `delete` 改为 `setDeleted(1)`（原为 `setStatus(0)`）；`update` 显式 version 校验；`toResponse` 加 version |

### 异常处理（1 个）
| 文件 | 改动 |
| --- | --- |
| `backend/bootstrap/src/main/java/com/lc/bootstrap/handler/GlobalExceptionHandler.java` | 新增 `ObjectOptimisticLockingFailureException` handler → 409 + `DATA_CONFLICT` |

---

## 2. V17 + V18 完整 SQL

### V17__add_soft_delete.sql
```sql
-- 为业务主表添加软删除字段
ALTER TABLE sys_tenant ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0=未删除，1=已删除';
ALTER TABLE sys_user   ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0=未删除，1=已删除';
ALTER TABLE sys_role   ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0=未删除，1=已删除';
ALTER TABLE sys_menu   ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0=未删除，1=已删除';

-- 软删除索引（按租户+删除状态查询）
CREATE INDEX idx_sys_tenant_deleted ON sys_tenant(deleted);
CREATE INDEX idx_sys_user_deleted   ON sys_user(deleted);
CREATE INDEX idx_sys_role_deleted   ON sys_role(deleted);
CREATE INDEX idx_sys_menu_deleted   ON sys_menu(deleted);
```

### V18__add_optimistic_lock.sql
```sql
-- 为可编辑配置表添加乐观锁版本字段
ALTER TABLE sys_tenant ADD COLUMN version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';
ALTER TABLE sys_user   ADD COLUMN version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';
ALTER TABLE sys_role   ADD COLUMN version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';
ALTER TABLE sys_menu   ADD COLUMN version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';
```

---

## 3. 实体改造片段（示例：SysTenant）

```java
package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_tenant")
@SQLRestriction("deleted = 0")          // C5：Hibernate 6 软删除过滤
public class SysTenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ... 其它字段 ...

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "deleted", nullable = false)
    private Integer deleted = 0;        // C5：软删除标记

    @Version                              // C6：JPA 乐观锁
    @Column(name = "version", nullable = false)
    private Long version = 0L;          // C6：版本号

    // ... 其余字段 / @PrePersist / @PreUpdate ...
}
```

四个实体改造方式完全一致（均位于 `system-core` 模块）。

---

## 4. ServiceImpl delete + update 改造片段

### 4.1 软删除改造（delete 方法）

**UserServiceImpl.deleteUser**
```java
@Override
@Transactional
public void deleteUser(Long id) {
    SysUser existing = getUserOrThrow(id);
    // 清理用户角色关联关系
    userRoleRepository.deleteByUserId(id);
    existing.setDeleted(1);              // 软删除
    userRepository.save(existing);
}
```

**RoleServiceImpl.delete**
```java
@Override
@Transactional
public void delete(Long id) {
    SysRole existing = getRoleOrThrow(id);
    roleMenuRepository.deleteByRoleId(id);
    userRoleRepository.deleteByRoleId(id);
    rolePermissionRepository.deleteByRoleId(id);
    existing.setDeleted(1);              // 软删除
    roleRepository.save(existing);
}
```

**MenuServiceImpl.delete**（保留“禁止删除含子菜单的节点”校验 + 关联清理）
```java
@Override
@Transactional
public void delete(Long id) {
    SysMenu existing = getMenuOrThrow(id);
    List<SysMenu> children = menuRepository.findByParentIdAndTenantId(id, existing.getTenantId());
    if (!children.isEmpty()) {
        throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "请先删除子菜单");
    }
    List<SysRoleMenu> roleMenus = roleMenuRepository.findByMenuId(id);
    if (!roleMenus.isEmpty()) {
        roleMenuRepository.deleteAll(roleMenus);
    }
    existing.setDeleted(1);              // 软删除
    menuRepository.save(existing);
}
```

**TenantServiceImpl.delete**（原为 `setStatus(0)`，改为统一软删除字段）
```java
@Override
@Transactional
public void delete(Long id) {
    SysTenant existing = tenantRepository.findById(id)
            .orElseThrow(() -> new BusinessException(GlobalErrorCode.TENANT_NOT_FOUND));
    existing.setDeleted(1);              // 软删除（原为 setStatus(0)）
    tenantRepository.save(existing);
}
```
> `toggleStatus` 方法保持不变（那是启用/禁用，不是删除）。

### 4.2 乐观锁改造（update 方法 + 显式 version 校验）

四个 ServiceImpl 的 DTO-based `update` 方法均在开头加了同样的前置校验（以 `UserServiceImpl.update` 为例）：

```java
@Override
@Transactional
public UserDTO.UserResponse update(Long id, UserDTO.UpdateRequest request) {
    SysUser existing = getUserOrThrow(id);
    // C6：显式乐观锁校验（比依赖 JPA @Version 异常更友好）
    if (request.getVersion() != null && !request.getVersion().equals(existing.getVersion())) {
        throw new BusinessException(GlobalErrorCode.DATA_CONFLICT);
    }
    // ... 原有 load-modify-save 逻辑 ...
    return toResponse(userRepository.save(existing));
}
```

四个 update 方法（`UserServiceImpl`、`RoleServiceImpl`、`MenuServiceImpl`、`TenantServiceImpl`）均加此校验。
JPA `@Version` 作为兜底：即使前端不传 version，并发写时也会在 flush 时抛 `ObjectOptimisticLockingFailureException`，由 `GlobalExceptionHandler` 转为 409。

### 4.3 toResponse 加 version（4 处）

```java
private UserDTO.UserResponse toResponse(SysUser user) {
    return UserDTO.UserResponse.builder()
            // ... 其它字段 ...
            .status(user.getStatus())
            .version(user.getVersion())     // 供前端下次更新回传
            .createdTime(user.getCreatedTime())
            .build();
}
```
四个 ServiceImpl 的 toResponse 均添加 `.version(entity.getVersion())`。

---

## 5. GlobalExceptionHandler 新增 handler

```java
import com.lc.common.exception.GlobalErrorCode;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
// ...

@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
@ResponseStatus(HttpStatus.CONFLICT)
public Result<Void> handleOptimisticLockingFailure(ObjectOptimisticLockingFailureException e) {
    log.warn("Optimistic locking conflict: {}", e.getMessage());
    return Result.fail(GlobalErrorCode.DATA_CONFLICT.getCode(), "数据已被他人修改，请刷新后重试");
}
```
- 返回 HTTP 409 + code=2004（`DATA_CONFLICT`）+ 中文友好提示。
- 该 handler 比 `Exception.class` 通用 handler 更具体，Spring 会优先匹配。
- 显式 version 校验抛出的 `BusinessException(DATA_CONFLICT)` 已由原有 `handleBusinessException` 处理（同 code，200）。

---

## 6. 编译验证输出

```
$ cd /workspace/backend && mvn clean compile
[INFO] Reactor Summary for lc-platform 1.0.0-SNAPSHOT:
[INFO]
[INFO] lc-platform ........................................ SUCCESS [  0.075 s]
[INFO] common ............................................. SUCCESS [  1.881 s]
[INFO] system-core ........................................ SUCCESS [  1.892 s]
[INFO] project-core ....................................... SUCCESS [  0.024 s]
[INFO] member-core ........................................ SUCCESS [  0.032 s]
[INFO] version-core ....................................... SUCCESS [  0.017 s]
[INFO] template-core ...................................... SUCCESS [  0.019 s]
[INFO] plugin-datasource .................................. SUCCESS [  0.016 s]
[INFO] plugin-form ........................................ SUCCESS [  0.017 s]
[INFO] plugin-bi .......................................... SUCCESS [  0.025 s]
[INFO] plugin-flow ........................................ SUCCESS [  0.014 s]
[INFO] plugin-api ......................................... SUCCESS [  0.015 s]
[INFO] sandbox-engine ..................................... SUCCESS [  0.014 s]
[INFO] code-generator ..................................... SUCCESS [  0.015 s]
[INFO] bootstrap .......................................... SUCCESS [  0.666 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  5.077 s
```
所有 15 个模块编译通过，无警告、无错误。

---

## 7. Commit Hash

```
550dceb4 fix(phase1-review): implement C5 soft delete + C6 optimistic lock
```
- 15 files changed, 95 insertions(+), 7 deletions(-)
- 2 个新文件：V17 / V18 迁移脚本
- 13 个修改文件：4 实体 + 4 DTO + 4 ServiceImpl + 1 GlobalExceptionHandler

---

## 8. 任何疑虑

1. **`@SQLRestriction` 对原生 SQL/JPQL 绕过的影响**：`@SQLRestriction("deleted = 0")` 会自动附加到 Hibernate 生成的所有 SELECT（含 findById / findByXxx），但项目里若有原生 SQL（`nativeQuery = true`）查询这些表，过滤条件不会自动加上，需要人工补 `WHERE deleted = 0`。当前 grep 未发现这些表的原生 SQL 查询，但未来新增时需注意。
2. **`UserServiceImpl.updateUser(SysUser user)`（非 DTO-based，第 67 行）未加 version 校验**：任务只要求改 DTO-based 的 4 个 `update` 方法，该方法是底层/内部调用入口（接收 `SysUser` 实体），未加显式校验。它依赖 JPA `@Version` 在 flush 时兜底（会抛 `ObjectOptimisticLockingFailureException` → 409），功能上仍受乐观锁保护，但错误码不如 DTO 路径友好。如对外暴露该入口需补校验。
3. **软删除后唯一约束的潜在冲突**：`sys_tenant.tenant_code`、`sys_user.username`（在 SysUser 实体中标注 `length=64` 但迁移 V2 中 `username` 未显式 UNIQUE）等唯一约束仍作用于所有行（含 deleted=1）。若同一 username 删除后想重建，可能因唯一索引冲突失败。后续如遇该场景需将唯一索引改为 `(username, deleted)` 复合索引——本次未改，因属范围外且现有数据无此问题。
