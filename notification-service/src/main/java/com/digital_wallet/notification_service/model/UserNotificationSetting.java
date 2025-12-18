package com.digital_wallet.notification_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "user_notification_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Boolean emailEnabled = false;
    private Boolean smsEnabled = false;
    private Boolean pushEnabled = false;
    private Boolean inAppEnabled = true;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
