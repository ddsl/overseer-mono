package io.github.ddsl.overseermono.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDto(
        String firstname,
        String lastname,
        @NotBlank
        String login,
        @Email
        String email,
        @NotBlank
        String password
) {}
