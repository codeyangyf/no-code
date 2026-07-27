package com.lc.system.service.impl;

import com.lc.common.context.UserContext;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.system.dto.MenuDTO;
import com.lc.system.entity.SysMenu;
import com.lc.system.entity.SysRoleMenu;
import com.lc.system.repository.SysMenuRepository;
import com.lc.system.repository.SysRoleMenuRepository;
import com.lc.system.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final SysMenuRepository menuRepository;
    private final SysRoleMenuRepository roleMenuRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO.MenuResponse> list(Long tenantId) {
        List<SysMenu> menus = menuRepository.findByTenantId(tenantId);
        return menus.stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO.MenuResponse> getMenuTree(Long tenantId) {
        List<SysMenu> menus = menuRepository.findByTenantId(tenantId);
        return buildTree(menus);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuDTO.MenuResponse getById(Long id) {
        return toResponse(getMenuOrThrow(id));
    }

    @Override
    @Transactional
    public MenuDTO.MenuResponse create(MenuDTO.CreateRequest request) {
        SysMenu menu = new SysMenu();
        menu.setTenantId(UserContext.getTenantId());
        menu.setParentId(request.getParentId());
        menu.setMenuName(request.getMenuName());
        menu.setPath(request.getPath());
        menu.setComponent(request.getComponent());
        menu.setIcon(request.getIcon());
        menu.setMenuType(request.getMenuType());
        menu.setPermission(request.getPermission());
        menu.setSortOrder(request.getSortOrder());
        menu.setStatus(1);
        return toResponse(menuRepository.save(menu));
    }

    @Override
    @Transactional
    public MenuDTO.MenuResponse update(Long id, MenuDTO.UpdateRequest request) {
        SysMenu existing = getMenuOrThrow(id);
        if (request.getParentId() != null) {
            existing.setParentId(request.getParentId());
        }
        if (request.getMenuName() != null) {
            existing.setMenuName(request.getMenuName());
        }
        if (request.getPath() != null) {
            existing.setPath(request.getPath());
        }
        if (request.getComponent() != null) {
            existing.setComponent(request.getComponent());
        }
        if (request.getIcon() != null) {
            existing.setIcon(request.getIcon());
        }
        if (request.getMenuType() != null) {
            existing.setMenuType(request.getMenuType());
        }
        if (request.getPermission() != null) {
            existing.setPermission(request.getPermission());
        }
        if (request.getSortOrder() != null) {
            existing.setSortOrder(request.getSortOrder());
        }
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }
        return toResponse(menuRepository.save(existing));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getMenuOrThrow(id);
        // 删除菜单前先清理角色关联关系
        List<SysRoleMenu> roleMenus = roleMenuRepository.findByMenuId(id);
        if (!roleMenus.isEmpty()) {
            roleMenuRepository.deleteAll(roleMenus);
        }
        menuRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO.MenuResponse> getMenusByRoleIds(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return new ArrayList<>();
        }
        Set<Long> menuIds = new HashSet<>();
        for (Long roleId : roleIds) {
            for (SysRoleMenu rm : roleMenuRepository.findByRoleId(roleId)) {
                menuIds.add(rm.getMenuId());
            }
        }
        if (menuIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<SysMenu> menus = menuRepository.findAllById(menuIds);
        return buildTree(menus);
    }

    private SysMenu getMenuOrThrow(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.NOT_FOUND));
    }

    private MenuDTO.MenuResponse toResponse(SysMenu menu) {
        return MenuDTO.MenuResponse.builder()
                .id(menu.getId())
                .tenantId(menu.getTenantId())
                .parentId(menu.getParentId())
                .menuName(menu.getMenuName())
                .path(menu.getPath())
                .component(menu.getComponent())
                .icon(menu.getIcon())
                .menuType(menu.getMenuType())
                .permission(menu.getPermission())
                .sortOrder(menu.getSortOrder())
                .status(menu.getStatus())
                .createdTime(menu.getCreatedTime())
                .updatedTime(menu.getUpdatedTime())
                .build();
    }

    /**
     * 递归构建菜单树。
     * 根节点为 parentId 为 null 的菜单；按 sortOrder 升序、id 升序排序。
     */
    private List<MenuDTO.MenuResponse> buildTree(List<SysMenu> menus) {
        Map<Long, List<MenuDTO.MenuResponse>> childrenMap = new HashMap<>();
        List<MenuDTO.MenuResponse> roots = new ArrayList<>();
        for (SysMenu menu : menus) {
            MenuDTO.MenuResponse resp = toResponse(menu);
            if (menu.getParentId() == null) {
                roots.add(resp);
            } else {
                childrenMap.computeIfAbsent(menu.getParentId(), k -> new ArrayList<>()).add(resp);
            }
        }
        for (MenuDTO.MenuResponse resp : roots) {
            fillChildren(resp, childrenMap);
        }
        Comparator<MenuDTO.MenuResponse> byOrder = Comparator
                .comparing((MenuDTO.MenuResponse m) -> m.getSortOrder() == null ? Integer.MAX_VALUE : m.getSortOrder())
                .thenComparing(MenuDTO.MenuResponse::getId, Comparator.nullsFirst(Comparator.naturalOrder()));
        roots.sort(byOrder);
        return roots;
    }

    private void fillChildren(MenuDTO.MenuResponse parent, Map<Long, List<MenuDTO.MenuResponse>> childrenMap) {
        List<MenuDTO.MenuResponse> children = childrenMap.get(parent.getId());
        if (children != null && !children.isEmpty()) {
            Comparator<MenuDTO.MenuResponse> byOrder = Comparator
                    .comparing((MenuDTO.MenuResponse m) -> m.getSortOrder() == null ? Integer.MAX_VALUE : m.getSortOrder())
                    .thenComparing(MenuDTO.MenuResponse::getId, Comparator.nullsFirst(Comparator.naturalOrder()));
            children.sort(byOrder);
            parent.setChildren(children);
            for (MenuDTO.MenuResponse child : children) {
                fillChildren(child, childrenMap);
            }
        }
    }
}
