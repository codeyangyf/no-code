package com.lc.system.service.impl;

import com.lc.common.context.UserContext;
import com.lc.common.dto.PageResult;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.system.dto.RoleDTO;
import com.lc.system.entity.SysMenu;
import com.lc.system.entity.SysRole;
import com.lc.system.entity.SysRoleMenu;
import com.lc.system.repository.SysMenuRepository;
import com.lc.system.repository.SysRoleMenuRepository;
import com.lc.system.repository.SysRolePermissionRepository;
import com.lc.system.repository.SysRoleRepository;
import com.lc.system.repository.SysUserRoleRepository;
import com.lc.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final SysRoleRepository roleRepository;
    private final SysRoleMenuRepository roleMenuRepository;
    private final SysUserRoleRepository userRoleRepository;
    private final SysRolePermissionRepository rolePermissionRepository;
    private final SysMenuRepository menuRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResult<RoleDTO.RoleResponse> list(String keyword, int page, int size) {
        int pageIndex = page < 1 ? 1 : page;
        int pageSize = size < 1 ? 10 : size;
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);

        Long tenantId = UserContext.getTenantId();
        Page<SysRole> rolePage;
        if (keyword == null || keyword.trim().isEmpty()) {
            rolePage = roleRepository.findByTenantId(tenantId, pageable);
        } else {
            rolePage = roleRepository.findByTenantIdAndRoleNameContaining(tenantId, keyword.trim(), pageable);
        }

        List<RoleDTO.RoleResponse> records = rolePage.getContent().stream()
                .map(this::toResponse)
                .toList();
        return PageResult.of(records, rolePage.getTotalElements(), pageIndex, pageSize);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO.RoleResponse getById(Long id) {
        return toResponse(getRoleOrThrow(id));
    }

    @Override
    @Transactional
    public RoleDTO.RoleResponse create(RoleDTO.CreateRequest request) {
        Long tenantId = UserContext.getTenantId();
        if (tenantId != null && roleRepository.findByTenantIdAndRoleCode(tenantId, request.getRoleCode()).isPresent()) {
            throw new BusinessException(GlobalErrorCode.DATA_CONFLICT);
        }
        SysRole role = new SysRole();
        role.setTenantId(tenantId);
        role.setRoleCode(request.getRoleCode());
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        role.setSortOrder(request.getSortOrder());
        role.setStatus(1);
        return toResponse(roleRepository.save(role));
    }

    @Override
    @Transactional
    public RoleDTO.RoleResponse update(Long id, RoleDTO.UpdateRequest request) {
        SysRole existing = getRoleOrThrow(id);
        if (request.getRoleName() != null) {
            existing.setRoleName(request.getRoleName());
        }
        if (request.getDescription() != null) {
            existing.setDescription(request.getDescription());
        }
        if (request.getSortOrder() != null) {
            existing.setSortOrder(request.getSortOrder());
        }
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }
        return toResponse(roleRepository.save(existing));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getRoleOrThrow(id);
        // 删除角色前清理全部关联关系：sys_role_menu / sys_user_role / sys_role_permission
        roleMenuRepository.deleteByRoleId(id);
        userRoleRepository.deleteByRoleId(id);
        rolePermissionRepository.deleteByRoleId(id);
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        getRoleOrThrow(roleId);
        roleMenuRepository.deleteByRoleId(roleId);
        if (menuIds == null || menuIds.isEmpty()) {
            return;
        }
        // 去重
        Set<Long> distinctIds = new HashSet<>(menuIds);
        // 校验关联菜单属于当前租户
        Long currentTenantId = UserContext.getTenantId();
        if (currentTenantId != null) {
            List<SysMenu> menus = menuRepository.findAllById(distinctIds);
            if (menus.size() != distinctIds.size()) {
                throw new BusinessException(GlobalErrorCode.NOT_FOUND);
            }
            for (SysMenu menu : menus) {
                if (!currentTenantId.equals(menu.getTenantId())) {
                    throw new BusinessException(GlobalErrorCode.NOT_FOUND);
                }
            }
        }
        for (Long menuId : distinctIds) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(roleId);
            rm.setMenuId(menuId);
            roleMenuRepository.save(rm);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getRoleMenuIds(Long roleId) {
        getRoleOrThrow(roleId);
        return roleMenuRepository.findMenuIdsByRoleId(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO.RoleResponse> listAll() {
        Long tenantId = UserContext.getTenantId();
        List<SysRole> roles = roleRepository.findByTenantId(tenantId);
        return roles.stream().map(this::toResponse).toList();
    }

    private SysRole getRoleOrThrow(Long id) {
        SysRole role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.ROLE_NOT_FOUND));
        // 校验租户归属（超级管理员 tenantId 为 null 时跳过）
        Long currentTenantId = UserContext.getTenantId();
        if (currentTenantId != null && !currentTenantId.equals(role.getTenantId())) {
            throw new BusinessException(GlobalErrorCode.NOT_FOUND);
        }
        return role;
    }

    private RoleDTO.RoleResponse toResponse(SysRole role) {
        return RoleDTO.RoleResponse.builder()
                .id(role.getId())
                .tenantId(role.getTenantId())
                .roleCode(role.getRoleCode())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .status(role.getStatus())
                .sortOrder(role.getSortOrder())
                .createdTime(role.getCreatedTime())
                .updatedTime(role.getUpdatedTime())
                .build();
    }
}
