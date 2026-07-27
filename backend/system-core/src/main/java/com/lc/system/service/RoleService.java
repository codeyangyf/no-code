package com.lc.system.service;

import com.lc.common.dto.PageResult;
import com.lc.system.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    PageResult<RoleDTO.RoleResponse> list(String keyword, int page, int size);

    RoleDTO.RoleResponse getById(Long id);

    RoleDTO.RoleResponse create(RoleDTO.CreateRequest request);

    RoleDTO.RoleResponse update(Long id, RoleDTO.UpdateRequest request);

    void delete(Long id);

    void assignMenus(Long roleId, List<Long> menuIds);

    List<Long> getRoleMenuIds(Long roleId);

    List<RoleDTO.RoleResponse> listAll();
}
