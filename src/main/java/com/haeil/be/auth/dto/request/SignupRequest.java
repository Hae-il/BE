package com.haeil.be.auth.dto.request;

import com.haeil.be.user.domain.type.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignupRequest(
        @NotNull String name, @Email String email, @NotNull String password, @NotNull Role role) {}
