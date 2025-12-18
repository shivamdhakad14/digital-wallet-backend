package com.digital_wallet.payment_service.client;

import com.digital_wallet.payment_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8081/api/users")
public interface UserClient {

    @GetMapping("/{userId}")
    UserDto getUserById(@PathVariable("userId") Long userId);
}
