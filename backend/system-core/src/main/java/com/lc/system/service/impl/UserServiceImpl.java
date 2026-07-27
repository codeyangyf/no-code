package com.lc.system.service.impl;

import com.lc.common.context.UserContext;
import com.lc.common.dto.PageResult;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.common.util.PasswordUtil;
import com.lc.system.dto.UserDTO;
import com.lc.system.entity.SysRole;
import com.lc.system.entity.SysUser;
import com.lc.system.entity.SysUserRole;
import com.lc.system.repository.SysRoleRepository;
import com.lc.system.repository.SysUserRepository;
import com.lc.system.repository.SysUserRoleRepository;
import com.lc.system.service.UserService;
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
public class UserServiceImpl implements UserService {

    private final SysUserRepository userRepository;
    private final SysUserRoleRepository userRoleRepository;
    private final SysRoleRepository roleRepository;

    @Override
    public SysUser findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.USER_NOT_FOUND));
    }

    @Override
    public SysUser findByTenantIdAndUsername(Long tenantId, String username) {
        return userRepository.findByTenantIdAndUsername(tenantId, username)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.USER_NOT_FOUND));
    }

    @Override
    public SysUser findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public SysUser createUser(SysUser user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BusinessException(GlobalErrorCode.DATA_CONFLICT);
        }
        if (user.getPassword() != null) {
            user.setPassword(PasswordUtil.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public SysUser updateUser(SysUser user) {
        SysUser existing = findById(user.getId());
        if (!existing.getUsername().equals(user.getUsername()) &&
                userRepository.existsByUsername(user.getUsername())) {
            throw new BusinessException(GlobalErrorCode.DATA_CONFLICT);
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(PasswordUtil.encode(user.getPassword()));
        } else {
            user.setPassword(existing.getPassword());
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        SysUser existing = getUserOrThrow(id);
        // 清理用户角色关联关系
        userRoleRepository.deleteByUserId(id);
        existing.setDeleted(1);
        userRepository.save(existing);
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return PasswordUtil.matches(rawPassword, encodedPassword);
    }

    // ===== 用户管理（DTO-based） =====

    @Override
    @Transactional(readOnly = true)
    public PageResult<UserDTO.UserResponse> list(Long tenantId, String keyword, int page, int size) {
        int pageIndex = page < 1 ? 1 : page;
        int pageSize = size < 1 ? 10 : size;
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);

        Page<SysUser> userPage;
        if (keyword == null || keyword.trim().isEmpty()) {
            userPage = userRepository.findByTenantId(tenantId, pageable);
        } else {
            String kw = keyword.trim();
            userPage = userRepository.findByTenantIdAndUsernameContainingOrRealNameContaining(tenantId, kw, kw, pageable);
        }

        List<UserDTO.UserResponse> records = userPage.getContent().stream()
                .map(this::toResponse)
                .toList();
        return PageResult.of(records, userPage.getTotalElements(), pageIndex, pageSize);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO.UserResponse getDetail(Long id) {
        return toResponse(getUserOrThrow(id));
    }

    @Override
    @Transactional
    public UserDTO.UserResponse create(UserDTO.CreateRequest request) {
        Long tenantId = UserContext.getTenantId();
        if (tenantId != null && userRepository.existsByTenantIdAndUsername(tenantId, request.getUsername())) {
            throw new BusinessException(GlobalErrorCode.DATA_CONFLICT);
        }
        SysUser user = new SysUser();
        user.setTenantId(tenantId);
        user.setUsername(request.getUsername());
        user.setPassword(PasswordUtil.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setEmail(request.getEmail());
        user.setStatus(1);
        return toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDTO.UserResponse update(Long id, UserDTO.UpdateRequest request) {
        SysUser existing = getUserOrThrow(id);
        if (request.getVersion() != null && !request.getVersion().equals(existing.getVersion())) {
            throw new BusinessException(GlobalErrorCode.DATA_CONFLICT);
        }
        if (request.getRealName() != null) {
            existing.setRealName(request.getRealName());
        }
        if (request.getEmail() != null) {
            existing.setEmail(request.getEmail());
        }
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }
        return toResponse(userRepository.save(existing));
    }

    @Override
    @Transactional
    public void resetPassword(Long id, String password) {
        SysUser existing = getUserOrThrow(id);
        if (password == null || password.isEmpty()) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR);
        }
        existing.setPassword(PasswordUtil.encode(password));
        userRepository.save(existing);
    }

    @Override
    @Transactional
    public void assignRoles(Long userId, List<Long> roleIds) {
        getUserOrThrow(userId);
        userRoleRepository.deleteByUserId(userId);
        if (roleIds == null || roleIds.isEmpty()) {
            return;
        }
        // 去重
        Set<Long> distinctIds = new HashSet<>(roleIds);
        // 校验关联角色属于当前租户
        Long currentTenantId = UserContext.getTenantId();
        if (currentTenantId != null) {
            List<SysRole> roles = roleRepository.findAllById(distinctIds);
            if (roles.size() != distinctIds.size()) {
                throw new BusinessException(GlobalErrorCode.NOT_FOUND);
            }
            for (SysRole role : roles) {
                if (!currentTenantId.equals(role.getTenantId())) {
                    throw new BusinessException(GlobalErrorCode.NOT_FOUND);
                }
            }
        }
        for (Long roleId : distinctIds) {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            userRoleRepository.save(ur);
        }
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        SysUser existing = getUserOrThrow(id);
        existing.setStatus(status);
        userRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getUserRoleIds(Long userId) {
        getUserOrThrow(userId);
        return userRoleRepository.findRoleIdsByUserId(userId);
    }

    /**
     * 按主键查询用户并校验租户归属。
     * UserContext.getTenantId() 为 null（超级管理员）时跳过租户校验。
     */
    private SysUser getUserOrThrow(Long id) {
        SysUser user = findById(id);
        Long currentTenantId = UserContext.getTenantId();
        if (currentTenantId != null && !currentTenantId.equals(user.getTenantId())) {
            throw new BusinessException(GlobalErrorCode.NOT_FOUND);
        }
        return user;
    }

    private UserDTO.UserResponse toResponse(SysUser user) {
        return UserDTO.UserResponse.builder()
                .id(user.getId())
                .tenantId(user.getTenantId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .email(user.getEmail())
                .status(user.getStatus())
                .version(user.getVersion())
                .createdTime(user.getCreatedTime())
                .build();
    }
}
