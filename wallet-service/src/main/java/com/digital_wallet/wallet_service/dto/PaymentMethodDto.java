package com.digital_wallet.wallet_service.dto;

import com.digital_wallet.wallet_service.enums.PaymentMethodType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDto {
    private Long id;
    private Long userId;
    private PaymentMethodType methodType;
    private Long accountId;
    private String detailMasked;
    private Boolean isDefault;
}
