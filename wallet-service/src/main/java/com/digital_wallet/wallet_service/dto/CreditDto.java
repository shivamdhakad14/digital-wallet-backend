package com.digital_wallet.wallet_service.dto;

import com.digital_wallet.wallet_service.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDto {
    private Long userId;
    private Double amount;
}
