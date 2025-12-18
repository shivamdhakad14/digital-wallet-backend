package com.digital_wallet.notification_service.repository;

import com.digital_wallet.notification_service.model.UserNotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserNotificationSettingRepo extends JpaRepository<UserNotificationSetting, Long> {

    Optional<UserNotificationSetting> findByUserId(Long userId);
}
