package com.haeil.be.auth.controller;

import com.haeil.be.auth.dto.request.LoginRequest;
import com.haeil.be.auth.dto.request.SignupRequest;
import com.haeil.be.auth.dto.response.LoginResponse;
import com.haeil.be.auth.service.AuthService;
import com.haeil.be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 관련 API")
@RequestMapping("/api/v1/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "기본 회원가입",
            description =
                    "이메일과 비밀번호로 회원가입을 합니다. '@'을 포함한 이메일과 비밀번호를 입력해주세요. "
                            + "임의로 개발한 API로, Role을 선택하여 회원가입을 진행합니다. ROLE에는 `ROLE_ATTORNEY`, `ROLE_COUNSEL`, `ROLE_ACCOUNT`, `ROLE_ADMIN`, `ROLE_SECRETARY`가 있습니다. 하나를 작성해주세요.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Object>> signUp(@Valid @RequestBody SignupRequest request) {
        authService.signup(request.name(), request.email(), request.password(), request.role());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }

    @Operation(summary = "기본 로그인", description = "이메일과 비밀번호를 통해 로그인을 진행합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(loginResponse));
    }
}
