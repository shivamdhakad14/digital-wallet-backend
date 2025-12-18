package com.digital_wallet.split_bill_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponseDto {
    private Long id;
    private String name;
    private String icon;
    private String color;
    private List<GroupMemberDto> members;
    private Double totalAmount;
    private Double yourShare;
    private Double settled;
    private Double pending;
    private Long expenses;
    private Timestamp lastActivity;
}
