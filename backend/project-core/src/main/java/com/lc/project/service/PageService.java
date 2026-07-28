package com.lc.project.service;

import com.lc.common.dto.PageResult;
import com.lc.project.dto.page.PageDTO;

public interface PageService {

    PageResult<PageDTO.PageResponse> list(Long projectId, Integer page, Integer size);

    PageDTO.PageResponse getById(Long projectId, Long id);

    PageDTO.PageResponse getByCode(Long projectId, String pageCode);

    PageDTO.PageResponse create(Long projectId, Long userId, PageDTO.CreateRequest request);

    PageDTO.PageResponse update(Long projectId, Long id, PageDTO.UpdateRequest request);

    void delete(Long projectId, Long id);
}
