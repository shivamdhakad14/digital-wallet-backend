package com.digital_wallet.payment_service.client;

import com.digital_wallet.payment_service.dto.NotificationReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://localhost:8085/api/notifications")
public interface NotificationClient {

    @PostMapping("/create")
    public void createNotification(@RequestBody NotificationReqDto notificationReqDto);
}
