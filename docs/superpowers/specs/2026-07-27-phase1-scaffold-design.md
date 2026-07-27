# Phase 1 脚手架搭建设计文档

## 1. 概述

### 1.1 文档目的
本文档为低代码平台 Phase 1 脚手架搭建的详细设计，涵盖后端 Maven 多模块架构、数据库初始化、认证会话体系、前端项目结构。作为任务1-3（项目脚手架搭建、平台库初始化、认证与会话）的实施依据。

### 1.2 技术栈
| 层级 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 3.2.x |
| Java | OpenJDK | 17 |
| 构建工具 | Maven | 3.9.x |
| 数据库 | MySQL | 8.0+ |
| 缓存/会话 | Redis | 7.x |
| 迁移工具 | Flyway | 最新 |
| 前端框架 | React | 18.x |
| 前端语言 | TypeScript | 5.x |
| 前端构建 | Vite | 6.x |
| UI组件库 | Ant Design | 5.x |
| Pro组件 | @ant-design/pro-components | 最新 |

### 1.3 覆盖范围
- **任务1**：后端 Maven 多模块骨架 + 前端 Vite/React 初始化
- **任务2**：平台库 `lc_platform` 核心表初始化（Flyway 迁移）
- **任务3**：JWT 认证、Refresh Token、Redis 会话管理、登录保护

---

## 2. 目录结构

### 2.1 仓库整体结构

```
no-code/
├── docs/                              # 设计文档
│   └── superpowers/specs/
│       ├── 2026-07-27-lowcode-platform-design.md
│       └── 2026-07-27-phase1-scaffold-design.md
├── backend/                           # Maven 多模块根
│   ├── pom.xml                        # 父 POM
│   ├── common/                        # 公共模块
│   ├── system-core/                   # 系统核心（租户/用户/角色/权限/认证）
│   ├── project-core/                  # 项目核心（占位）
│   ├── member-core/                   # 成员管理（占位）
│   ├── version-core/                  # 版本管理（占位）
│   ├── template-core/                 # 模板市场（占位）
│   ├── plugin-datasource/             # 数据源插件（占位）
│   ├── plugin-form/                   # 表单插件（占位）
│   ├── plugin-bi/                     # BI插件（占位）
│   ├── plugin-flow/                   # 流程插件（占位）
│   ├── plugin-api/                    # 接口插件（占位）
│   ├── sandbox-engine/                # 沙箱引擎（占位）
│   ├── code-generator/                # 代码生成器（占位）
│   └── bootstrap/                     # 启动模块
├── frontend/                          # 前端项目
│   ├── package.json
│   ├── vite.config.ts
│   ├── tsconfig.json
│   └── src/
└── README.md
```

### 2.2 后端模块职责

| 模块 | 任务1-3状态 | 职责说明 |
|------|-------------|----------|
| common | 有代码 | 工具类、异常定义、基础DTO、多租户上下文、Redis配置 |
| system-core | 有代码 | 租户/用户/角色/权限/菜单/组织/字典管理；JWT认证服务；RefreshToken服务 |
| project-core | 占位 | 项目CRUD（后续实现） |
| member-core | 占位 | 项目成员管理（后续实现） |
| version-core | 占位 | 版本管理（后续实现） |
| template-core | 占位 | 模板市场（后续实现） |
| plugin-datasource | 占位 | 数据源插件（后续实现） |
| plugin-form | 占位 | 表单插件（后续实现） |
| plugin-bi | 占位 | BI插件（后续实现） |
| plugin-flow | 占位 | 流程插件（后续实现） |
| plugin-api | 占位 | 接口插件（后续实现） |
| sandbox-engine | 占位 | 沙箱引擎（后续实现） |
| code-generator | 占位 | 代码生成器（后续实现） |
| bootstrap | 有代码 | Spring Boot 启动类、Web配置、Security装配、登录接口、全局异常处理 |

### 2.3 前端目录结构

```
frontend/
├── package.json
├── vite.config.ts
├── tsconfig.json
├── src/
│   ├── main.tsx                       # 入口文件
│   ├── App.tsx                        # 根组件
│   ├── layouts/                       # 布局组件
│   │   └── MainLayout.tsx             # 侧边栏 + 顶部导航
│   ├── pages/                         # 页面组件
│   │   ├── Login/index.tsx            # 登录页
│   │   └── Dashboard/index.tsx        # 首页占位
│   ├── api/                           # API 请求层
│   │   ├── auth.ts                    # 认证接口
│   │   └── index.ts                   # 统一导出
│   ├── hooks/                         # 自定义 Hooks
│   │   └── useAuth.ts                 # 登录状态管理
│   ├── utils/                         # 工具函数
│   │   ├── request.ts                 # Axios 封装
│   │   └── token.ts                   # Token 管理
│   ├── types/                         # 类型定义
│   │   └── index.ts
│   └── styles/                        # 全局样式
│       └── index.css
```

---

## 3. 后端模块依赖

### 3.1 依赖关系图

```
bootstrap (启动模块)
    │
    ├──→ common
    ├──→ system-core
    ├──→ project-core (占位)
    ├──→ member-core (占位)
    ├──→ version-core (占位)
    ├──→ template-core (占位)
    ├──→ plugin-datasource (占位)
    ├──→ plugin-form (占位)
    ├──→ plugin-bi (占位)
    ├──→ plugin-flow (占位)
    ├──→ plugin-api (占位)
    ├──→ sandbox-engine (占位)
    └──→ code-generator (占位)

system-core
    │
    └──→ common

其余业务模块 (project/member/version/plugin-*等)
    │
    └──→ common
```

### 3.2 依赖约束
- `bootstrap` 是唯一启动模块，依赖所有业务模块
- 业务模块之间**互不依赖**，仅依赖 `common`
- 禁止循环依赖

### 3.3 父 POM 依赖管理

| 依赖 | GroupId | ArtifactId | Version | 说明 |
|------|---------|------------|---------|------|
| Spring Boot | org.springframework.boot | spring-boot-starter-parent | 3.2.5 | 父POM |
| Spring Boot Web | org.springframework.boot | spring-boot-starter-web | (由parent管理) | Web层 |
| Spring Boot Security | org.springframework.boot | spring-boot-starter-security | (由parent管理) | 安全框架 |
| Spring Boot Data Redis | org.springframework.boot | spring-boot-starter-data-redis | (由parent管理) | Redis |
| Spring Boot Validation | org.springframework.boot | spring-boot-starter-validation | (由parent管理) | 参数校验 |
| MySQL Connector | com.mysql | mysql-connector-j | 8.0.x | 数据库驱动 |
| Flyway | org.flywaydb | flyway-core | (由parent管理) | 迁移工具 |
| JJWT API | io.jsonwebtoken | jjwt-api | 0.12.x | JWT |
| JJWT Impl | io.jsonwebtoken | jjwt-impl | 0.12.x | JWT实现 |
| JJWT Jackson | io.jsonwebtoken | jjwt-jackson | 0.12.x | JWT序列化 |
| Lombok | org.projectlombok | lombok | (由parent管理) | 简化代码 |
| MapStruct | org.mapstruct | mapstruct | 1.5.x | 对象映射 |
| MapStruct Processor | org.mapstruct | mapstruct-processor | 1.5.x | 注解处理器 |

---

## 4. 认证架构

### 4.1 认证流程

```
┌─────────────────────────────────────────────────────────────────┐
│                      认证流程                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   客户端                   bootstrap                      system-core │
│      │                        │                               │
│      │── POST /api/auth/login ──→│                               │
│      │   {username, password}   │                               │
│      │                          │── 查询 sys_user ──────────→│
│      │                          │   BCrypt校验               │
│      │                          │←── 返回用户信息 ───────────│
│      │                          │── 生成 AccessToken(15min) │
│      │                          │── 生成 RefreshToken(7天)   │
│      │                          │── 存入 Redis ─────────────│
│      │←── {accessToken, refreshToken} ──────────────────────│
│      │                          │                               │
│      │── GET /api/xxx (Bearer Token) ──→│                     │
│      │                          │── JwtFilter解析Token        │
│      │                          │── 校验签名/过期              │
│      │                          │── 设置 SecurityContext      │
│      │                          │── 业务处理                   │
│      │←── 返回数据 ──────────────────────────────────────────│
│      │                          │                               │
│      │── POST /api/auth/refresh ──→│                           │
│      │   {refreshToken}          │── 校验 RefreshToken ─────→│
│      │                          │── 查询 Redis               │
│      │                          │── 生成新Token对            │
│      │                          │── 旧RefreshToken失效       │
│      │←── {newAccessToken, newRefreshToken} ─────────────────│
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 4.2 Token 策略

| Token类型 | 过期时间 | 存储位置 | 用途 |
|-----------|----------|----------|------|
| AccessToken | 15分钟 | 客户端(localStorage/Cookie) | 日常请求认证 |
| RefreshToken | 7天 | Redis + 客户端 | 刷新AccessToken |

### 4.3 Security 过滤器链

```
SecurityFilterChain
    │
    ├── WebSecurityConfigurerAdapter 弃用，使用 SecurityFilterChain Bean
    │
    ├── JwtAuthenticationFilter       # 解析 Authorization 头
    │       └── 验证签名 → 设置 SecurityContext
    │
    ├── TenantContextFilter           # 设置租户上下文（从请求头/域名解析）
    │
    ├── ExceptionTranslationFilter    # 异常处理
    │
    └── FilterSecurityInterceptor     # 权限校验
```

### 4.4 接口安全配置

| 路径模式 | 安全要求 | 说明 |
|----------|----------|------|
| `/api/auth/login` | 匿名访问 | 登录接口 |
| `/api/auth/refresh` | 匿名访问 | 刷新Token |
| `/api/auth/logout` | 需要认证 | 登出接口 |
| `/api/system/**` | 需要认证 | 系统管理接口 |
| `/api/**` | 需要认证 | 其他所有API |

---

## 5. 数据库设计

### 5.1 平台库 lc_platform

任务1-3 阶段创建以下核心表：

| 表名 | Flyway脚本 | 说明 |
|------|------------|------|
| sys_tenant | V1__sys_tenant.sql | 租户表 |
| sys_user | V2__sys_user.sql | 用户表 |
| sys_role | V3__sys_role.sql | 角色表 |
| sys_user_role | V4__sys_user_role.sql | 用户角色关联表 |
| sys_menu | V5__sys_menu.sql | 菜单表 |
| sys_permission | V6__sys_permission.sql | 权限表 |
| sys_org | V7__sys_org.sql | 组织表 |
| sys_dict | V8__sys_dict.sql | 字典表 |
| project_info | V9__project_info.sql | 项目信息表（基础字段） |
| project_member | V10__project_member.sql | 项目成员表 |
| project_version | V11__project_version.sql | 版本表（占位） |
| build_job | V12__build_job.sql | 构建任务表（占位） |
| audit_log | V13__audit_log.sql | 审计日志表 |
| sandbox_session | V14__sandbox_session.sql | 沙箱会话表（占位） |

### 5.2 Flyway 配置

脚本存放路径：`system-core/src/main/resources/db/migration/`

配置（application.yml）：
```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    table: flyway_schema_history
```

### 5.3 Redis 配置

| 用途 | Key 格式 | Value | 过期时间 |
|------|----------|-------|----------|
| RefreshToken | `refresh:{userId}` | RefreshToken字符串 | 7天 |
| Token黑名单 | `token:blacklist:{jti}` | 过期时间戳 | AccessToken剩余有效期 |
| 用户会话 | `session:{userId}` | 设备信息JSON | 7天 |

---

## 6. 前端设计

### 6.1 关键依赖

| 包 | 版本 | 用途 |
|----|------|------|
| react | ^18.2.0 | 核心框架 |
| react-dom | ^18.2.0 | DOM渲染 |
| typescript | ^5.4.0 | 类型安全 |
| vite | ^6.0.0 | 构建工具 |
| antd | ^5.15.0 | UI组件库 |
| @ant-design/pro-components | ^2.6.0 | Pro组件 |
| axios | ^1.6.0 | HTTP请求 |
| lucide-react | ^0.310.0 | 图标 |

### 6.2 API 请求封装

`src/utils/request.ts`：
- 封装 axios 实例
- 请求拦截器：自动附加 `Authorization: Bearer {token}`
- 响应拦截器：统一处理 401（跳转登录）、错误提示
- 支持取消重复请求

### 6.3 登录状态管理

`src/hooks/useAuth.ts`：
- 获取/设置/清除 Token（localStorage）
- 判断登录状态
- 自动刷新 Token（过期前5分钟）

### 6.4 页面路由

任务1-3 阶段仅实现：
- `/login`：登录页
- `/dashboard`：首页（登录后重定向）

---

## 7. 配置与运行

### 7.1 后端配置

`bootstrap/src/main/resources/application.yml`：

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
      password:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true

# JWT配置
jwt:
  secret: ${JWT_SECRET:your-256-bit-secret-key-here}
  access-token-expire-minutes: 15
  refresh-token-expire-days: 7
```

### 7.2 启动方式

**后端**：
```bash
cd backend
mvn spring-boot:run -pl bootstrap -am
```

**前端**：
```bash
cd frontend
npm install
npm run dev
```

### 7.3 环境要求

| 服务 | 版本 | 端口 |
|------|------|------|
| MySQL | 8.0+ | 3306 |
| Redis | 7.x | 6379 |

---

## 8. 验收标准

### 8.1 后端验收
- [ ] Maven 多模块可编译通过（`mvn clean package`）
- [ ] `bootstrap` 可正常启动（端口 8080）
- [ ] Flyway 自动执行迁移，创建所有核心表
- [ ] `/api/auth/login` 接口可用，返回 AccessToken/RefreshToken
- [ ] `/api/auth/refresh` 接口可用，刷新 Token
- [ ] JWT 认证过滤器生效，未登录请求返回 401
- [ ] Redis 中可查询到 RefreshToken

### 8.2 前端验收
- [ ] `npm install` 成功
- [ ] `npm run dev` 正常启动（端口 5173）
- [ ] 登录页可访问
- [ ] 登录成功后重定向到首页
- [ ] 请求自动附加 Authorization 头
- [ ] 401 自动跳转到登录页

---

## 9. 后续扩展

任务1-3 完成后，后续任务按设计文档 Phase 1 继续：
- **任务4**：多租户上下文（租户识别、租户内唯一约束、越权拦截器）
- **任务5**：RBAC权限体系（菜单权限、项目成员角色、接口权限声明）
- **任务6**：对象存储与密钥管理
- **任务7**：安全基线（SSRF防护、文件上传限制、审计日志）
