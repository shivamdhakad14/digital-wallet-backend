package com.digital_wallet.wallet_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletRequestDto {
    private Long userId;
    private String walletName;
    private String pin;
    private String confirmPin;
}
