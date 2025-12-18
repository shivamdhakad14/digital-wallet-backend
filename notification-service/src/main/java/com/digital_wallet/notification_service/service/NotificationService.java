package com.digital_wallet.notification_service.service;

import com.digital_wallet.notification_service.dto.NotificationReqDto;
import com.digital_wallet.notification_service.dto.NotificationSettingDto;
import com.digital_wallet.notification_service.exception.NotificationNotEnableException;
import com.digital_wallet.notification_service.model.Notification;
import com.digital_wallet.notification_service.model.UserNotificationSetting;
import com.digital_wallet.notification_service.repository.NotificationRepo;
import com.digital_wallet.notification_service.repository.UserNotificationSettingRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private  NotificationRepo notificationRepo;
    @Autowired
    private  UserNotificationSettingRepo userNotificationSettingRepo;

    @Transactional
    public void createNotification(NotificationReqDto notificationReqDto){
        UserNotificationSetting userNotificationSetting = userNotificationSettingRepo
                .findByUserId(notificationReqDto.getUserId())
                .orElseThrow(() -> new RuntimeException("user notification setting not found"));

        if(userNotificationSetting.getInAppEnabled()){
            Notification notification = new Notification();
            notification.setUserId(notificationReqDto.getUserId());
            notification.setTittle(notificationReqDto.getTittle());
            notification.setChannel(notificationReqDto.getChannel());
            notification.setMessage(notificationReqDto.getMessage());
            notification.setPriority(notificationReqDto.getPriority());
            notification.setStatus(notificationReqDto.getStatus());
            notification.setType(notificationReqDto.getType());
            notificationRepo.save(notification);
         } else {
            throw new NotificationNotEnableException("user not enable in app notification " +
                    notificationReqDto.getUserId());
        }
    }

    @Transactional
    public String notificationEnable(NotificationSettingDto notificationSettingDto){
        UserNotificationSetting userNotificationSetting = new UserNotificationSetting();
        userNotificationSetting.setUserId(notificationSettingDto.getUserId());
        userNotificationSetting.setEmailEnabled(notificationSettingDto.getEmailEnabled());
        userNotificationSetting.setSmsEnabled(notificationSettingDto.getSmsEnabled());
        userNotificationSetting.setPushEnabled(notificationSettingDto.getPushEnabled());
        userNotificationSetting.setInAppEnabled(notificationSettingDto.getInAppEnabled());
        userNotificationSettingRepo.save(userNotificationSetting);
        return "notification enabled successfully";
    }

    public List<NotificationReqDto> getLatestNotificationsByUserId(Long userId) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        Page<Notification> notificationPage = notificationRepo.findAllByUserId(userId, pageable);

        return notificationPage.getContent()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private NotificationReqDto convertToDto(Notification notification) {
        NotificationReqDto notificationReqDto = new NotificationReqDto();
        notificationReqDto.setId(notification.getId());
        notificationReqDto.setChannel(notification.getChannel());
        notificationReqDto.setTittle(notification.getTittle());
        notificationReqDto.setPriority(notification.getPriority());
        notificationReqDto.setMessage(notification.getMessage());
        notificationReqDto.setStatus(notification.getStatus());
        notificationReqDto.setType(notification.getType());
        return notificationReqDto;
    }
}
