package com.lc.common.storage;

import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * MinIO 对象存储实现。
 * 通过 MinIO Java SDK 操作对象存储。
 */
@Slf4j
public class MinioStorageServiceImpl implements StorageService {

    private final MinioClient minioClient;

    public MinioStorageServiceImpl(StorageProperties properties) {
        this.minioClient = MinioClient.builder()
                .endpoint(properties.getEndpoint())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();
    }

    @Override
    public String upload(String bucket, String key, InputStream input, String contentType, long size) {
        try {
            ensureBucket(bucket);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .stream(input, size, -1)
                    .contentType(contentType)
                    .build());
            log.debug("MinIO uploaded: {}/{}", bucket, key);
            return key;
        } catch (Exception e) {
            log.error("MinIO upload failed: {}/{}", bucket, key, e);
            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public InputStream download(String bucket, String key) {
        try {
            GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .build());
            return response;
        } catch (ErrorResponseException e) {
            log.warn("MinIO object not found: {}/{}", bucket, key);
            throw new BusinessException(GlobalErrorCode.NOT_FOUND);
        } catch (Exception e) {
            log.error("MinIO download failed: {}/{}", bucket, key, e);
            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public void delete(String bucket, String key) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .build());
            log.debug("MinIO deleted: {}/{}", bucket, key);
        } catch (Exception e) {
            log.error("MinIO delete failed: {}/{}", bucket, key, e);
            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public String getPresignedUrl(String bucket, String key, long expireSeconds) {
        try {
            return minioClient.getPresignedObjectUrl(
                    io.minio.GetPresignedObjectUrlArgs.builder()
                            .method(io.minio.http.Method.GET)
                            .bucket(bucket)
                            .object(key)
                            .expiry((int) expireSeconds)
                            .build());
        } catch (Exception e) {
            log.error("MinIO getPresignedUrl failed: {}/{}", bucket, key, e);
            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public boolean exists(String bucket, String key) {
        try {
            StatObjectResponse response = minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .build());
            return response != null;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            log.error("MinIO exists check failed: {}/{}", bucket, key, e);
            return false;
        }
    }

    private void ensureBucket(String bucket) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            log.info("MinIO bucket created: {}", bucket);
        }
    }
}
