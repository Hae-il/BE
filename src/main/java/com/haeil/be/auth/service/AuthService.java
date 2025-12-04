package com.haeil.be.auth.service;

import static com.haeil.be.auth.exception.errorcode.AuthErrorCode.INVALID_CREDENTIALS;
import static com.haeil.be.user.exception.errorcode.UserErrorCode.USER_ALREADY_EXISTS;
import static com.haeil.be.user.exception.errorcode.UserErrorCode.USER_NOT_FOUND;

import com.haeil.be.auth.dto.request.SignupRequest;
import com.haeil.be.auth.dto.response.LoginResponse;
import com.haeil.be.auth.exception.AuthException;
import com.haeil.be.auth.util.JwtTokenProvider;
import com.haeil.be.client.domain.Client;
import com.haeil.be.client.repository.ClientRepository;
import com.haeil.be.user.domain.User;
import com.haeil.be.user.domain.type.Role;
import com.haeil.be.user.exception.UserException;
import com.haeil.be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ClientRepository clientRepository;

    @Transactional
    public LoginResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserException(USER_ALREADY_EXISTS);
        }

        userRepository.save(
                User.builder()
                        .name(request.name())
                        .email(request.email())
                        .password(passwordEncoder.encode(request.password()))
                        .role(request.role())
                        .build());

        if (request.role() == Role.ROLE_CLIENT) {
            clientRepository.save(
                    Client.clientSignupBuilder()
                            .name(request.name())
                            .email(request.email())
                            .build());
        }

        return login(request.email(), request.password());
    }

    public LoginResponse login(String email, String password) {
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException(INVALID_CREDENTIALS);
        }

        String token = jwtTokenProvider.createToken(user.getId().toString());
        return LoginResponse.from(user.getName(), user.getRole(), token);
    }
}
