package com.lc.system.repository;

import com.lc.system.entity.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {
    List<SysUserRole> findByUserId(Long userId);
    void deleteByUserId(Long userId);

    List<SysUserRole> findByRoleId(Long roleId);
    void deleteByRoleId(Long roleId);

    @Query("SELECT ur.roleId FROM SysUserRole ur WHERE ur.userId = :userId")
    List<Long> findRoleIdsByUserId(@Param("userId") Long userId);
}
