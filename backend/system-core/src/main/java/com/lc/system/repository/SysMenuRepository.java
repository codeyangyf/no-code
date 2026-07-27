package com.lc.system.repository;

import com.lc.system.entity.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuRepository extends JpaRepository<SysMenu, Long> {
    List<SysMenu> findByTenantId(Long tenantId);
    List<SysMenu> findByTenantIdAndStatus(Long tenantId, Integer status);
    List<SysMenu> findByParentIdAndTenantId(Long parentId, Long tenantId);
}
