package com.lc.system.repository;

import com.lc.system.entity.SysTenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysTenantRepository extends JpaRepository<SysTenant, Long> {
    Optional<SysTenant> findByTenantCode(String tenantCode);
    boolean existsByTenantCode(String tenantCode);

    Page<SysTenant> findByTenantNameContainingOrTenantCodeContaining(String tenantName, String tenantCode, Pageable pageable);
}
