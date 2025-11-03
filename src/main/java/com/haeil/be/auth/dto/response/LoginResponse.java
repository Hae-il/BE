package com.haeil.be.auth.dto.response;

import com.haeil.be.user.domain.type.Role;

public record LoginResponse(String name, Role role, String accessToken) {

    public static LoginResponse from(String name, Role role, String accessToken) {
        return new LoginResponse(name, role, accessToken);
    }
}
