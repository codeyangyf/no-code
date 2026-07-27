# Task 7-2 实施报告

## 1. 创建的文件清单

| # | 文件路径 | 说明 |
|---|---------|------|
| 1 | `/workspace/backend/system-core/src/main/java/com/lc/system/repository/AuditLogRepository.java` | Repository 接口，继承 JpaRepository + JpaSpecificationExecutor |
| 2 | `/workspace/backend/system-core/src/main/java/com/lc/system/dto/AuditLogDTO.java` | DTO，含 QueryRequest / Response 两个内部类 |
| 3 | `/workspace/backend/system-core/src/main/java/com/lc/system/service/AuditLogService.java` | Service 接口 |
| 4 | `/workspace/backend/system-core/src/main/java/com/lc/system/service/impl/AuditLogServiceImpl.java` | Service 实现 |

## 2. AuditLogServiceImpl 完整代码

```java
package com.lc.system.service.impl;

import com.lc.common.dto.PageResult;
import com.lc.system.dto.AuditLogDTO;
import com.lc.system.entity.AuditLogEntity;
import com.lc.system.repository.AuditLogRepository;
import com.lc.system.service.AuditLogService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void save(AuditLogEntity entity) {
        try {
            auditLogRepository.save(entity);
        } catch (Exception e) {
            log.warn("保存审计日志失败", e);
        }
    }

    @Override
    public PageResult<AuditLogDTO.Response> pageQuery(AuditLogDTO.QueryRequest request) {
        int page = request.getPage() < 1 ? 1 : request.getPage();
        int size = request.getSize() < 1 ? 10 : request.getSize();

        Specification<AuditLogEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getTenantId() != null) {
                predicates.add(cb.equal(root.get("tenantId"), request.getTenantId()));
            }
            if (request.getProjectId() != null) {
                predicates.add(cb.equal(root.get("projectId"), request.getProjectId()));
            }
            if (request.getUserId() != null) {
                predicates.add(cb.equal(root.get("userId"), request.getUserId()));
            }
            if (request.getAction() != null && !request.getAction().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("action"), request.getAction()));
            }
            if (request.getResourceType() != null) {
                predicates.add(cb.equal(root.get("resourceType"), request.getResourceType()));
            }
            if (request.getResult() != null) {
                predicates.add(cb.equal(root.get("result"), request.getResult()));
            }
            if (request.getStartTime() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdTime"), request.getStartTime()));
            }
            if (request.getEndTime() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdTime"), request.getEndTime()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Sort sort = Sort.by(Sort.Direction.DESC, "createdTime");
        PageRequest pageable = PageRequest.of(page - 1, size, sort);

        Page<AuditLogEntity> pageResult = auditLogRepository.findAll(spec, pageable);

        List<AuditLogDTO.Response> records = pageResult.getContent().stream()
                .map(this::toResponse)
                .toList();

        return PageResult.of(records, pageResult.getTotalElements(), page, size);
    }

    private AuditLogDTO.Response toResponse(AuditLogEntity e) {
        AuditLogDTO.Response resp = new AuditLogDTO.Response();
        resp.setId(e.getId());
        resp.setTenantId(e.getTenantId());
        resp.setProjectId(e.getProjectId());
        resp.setUserId(e.getUserId());
        resp.setUserName(e.getUserName());
        resp.setAction(e.getAction());
        resp.setResourceType(e.getResourceType());
        resp.setResourceId(e.getResourceId());
        resp.setClientIp(e.getClientIp());
        resp.setResult(e.getResult());
        resp.setErrorMessage(e.getErrorMessage());
        resp.setDetail(e.getDetail());
        resp.setRequestId(e.getRequestId());
        resp.setCreatedTime(e.getCreatedTime());
        return resp;
    }
}
```

## 3. 编译验证命令及输出

命令：
```bash
cd /workspace/backend && mvn -pl system-core -am compile -q
```

输出：`-q` 静默模式，无错误输出，退出码 `0`，编译通过。

## 4. Commit Hash

```
d578e52e7a756f3732dd637d1b04df9e407cb003
```

提交信息：
```
feat(task-7-2): add AuditLogRepository, AuditLogDTO, AuditLogService + Impl with dynamic Specification query
```

分支：`feature/phase1-security-rbac`，4 files changed, 178 insertions(+)。

## 5. 疑虑

1. **租户隔离未在 Impl 内强制实现**：当前 `pageQuery` 完全依据 `request.getTenantId()` 是否为 null 来决定查询范围，并未在 Service 层自动从上下文注入当前用户的 tenantId（即普通用户不传 tenantId 时会查到所有租户数据）。任务规范只要求"普通用户只能查询自己租户"作为接口注释，实际租户上下文注入预计由 Controller / AOP 层在 Task 7-3 完成，需确认此分工。

2. **`@EqualsAndHashCode(callSuper = true)` 触发 Lombok 警告**：QueryRequest 继承 PageRequest 并调用父类字段，Lombok 默认会发出 "Generating equals/hashCode implementation but without a call to superclass" 警告，已通过 `callSuper = true` 显式消除，编译无报错。

3. **未加 `@Transactional`**：`save` 方法未加事务注解，单条 save 由 JPA 默认事务支持可正常提交；若后续 AOP 切面批量写入审计日志，建议在切面层或新增批量方法上补充事务管理。
