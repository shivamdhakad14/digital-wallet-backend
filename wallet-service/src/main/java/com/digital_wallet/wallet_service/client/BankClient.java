package com.digital_wallet.wallet_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://localhost:8081/api/banks")
public interface BankClient {

    @PostMapping("/debit")
    String debitFromBank(@RequestParam("accountId") Long accountId,
                         @RequestParam("amount") Double amount);
}
