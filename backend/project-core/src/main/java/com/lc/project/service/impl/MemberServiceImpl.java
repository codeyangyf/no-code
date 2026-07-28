package com.lc.project.service.impl;

import com.lc.common.dto.PageResult;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.project.dto.MemberDTO;
import com.lc.project.service.MemberService;
import com.lc.system.entity.ProjectMember;
import com.lc.system.entity.SysUser;
import com.lc.system.repository.ProjectMemberRepository;
import com.lc.system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final UserService userService;

    @Override
    public PageResult<MemberDTO.MemberResponse> list(Long projectId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "joinedTime"));
        Page<ProjectMember> memberPage = projectMemberRepository.findByProjectId(projectId, pageable);

        return PageResult.of(
                memberPage.getContent().stream().map(this::toResponse).toList(),
                memberPage.getTotalElements(),
                page,
                size
        );
    }

    @Override
    public MemberDTO.MemberResponse getById(Long projectId, Long id) {
        ProjectMember member = projectMemberRepository.findByProjectIdAndId(projectId, id)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.NOT_FOUND));
        return toResponse(member);
    }

    @Override
    @Transactional
    public MemberDTO.MemberResponse invite(Long projectId, Long operatorId, MemberDTO.InviteRequest request) {
        if (projectMemberRepository.existsByProjectIdAndUserId(projectId, request.getUserId())) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "用户已加入该项目");
        }

        SysUser user = userService.findById(request.getUserId());
        if (user == null) {
            throw new BusinessException(GlobalErrorCode.NOT_FOUND.getCode(), "用户不存在");
        }

        validateRole(request.getRole());

        ProjectMember member = new ProjectMember();
        member.setProjectId(projectId);
        member.setUserId(request.getUserId());
        member.setRole(request.getRole());
        member.setStatus(1);

        ProjectMember saved = projectMemberRepository.save(member);
        log.info("Invited user {} to project {} with role {}", user.getUsername(), projectId, request.getRole());

        return toResponse(saved);
    }

    @Override
    @Transactional
    public MemberDTO.MemberResponse updateRole(Long projectId, Long id, MemberDTO.UpdateRoleRequest request) {
        ProjectMember member = projectMemberRepository.findByProjectIdAndId(projectId, id)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.NOT_FOUND));

        validateRole(request.getRole());
        member.setRole(request.getRole());

        ProjectMember saved = projectMemberRepository.save(member);
        log.info("Updated member {} role to {}", id, request.getRole());

        return toResponse(saved);
    }

    @Override
    @Transactional
    public void remove(Long projectId, Long id) {
        ProjectMember member = projectMemberRepository.findByProjectIdAndId(projectId, id)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.NOT_FOUND));
        projectMemberRepository.delete(member);
        log.info("Removed member {} from project {}", id, projectId);
    }

    @Override
    @Transactional
    public void updateStatus(Long projectId, Long id, Integer status) {
        ProjectMember member = projectMemberRepository.findByProjectIdAndId(projectId, id)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.NOT_FOUND));
        member.setStatus(status);
        projectMemberRepository.save(member);
        log.info("Updated member {} status to {}", id, status);
    }

    @Override
    public List<MemberDTO.MemberResponse> getMembersByProject(Long projectId) {
        List<ProjectMember> members = projectMemberRepository.findByProjectId(projectId);
        return members.stream().map(this::toResponse).toList();
    }

    @Override
    public boolean isMember(Long projectId, Long userId) {
        return projectMemberRepository.existsByProjectIdAndUserId(projectId, userId);
    }

    @Override
    public String getMemberRole(Long projectId, Long userId) {
        return projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .map(ProjectMember::getRole)
                .orElse(null);
    }

    private void validateRole(String role) {
        if (!List.of("VIEWER", "EDITOR", "ADMIN", "PUBLISHER").contains(role)) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "无效的角色");
        }
    }

    private MemberDTO.MemberResponse toResponse(ProjectMember member) {
        SysUser user = userService.findById(member.getUserId());
        return MemberDTO.MemberResponse.builder()
                .id(member.getId())
                .projectId(member.getProjectId())
                .userId(member.getUserId())
                .username(user != null ? user.getUsername() : null)
                .realName(user != null ? user.getRealName() : null)
                .email(user != null ? user.getEmail() : null)
                .role(member.getRole())
                .status(member.getStatus())
                .joinedTime(member.getJoinedTime())
                .build();
    }
}