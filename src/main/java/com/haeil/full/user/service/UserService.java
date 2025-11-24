package com.haeil.full.user.service;

import com.haeil.full.consultation.exception.ConsultationException;
import com.haeil.full.consultation.exception.errorcode.ConsultationErrorCode;
import com.haeil.full.user.domain.User;
import com.haeil.full.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public User getUser(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ConsultationException(ConsultationErrorCode.USER_NOT_FOUND));
    }

    public User getUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ConsultationException(ConsultationErrorCode.USER_NOT_FOUND));
    }
}
