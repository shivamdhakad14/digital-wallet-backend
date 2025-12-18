package com.digital_wallet.payment_service.dto;

import com.digital_wallet.payment_service.enums.MethodType;
import com.digital_wallet.payment_service.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private String transactionId;
    private MethodType methodType;
    private Timestamp date;
    private Status status;
}
