package com.digital_wallet.split_bill_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupHistoryDto {
    private Long groupId;
    private String groupName;
    private Double totalExpense;
    private Double perShareAmount;
    private Boolean isSettled;
}
