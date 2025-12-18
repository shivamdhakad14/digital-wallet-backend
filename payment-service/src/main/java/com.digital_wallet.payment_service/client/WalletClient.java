package com.digital_wallet.payment_service.client;

import com.digital_wallet.payment_service.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "wallet-service", url = "http://localhost:8082/api/wallets")
public interface WalletClient {

    @GetMapping("/{userId}")
    WalletDto getWalletByUserId(@PathVariable ("userId") Long userId);

    @PostMapping("/credit")
    ResponseDto credit(@RequestBody CreditDto creditDto);

    @PostMapping("/debit")
    ResponseDto debit(@RequestBody DebitDto debitDto);

    @GetMapping("/payment-method/{id}")
    PaymentMethodDto getPaymentMethodById(@PathVariable("id") Long id);
}
