package com.lc.bootstrap.config;

import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.common.storage.LocalStorageServiceImpl;
import com.lc.common.storage.StorageProperties;
import com.lc.common.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 对象存储配置。
 * 根据 storage.type 创建本地存储或 MinIO 存储实现。
 * MinIO 实现通过反射加载，避免在 type=local 时硬依赖 MinIO SDK。
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(StorageProperties.class)
@RequiredArgsConstructor
public class StorageConfig {

    private static final String MINIO_IMPL_CLASS = "com.lc.common.storage.MinioStorageServiceImpl";

    private final StorageProperties storageProperties;

    @Bean
    public StorageService storageService() {
        String type = storageProperties.getType();
        if ("minio".equalsIgnoreCase(type)) {
            return createMinioStorage();
        }
        return createLocalStorage();
    }

    private StorageService createLocalStorage() {
        try {
            Files.createDirectories(Paths.get(storageProperties.getLocalPath()));
            log.info("Local storage initialized at: {}", storageProperties.getLocalPath());
        } catch (IOException e) {
            log.error("Failed to create local storage dir: {}", storageProperties.getLocalPath(), e);
            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
        }
        return new LocalStorageServiceImpl(storageProperties);
    }

    private StorageService createMinioStorage() {
        try {
            Class<?> clazz = Class.forName(MINIO_IMPL_CLASS);
            Constructor<?> ctor = clazz.getConstructor(StorageProperties.class);
            StorageService service = (StorageService) ctor.newInstance(storageProperties);
            log.info("MinIO storage initialized at: {}", storageProperties.getEndpoint());
            return service;
        } catch (ClassNotFoundException e) {
            log.error("MinIO SDK not on classpath. Add io.minio:minio dependency to use storage.type=minio", e);
            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
        } catch (Exception e) {
            log.error("Failed to initialize MinIO storage", e);
            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
        }
    }
}
