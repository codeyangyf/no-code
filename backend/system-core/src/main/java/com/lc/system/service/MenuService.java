package com.lc.system.service;

import com.lc.system.dto.MenuDTO;

import java.util.List;

public interface MenuService {
    List<MenuDTO.MenuResponse> list(Long tenantId);

    List<MenuDTO.MenuResponse> getMenuTree(Long tenantId);

    MenuDTO.MenuResponse getById(Long id);

    MenuDTO.MenuResponse create(MenuDTO.CreateRequest request);

    MenuDTO.MenuResponse update(Long id, MenuDTO.UpdateRequest request);

    void delete(Long id);

    List<MenuDTO.MenuResponse> getMenusByRoleIds(List<Long> roleIds);
}
