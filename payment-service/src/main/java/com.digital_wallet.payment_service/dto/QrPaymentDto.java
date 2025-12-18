package com.digital_wallet.payment_service.dto;

import com.digital_wallet.payment_service.enums.MethodType;
import com.digital_wallet.payment_service.enums.Status;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrPaymentDto {
    private Long payerId;
    private Long payeeId;
    private Long payerWalletId;
    private Long payeeWalletId;
    private Double amount;
    private MethodType methodType;
    private String pin;
    private Long methodId;
    private String description;
}
