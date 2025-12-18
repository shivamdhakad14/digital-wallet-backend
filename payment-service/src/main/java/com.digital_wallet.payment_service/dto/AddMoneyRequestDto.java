package com.digital_wallet.payment_service.dto;

import com.digital_wallet.payment_service.enums.PaymentMethodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddMoneyRequestDto {
    private Long walletId;
    private Long userId;
    private Double amount;
    private Long paymentMethodId;
    private PaymentMethodType paymentMethodType;
}
