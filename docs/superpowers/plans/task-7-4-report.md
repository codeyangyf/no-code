# Phase 1 Task 7-4 实现报告

## 1. 创建/修改的文件清单

| 类型 | 文件路径 |
| --- | --- |
| 新建 | `backend/common/src/main/java/com/lc/common/security/FileUploadValidator.java` |
| 新建 | `backend/common/src/main/java/com/lc/common/security/SsrfProtector.java` |
| 新建 | `backend/system-core/src/main/java/com/lc/system/controller/AuditLogController.java` |
| 修改 | `backend/system-core/src/main/java/com/lc/system/controller/FileController.java`（重构 upload 方法） |
| 修改 | `backend/common/pom.xml`（新增 `spring-web` 依赖，为 common 模块提供 `MultipartFile`） |

> 说明：`common` 模块原先未引入 `spring-web`，而 `FileUploadValidator` 需要 `org.springframework.web.multipart.MultipartFile`。为使 common 模块可独立编译，新增 `org.springframework:spring-web` 依赖（版本由 Spring Boot 3.2.5 BOM 管理，无需显式声明版本）。该变更已一并纳入提交，否则提交后将无法编译。

## 2. FileUploadValidator 完整代码

```java
package com.lc.common.security;

import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.common.storage.StorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * 文件上传校验器，统一封装 MIME 白名单、大小上限与文件名安全校验。
 * <p>
 * 通用 {@link #validate(MultipartFile, Set)} 允许调用方传入自定义白名单；
 * {@link #validateImage(MultipartFile)} 与 {@link #validateDocument(MultipartFile)}
 * 分别使用预定义的图片/文档白名单；{@link #validateGeneral(MultipartFile)}
 * 使用图片与文档白名单的并集，适合通用上传场景。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadValidator {

    /** 图片 MIME 白名单 */
    public static final Set<String> IMAGE_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp", "image/svg+xml"
    );

    /** 文档 MIME 白名单 */
    public static final Set<String> DOC_TYPES = Set.of(
            "application/pdf",
            "text/plain",
            "application/json",
            "application/zip",
            "application/x-zip-compressed",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    );

    /** 通用白名单：图片与文档的并集 */
    private static final Set<String> GENERAL_TYPES = mergeSets(IMAGE_TYPES, DOC_TYPES);

    private final StorageProperties storageProperties;

    /**
     * 校验图片文件：仅允许 image/* 类型。
     */
    public void validateImage(MultipartFile file) {
        validate(file, IMAGE_TYPES);
    }

    /**
     * 校验文档文件：允许 pdf/txt/json/zip/doc/docx/xls/xlsx/ppt/pptx 等。
     */
    public void validateDocument(MultipartFile file) {
        validate(file, DOC_TYPES);
    }

    /**
     * 通用校验：使用图片与文档并集白名单。
     */
    public void validateGeneral(MultipartFile file) {
        validate(file, GENERAL_TYPES);
    }

    /**
     * 通用校验：白名单 + 大小 + 文件名安全（无路径遍历）。
     *
     * @param file         待校验文件
     * @param allowedTypes 允许的 Content-Type 集合
     */
    public void validate(MultipartFile file, Set<String> allowedTypes) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "文件不能为空");
        }
        if (file.getSize() > storageProperties.getMaxFileSize()) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "文件大小超过限制");
        }

        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType.toLowerCase())) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(),
                    "不支持的文件类型: " + contentType);
        }

        String originalName = file.getOriginalFilename();
        if (originalName != null
                && (originalName.contains("..") || originalName.contains("/") || originalName.contains("\\"))) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "文件名非法");
        }
    }

    private static Set<String> mergeSets(Set<String> a, Set<String> b) {
        Set<String> merged = new java.util.HashSet<>(a.size() + b.size());
        merged.addAll(a);
        merged.addAll(b);
        return Set.copyOf(merged);
    }
}
```

> 实现说明：`StorageProperties` 仅标注 `@ConfigurationProperties`，但在 bootstrap 模块的 `StorageConfig` 通过 `@EnableConfigurationProperties(StorageProperties.class)` 注册为 Bean，与现有 `FileController` 的注入方式一致，故可直接用 `@RequiredArgsConstructor` 注入。
>
> 异常构造：任务伪代码使用 `BusinessException(GlobalErrorCode.VALIDATION_ERROR, "msg")`，但 `BusinessException` 仅有 `(int, String)` 与 `(GlobalErrorCode)` 两个构造器，不存在 `(GlobalErrorCode, String)`。为保留自定义错误消息并复用既有 code，采用 `new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "msg")`，与原 `FileController` 中"不支持的文件类型"的写法保持一致。

## 3. SsrfProtector 完整代码

```java
package com.lc.common.security;

import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

/**
 * SSRF（服务端请求伪造）防护工具。
 * <p>
 * 在服务端发起外网请求前，通过 {@link #validateUrl(String)} 或 {@link #isSafeUrl(String)}
 * 校验目标 URL：禁止协议非法、指向内网/回环/链路本地/保留地址的目标，并对 host 做 DNS
 * 解析后再次检查解析结果，避免使用域名绕过 IP 段校验。
 */
@Slf4j
@Component
public class SsrfProtector {

    /**
     * 校验 URL 是否安全（非内网、协议合法）。
     *
     * @param url 待校验的 URL
     * @throws BusinessException 当 URL 指向内网或协议非法时
     */
    public void validateUrl(String url) {
        String host = doCheck(url);
        if (host != null) {
            throw new BusinessException(GlobalErrorCode.PERMISSION_DENIED.getCode(),
                    "URL 不允许访问: " + host);
        }
    }

    /**
     * 判断 URL 是否安全（不抛异常，返回 boolean）。
     *
     * @param url 待校验的 URL
     * @return true 表示安全，false 表示不安全或校验异常
     */
    public boolean isSafeUrl(String url) {
        try {
            return doCheck(url) == null;
        } catch (Exception e) {
            log.debug("SSRF isSafeUrl check failed for url={}", url, e);
            return false;
        }
    }

    /**
     * 执行校验逻辑。
     *
     * @return null 表示安全；非 null 表示不安全，返回值为拒绝原因中使用的 host
     */
    private String doCheck(String url) {
        if (url == null || url.isBlank()) {
            return "";
        }

        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            log.debug("SSRF check: invalid url syntax {}", url);
            return "";
        }

        String scheme = uri.getScheme();
        if (scheme == null
                || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) {
            log.debug("SSRF check: illegal scheme {}", scheme);
            return "";
        }

        String host = uri.getHost();
        if (host == null || host.isBlank()) {
            return "";
        }
        String lowerHost = host.toLowerCase();

        // 1. 基于字符串的内网/保留地址判定
        if (isReservedHost(lowerHost)) {
            return host;
        }

        // 2. DNS 解析后再判定，防止用域名绕过
        InetAddress[] addresses;
        try {
            addresses = InetAddress.getAllByName(host);
        } catch (UnknownHostException e) {
            // DNS 解析失败：拒绝，避免解析失败绕过
            log.debug("SSRF check: DNS resolution failed for {}", host);
            return host;
        }
        for (InetAddress addr : addresses) {
            if (addr.isSiteLocalAddress() || addr.isLoopbackAddress()
                    || addr.isLinkLocalAddress() || addr.isAnyLocalAddress()) {
                return host;
            }
        }

        return null;
    }

    /**
     * 基于字符串判定 host 是否属于内网/回环/链路本地/保留地址。
     */
    private boolean isReservedHost(String host) {
        if ("localhost".equals(host) || host.endsWith(".localhost")) {
            return true;
        }
        if ("127.0.0.1".equals(host) || host.startsWith("127.")) {
            return true;
        }
        if (host.startsWith("10.")) {
            return true;
        }
        if (host.startsWith("192.168.")) {
            return true;
        }
        if (host.startsWith("169.254.")) {
            return true;
        }
        if ("0.0.0.0".equals(host)) {
            return true;
        }
        // IPv6 回环
        if ("::1".equals(host) || "[::1]".equals(host)) {
            return true;
        }
        // IPv6 ULA（fc00::/7）
        if (host.startsWith("fc") || host.startsWith("fd")) {
            return true;
        }
        // 172.16.0.0 ~ 172.31.255.255
        if (host.startsWith("172.")) {
            String[] parts = host.split("\\.");
            if (parts.length >= 2) {
                try {
                    int second = Integer.parseInt(parts[1]);
                    if (second >= 16 && second <= 31) {
                        return true;
                    }
                } catch (NumberFormatException ignore) {
                    // 非 IPv4 数字段，交给 DNS 解析判定
                }
            }
        }
        return false;
    }
}
```

## 4. AuditLogController 完整代码

```java
package com.lc.system.controller;

import com.lc.common.annotation.AuditLog;
import com.lc.common.annotation.PreAuthorize;
import com.lc.common.context.UserContext;
import com.lc.common.dto.PageResult;
import com.lc.common.dto.Result;
import com.lc.system.dto.AuditLogDTO;
import com.lc.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 审计日志查询 REST 接口。
 * <p>
 * 普通用户只能查询本租户的审计日志；超级管理员（{@link UserContext#getTenantId()} 为 null）
 * 可查询任意租户（按 request.tenantId 过滤）。
 */
@RestController
@RequestMapping("/api/system/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("audit:log:list")
    @AuditLog(action = "查询审计日志", resourceType = "AUDIT_LOG")
    public Result<PageResult<AuditLogDTO.Response>> list(AuditLogDTO.QueryRequest request) {
        // 租户隔离：普通用户强制覆盖 tenantId 为当前用户租户
        Long currentTenantId = UserContext.getTenantId();
        if (currentTenantId != null) {
            // 普通用户：强制只能查自己租户
            request.setTenantId(currentTenantId);
        }
        // currentTenantId == null 表示超级管理员，允许查任意租户（按 request.tenantId 过滤）
        return Result.success(auditLogService.pageQuery(request));
    }
}
```

## 5. FileController.upload 重构后的关键片段

注入新增字段并删除内联白名单常量：

```java
private final StorageService storageService;
private final StorageProperties storageProperties;
private final FileUploadValidator fileUploadValidator;

@PostMapping("/upload")
public Result<FileUploadResponse> upload(@RequestParam("file") MultipartFile file) {
    fileUploadValidator.validateGeneral(file);

    Long tenantId = UserContext.getTenantId();
    if (tenantId == null) {
        throw new BusinessException(GlobalErrorCode.UNAUTHORIZED);
    }

    String originalName = file.getOriginalFilename();
    String ext = extractExtension(originalName);
    String uuid = UUID.randomUUID().toString().replace("-", "");
    String fileName = ext.isEmpty() ? uuid : uuid + "." + ext;
    String datePath = LocalDate.now().format(DATE_FMT);
    String key = String.format("tenant/%d/%s/%s", tenantId, datePath, fileName);

    String bucket = storageProperties.getDefaultBucket();
    try (InputStream input = file.getInputStream()) {
        storageService.upload(bucket, key, input, file.getContentType(), file.getSize());
    } catch (IOException e) {
        log.error("File upload read failed", e);
        throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
    }
    // ...后续 getPresignedUrl / 返回逻辑不变
}
```

变更要点：
- 删除 `private static final Set<String> ALLOWED_TYPES` 常量及 `import java.util.Set;`
- 原内联的 `file.isEmpty()` / `file.getSize()` / Content-Type 白名单三段校验替换为 `fileUploadValidator.validateGeneral(file);`
- 租户目录构造（`tenant/{tenantId}/...`）、UUID 文件名、`storageService.upload` 等逻辑保持不变
- `storageProperties` 字段保留（仍用于 `getDefaultBucket()`）
- download / delete 的 `validateTenantAccess` 私有方法保持不变
- 注：`validateGeneral` 使用图片+文档并集白名单，相比原先仅 10 项的白名单，新增了 doc/docx/xls/xlsx/ppt/pptx 共 6 类，覆盖范围扩大（更贴近"通用上传"语义）

## 6. 编译验证命令及输出

命令：

```bash
cd /workspace/backend && mvn -pl common,system-core -am compile -q
```

输出（quiet 模式，仅错误输出；本次无错误，退出码 0）：

```
（无输出，exit code 0）
```

补充非 quiet 复核：

```
[INFO] BUILD SUCCESS
```

## 7. Commit hash

```
5c90321cc3543b6376b2ac0d566c1a90fa40c47a
```

提交信息：`feat(task-7-4): add FileUploadValidator, SsrfProtector, AuditLogController; refactor FileController to use validator`

提交包含 5 个文件变更（307 insertions, 19 deletions）：4 个源文件 + `backend/common/pom.xml`。

## 8. 任何疑虑

1. **`common/pom.xml` 被修改**：任务提交规范仅列出 4 个源文件，但 `common` 模块原本缺少 `spring-web` 依赖，`FileUploadValidator` 无法编译。为达成"编译通过"的硬性要求，已在 `common/pom.xml` 新增 `org.springframework:spring-web` 依赖（版本由 Spring Boot BOM 管理），并将该文件一并纳入提交。若不提交 pom.xml 变更，该 commit 将无法独立编译。

2. **`BusinessException` 无 `(GlobalErrorCode, String)` 构造器**：任务伪代码多处使用 `new BusinessException(GlobalErrorCode.VALIDATION_ERROR, "msg")`，但实际类仅有 `(int, String)` 与 `(GlobalErrorCode)` 两个构造器。为保留自定义错误消息，统一改用 `new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "msg")`，与原 `FileController` 既有的"不支持的文件类型"写法一致，行为不变。建议后续若需统一可考虑给 `BusinessException` 增加 `(GlobalErrorCode, String)` 构造器。

3. **`SsrfProtector` 仅完成校验工具，未接入具体调用点**：本任务范围仅创建工具类，未在任何发外网请求的服务中接入 `validateUrl`/`isSafeUrl`。后续若有 HTTP/MinIO 外呼场景需另行接入。另注：当前判定依赖 `InetAddress.isSiteLocalAddress()` 等内置方法 + 字符串规则，对于 DNS 重绑定（TTL=0 的攻击域名）无法在单次校验内完全防御——若场景敏感建议在真正发起连接时再次校验解析后的 IP。

4. **`FileUploadValidator` 校验顺序**：先校验大小再校验 MIME；MIME 校验失败时的错误消息会拼接 `contentType`（可能为 null），与原 `FileController` 行为一致，未做额外保护。
