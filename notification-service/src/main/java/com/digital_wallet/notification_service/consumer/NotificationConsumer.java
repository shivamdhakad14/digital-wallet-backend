package com.digital_wallet.notification_service.consumer;

import com.digital_wallet.notification_service.dto.NotificationReqDto;
import com.digital_wallet.notification_service.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
@Service
public class NotificationConsumer {

    private final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void handlePaymentEvent(NotificationReqDto event) {
        try {
            notificationService.createNotification(event);
            System.out.println(event);
        } catch (Exception e) {
            throw new RuntimeException("Error in Consumer" + e.getMessage());
        }
    }
}
