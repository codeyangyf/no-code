package com.lc.system.security;

import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import com.lc.system.config.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtConfig jwtConfig;

    private static final String REFRESH_TOKEN_KEY = "refresh:%s";

    public void saveRefreshToken(Long userId, String refreshToken) {
        String key = String.format(REFRESH_TOKEN_KEY, userId);
        redisTemplate.opsForValue().set(key, refreshToken, jwtConfig.getRefreshTokenExpireDays(), TimeUnit.DAYS);
    }

    public String getRefreshToken(Long userId) {
        String key = String.format(REFRESH_TOKEN_KEY, userId);
        Object token = redisTemplate.opsForValue().get(key);
        return token != null ? token.toString() : null;
    }

    public void invalidateRefreshToken(Long userId) {
        String key = String.format(REFRESH_TOKEN_KEY, userId);
        redisTemplate.delete(key);
    }

    public boolean validateRefreshToken(Long userId, String token) {
        String storedToken = getRefreshToken(userId);
        if (storedToken == null) {
            throw new BusinessException(GlobalErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        if (!storedToken.equals(token)) {
            throw new BusinessException(GlobalErrorCode.REFRESH_TOKEN_INVALID);
        }
        return true;
    }
}
