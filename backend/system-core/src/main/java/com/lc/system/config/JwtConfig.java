package com.lc.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secret = "your-256-bit-secret-key-here-must-be-at-least-32-characters";
    private int accessTokenExpireMinutes = 15;
    private int refreshTokenExpireDays = 7;
}