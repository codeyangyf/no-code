package com.lc.system.repository;

import com.lc.system.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    Optional<SysUser> findByUsername(String username);
    Optional<SysUser> findByTenantIdAndUsername(Long tenantId, String username);
    boolean existsByUsername(String username);
    boolean existsByTenantIdAndUsername(Long tenantId, String username);

    Page<SysUser> findByTenantId(Long tenantId, Pageable pageable);

    Page<SysUser> findByTenantIdAndUsernameContainingOrRealNameContaining(
            Long tenantId, String username, String realName, Pageable pageable);
}
