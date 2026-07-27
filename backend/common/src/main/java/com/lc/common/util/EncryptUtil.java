package com.lc.common.util;

import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES 加解密工具。
 * 算法：AES/ECB/PKCS5Padding
 * 密钥长度必须为 16/24/32 字节。
 */
@Slf4j
public class EncryptUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    private EncryptUtil() {
    }

    /**
     * 加密
     *
     * @param plainText 明文
     * @param key       密钥（16/24/32 字节）
     * @return Base64 编码的密文
     */
    public static String encrypt(String plainText, String key) {
        validateKey(key);
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("AES encrypt failed", e);
            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
        }
    }

    /**
     * 解密
     *
     * @param cipherText Base64 编码的密文
     * @param key        密钥（16/24/32 字节）
     * @return 明文
     */
    public static String decrypt(String cipherText, String key) {
        validateKey(key);
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("AES decrypt failed", e);
            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
        }
    }

    private static void validateKey(String key) {
        if (key == null) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR);
        }
        int len = key.getBytes(StandardCharsets.UTF_8).length;
        if (len != 16 && len != 24 && len != 32) {
            throw new BusinessException(GlobalErrorCode.VALIDATION_ERROR);
        }
    }
}
