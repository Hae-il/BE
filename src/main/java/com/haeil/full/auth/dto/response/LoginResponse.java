package com.haeil.full.auth.dto.response;

import com.haeil.full.user.domain.type.Role;

public record LoginResponse(String name, Role role, String accessToken) {

    public static LoginResponse from(String name, Role role, String accessToken) {
        return new LoginResponse(name, role, accessToken);
    }
}
