## Task 6: 对象存储与密钥管理 ⬜ 待实施

> 设计文档 Task 6：快照/产物/附件存储，密钥加密与脱敏

**目标：** 抽象对象存储接口，支持本地文件系统和MinIO切换，提供AES加密工具和文件上传接口。

**Files:**
- Create: `backend/common/src/main/java/com/lc/common/storage/StorageService.java`
- Create: `backend/common/src/main/java/com/lc/common/storage/StorageProperties.java`
- Create: `backend/common/src/main/java/com/lc/common/storage/LocalStorageServiceImpl.java`
- Create: `backend/common/src/main/java/com/lc/common/storage/MinioStorageServiceImpl.java`
- Create: `backend/common/src/main/java/com/lc/common/util/EncryptUtil.java`
- Create: `backend/bootstrap/src/main/java/com/lc/bootstrap/config/StorageConfig.java`
- Create: `backend/system-core/src/main/java/com/lc/system/controller/FileController.java`
- Modify: `backend/bootstrap/src/main/resources/application.yml`（添加 storage 配置）
- Modify: `backend/common/pom.xml`（添加 MinIO SDK 依赖，optional）

**Interfaces:**
- Consumes: `StorageProperties`（配置）, `UserContext`（租户目录隔离）
- Produces: `StorageService`（上传/下载/删除/预签名URL）, `FileController`（文件上传REST接口）, `EncryptUtil`（AES加解密）

**关键设计：**
1. 对象存储抽象：`StorageService` 接口统一上传/下载/删除/预签名URL，通过 `storage.type` 配置切换实现
2. 密钥管理：`EncryptUtil` 提供 AES/ECB/PKCS5Padding 加解密，密钥通过环境变量 `LC_ENCRYPT_KEY` 注入
3. 文件上传：类型校验（MIME白名单）、大小限制（默认10MB）、随机文件名、按 `tenant/{tenantId}/` 目录隔离

**StorageService.java：**
```java
public interface StorageService {
    String upload(String bucket, String key, InputStream input, String contentType, long size);
    InputStream download(String bucket, String key);
    void delete(String bucket, String key);
    String getPresignedUrl(String bucket, String key, long expireSeconds);
    boolean exists(String bucket, String key);
}
```

**application.yml 新增配置：**
```yaml
storage:
  type: local          # local | minio
  local-path: ./storage
  endpoint: ${MINIO_ENDPOINT:}
  access-key: ${MINIO_ACCESS_KEY:}
  secret-key: ${MINIO_SECRET_KEY:}
  default-bucket: lc-platform
  max-file-size: 10485760

encrypt:
  key: ${LC_ENCRYPT_KEY:lc-platform-2026-secure-key-32b}
```

- [ ] **Task 6-1: 创建 StorageService 接口 + StorageProperties + LocalStorageServiceImpl**
- [ ] **Task 6-2: 创建 MinioStorageServiceImpl + StorageConfig + 添加MinIO依赖(optional)**
- [ ] **Task 6-3: 创建 EncryptUtil 加密工具**
- [ ] **Task 6-4: 创建 FileController 文件上传接口 + 更新 application.yml**
- [ ] **Task 6-5: 编译验证 + Commit**

---

### Task 6 验收
- [ ] 文件上传接口可用，文件保存到 ./storage/{bucket}/{key}
- [ ] 可通过 storage.type 配置切换本地存储和MinIO存储
- [ ] AES 加解密工具正常工作

