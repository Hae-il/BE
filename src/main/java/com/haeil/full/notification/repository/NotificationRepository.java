package com.haeil.full.notification.repository;

import com.haeil.full.notification.domain.Notification;
import com.haeil.full.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiverOrderByIdDesc(User receiver);
}

