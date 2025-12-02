package com.haeil.full.notification.domain;

import com.haeil.full.global.entity.BaseEntity;
import com.haeil.full.notification.domain.type.NotificationType;
import com.haeil.full.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "notifications")
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @Column(name = "content")
    private String content;

    @Column(name = "related_url")
    private String relatedUrl;

    @Column(name = "is_read")
    private boolean isRead;

    @Builder
    public Notification(
            User receiver, NotificationType notificationType, String content, String relatedUrl) {
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.content = content;
        this.relatedUrl = relatedUrl;
        this.isRead = false;
    }

    public void read() {
        this.isRead = true;
    }
}
