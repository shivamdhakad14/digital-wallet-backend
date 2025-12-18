package com.digital_wallet.notification_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotificationNotEnableException extends RuntimeException {
    public NotificationNotEnableException(String message) {
        super(message);
    }
}
