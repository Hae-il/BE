package com.haeil.be.user.service;

import com.haeil.be.consultation.exception.ConsultationException;
import com.haeil.be.consultation.exception.errorcode.ConsultationErrorCode;
import com.haeil.be.user.domain.User;
import com.haeil.be.user.repository.UserRepository;
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
}
