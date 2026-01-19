package io.github.ddsl.overseermono.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginUserDto(
        @NotBlank
       String login,
       @NotBlank
       String password
) {}
