package com.lc.system.security;

public interface RefreshTokenService {

    void saveRefreshToken(Long userId, String refreshToken);

    String getRefreshToken(Long userId);

    void invalidateRefreshToken(Long userId);

    boolean validateRefreshToken(Long userId, String token);
}
