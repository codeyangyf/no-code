package com.lc.system.security;

import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Profile({"h2", "memory"})
public class InMemoryRefreshTokenService implements RefreshTokenService {

    private final Map<Long, String> tokenStore = new ConcurrentHashMap<>();

    @Override
    public void saveRefreshToken(Long userId, String refreshToken) {
        tokenStore.put(userId, refreshToken);
    }

    @Override
    public String getRefreshToken(Long userId) {
        return tokenStore.get(userId);
    }

    @Override
    public void invalidateRefreshToken(Long userId) {
        tokenStore.remove(userId);
    }

    @Override
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
