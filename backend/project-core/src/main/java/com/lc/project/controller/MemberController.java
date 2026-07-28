package com.lc.project.controller;

import com.lc.common.annotation.AuditLog;
import com.lc.common.annotation.PreAuthorize;
import com.lc.common.context.UserContext;
import com.lc.common.dto.PageResult;
import com.lc.common.dto.Result;
import com.lc.project.dto.MemberDTO;
import com.lc.project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/project/{projectId}")
    @PreAuthorize("project:view")
    public Result<PageResult<MemberDTO.MemberResponse>> list(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return Result.success(memberService.list(projectId, page, size));
    }

    @GetMapping("/project/{projectId}/{id}")
    @PreAuthorize("project:view")
    public Result<MemberDTO.MemberResponse> getById(@PathVariable Long projectId, @PathVariable Long id) {
        return Result.success(memberService.getById(projectId, id));
    }

    @GetMapping("/project/{projectId}/all")
    @PreAuthorize("project:view")
    public Result<List<MemberDTO.MemberResponse>> getAll(@PathVariable Long projectId) {
        return Result.success(memberService.getMembersByProject(projectId));
    }

    @PostMapping("/project/{projectId}/invite")
    @PreAuthorize("project:update")
    @AuditLog(action = "邀请项目成员", resourceType = "PROJECT_MEMBER")
    public Result<MemberDTO.MemberResponse> invite(@PathVariable Long projectId,
                                                   @RequestBody MemberDTO.InviteRequest request) {
        return Result.success(memberService.invite(projectId, UserContext.getUserId(), request));
    }

    @PutMapping("/project/{projectId}/{id}/role")
    @PreAuthorize("project:update")
    @AuditLog(action = "更新成员角色", resourceType = "PROJECT_MEMBER", resourceIdParam = "#id")
    public Result<MemberDTO.MemberResponse> updateRole(@PathVariable Long projectId, @PathVariable Long id,
                                                       @RequestBody MemberDTO.UpdateRoleRequest request) {
        return Result.success(memberService.updateRole(projectId, id, request));
    }

    @PutMapping("/project/{projectId}/{id}/status")
    @PreAuthorize("project:update")
    @AuditLog(action = "更新成员状态", resourceType = "PROJECT_MEMBER", resourceIdParam = "#id")
    public Result<Void> updateStatus(@PathVariable Long projectId, @PathVariable Long id,
                                     @RequestParam Integer status) {
        memberService.updateStatus(projectId, id, status);
        return Result.success();
    }

    @DeleteMapping("/project/{projectId}/{id}")
    @PreAuthorize("project:delete")
    @AuditLog(action = "移除项目成员", resourceType = "PROJECT_MEMBER", resourceIdParam = "#id")
    public Result<Void> remove(@PathVariable Long projectId, @PathVariable Long id) {
        memberService.remove(projectId, id);
        return Result.success();
    }
}