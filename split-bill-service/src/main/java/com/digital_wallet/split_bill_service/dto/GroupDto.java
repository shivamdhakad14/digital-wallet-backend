package com.digital_wallet.split_bill_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    private String name;
    private String icon;
    private String color;
    private List<GroupMemberDto> groupMemberList;
    private Integer totalMember;
}
