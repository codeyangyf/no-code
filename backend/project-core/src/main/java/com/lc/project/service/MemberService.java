package com.lc.project.service;

import com.lc.common.dto.PageResult;
import com.lc.project.dto.MemberDTO;

import java.util.List;

public interface MemberService {

    PageResult<MemberDTO.MemberResponse> list(Long projectId, Integer page, Integer size);

    MemberDTO.MemberResponse getById(Long projectId, Long id);

    MemberDTO.MemberResponse invite(Long projectId, Long operatorId, MemberDTO.InviteRequest request);

    MemberDTO.MemberResponse updateRole(Long projectId, Long id, MemberDTO.UpdateRoleRequest request);

    void remove(Long projectId, Long id);

    void updateStatus(Long projectId, Long id, Integer status);

    List<MemberDTO.MemberResponse> getMembersByProject(Long projectId);

    boolean isMember(Long projectId, Long userId);

    String getMemberRole(Long projectId, Long userId);
}