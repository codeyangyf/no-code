package com.lc.common.security;

import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.common.storage.StorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * 文件上传校验器，统一封装 MIME 白名单、大小上限与文件名安全校验。
 * <p>
 * 通用 {@link #validate(MultipartFile, Set)} 允许调用方传入自定义白名单；
 * {@link #validateImage(MultipartFile)} 与 {@link #validateDocument(MultipartFile)}
 * 分别使用预定义的图片/文档白名单；{@link #validateGeneral(MultipartFile)}
 * 使用图片与文档白名单的并集，适合通用上传场景。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadValidator {

    /** 图片 MIME 白名单 */
    public static final Set<String> IMAGE_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp", "image/svg+xml"
    );

    /** 文档 MIME 白名单 */
    public static final Set<String> DOC_TYPES = Set.of(
            "application/pdf",
            "text/plain",
            "application/json",
            "application/zip",
            "application/x-zip-compressed",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    );

    /** 通用白名单：图片与文档的并集 */
    private static final Set<String> GENERAL_TYPES = mergeSets(IMAGE_TYPES, DOC_TYPES);

    private final StorageProperties storageProperties;

    /**
     * 校验图片文件：仅允许 image/* 类型。
     */
    public void validateImage(MultipartFile file) {
        validate(file, IMAGE_TYPES);
    }

    /**
     * 校验文档文件：允许 pdf/txt/json/zip/doc/docx/xls/xlsx/ppt/pptx 等。
     */
    public void validateDocument(MultipartFile file) {
        validate(file, DOC_TYPES);
    }

    /**
     * 通用校验：使用图片与文档并集白名单。
     */
    public void validateGeneral(MultipartFile file) {
        validate(file, GENERAL_TYPES);
    }

    /**
     * 通用校验：白名单 + 大小 + 文件名安全（无路径遍历）。
     *
     * @param file         待校验文件
     * @param allowedTypes 允许的 Content-Type 集合
     */
    public void validate(MultipartFile file, Set<String> allowedTypes) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "文件不能为空");
        }
        if (file.getSize() > storageProperties.getMaxFileSize()) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "文件大小超过限制");
        }

        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType.toLowerCase())) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(),
                    "不支持的文件类型: " + contentType);
        }

        String originalName = file.getOriginalFilename();
        if (originalName != null
                && (originalName.contains("..") || originalName.contains("/") || originalName.contains("\\"))) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR.getCode(), "文件名非法");
        }
    }

    private static Set<String> mergeSets(Set<String> a, Set<String> b) {
        Set<String> merged = new java.util.HashSet<>(a.size() + b.size());
        merged.addAll(a);
        merged.addAll(b);
        return Set.copyOf(merged);
    }
}
