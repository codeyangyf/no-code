package com.lc.system.repository;

import com.lc.system.entity.SysRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRolePermissionRepository extends JpaRepository<SysRolePermission, Long> {
    List<SysRolePermission> findByRoleId(Long roleId);
    void deleteByRoleId(Long roleId);

    @Query("SELECT rp.permissionId FROM SysRolePermission rp WHERE rp.roleId = :roleId")
    List<Long> findPermissionIdsByRoleId(@Param("roleId") Long roleId);
}
