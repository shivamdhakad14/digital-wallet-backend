package com.digital_wallet.split_bill_service.dto;

import com.digital_wallet.split_bill_service.enums.SplitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDto {
    private Long groupId;
    private Double amount;
    private String note;
    private String category;
    private Long paidBy;
    private String paidByName;
    private SplitType splitType;
    private Double shareAmount;
    private List<ExpenseSpiltDto> expenseSpilt;
    private String title;
    private Timestamp Date;
}
