package com.haeil.full.global.controller;

import com.haeil.full.user.domain.User;
import com.haeil.full.user.service.UserService;
import com.haeil.full.user.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // 인증되지 않았거나 익명 사용자인 경우 로그인 페이지로 리다이렉트
        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/auth/login";
        }
        
        // 인증된 사용자 정보 가져오기
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userService.getUser(userDetails.getId());
            
            model.addAttribute("user", user);
            model.addAttribute("username", user.getName());
            model.addAttribute("userRole", getRoleDisplayName(user.getRole()));
        } catch (Exception e) {
            return "redirect:/auth/login?error=user_load_failed";
        }
        
        return "pages/index";
    }
    
    private String getRoleDisplayName(com.haeil.full.user.domain.type.Role role) {
        switch (role) {
            case ROLE_ADMIN: return "관리자";
            case ROLE_ATTORNEY: return "변호사";
            case ROLE_SECRETARY: return "사무관";
            case ROLE_COUNSEL: return "상담사";
            case ROLE_ACCOUNT: return "회계";
            default: return "사용자";
        }
    }
}