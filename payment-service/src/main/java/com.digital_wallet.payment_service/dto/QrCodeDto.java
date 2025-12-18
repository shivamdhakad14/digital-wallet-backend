package com.digital_wallet.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeDto {
    private Long walletId;
    private Long userId;
    private String upiId;
}
