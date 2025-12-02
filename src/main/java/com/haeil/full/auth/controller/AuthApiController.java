package com.haeil.full.auth.controller;

import com.haeil.full.auth.dto.request.LoginRequest;
import com.haeil.full.auth.dto.request.SignupRequest;
import com.haeil.full.auth.dto.response.LoginResponse;
import com.haeil.full.auth.service.AuthService;
import com.haeil.full.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/api")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Object>> apiSignup(
            @Valid @RequestBody SignupRequest request) {
        authService.signup(request.name(), request.email(), request.password(), request.role());
        LoginResponse loginResponse = authService.login(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(loginResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> apiLogin(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(loginResponse));
    }
}
