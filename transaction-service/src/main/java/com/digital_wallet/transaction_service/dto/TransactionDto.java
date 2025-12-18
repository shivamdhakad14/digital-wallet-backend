package com.digital_wallet.transaction_service.dto;

import com.digital_wallet.transaction_service.enums.Direction;
import com.digital_wallet.transaction_service.enums.MethodType;
import com.digital_wallet.transaction_service.enums.PaymentType;
import com.digital_wallet.transaction_service.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private Double amount;
    private String time;
    private String date;
    private Direction direction;
    private PaymentType type;
    private Status status;
    private MethodType method;
    private String from;
    private String to;
}
