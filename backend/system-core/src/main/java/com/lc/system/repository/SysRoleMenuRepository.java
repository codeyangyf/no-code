package com.lc.system.repository;

import com.lc.system.entity.SysRoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleMenuRepository extends JpaRepository<SysRoleMenu, Long> {
    List<SysRoleMenu> findByRoleId(Long roleId);
    List<SysRoleMenu> findByMenuId(Long menuId);
    void deleteByRoleId(Long roleId);

    @Query("SELECT rm.menuId FROM SysRoleMenu rm WHERE rm.roleId = :roleId")
    List<Long> findMenuIdsByRoleId(@Param("roleId") Long roleId);
}
