# Task 3: system-core 模块 - 实体类与数据库迁移

**Goal:** 创建 system-core 模块的 JPA 实体类和 Flyway 数据库迁移脚本。

**Files - 实体类:**
- Create: `backend/system-core/src/main/java/com/lc/system/entity/SysTenant.java`
- Create: `backend/system-core/src/main/java/com/lc/system/entity/SysUser.java`
- Create: `backend/system-core/src/main/java/com/lc/system/entity/SysRole.java`
- Create: `backend/system-core/src/main/java/com/lc/system/entity/SysUserRole.java`
- Create: `backend/system-core/src/main/java/com/lc/system/entity/SysMenu.java`
- Create: `backend/system-core/src/main/java/com/lc/system/entity/SysPermission.java`
- Create: `backend/system-core/src/main/java/com/lc/system/entity/SysOrg.java`
- Create: `backend/system-core/src/main/java/com/lc/system/entity/SysDict.java`
- Create: `backend/system-core/src/main/java/com/lc/system/entity/ProjectInfo.java`
- Create: `backend/system-core/src/main/java/com/lc/system/entity/ProjectMember.java`

**Files - Flyway迁移脚本:**
- Create: `backend/system-core/src/main/resources/db/migration/V1__sys_tenant.sql`
- Create: `backend/system-core/src/main/resources/db/migration/V2__sys_user.sql`
- Create: `backend/system-core/src/main/resources/db/migration/V3__sys_role.sql`
- Create: `backend/system-core/src/main/resources/db/migration/V4__sys_user_role.sql`
- Create: `backend/system-core/src/main/resources/db/migration/V5__sys_menu.sql`
- Create: `backend/system-core/src/main/resources/db/migration/V6__sys_permission.sql`
- Create: `backend/system-core/src/main/resources/db/migration/V7__sys_org.sql`
- Create: `backend/system-core/src/main/resources/db/migration/V8__sys_dict.sql`
- Create: `backend/system-core/src/main/resources/db/migration/V9__project_info.sql`
- Create: `backend/system-core/src/main/resources/db/migration/V10__project_member.sql`
- Create: `backend/system-core/src/main/resources/db/migration/V11__audit_log.sql`

**SysTenant.java:**

```java
package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_tenant")
public class SysTenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_code", nullable = false, length = 64, unique = true)
    private String tenantCode;

    @Column(name = "tenant_name", nullable = false, length = 128)
    private String tenantName;

    @Column(name = "logo_url", length = 512)
    private String logoUrl;

    @Column(name = "domain", length = 256)
    private String domain;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "expire_time")
    private LocalDateTime expireTime;

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

**SysUser.java:**

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

    @Column(name = "phone", length = 32)
    private String phone;

    @Column(name = "avatar_url", length = 512)
    private String avatarUrl;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

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

**SysRole.java:**

```java
package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_role")
public class SysRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "role_code", nullable = false, length = 64)
    private String roleCode;

    @Column(name = "role_name", nullable = false, length = 128)
    private String roleName;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "sort_order")
    private Integer sortOrder;

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

**SysUserRole.java:**

```java
package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sys_user_role", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "role_id"})
})
public class SysUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "role_id", nullable = false)
    private Long roleId;
}
```

**SysMenu.java:**

```java
package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_menu")
public class SysMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "menu_name", nullable = false, length = 128)
    private String menuName;

    @Column(name = "path", length = 256)
    private String path;

    @Column(name = "component", length = 256)
    private String component;

    @Column(name = "icon", length = 128)
    private String icon;

    @Column(name = "menu_type", nullable = false, length = 16)
    private String menuType;

    @Column(name = "permission", length = 128)
    private String permission;

    @Column(name = "sort_order")
    private Integer sortOrder;

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

**SysPermission.java:**

```java
package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_permission")
public class SysPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "perm_code", nullable = false, length = 128)
    private String permCode;

    @Column(name = "perm_name", nullable = false, length = 128)
    private String permName;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}
```

**SysOrg.java:**

```java
package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_org")
public class SysOrg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "org_code", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "org_name", nullable = false, length = 128)
    private String orgName;

    @Column(name = "org_type", length = 32)
    private String orgType;

    @Column(name = "sort_order")
    private Integer sortOrder;

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

**SysDict.java:**

```java
package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_dict")
public class SysDict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "dict_code", nullable = false, length = 64)
    private String dictCode;

    @Column(name = "dict_name", nullable = false, length = 128)
    private String dictName;

    @Column(name = "description", length = 512)
    private String description;

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

**ProjectInfo.java:**

```java
package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "project_info")
public class ProjectInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "project_code", nullable = false, length = 64)
    private String projectCode;

    @Column(name = "project_name", nullable = false, length = 128)
    private String projectName;

    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "icon", length = 128)
    private String icon;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "lifecycle_status", nullable = false, length = 32)
    private String lifecycleStatus = "ACTIVE";

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

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

**ProjectMember.java:**

```java
package com.lc.system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "project_member", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_id", "user_id"})
})
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "role", nullable = false, length = 32)
    private String role;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "joined_time", nullable = false)
    private LocalDateTime joinedTime;

    @PrePersist
    protected void onCreate() {
        joinedTime = LocalDateTime.now();
    }
}
```

**V1__sys_tenant.sql:**

```sql
CREATE TABLE IF NOT EXISTS sys_tenant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_code VARCHAR(64) NOT NULL UNIQUE COMMENT '租户编码',
    tenant_name VARCHAR(128) NOT NULL COMMENT '租户名称',
    logo_url VARCHAR(512) COMMENT 'Logo地址',
    domain VARCHAR(256) COMMENT '域名',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    expire_time DATETIME COMMENT '过期时间',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间',
    INDEX idx_tenant_code (tenant_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户表';
```

**V2__sys_user.sql:**

```sql
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT COMMENT '租户ID',
    username VARCHAR(64) NOT NULL COMMENT '用户名',
    password VARCHAR(256) NOT NULL COMMENT '密码(BCrypt)',
    real_name VARCHAR(64) COMMENT '真实姓名',
    email VARCHAR(128) COMMENT '邮箱',
    phone VARCHAR(32) COMMENT '手机号',
    avatar_url VARCHAR(512) COMMENT '头像地址',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    last_login_time DATETIME COMMENT '最后登录时间',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间',
    INDEX idx_user_tenant_id (tenant_id),
    INDEX idx_user_username (username),
    CONSTRAINT uk_user_tenant_username UNIQUE (tenant_id, username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
```

**V3__sys_role.sql:**

```sql
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT COMMENT '租户ID',
    role_code VARCHAR(64) NOT NULL COMMENT '角色编码',
    role_name VARCHAR(128) NOT NULL COMMENT '角色名称',
    description VARCHAR(512) COMMENT '描述',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    sort_order INT COMMENT '排序',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间',
    INDEX idx_role_tenant_id (tenant_id),
    CONSTRAINT uk_role_tenant_code UNIQUE (tenant_id, role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';
```

**V4__sys_user_role.sql:**

```sql
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    CONSTRAINT uk_user_role UNIQUE (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';
```

**V5__sys_menu.sql:**

```sql
CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT COMMENT '租户ID',
    parent_id BIGINT COMMENT '父菜单ID',
    menu_name VARCHAR(128) NOT NULL COMMENT '菜单名称',
    path VARCHAR(256) COMMENT '路由路径',
    component VARCHAR(256) COMMENT '组件路径',
    icon VARCHAR(128) COMMENT '图标',
    menu_type VARCHAR(16) NOT NULL COMMENT '菜单类型 MENU/BUTTON/DIRECTORY',
    permission VARCHAR(128) COMMENT '权限标识',
    sort_order INT COMMENT '排序',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间',
    INDEX idx_menu_tenant_id (tenant_id),
    INDEX idx_menu_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';
```

**V6__sys_permission.sql:**

```sql
CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT COMMENT '租户ID',
    perm_code VARCHAR(128) NOT NULL COMMENT '权限编码',
    perm_name VARCHAR(128) NOT NULL COMMENT '权限名称',
    description VARCHAR(512) COMMENT '描述',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    INDEX idx_perm_tenant_id (tenant_id),
    CONSTRAINT uk_perm_tenant_code UNIQUE (tenant_id, perm_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';
```

**V7__sys_org.sql:**

```sql
CREATE TABLE IF NOT EXISTS sys_org (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT COMMENT '租户ID',
    parent_id BIGINT COMMENT '父组织ID',
    org_code VARCHAR(64) NOT NULL COMMENT '组织编码',
    org_name VARCHAR(128) NOT NULL COMMENT '组织名称',
    org_type VARCHAR(32) COMMENT '组织类型',
    sort_order INT COMMENT '排序',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间',
    INDEX idx_org_tenant_id (tenant_id),
    INDEX idx_org_parent_id (parent_id),
    CONSTRAINT uk_org_tenant_code UNIQUE (tenant_id, org_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织表';
```

**V8__sys_dict.sql:**

```sql
CREATE TABLE IF NOT EXISTS sys_dict (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT COMMENT '租户ID',
    dict_code VARCHAR(64) NOT NULL COMMENT '字典编码',
    dict_name VARCHAR(128) NOT NULL COMMENT '字典名称',
    description VARCHAR(512) COMMENT '描述',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间',
    INDEX idx_dict_tenant_id (tenant_id),
    CONSTRAINT uk_dict_tenant_code UNIQUE (tenant_id, dict_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典表';
```

**V9__project_info.sql:**

```sql
CREATE TABLE IF NOT EXISTS project_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    project_code VARCHAR(64) NOT NULL COMMENT '项目编码',
    project_name VARCHAR(128) NOT NULL COMMENT '项目名称',
    description VARCHAR(1024) COMMENT '描述',
    icon VARCHAR(128) COMMENT '图标',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    lifecycle_status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '生命周期状态',
    created_by BIGINT NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间',
    INDEX idx_project_tenant_id (tenant_id),
    INDEX idx_project_code (project_code),
    CONSTRAINT uk_project_tenant_code UNIQUE (tenant_id, project_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目信息表';
```

**V10__project_member.sql:**

```sql
CREATE TABLE IF NOT EXISTS project_member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    project_id BIGINT NOT NULL COMMENT '项目ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role VARCHAR(32) NOT NULL COMMENT '角色 READ_ONLY/EDITOR/ADMIN/PUBLISHER',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    joined_time DATETIME NOT NULL COMMENT '加入时间',
    CONSTRAINT uk_project_member UNIQUE (project_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目成员表';
```

**V11__audit_log.sql:**

```sql
CREATE TABLE IF NOT EXISTS audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT COMMENT '租户ID',
    project_id BIGINT COMMENT '项目ID',
    user_id BIGINT COMMENT '操作用户ID',
    user_name VARCHAR(64) COMMENT '操作用户名',
    operation VARCHAR(128) NOT NULL COMMENT '操作类型',
    target_type VARCHAR(64) COMMENT '目标类型',
    target_id BIGINT COMMENT '目标ID',
    before_data TEXT COMMENT '操作前数据',
    after_data TEXT COMMENT '操作后数据',
    detail VARCHAR(2048) COMMENT '操作详情',
    ip VARCHAR(64) COMMENT 'IP地址',
    user_agent VARCHAR(512) COMMENT 'UserAgent',
    request_id VARCHAR(64) COMMENT '请求ID',
    created_time DATETIME NOT NULL COMMENT '创建时间',
    INDEX idx_audit_tenant_id (tenant_id),
    INDEX idx_audit_project_id (project_id),
    INDEX idx_audit_user_id (user_id),
    INDEX idx_audit_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审计日志表';
```

**Steps:**
1. 创建 10 个 JPA 实体类
2. 创建 11 个 Flyway 迁移脚本
3. 编译验证：`cd backend && mvn clean compile -q -pl system-core -am`
4. Commit，提交信息："feat: system-core模块 - JPA实体类与Flyway迁移脚本"

**Global Constraints:**
- Java: 17
- Spring Boot: 3.2.5
- Maven: 3.9.x
- MySQL: 8.0+
- Flyway: 9.22.3

__tr_native_ec=$?; pwd -P >| '/var/log/tool/jobs/job-90c90ac5afad40f68ef688d070919133/cwd.txt'; exit "$__tr_native_ec"