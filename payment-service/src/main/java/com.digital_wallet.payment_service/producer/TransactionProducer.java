package com.digital_wallet.payment_service.producer;

import com.digital_wallet.payment_service.dto.TransactionReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTransaction(TransactionReqDto transactionReqDto){
        kafkaTemplate.send("transaction-topic", transactionReqDto);
    }
}
