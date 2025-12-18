package com.digital_wallet.split_bill_service.service;

import com.digital_wallet.split_bill_service.dto.UserGroupBalanceDto;
import com.digital_wallet.split_bill_service.model.GroupBalance;
import com.digital_wallet.split_bill_service.repository.GroupBalanceRepository;
import com.digital_wallet.split_bill_service.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupBalanceService {

    private final GroupBalanceRepository balanceRepository;
    private final GroupRepository groupRepository;

    public UserGroupBalanceDto getUserBalance(Long groupId, Long userId) {

        List<GroupBalance> youGetBackList =
                balanceRepository.findByToUser(userId)
                        .stream()
                        .filter(b -> b.getGroup().getId().equals(groupId))
                        .toList();

        List<GroupBalance> youOweList =
                balanceRepository.findByFromUser(userId)
                        .stream()
                        .filter(b -> b.getGroup().getId().equals(groupId))
                        .toList();

        UserGroupBalanceDto dto = new UserGroupBalanceDto();
        dto.setGroupId(groupId);

        dto.setYouGetBack(
                youGetBackList.stream().map(b -> {
                    UserGroupBalanceDto.BalanceEntry entry =
                            new UserGroupBalanceDto.BalanceEntry();
                    entry.setOtherUserId(b.getFromUser());
                    entry.setAmount(b.getAmount());
                    entry.setName(b.getFromUserName());
                    return entry;
                }).toList()
        );

        dto.setYouOwe(
                youOweList.stream().map(b -> {
                    UserGroupBalanceDto.BalanceEntry entry =
                            new UserGroupBalanceDto.BalanceEntry();
                    entry.setOtherUserId(b.getToUser());
                    entry.setAmount(b.getAmount());
                    entry.setName(b.getToUserName());
                    return entry;
                }).toList()
        );

        return dto;
    }
}

