package com.lc.system.repository;

import com.lc.system.entity.SysRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
    Optional<SysRole> findByRoleCode(String roleCode);
    Optional<SysRole> findByTenantIdAndRoleCode(Long tenantId, String roleCode);

    List<SysRole> findByTenantId(Long tenantId);

    Page<SysRole> findByTenantIdAndRoleNameContaining(Long tenantId, String roleName, Pageable pageable);

    Page<SysRole> findByTenantId(Long tenantId, Pageable pageable);
}
