package com.haeil.full.user.repository;

import com.haeil.full.user.domain.User;
import com.haeil.full.user.domain.type.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long id);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findAllByRole(Role role);
}
