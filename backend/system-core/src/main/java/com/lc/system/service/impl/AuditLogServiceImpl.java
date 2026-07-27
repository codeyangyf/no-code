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
