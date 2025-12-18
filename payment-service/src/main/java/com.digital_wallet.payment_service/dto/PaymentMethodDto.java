package com.digital_wallet.payment_service.dto;

import com.digital_wallet.payment_service.enums.MethodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDto {
    private Long id;
    private Long userId;
    private MethodType methodType;
    private Long accountId;
    private String detailMasked;
    private Boolean isDefault;
}
