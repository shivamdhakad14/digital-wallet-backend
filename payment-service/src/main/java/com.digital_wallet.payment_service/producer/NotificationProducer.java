package com.digital_wallet.payment_service.producer;

import com.digital_wallet.payment_service.dto.NotificationReqDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentEvent(NotificationReqDto event)  {
        kafkaTemplate.send("notification-topic", event);
        System.out.println("✅ Sent PaymentEvent: " + event);
    }
}
