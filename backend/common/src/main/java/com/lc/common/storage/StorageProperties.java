package com.lc.common.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 对象存储配置属性。
 * 通过 storage.type 配置切换本地实现或 MinIO 实现。
 */
@Data
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    /**
     * 存储类型：local | minio
     */
    private String type = "local";

    /**
     * 本地存储根路径
     */
    private String localPath = "./storage";

    /**
     * MinIO 服务端点
     */
    private String endpoint;

    /**
     * MinIO 访问密钥
     */
    private String accessKey;

    /**
     * MinIO 秘密密钥
     */
    private String secretKey;

    /**
     * 默认存储桶
     */
    private String defaultBucket = "lc-platform";

    /**
     * 文件大小上限（字节），默认 10MB
     */
    private long maxFileSize = 10485760L;
}
