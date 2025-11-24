package com.haeil.be.notification.repository;

import com.haeil.be.notification.domain.Notification;
import com.haeil.be.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiverOrderByIdDesc(User receiver);
}

