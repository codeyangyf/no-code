package com.lc.project.repository;

import com.lc.system.entity.ProjectInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectInfo, Long> {

    Page<ProjectInfo> findByTenantId(Long tenantId, Pageable pageable);

    Optional<ProjectInfo> findByTenantIdAndId(Long tenantId, Long id);

    Optional<ProjectInfo> findByTenantIdAndProjectCode(Long tenantId, String projectCode);

    boolean existsByTenantIdAndProjectCode(Long tenantId, String projectCode);

    @Modifying
    @Query("UPDATE ProjectInfo p SET p.lifecycleStatus = :status WHERE p.id = :id")
    void updateLifecycleStatus(@Param("id") Long id, @Param("status") String status);
}
