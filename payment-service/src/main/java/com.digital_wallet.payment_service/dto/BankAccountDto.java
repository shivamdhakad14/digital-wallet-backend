package com.digital_wallet.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDto {
    private Long id;
    private String bankName;
    private String accountNumber;
    private Boolean isPrimary;
}
