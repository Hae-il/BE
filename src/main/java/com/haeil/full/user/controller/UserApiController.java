package com.haeil.full.user.controller;

import com.haeil.full.user.domain.type.Role;
import com.haeil.full.user.service.UserService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @GetMapping("/attorneys")
    public ResponseEntity<List<Map<String, Object>>> getAttorneys() {
        return ResponseEntity.ok(getUsersByRole(Role.ROLE_ATTORNEY));
    }

    @GetMapping("/counsels")
    public ResponseEntity<List<Map<String, Object>>> getCounsels() {
        return ResponseEntity.ok(getUsersByRole(Role.ROLE_COUNSEL));
    }

    private List<Map<String, Object>> getUsersByRole(Role role) {
        return userService.getUsersByRole(role).stream()
                .map(
                        user ->
                                Map.of(
                                        "id",
                                        (Object) user.getId(),
                                        "name",
                                        user.getName(),
                                        "email",
                                        user.getEmail() != null ? user.getEmail() : ""))
                .collect(Collectors.toList());
    }
}
