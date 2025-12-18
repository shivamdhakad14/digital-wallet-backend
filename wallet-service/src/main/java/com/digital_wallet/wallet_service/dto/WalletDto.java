package com.digital_wallet.wallet_service.dto;

import com.digital_wallet.wallet_service.enums.WalletStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {
    private Long id;
    private Double balance;
    private String currency;
    private String walletName;
    private Boolean isPinSet;
    private String upiId;
    private String mobileNumber;
    private WalletStatus status;
}
