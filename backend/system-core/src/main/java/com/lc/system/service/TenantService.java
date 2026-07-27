package com.lc.system.service;

import com.lc.common.dto.PageResult;
import com.lc.system.dto.TenantDTO;
import com.lc.system.entity.SysTenant;

public interface TenantService {
    PageResult<TenantDTO.TenantResponse> list(String keyword, int page, int size);
    TenantDTO.TenantResponse getById(Long id);
    TenantDTO.TenantResponse create(TenantDTO.CreateRequest request);
    TenantDTO.TenantResponse update(Long id, TenantDTO.UpdateRequest request);
    void delete(Long id);
    TenantDTO.TenantResponse toggleStatus(Long id, Integer status);
    SysTenant getByCode(String tenantCode);
}
