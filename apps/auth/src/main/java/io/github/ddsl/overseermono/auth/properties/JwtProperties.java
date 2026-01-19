package io.github.ddsl.overseermono.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        AccessTokenProperties accessToken,
        RefreshTokenProperties refreshToken
) {
    public record AccessTokenProperties(
            String cookie,
            Long expiresIn
    ) {}
    public record RefreshTokenProperties(
            String cookie,
            Long expiresIn
    ) {}
}
