package com.lc.system.service.impl;

import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.common.util.PasswordUtil;
import com.lc.system.entity.SysUser;
import com.lc.system.repository.SysUserRepository;
import com.lc.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SysUserRepository userRepository;

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
        if (!userRepository.existsById(id)) {
            throw new BusinessException(GlobalErrorCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return PasswordUtil.matches(rawPassword, encodedPassword);
    }
}