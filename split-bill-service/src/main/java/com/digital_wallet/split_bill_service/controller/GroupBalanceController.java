package com.digital_wallet.split_bill_service.controller;

import com.digital_wallet.split_bill_service.dto.UserGroupBalanceDto;
import com.digital_wallet.split_bill_service.service.GroupBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/split-bill/group-balance")
public class GroupBalanceController {

    private final GroupBalanceService balanceService;

    @GetMapping("/{groupId}/user/{userId}")
    public ResponseEntity<UserGroupBalanceDto> getBalance(@PathVariable Long groupId,
                                                          @PathVariable Long userId) {
        return ResponseEntity.ok(balanceService.getUserBalance(groupId, userId));
    }
}

