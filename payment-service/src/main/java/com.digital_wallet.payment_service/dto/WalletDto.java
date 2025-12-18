package com.digital_wallet.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {
    private Long id;
    private String upiId;
    private Long userId;
    private String walletName;
    private Double balance;
    private String currency;
    private Boolean isPinSet;
}
