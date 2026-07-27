package com.lc.common.storage;

import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 本地文件系统存储实现。
 * 文件保存在 {localPath}/{bucket}/{key} 路径下。
 */
@Slf4j
public class LocalStorageServiceImpl implements StorageService {

    private final StorageProperties properties;

    public LocalStorageServiceImpl(StorageProperties properties) {
        this.properties = properties;
    }

    @Override
    public String upload(String bucket, String key, InputStream input, String contentType, long size) {
        try {
            Path filePath = resolvePath(bucket, key);
            Files.createDirectories(filePath.getParent());
            FileCopyUtils.copy(input, Files.newOutputStream(filePath));
            log.debug("Local storage uploaded: {}/{}", bucket, key);
            return key;
        } catch (IOException e) {
            log.error("Local storage upload failed: {}/{}", bucket, key, e);
            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public InputStream download(String bucket, String key) {
        try {
            Path filePath = resolvePath(bucket, key);
            if (!Files.exists(filePath)) {
                throw new BusinessException(GlobalErrorCode.NOT_FOUND);
            }
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            log.error("Local storage download failed: {}/{}", bucket, key, e);
            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public void delete(String bucket, String key) {
        try {
            Path filePath = resolvePath(bucket, key);
            Files.deleteIfExists(filePath);
            log.debug("Local storage deleted: {}/{}", bucket, key);
        } catch (IOException e) {
            log.error("Local storage delete failed: {}/{}", bucket, key, e);
            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public String getPresignedUrl(String bucket, String key, long expireSeconds) {
        return "/api/files/" + bucket + "/" + key;
    }

    @Override
    public boolean exists(String bucket, String key) {
        return Files.exists(resolvePath(bucket, key));
    }

    private Path resolvePath(String bucket, String key) {
        return Paths.get(properties.getLocalPath(), bucket, key);
    }
}
