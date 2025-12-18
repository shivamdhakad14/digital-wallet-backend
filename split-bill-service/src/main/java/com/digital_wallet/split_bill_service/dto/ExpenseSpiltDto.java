package com.digital_wallet.split_bill_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseSpiltDto {
    private Long expenseId;
    private Long userId;
    private String userName;
    private Double shareAmount;
    private Double percentage;
    private Boolean isSettled;
}
