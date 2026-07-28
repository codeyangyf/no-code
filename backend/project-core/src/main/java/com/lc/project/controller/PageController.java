package com.lc.project.controller;

import com.lc.common.annotation.AuditLog;
import com.lc.common.annotation.PreAuthorize;
import com.lc.common.context.UserContext;
import com.lc.common.dto.PageResult;
import com.lc.common.dto.Result;
import com.lc.project.dto.page.PageDTO;
import com.lc.project.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pages")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @GetMapping("/project/{projectId}")
    @PreAuthorize("project:view")
    public Result<PageResult<PageDTO.PageResponse>> list(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return Result.success(pageService.list(projectId, page, size));
    }

    @GetMapping("/project/{projectId}/{id}")
    @PreAuthorize("project:view")
    public Result<PageDTO.PageResponse> getById(@PathVariable Long projectId, @PathVariable Long id) {
        return Result.success(pageService.getById(projectId, id));
    }

    @GetMapping("/project/{projectId}/code/{pageCode}")
    @PreAuthorize("project:view")
    public Result<PageDTO.PageResponse> getByCode(@PathVariable Long projectId, @PathVariable String pageCode) {
        return Result.success(pageService.getByCode(projectId, pageCode));
    }

    @PostMapping("/project/{projectId}")
    @PreAuthorize("project:update")
    @AuditLog(action = "创建页面", resourceType = "PAGE")
    public Result<PageDTO.PageResponse> create(@PathVariable Long projectId,
                                               @RequestBody PageDTO.CreateRequest request) {
        return Result.success(pageService.create(projectId, UserContext.getUserId(), request));
    }

    @PutMapping("/project/{projectId}/{id}")
    @PreAuthorize("project:update")
    @AuditLog(action = "更新页面", resourceType = "PAGE", resourceIdParam = "#id")
    public Result<PageDTO.PageResponse> update(@PathVariable Long projectId, @PathVariable Long id,
                                               @RequestBody PageDTO.UpdateRequest request) {
        return Result.success(pageService.update(projectId, id, request));
    }

    @DeleteMapping("/project/{projectId}/{id}")
    @PreAuthorize("project:delete")
    @AuditLog(action = "删除页面", resourceType = "PAGE", resourceIdParam = "#id")
    public Result<Void> delete(@PathVariable Long projectId, @PathVariable Long id) {
        pageService.delete(projectId, id);
        return Result.success();
    }
}
