package com.lc.system.controller;

import com.lc.common.context.UserContext;
import com.lc.common.dto.Result;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.common.storage.StorageProperties;
import com.lc.common.storage.StorageService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;

/**
 * 文件上传/下载/删除 REST 接口。
 * 按 tenant/{tenantId}/{yyyy/MM/dd}/{uuid}.{ext} 路径隔离租户文件。
 */
@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp", "image/svg+xml",
            "application/pdf", "text/plain", "application/json",
            "application/zip", "application/x-zip-compressed"
    );

    private final StorageService storageService;
    private final StorageProperties storageProperties;

    @PostMapping("/upload")
    public Result<FileUploadResponse> upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR);
        }
        if (file.getSize() > storageProperties.getMaxFileSize()) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR);
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(),
                    "不支持的文件类型: " + contentType);
        }

        Long tenantId = UserContext.getTenantId();
        if (tenantId == null) {
            throw new BusinessException(GlobalErrorCode.UNAUTHORIZED);
        }

        String originalName = file.getOriginalFilename();
        String ext = extractExtension(originalName);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = ext.isEmpty() ? uuid : uuid + "." + ext;
        String datePath = LocalDate.now().format(DATE_FMT);
        String key = String.format("tenant/%d/%s/%s", tenantId, datePath, fileName);

        String bucket = storageProperties.getDefaultBucket();
        try (InputStream input = file.getInputStream()) {
            storageService.upload(bucket, key, input, file.getContentType(), file.getSize());
        } catch (IOException e) {
            log.error("File upload read failed", e);
            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
        }

        String url = storageService.getPresignedUrl(bucket, key, 3600);
        FileUploadResponse resp = FileUploadResponse.builder()
                .key(key)
                .fileName(originalName)
                .size(file.getSize())
                .url(url)
                .build();
        return Result.success(resp);
    }

    @GetMapping("/{bucket}/{key:.+}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String bucket,
                                                          @PathVariable String key) {
        validateTenantAccess(key);
        InputStream input = storageService.download(bucket, key);
        String fileName = key.contains("/") ? key.substring(key.lastIndexOf('/') + 1) : key;
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(input));
    }

    @DeleteMapping("/{bucket}/{key:.+}")
    public Result<Void> delete(@PathVariable String bucket, @PathVariable String key) {
        validateTenantAccess(key);
        storageService.delete(bucket, key);
        return Result.success();
    }

    private String extractExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int idx = fileName.lastIndexOf('.');
        if (idx < 0 || idx == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(idx + 1);
    }

    private void validateTenantAccess(String key) {
        Long currentTenantId = UserContext.getTenantId();
        if (currentTenantId != null) {
            // key 格式: tenant/{tenantId}/...
            if (!key.startsWith("tenant/" + currentTenantId + "/")) {
                throw new BusinessException(GlobalErrorCode.PERMISSION_DENIED.getCode(),
                        "无权访问该文件");
            }
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileUploadResponse {
        private String key;
        private String fileName;
        private long size;
        private String url;
    }
}
