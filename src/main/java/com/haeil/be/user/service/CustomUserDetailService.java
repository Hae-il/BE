package com.haeil.be.user.service;

import static com.haeil.be.user.exception.errorcode.UserErrorCode.USER_NOT_FOUND;

import com.haeil.be.user.domain.User;
import com.haeil.be.user.exception.UserException;
import com.haeil.be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user =
                userRepository
                        .findUserById(Long.parseLong(id))
                        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        return new CustomUserDetails(user.getId(), user.getName(), user.getRole().name());
    }
}
