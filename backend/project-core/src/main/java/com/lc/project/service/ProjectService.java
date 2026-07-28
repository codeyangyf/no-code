package com.lc.project.service;

import com.lc.common.dto.PageResult;
import com.lc.project.dto.ProjectDTO;

public interface ProjectService {

    PageResult<ProjectDTO.ProjectResponse> list(Long tenantId, Integer page, Integer size);

    ProjectDTO.ProjectResponse getById(Long tenantId, Long id);

    ProjectDTO.ProjectResponse create(Long tenantId, Long userId, ProjectDTO.CreateRequest request);

    ProjectDTO.ProjectResponse update(Long tenantId, Long id, ProjectDTO.UpdateRequest request);

    void delete(Long tenantId, Long id);

    ProjectDTO.ProjectResponse updateStatus(Long tenantId, Long id, ProjectDTO.UpdateStatusRequest request);
}
