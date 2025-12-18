package com.digital_wallet.transaction_service.consumer;

import com.digital_wallet.transaction_service.dto.TransactionReqDto;
import com.digital_wallet.transaction_service.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionConsumer {

    @Autowired
    private TransactionService transactionService;

    @KafkaListener(topics = "transaction-topic", groupId = "transaction-group")
    public void consume(TransactionReqDto transactionReqDto){
        try {
            transactionService.createTransaction(transactionReqDto);
            System.out.println(transactionReqDto);
        } catch (Exception e) {
            throw new RuntimeException("Error in Consumer" + e.getMessage());
        }
    }
}
