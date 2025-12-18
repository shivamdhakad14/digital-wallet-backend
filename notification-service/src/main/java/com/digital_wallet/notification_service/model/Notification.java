package com.digital_wallet.notification_service.model;

import com.digital_wallet.notification_service.enums.NotificationChannel;
import com.digital_wallet.notification_service.enums.NotificationPriority;
import com.digital_wallet.notification_service.enums.NotificationStatus;
import com.digital_wallet.notification_service.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;
    private String roleTarget = "ROLE_USER";

    @Column(nullable = false)
    private String tittle;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private NotificationStatus status = NotificationStatus.UNREAD;

    @Column(nullable = false)
    private NotificationPriority priority = NotificationPriority.LOW;

    @Column(nullable = false)
    private NotificationChannel channel = NotificationChannel.IN_APP;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    private Timestamp readAt;

    private Timestamp expiredAt;

}
