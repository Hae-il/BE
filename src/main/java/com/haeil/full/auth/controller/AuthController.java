package com.haeil.full.auth.controller;

import com.haeil.full.auth.dto.request.LoginRequest;
import com.haeil.full.auth.dto.request.SignupRequest;
import com.haeil.full.auth.dto.response.LoginResponse;
import com.haeil.full.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth")
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupRequest", new SignupRequest("", "", "", null));
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signUp(@Valid @ModelAttribute SignupRequest request, Model model) {
        try {
            authService.signup(request.name(), request.email(), request.password(), request.role());
            return "redirect:/auth/login?success";
        } catch (Exception e) {
            model.addAttribute("error", "회원가입 중 오류가 발생했습니다: " + e.getMessage());
            return "auth/signup";
        }
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest("", ""));
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequest request, 
                       Model model, HttpServletResponse response) {
        try {
            LoginResponse loginResponse = authService.login(request.email(), request.password());
            
            // JWT 토큰을 쿠키에 설정 (Bearer 프리픽스 제외)
            Cookie cookie = new Cookie("Authorization", loginResponse.accessToken());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7일
            response.addCookie(cookie);
            
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "로그인 중 오류가 발생했습니다: " + e.getMessage());
            return "auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // JWT 쿠키 삭제
        Cookie cookie = new Cookie("Authorization", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
        
        return "redirect:/auth/login?logout";
    }
}
