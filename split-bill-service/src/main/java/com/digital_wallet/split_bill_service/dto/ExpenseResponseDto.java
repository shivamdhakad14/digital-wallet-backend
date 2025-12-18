package com.digital_wallet.split_bill_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponseDto {
    private Long id;
    private String tittle;
    private Double amount;
    private String paidByName;
    private Timestamp date;
    private Double yourShare;
}
