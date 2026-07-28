package com.lc.project.service;

import com.lc.common.dto.PageResult;
import com.lc.project.dto.form.FormDTO;

public interface FormService {

    PageResult<FormDTO.FormResponse> list(Long projectId, Integer page, Integer size);

    FormDTO.FormResponse getById(Long projectId, Long id);

    FormDTO.FormResponse getByCode(Long projectId, String formCode);

    FormDTO.FormResponse create(Long projectId, Long userId, FormDTO.CreateRequest request);

    FormDTO.FormResponse update(Long projectId, Long id, FormDTO.UpdateRequest request);

    void delete(Long projectId, Long id);
}
