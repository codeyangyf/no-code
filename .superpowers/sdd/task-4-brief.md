# Task 4: system-core 模块 - Repository 与 UserService

**Goal:** 创建 system-core 模块的 Repository 接口和 UserService 接口及实现。

**Files:**
- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysUserRepository.java`
- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysRoleRepository.java`
- Create: `backend/system-core/src/main/java/com/lc/system/repository/SysUserRoleRepository.java`
- Create: `backend/system-core/src/main/java/com/lc/system/service/UserService.java`
- Create: `backend/system-core/src/main/java/com/lc/system/service/impl/UserServiceImpl.java`

**SysUserRepository.java:**

```java
package com.lc.system.repository;

import com.lc.system.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    Optional<SysUser> findByUsername(String username);
    Optional<SysUser> findByTenantIdAndUsername(Long tenantId, String username);
    boolean existsByUsername(String username);
    boolean existsByTenantIdAndUsername(Long tenantId, String username);
}
```

**SysRoleRepository.java:**

```java
package com.lc.system.repository;

import com.lc.system.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
    Optional<SysRole> findByRoleCode(String roleCode);
    Optional<SysRole> findByTenantIdAndRoleCode(Long tenantId, String roleCode);
}
```

**SysUserRoleRepository.java:**

```java
package com.lc.system.repository;

import com.lc.system.entity.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {
    List<SysUserRole> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
```

**UserService.java:**

```java
package com.lc.system.service;

import com.lc.system.entity.SysUser;

public interface UserService {
    SysUser findByUsername(String username);
    SysUser findByTenantIdAndUsername(Long tenantId, String username);
    SysUser findById(Long id);
    SysUser createUser(SysUser user);
    SysUser updateUser(SysUser user);
    void deleteUser(Long id);
    boolean verifyPassword(String rawPassword, String encodedPassword);
}
```

**UserServiceImpl.java:**

```java
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
```

**Steps:**
1. 创建 SysUserRepository.java
2. 创建 SysRoleRepository.java
3. 创建 SysUserRoleRepository.java
4. 创建 UserService.java
5. 创建 UserServiceImpl.java
6. 编译验证：`cd backend && mvn clean compile -q -pl system-core -am`
7. Commit，提交信息："feat: system-core模块 - Repository与UserService"

**Global Constraints:**
- Java: 17
- Spring Boot: 3.2.5
- Maven: 3.9.x

__tr_native_ec=$?; pwd -P >| '/var/log/tool/jobs/job-a17d8d0b458a46c2ac5c9bc573a1937c/cwd.txt'; exit "$__tr_native_ec"