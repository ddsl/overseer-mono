package io.github.ddsl.overseermono.auth.service;

import io.github.ddsl.overseermono.auth.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JwtService {
    private final SecretKey jwtSecretKey;
    private final JwtProperties jwtProperties;

    public String generateAccessToken(Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        String username  = userDetails.getUsername();

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Date now = new Date();
        var accessExpiresIn = jwtProperties.accessToken().expiresIn();
        Date expiryDate = new Date(now.getTime() + accessExpiresIn*1000);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .claim("roles", roles)
                .id(UUID.randomUUID().toString())
                .signWith(jwtSecretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        String username  = userDetails.getUsername();

        List<String> roles = Collections.emptyList(); //refresh can only refresh token

        Date now = new Date();
        var refreshExpiresIn = jwtProperties.refreshToken().expiresIn();
        Date expiryDate = new Date(now.getTime() + refreshExpiresIn*1000);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .claim("roles", roles)
                .id(UUID.randomUUID().toString())
                .signWith(jwtSecretKey, Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(jwtSecretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(jwtSecretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            } catch (JwtException | IllegalArgumentException e) {
                return null;
            }
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromClaims(Claims claims) {
        Object rolesClaim = claims.get("roles");
        if (rolesClaim instanceof List) {
            var roles = (List<String>) rolesClaim;
            return roles;
        }
        return List.of();
    }

    public ResponseCookie generateAccessCookie(Authentication authentication) {
        String accessCookieName = jwtProperties.accessToken().cookie();
        Long accessExpiresIn = jwtProperties.accessToken().expiresIn();
        var accessJwtToken = generateAccessToken(authentication);
        return makeStrictCookie(
                accessCookieName,
                accessJwtToken,
                accessExpiresIn);
    }

    public ResponseCookie generateRefreshCookie(Authentication authentication) {
        String refreshCookieName = jwtProperties.refreshToken().cookie();
        Long refreshExpiresIn = jwtProperties.refreshToken().expiresIn();
        var refreshJwtToken = generateRefreshToken(authentication);
        return makeStrictCookie(
                refreshCookieName,
                refreshJwtToken,
                refreshExpiresIn);
    }

    private ResponseCookie makeStrictCookie(String cookieName, String cookieValue, Long expiresIn) {
        return ResponseCookie.from(cookieName, cookieValue)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(expiresIn)
                .build();
    }
}
