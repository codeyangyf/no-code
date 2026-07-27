package com.lc.system.service.impl;

import com.lc.common.dto.PageResult;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.system.dto.TenantDTO;
import com.lc.system.entity.SysTenant;
import com.lc.system.repository.SysTenantRepository;
import com.lc.system.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final SysTenantRepository tenantRepository;

    @Override
    public PageResult<TenantDTO.TenantResponse> list(String keyword, int page, int size) {
        int pageIndex = page < 1 ? 1 : page;
        int pageSize = size < 1 ? 10 : size;
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);

        Page<SysTenant> tenantPage;
        if (keyword == null || keyword.trim().isEmpty()) {
            tenantPage = tenantRepository.findAll(pageable);
        } else {
            String kw = keyword.trim();
            tenantPage = tenantRepository.findByTenantNameContainingOrTenantCodeContaining(kw, kw, pageable);
        }

        List<TenantDTO.TenantResponse> records = tenantPage.getContent().stream()
                .map(this::toResponse)
                .toList();
        return PageResult.of(records, tenantPage.getTotalElements(), pageIndex, pageSize);
    }

    @Override
    public TenantDTO.TenantResponse getById(Long id) {
        SysTenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.TENANT_NOT_FOUND));
        return toResponse(tenant);
    }

    @Override
    @Transactional
    public TenantDTO.TenantResponse create(TenantDTO.CreateRequest request) {
        if (tenantRepository.existsByTenantCode(request.getTenantCode())) {
            throw new BusinessException(GlobalErrorCode.DATA_CONFLICT);
        }
        SysTenant tenant = new SysTenant();
        tenant.setTenantCode(request.getTenantCode());
        tenant.setTenantName(request.getTenantName());
        tenant.setDomain(request.getDomain());
        tenant.setExpireTime(request.getExpireTime());
        tenant.setStatus(1);
        SysTenant saved = tenantRepository.save(tenant);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public TenantDTO.TenantResponse update(Long id, TenantDTO.UpdateRequest request) {
        SysTenant existing = tenantRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.TENANT_NOT_FOUND));
        if (request.getTenantName() != null) {
            existing.setTenantName(request.getTenantName());
        }
        if (request.getDomain() != null) {
            existing.setDomain(request.getDomain());
        }
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }
        if (request.getExpireTime() != null) {
            existing.setExpireTime(request.getExpireTime());
        }
        SysTenant saved = tenantRepository.save(existing);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        SysTenant existing = tenantRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.TENANT_NOT_FOUND));
        // 软删除：禁用租户
        existing.setStatus(0);
        tenantRepository.save(existing);
    }

    @Override
    @Transactional
    public TenantDTO.TenantResponse toggleStatus(Long id, Integer status) {
        SysTenant existing = tenantRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.TENANT_NOT_FOUND));
        existing.setStatus(status);
        SysTenant saved = tenantRepository.save(existing);
        return toResponse(saved);
    }

    @Override
    public SysTenant getByCode(String tenantCode) {
        return tenantRepository.findByTenantCode(tenantCode)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.TENANT_NOT_FOUND));
    }

    private TenantDTO.TenantResponse toResponse(SysTenant tenant) {
        return TenantDTO.TenantResponse.builder()
                .id(tenant.getId())
                .tenantCode(tenant.getTenantCode())
                .tenantName(tenant.getTenantName())
                .logoUrl(tenant.getLogoUrl())
                .domain(tenant.getDomain())
                .status(tenant.getStatus())
                .expireTime(tenant.getExpireTime())
                .createdTime(tenant.getCreatedTime())
                .updatedTime(tenant.getUpdatedTime())
                .build();
    }
}
