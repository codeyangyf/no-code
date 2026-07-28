package com.lc.project.service.impl;

import com.lc.common.dto.PageResult;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.project.dto.ProjectDTO;
import com.lc.project.repository.ProjectRepository;
import com.lc.project.service.DatabaseService;
import com.lc.project.service.ProjectService;
import com.lc.system.entity.ProjectInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final DatabaseService databaseService;

    private static final List<String> VALID_STATUS = Arrays.asList(
            "INITIALIZING", "READY", "FAILED", "ARCHIVED", "PENDING_DELETE"
    );

    @Override
    @Transactional(readOnly = true)
    public PageResult<ProjectDTO.ProjectResponse> list(Long tenantId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<ProjectInfo> projectPage = projectRepository.findByTenantId(tenantId, pageable);
        return PageResult.of(
                projectPage.getContent().stream().map(this::toResponse).toList(),
                projectPage.getTotalElements(),
                page,
                size
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO.ProjectResponse getById(Long tenantId, Long id) {
        ProjectInfo project = projectRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.NOT_FOUND));
        return toResponse(project);
    }

    @Override
    @Transactional
    public ProjectDTO.ProjectResponse create(Long tenantId, Long userId, ProjectDTO.CreateRequest request) {
        if (projectRepository.existsByTenantIdAndProjectCode(tenantId, request.getProjectCode())) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "项目编码已存在");
        }

        ProjectInfo project = new ProjectInfo();
        project.setTenantId(tenantId);
        project.setProjectCode(request.getProjectCode());
        project.setProjectName(request.getProjectName());
        project.setDescription(request.getDescription());
        project.setIcon(request.getIcon());
        project.setStatus(1);
        project.setLifecycleStatus("INITIALIZING");
        project.setCreatedBy(userId);

        ProjectInfo savedProject = projectRepository.save(project);

        try {
            databaseService.createProjectDatabase(savedProject.getId());
            databaseService.createSandboxDatabase(savedProject.getId());
            databaseService.executeBaselineMigration(savedProject.getId());
            databaseService.executeSandboxBaselineMigration(savedProject.getId());

            savedProject.setLifecycleStatus("READY");
            projectRepository.save(savedProject);
            log.info("Project database created successfully: project_{}", savedProject.getId());
        } catch (Exception e) {
            log.error("Failed to create project database", e);
            savedProject.setLifecycleStatus("FAILED");
            projectRepository.save(savedProject);
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "项目数据库创建失败: " + e.getMessage());
        }

        return toResponse(savedProject);
    }

    @Override
    @Transactional
    public ProjectDTO.ProjectResponse update(Long tenantId, Long id, ProjectDTO.UpdateRequest request) {
        ProjectInfo project = projectRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.NOT_FOUND));

        if (request.getProjectName() != null) {
            project.setProjectName(request.getProjectName());
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        if (request.getIcon() != null) {
            project.setIcon(request.getIcon());
        }
        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }

        return toResponse(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void delete(Long tenantId, Long id) {
        ProjectInfo project = projectRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.NOT_FOUND));

        try {
            databaseService.deleteProjectDatabase(project.getId());
            databaseService.deleteSandboxDatabase(project.getId());
            log.info("Project database deleted successfully: project_{}", project.getId());
        } catch (Exception e) {
            log.error("Failed to delete project database", e);
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "项目数据库删除失败: " + e.getMessage());
        }

        project.setStatus(0);
        project.setLifecycleStatus("PENDING_DELETE");
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public ProjectDTO.ProjectResponse updateStatus(Long tenantId, Long id, ProjectDTO.UpdateStatusRequest request) {
        ProjectInfo project = projectRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.NOT_FOUND));

        String newStatus = request.getLifecycleStatus();
        if (!VALID_STATUS.contains(newStatus)) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "无效的生命周期状态");
        }

        validateStatusTransition(project.getLifecycleStatus(), newStatus);

        project.setLifecycleStatus(newStatus);
        return toResponse(projectRepository.save(project));
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        if ("INITIALIZING".equals(currentStatus)) {
            if (!Arrays.asList("READY", "FAILED").contains(newStatus)) {
                throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(),
                        "初始化中的项目只能转换为就绪或失败状态");
            }
        } else if ("READY".equals(currentStatus)) {
            if (!Arrays.asList("ARCHIVED", "PENDING_DELETE").contains(newStatus)) {
                throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(),
                        "就绪的项目只能转换为归档或待删除状态");
            }
        } else if ("ARCHIVED".equals(currentStatus)) {
            if (!"READY".equals(newStatus)) {
                throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(),
                        "已归档的项目只能恢复为就绪状态");
            }
        } else if ("FAILED".equals(currentStatus)) {
            if (!"INITIALIZING".equals(newStatus)) {
                throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(),
                        "创建失败的项目只能重新初始化");
            }
        } else if ("PENDING_DELETE".equals(currentStatus)) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(),
                    "待删除的项目不允许转换其他状态");
        }
    }

    private ProjectDTO.ProjectResponse toResponse(ProjectInfo project) {
        return ProjectDTO.ProjectResponse.builder()
                .id(project.getId())
                .tenantId(project.getTenantId())
                .projectCode(project.getProjectCode())
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .icon(project.getIcon())
                .status(project.getStatus())
                .lifecycleStatus(project.getLifecycleStatus())
                .createdBy(project.getCreatedBy())
                .createdTime(project.getCreatedTime())
                .updatedTime(project.getUpdatedTime())
                .build();
    }
}
