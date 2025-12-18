package com.digital_wallet.payment_service.client;


import com.digital_wallet.payment_service.dto.TransactionReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "transaction-service", url = "http://localhost:8084/api/transactions")
public interface TransactionClient {

    @PostMapping("/create")
    void createTransaction(@RequestBody TransactionReqDto transactionReqDto);
}
