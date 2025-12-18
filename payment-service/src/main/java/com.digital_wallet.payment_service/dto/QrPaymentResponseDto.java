package com.digital_wallet.payment_service.dto;

import com.digital_wallet.payment_service.enums.MethodType;
import com.digital_wallet.payment_service.enums.PaymentMethodType;
import com.digital_wallet.payment_service.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrPaymentResponseDto {
    private String txnId;
    private Timestamp paymentTime;
    private MethodType paymentMethodType;
    private Status status;
    private String message;
}
