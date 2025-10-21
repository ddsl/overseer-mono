package io.github.ddsl.overseermono.commonlib.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    USER,
    MODERATOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
