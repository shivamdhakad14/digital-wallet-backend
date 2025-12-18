package com.digital_wallet.notification_service.repository;

import com.digital_wallet.notification_service.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);

    Page<Notification> findAllByUserId(Long userId, Pageable pageable);
}