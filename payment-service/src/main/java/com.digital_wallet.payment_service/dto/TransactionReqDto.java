package com.digital_wallet.payment_service.dto;

import com.digital_wallet.payment_service.enums.Direction;
import com.digital_wallet.payment_service.enums.MethodType;
import com.digital_wallet.payment_service.enums.PaymentType;
import com.digital_wallet.payment_service.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.ScrollPosition;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionReqDto {
    private String from;
    private String to;
    private Long payerId;
    private Long payeeId;
    private Long payerWalletId;
    private Long payeeWalletId;
    private Double amount;
    private String currency;
    private PaymentType type;
    private Status status;
    private Direction direction;
    private MethodType methodType;
    private String description;
    private String failureReason;
}
