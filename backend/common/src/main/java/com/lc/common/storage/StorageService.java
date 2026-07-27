package com.lc.common.storage;

import java.io.InputStream;

/**
 * 对象存储抽象接口，统一上传/下载/删除/预签名URL。
 * 通过 storage.type 配置切换本地实现或 MinIO 实现。
 */
public interface StorageService {

    /**
     * 上传文件
     *
     * @param bucket      存储桶
     * @param key         对象 key
     * @param input       输入流
     * @param contentType 内容类型
     * @param size        文件大小（字节）
     * @return 上传后的对象 key
     */
    String upload(String bucket, String key, InputStream input, String contentType, long size);

    /**
     * 下载文件
     *
     * @param bucket 存储桶
     * @param key    对象 key
     * @return 文件输入流
     */
    InputStream download(String bucket, String key);

    /**
     * 删除文件
     *
     * @param bucket 存储桶
     * @param key    对象 key
     */
    void delete(String bucket, String key);

    /**
     * 获取预签名 URL
     *
     * @param bucket        存储桶
     * @param key           对象 key
     * @param expireSeconds 过期时间（秒）
     * @return 预签名 URL
     */
    String getPresignedUrl(String bucket, String key, long expireSeconds);

    /**
     * 判断对象是否存在
     *
     * @param bucket 存储桶
     * @param key    对象 key
     * @return 是否存在
     */
    boolean exists(String bucket, String key);
}
