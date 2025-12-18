package com.digital_wallet.payment_service.client;

import com.digital_wallet.payment_service.dto.BankAccountDto;
import com.digital_wallet.payment_service.dto.BankResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://localhost:8081/api/banks")
public interface BankClient {

    @PostMapping("/debit")
    BankResponse debitFromBank(@RequestParam("accountId") Long accountId,
                               @RequestParam("amount") Double amount);

    @GetMapping("/{bankId}")
    BankAccountDto getBankAccount(@PathVariable Long bankId);
}
