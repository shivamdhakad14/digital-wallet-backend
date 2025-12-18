package com.digital_wallet.notification_service.controller;

import com.digital_wallet.notification_service.dto.NotificationReqDto;
import com.digital_wallet.notification_service.dto.NotificationSettingDto;
import com.digital_wallet.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/create")
    public void createNotification(@RequestBody NotificationReqDto notificationReqDto){
        notificationService.createNotification(notificationReqDto);
    }

    @PostMapping("/setting")
    public ResponseEntity<String> notificationSetting(@RequestBody NotificationSettingDto notificationSettingDto){
        return ResponseEntity.ok(notificationService.notificationEnable(notificationSettingDto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationReqDto>> getAllNotification(@PathVariable Long userId){
        return ResponseEntity.ok(notificationService.getLatestNotificationsByUserId(userId));
    }
}
