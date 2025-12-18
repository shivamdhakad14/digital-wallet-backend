package com.digital_wallet.split_bill_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDetailDto {
    private Long id;
    private String name;
    private String icon;
    private String color;
    private List<GroupMemberDto> groupMemberList;
    private Integer totalMember;
    private Double totalAmount;
    private Double yourShare;
    private Double settled;
    private Double pending;
    private Timestamp createdAt;
    private List<ExpenseResponseDto> expenseDto;
    private UserGroupBalanceDto groupBalanceDto;
}
