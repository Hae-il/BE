package com.haeil.full.global.controller;

import com.haeil.full.user.domain.User;
import com.haeil.full.user.domain.type.Role;
import com.haeil.full.user.service.CustomUserDetails;
import com.haeil.full.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UserService userService;

    @ModelAttribute
    public void addAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {

            try {
                Object principal = authentication.getPrincipal();

                if (principal instanceof CustomUserDetails) {
                    CustomUserDetails userDetails = (CustomUserDetails) principal;
                    User user = userService.getUser(userDetails.getId());

                    model.addAttribute("user", user);
                    model.addAttribute("username", user.getName());
                    model.addAttribute("userRole", getRoleDisplayName(user.getRole()));
                }
            } catch (Exception e) {
                // 사용자 정보를 가져오는 데 실패하더라도, 페이지 전체가 에러나지 않도록 로그만 남기거나 무시
            }
        }
    }

    private String getRoleDisplayName(Role role) {
        switch (role) {
            case ROLE_ADMIN:
                return "관리자";
            case ROLE_ATTORNEY:
                return "변호사";
            case ROLE_SECRETARY:
                return "사무관";
            case ROLE_COUNSEL:
                return "상담사";
            case ROLE_ACCOUNT:
                return "회계";
            default:
                return "사용자";
        }
    }
}


