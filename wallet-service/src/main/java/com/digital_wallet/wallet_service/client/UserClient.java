package com.digital_wallet.wallet_service.client;

import com.digital_wallet.wallet_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "http://localhost:8081/api/users")
public interface UserClient {

    @GetMapping("/{userId}")
    UserDto getUserById(@PathVariable("userId") Long userId);

    @PostMapping("/wallet-created")
    void markWalletCreated(@RequestParam("userId") Long userId);
}
