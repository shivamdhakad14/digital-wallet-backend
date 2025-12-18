package com.digital_wallet.payment_service.dto;

import com.digital_wallet.payment_service.enums.notificationEnums.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationReqDto {
    private Long userId;
    private String tittle;
    private String message;
    private NotificationType type;
    private NotificationStatus status;
    private NotificationPriority priority;
    private NotificationChannel channel;
}
