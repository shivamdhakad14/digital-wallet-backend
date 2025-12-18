package com.digital_wallet.notification_service.dto;

import com.digital_wallet.notification_service.enums.NotificationChannel;
import com.digital_wallet.notification_service.enums.NotificationPriority;
import com.digital_wallet.notification_service.enums.NotificationStatus;
import com.digital_wallet.notification_service.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationReqDto {
    private Long id;
    private Long userId;
    private String tittle;
    private String message;
    private NotificationType type;
    private NotificationStatus status;
    private NotificationPriority priority;
    private NotificationChannel channel;
}
