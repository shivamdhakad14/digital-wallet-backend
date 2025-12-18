package com.digital_wallet.split_bill_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMemberDto {
    private Long id;
    private String name;
}
