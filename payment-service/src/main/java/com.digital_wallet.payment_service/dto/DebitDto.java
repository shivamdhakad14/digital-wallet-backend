package com.digital_wallet.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitDto {
    private Long userId;
    private Double amount;
    private String pin;
    private Long paymentMethodId;
}
