package com.digital_wallet.split_bill_service.service;

import com.digital_wallet.split_bill_service.client.UserClient;
import com.digital_wallet.split_bill_service.dto.*;
import com.digital_wallet.split_bill_service.enums.JoinMethod;
import com.digital_wallet.split_bill_service.enums.Status;
import com.digital_wallet.split_bill_service.model.*;
import com.digital_wallet.split_bill_service.repository.GroupMemberRepository;
import com.digital_wallet.split_bill_service.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupService {
    
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private UserClient userClient;
    @Autowired
    private GroupBalanceService groupBalanceService;
    @Autowired
    private ExpenseService expenseService;

    public void createGroup(GroupDto groupDto) {
            List<UserDto> groupMembers = groupDto.getGroupMemberList()
                    .stream()
                    .map(m -> userClient.getUserById(m.getId()))
                    .toList();

            Group group = new Group();
            group.setName(groupDto.getName());
            group.setIcon(groupDto.getIcon());
            group.setColor(groupDto.getColor());
            group.setCreatedBy(groupMembers.getFirst().getId());
            group.setIsActive(true);
            group.setIsSettled(false);
            group.setTotalMember(groupMembers.size());

            Set<GroupMember> groupMemberSet = new HashSet<>();

            for (UserDto member : groupMembers) {
                GroupMember gm = new GroupMember();
                gm.setMemberName(member.getName());
                gm.setIsGroupAdmin(false);
                gm.setStatus(Status.ACTIVE);
                gm.setJoinMethod(JoinMethod.MANUAL);
                gm.setUserId(member.getId());
                gm.setGroup(group);
                groupMemberSet.add(gm);
            }

            group.setGroupMembers(groupMemberSet);
            groupRepository.save(group);
    }

    public List<GroupDto> getAllGroupUserExistOn(Long userId){
        List<Group> groups = groupRepository
                .findAllByGroupMembers_UserIdAndDeletedAtIsNull(userId);
        return groups.stream().map(group -> {
            GroupDto groupDto = new GroupDto();
            groupDto.setName(group.getName());
            groupDto.setIcon(group.getIcon());
            groupDto.setTotalMember(group.getTotalMember());
            return groupDto;
        }).collect(Collectors.toList());
    }

    public GroupDto getGroupById(Long groupId){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("group not found"));
        GroupDto groupDto = new GroupDto();
        groupDto.setName(group.getName());
        groupDto.setIcon(group.getIcon());
        groupDto.setTotalMember(group.getTotalMember());
        return groupDto;
    }

    public List<GroupMemberDto> getAllGroupMember(Long groupId){
        List<GroupMember> groupMembers = groupMemberRepository
                .findAllByGroupId(groupId);
        return groupMembers.stream().map(groupMember -> {
            GroupMemberDto groupMemberDto = new GroupMemberDto();
            groupMemberDto.setId(groupMember.getUserId());
            groupMemberDto.setName(groupMember.getMemberName());
            return groupMemberDto;
        }).collect(Collectors.toList());
    }

    public GroupDetailDto getAllDetailGroupById(Long groupId, Long userId){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("group not found"));

        GroupDetailDto groupDetailDto = new GroupDetailDto();
        groupDetailDto.setId(group.getId());
        groupDetailDto.setName(group.getName());
        groupDetailDto.setIcon(group.getIcon());
        groupDetailDto.setCreatedAt(group.getCreatedAt());
        groupDetailDto.setColor(group.getColor());
        groupDetailDto.setTotalMember(group.getTotalMember());

        Set<GroupMember> groupMembers = group.getGroupMembers();
        List<GroupMemberDto> groupMembersSet = groupMembers.stream().map(groupMember -> {
            GroupMemberDto groupMemberDto = new GroupMemberDto();
            groupMemberDto.setId(groupMember.getUserId());
            groupMemberDto.setName(groupMember.getMemberName());
            return groupMemberDto;
        }).collect(Collectors.toList());
        groupDetailDto.setGroupMemberList(groupMembersSet);

        List<Expenses> expenses = group.getExpenses().reversed();
        List<ExpenseResponseDto> expenseList = expenses.stream().map(expense -> {
            ExpenseResponseDto expenseDto = new ExpenseResponseDto();
            expenseDto.setId(expense.getId());
            expenseDto.setTittle(expense.getNote());
            expenseDto.setAmount(expense.getAmount());
            expenseDto.setYourShare(expense.getShareAmount());
            expenseDto.setDate(expense.getCreatedAt());
            expenseDto.setPaidByName(expense.getPaidByName());
            return expenseDto;
        }).collect(Collectors.toList());
        groupDetailDto.setExpenseDto(expenseList);

        double totalAmount = expenses.stream()
                .mapToDouble(Expenses::getAmount)
                .sum();
        groupDetailDto.setTotalAmount(totalAmount);

        double yourShare = expenses.stream()
                .mapToDouble(Expenses::getShareAmount)
                .sum();
        groupDetailDto.setYourShare(yourShare);

        double settled = expenses.stream()
                .filter(e -> e.getPaidBy().equals(userId))
                .mapToDouble(Expenses::getAmount)
                .sum();
        groupDetailDto.setSettled(settled);

        double pending = yourShare - settled;

        if (pending > 0){
            groupDetailDto.setPending(yourShare - settled);
        }else{
            groupDetailDto.setPending(0.0);
        }

        UserGroupBalanceDto userBalance = groupBalanceService.getUserBalance(group.getId(), userId);
        groupDetailDto.setGroupBalanceDto(userBalance);
        return groupDetailDto;
    }

    public List<GroupResponseDto> getAllGroupListUserExistOn(Long userId) {

        List<Group> groups = groupRepository
                .findAllByGroupMembers_UserIdAndDeletedAtIsNull(userId);

        return groups.stream().map(group -> {

            GroupResponseDto dto = new GroupResponseDto();
            dto.setId(group.getId());
            dto.setColor(group.getColor());
            dto.setIcon(group.getIcon());
            dto.setName(group.getName());

            List<GroupMemberDto> members = group.getGroupMembers().stream()
                    .map(m -> new GroupMemberDto(m.getUserId(), m.getMemberName()))
                    .collect(Collectors.toList());
            dto.setMembers(members);

            List<Expenses> expenses = group.getExpenses();
            dto.setExpenses((long) expenses.size());

            double totalAmount = expenses.stream()
                    .mapToDouble(Expenses::getAmount)
                    .sum();
            dto.setTotalAmount(totalAmount);

            double yourShare = expenses.stream()
                    .mapToDouble(Expenses::getShareAmount)
                    .sum();
            dto.setYourShare(yourShare);

            double settled = expenses.stream()
                    .filter(e -> e.getPaidBy().equals(userId))
                    .mapToDouble(Expenses::getAmount)
                    .sum();
            dto.setSettled(settled);

            dto.setPending(yourShare - settled);

            dto.setLastActivity(group.getUpdatedAt());

            return dto;

        }).collect(Collectors.toList());
    }


}
