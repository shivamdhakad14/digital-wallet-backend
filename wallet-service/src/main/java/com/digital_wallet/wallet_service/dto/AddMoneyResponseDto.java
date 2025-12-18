package com.digital_wallet.wallet_service.dto;

import com.digital_wallet.wallet_service.enums.PaymentMethodType;
import com.digital_wallet.wallet_service.enums.TransactionStatus;
import com.digital_wallet.wallet_service.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddMoneyResponseDto {
    private String transactionId;
    private PaymentMethodType paymentMethodType;
    private Timestamp paymentTime;
    private TransactionStatus transactionStatus;
}
