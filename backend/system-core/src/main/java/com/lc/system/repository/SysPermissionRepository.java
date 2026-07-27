package com.lc.system.repository;

import com.lc.system.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysPermissionRepository extends JpaRepository<SysPermission, Long> {
    List<SysPermission> findByTenantId(Long tenantId);
    Optional<SysPermission> findByPermCode(String permCode);
}
