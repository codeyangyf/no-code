# Task 7-1 实施报告

> 子代理实施报告：创建 `@AuditLog` 注解、V15 迁移脚本、`AuditLogEntity` 实体

## 1. 创建的文件清单（绝对路径）

| # | 文件路径 | 类型 |
|---|---------|------|
| 1 | `/workspace/backend/common/src/main/java/com/lc/common/annotation/AuditLog.java` | 注解（common 模块） |
| 2 | `/workspace/backend/system-core/src/main/resources/db/migration/V15__audit_log_align.sql` | Flyway 迁移脚本 |
| 3 | `/workspace/backend/system-core/src/main/java/com/lc/system/entity/AuditLogEntity.java` | JPA 实体（system-core 模块） |

## 2. V15 迁移脚本完整内容

文件：`/workspace/backend/system-core/src/main/resources/db/migration/V15__audit_log_align.sql`

```sql
-- 对齐设计文档要求的审计日志字段
-- 新增字段：action / resource_type / resource_id / client_ip / result / error_message
-- 旧字段 operation / target_type / target_id / ip 保留以兼容历史数据

ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS action VARCHAR(128) COMMENT '操作动作(对齐设计文档，如 用户登录/创建项目)';
ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS resource_type VARCHAR(64) COMMENT '资源类型(对齐设计文档，如 USER/PROJECT/ROLE)';
ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS resource_id VARCHAR(128) COMMENT '资源ID(对齐设计文档)';
ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS client_ip VARCHAR(64) COMMENT '客户端IP(对齐设计文档)';
ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS result VARCHAR(32) NOT NULL DEFAULT 'SUCCESS' COMMENT '操作结果 SUCCESS/FAILED';
ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS error_message VARCHAR(512) COMMENT '错误信息(FAILED时记录)';

-- 将旧字段数据迁移到新字段（仅当新字段为 NULL 时）
UPDATE audit_log SET action = operation WHERE action IS NULL AND operation IS NOT NULL;
UPDATE audit_log SET resource_type = target_type WHERE resource_type IS NULL AND target_type IS NOT NULL;
UPDATE audit_log SET resource_id = CAST(target_id AS CHAR) WHERE resource_id IS NULL AND target_id IS NOT NULL;
UPDATE audit_log SET client_ip = ip WHERE client_ip IS NULL AND ip IS NOT NULL;

-- 辅助查询索引：按结果统计成功率/失败率、按动作/资源类型筛选
CREATE INDEX idx_audit_result ON audit_log(result);
CREATE INDEX idx_audit_action ON audit_log(action);
CREATE INDEX idx_audit_resource_type ON audit_log(resource_type);
```

### 设计要点
- **V15 版本号**：现有最大迁移版本为 V14（已被 sys_role_menu/sys_role_permission 占用），按规范使用 V15
- **IF NOT EXISTS**：每条 ADD COLUMN 均加 `IF NOT EXISTS`，兼容已建库（MySQL 8.0 支持）
- **COMMENT**：每个新字段均带中文注释
- **数据迁移**：仅在新字段为 NULL 且旧字段非 NULL 时回填，避免覆盖切面后续写入的新数据
- **索引**：按 result/action/resource_type 各建一条普通索引，支持成功率统计与按动作/资源类型筛选查询
- **CREATE INDEX 未加 IF NOT EXISTS**：MySQL 8.0 对 `CREATE INDEX` 不支持 `IF NOT EXISTS` 语法，故未加；首次执行迁移时这些索引尚不存在，无重复执行风险（Flyway 通过 schema_history 表保证每条迁移只执行一次）

## 3. AuditLogEntity 完整代码

文件：`/workspace/backend/system-core/src/main/java/com/lc/system/entity/AuditLogEntity.java`

```java
package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "audit_log")
public class AuditLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "operation")
    private String operation;

    @Column(name = "target_type")
    private String targetType;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "before_data")
    private String beforeData;

    @Column(name = "after_data")
    private String afterData;

    @Column(name = "detail")
    private String detail;

    @Column(name = "ip")
    private String ip;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "action")
    private String action;

    @Column(name = "resource_type")
    private String resourceType;

    @Column(name = "resource_id")
    private String resourceId;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "result")
    private String result = "SUCCESS";

    @Column(name = "error_message")
    private String errorMessage;
}
```

### 字段对齐情况
| 数据库字段 | 实体字段 | 来源 |
|-----------|---------|------|
| id | Long id | V11 |
| tenant_id | Long tenantId | V11 |
| project_id | Long projectId | V11 |
| user_id | Long userId | V11 |
| user_name | String userName | V11 |
| operation | String operation | V11（旧，保留） |
| target_type | String targetType | V11（旧，保留） |
| target_id | Long targetId | V11（旧，保留） |
| before_data | String beforeData | V11 |
| after_data | String afterData | V11 |
| detail | String detail | V11 |
| ip | String ip | V11（旧，保留） |
| user_agent | String userAgent | V11 |
| request_id | String requestId | V11 |
| created_time | LocalDateTime createdTime | V11 |
| action | String action | **V15 新增** |
| resource_type | String resourceType | **V15 新增** |
| resource_id | String resourceId | **V15 新增** |
| client_ip | String clientIp | **V15 新增** |
| result | String result（默认 "SUCCESS"） | **V15 新增** |
| error_message | String errorMessage | **V15 新增** |

字段总数 21，与 V15 后表结构完全对齐。仅使用 `@Data`，未加 `@NoArgsConstructor`/`@AllArgsConstructor`。

## 4. AuditLog 注解完整代码

文件：`/workspace/backend/common/src/main/java/com/lc/common/annotation/AuditLog.java`

```java
package com.lc.common.annotation;

import java.lang.annotation.*;

/**
 * 审计日志注解，标注在 Controller 方法上，由 AOP 切面自动记录审计日志。
 * 操作结果（成功/失败）、操作人、IP、UserAgent 由切面自动填充。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {
    /** 操作动作，如 "用户登录"、"创建项目" */
    String action();

    /** 资源类型，如 "USER"、"PROJECT"、"ROLE" */
    String resourceType();

    /**
     * 资源ID参数名，从方法参数中按名取值，支持 SpEL 表达式 #id。
     * 若不指定或为空字符串，则不记录 resource_id。
     */
    String resourceIdParam() default "";
}
```

### 注解设计说明
- `@Target(ElementType.METHOD)`：仅方法级别（Controller 方法）
- `resourceIdParam()` 默认为空字符串（非 "id"），更安全——若使用方未指定，则不写 resource_id，避免误取同名参数
- 与计划文档示例（默认 "id"）略有偏差，以本任务描述中的代码块为准

## 5. 编译验证命令及输出

**命令：**
```bash
cd /workspace/backend && mvn -pl common,system-core -am compile -q
```

**输出：**
```
（空，无任何 ERROR/WARNING）
```

**Exit code：** 0（成功）

`-q` 安静模式下无任何输出，表示编译完全通过，无警告无错误。

## 6. Commit Hash

```
6dc93d0428bb62ca2be94f42fb47ccb73107b06d
```

分支：`feature/phase1-security-rbac`

提交信息：
```
feat(task-7-1): add @AuditLog annotation, V15 audit_log align migration, AuditLogEntity
```

提交统计：
```
3 files changed, 120 insertions(+)
```

## 7. 遗留问题或疑虑

### 7.1 计划文档与子任务描述的轻微偏差
- 计划文档（line 358）原写 `String resourceIdParam() default "id";`
- 本子任务描述要求 `default ""`
- 已按本任务描述采用 `default ""`（更安全：使用方不指定则不记录 resource_id）
- 父代理后续若发现计划文档需同步，可调整 line 358

### 7.2 CREATE INDEX 未加 IF NOT EXISTS
- MySQL 8.0 对 `CREATE INDEX` 不支持 `IF NOT EXISTS` 语法
- Flyway 通过 `flyway_schema_history` 表保证 V15 只执行一次，正常流程下不会重复创建
- 但若已手动建过同名索引（如 DBA 手工操作），V15 会执行失败——目前无此情况，仅记录

### 7.3 result 字段默认值与切面协同
- 实体层 `result = "SUCCESS"`（Java 字段默认值）与数据库层 `DEFAULT 'SUCCESS'` 一致
- Task 7-3 的 `AuditLogAspect` 在方法异常时需显式写入 `"FAILED"` 并填 `errorMessage`，否则默认值会被持久化为 SUCCESS
- 这是设计意图，符合"操作结果由切面自动填充"的语义，留给 Task 7-3 实现

### 7.4 旧字段保留策略
- `operation / target_type / target_id / ip` 旧字段在 V15 后仍保留在表中
- 实体也保留对应字段，便于历史数据查询与切面过渡期双写
- 设计文档未明确要求废弃旧字段，本任务按"保留兼容"处理；后续若需废弃可单独提迁移脚本
