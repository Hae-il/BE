package com.haeil.be.auth.service;


import com.haeil.be.user.domain.User;
import com.haeil.be.user.domain.type.Role;
import com.haeil.be.user.exception.UserException;
import com.haeil.be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.haeil.be.user.exception.errorcode.UserErrorCode.USER_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(String name, String email, String password, Role role) {
        if (userRepository.existsByEmail(email)){
            throw new UserException(USER_ALREADY_EXISTS);
        }

        userRepository.save(
                User.builder()
                        .name(name)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .role(role)
                        .build());
    }
}
