package com.digital_wallet.split_bill_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserGroupBalanceDto {

    private Long groupId;

    private List<BalanceEntry> youGetBack;
    private List<BalanceEntry> youOwe;

    @Data
    public static class BalanceEntry {
        private Long otherUserId;
        private String name;
        private Double amount;
    }
}

