package io.github.ddsl.overseermono.auth.filter;

import io.github.ddsl.overseermono.auth.properties.JwtProperties;
import io.github.ddsl.overseermono.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTCookieFilter extends OncePerRequestFilter {
    private final JwtProperties jwtProperties;
    private final JwtService jwtService;
    private final RequestMatcher authEndpointsMatcher;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        if (authEndpointsMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessCookieName = jwtProperties.accessToken().cookie();
        var jwt = extractJwt(request);
        if (jwt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        var jwtString = jwt.get();
        if (!jwtService.validateToken(jwtString)) {
            filterChain.doFilter(request, response);
            return;
        }
        // проверяем только access token!!! refresh будем проверять в отдельном ендпоинте
        //extract claims & fill authentication
        var claims = jwtService.extractClaims(jwtString);
        var username = claims.getSubject();
        var roles = jwtService.getRolesFromClaims(claims);
        var authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        // Fast implementation with principal as a String username but
        // could be swapped with AuthService.generateAuthenticationByUsername(...)
        // to load userDetails as principal from db
        var authentication = new UsernamePasswordAuthenticationToken(
            username,
            null,
            authorities
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return super.shouldNotFilter(request);
    }

    private Optional<String> extractJwt(HttpServletRequest req) {
        String accessCookieName = jwtProperties.accessToken().cookie();
        var cookies = req.getCookies();
        if (cookies == null) return  Optional.empty();
        var jwt = Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals(accessCookieName))
                .map(Cookie::getValue)
                .findFirst();
        return  jwt;
    }
}
