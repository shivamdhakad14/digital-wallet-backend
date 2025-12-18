package com.digital_wallet.split_bill_service.service;

import com.digital_wallet.split_bill_service.dto.ExpenseDto;
import com.digital_wallet.split_bill_service.dto.ExpenseSpiltDto;
import com.digital_wallet.split_bill_service.model.*;
import com.digital_wallet.split_bill_service.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpensesRepository expensesRepository;
    @Autowired
    private ExpenseSplitRepository expenseSplitRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupBalanceRepository balanceRepository;
    @Autowired
    private GroupExpenseHistoryRepository groupExpenseHistoryRepository;

    @Transactional
    public void addExpense(ExpenseDto expenseDto) {

        Group group = groupRepository.findById(expenseDto.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        boolean payerExists = group.getGroupMembers().stream()
                .anyMatch(m -> m.getUserId().equals(expenseDto.getPaidBy()));

        if (!payerExists) {
            throw new RuntimeException("Payer is not a member of this group");
        }

        Expenses expenses = new Expenses();
        expenses.setTitle(expenseDto.getTitle());
        expenses.setNote(expenseDto.getNote());
        expenses.setCategory(expenseDto.getCategory());
        expenses.setAmount(expenseDto.getAmount());
        expenses.setPaidBy(expenseDto.getPaidBy());
        expenses.setPaidByName(expenseDto.getPaidByName());
        expenses.setSplitType(expenseDto.getSplitType());
        expenses.setShareAmount(expenseDto.getShareAmount());
        expenses.setGroup(group);

        List<ExpenseSplit> expenseSplits = expenseDto.getExpenseSpilt().stream()
                .map(splitDto -> {
                    ExpenseSplit split = new ExpenseSplit();
                    split.setUserId(splitDto.getUserId());
                    split.setPercentage(splitDto.getPercentage());
                    split.setShareAmount(splitDto.getShareAmount());
                    split.setUserName(splitDto.getUserName());
                    split.setExpenses(expenses);
                    return split;
                })
                .collect(Collectors.toList());

        expenses.setExpenseSplits(expenseSplits);
        Expenses save = expensesRepository.save(expenses);

        Long payerId = expenseDto.getPaidBy();
        String payerName = expenseDto.getPaidByName();

        for (ExpenseSplit split : expenseSplits) {

            Long memberId = split.getUserId();
            String memberName = split.getUserName();
            Double share = split.getShareAmount();

            if (memberId.equals(payerId)) {
                continue;
            }

            Optional<GroupBalance> directBalanceOpt =
                    balanceRepository.findByGroup_IdAndFromUserAndToUser(
                            group.getId(), memberId, payerId
                    );

            Optional<GroupBalance> reverseBalanceOpt =
                    balanceRepository.findByGroup_IdAndFromUserAndToUser(
                            group.getId(), payerId, memberId
                    );

            if (directBalanceOpt.isPresent()) {
                GroupBalance bal = directBalanceOpt.get();
                bal.setAmount(bal.getAmount() + share);
                balanceRepository.save(bal);

            } else if (reverseBalanceOpt.isPresent()) {
                GroupBalance reverse = reverseBalanceOpt.get();

                if (reverse.getAmount() > share) {
                    reverse.setAmount(reverse.getAmount() - share);
                    balanceRepository.save(reverse);

                } else if (reverse.getAmount() < share) {
                    double diff = share - reverse.getAmount();
                    balanceRepository.delete(reverse);

                    GroupBalance direct = new GroupBalance();
                    direct.setGroup(group);
                    direct.setFromUser(memberId);
                    direct.setFromUserName(memberName);
                    direct.setToUser(payerId);
                    direct.setToUserName(payerName);
                    direct.setAmount(diff);
                    balanceRepository.save(direct);

                } else {
                    balanceRepository.delete(reverse);
                }

            } else {
                GroupBalance bal = new GroupBalance();
                bal.setGroup(group);
                bal.setFromUser(memberId);
                bal.setFromUserName(memberName);
                bal.setToUser(payerId);
                bal.setToUserName(payerName);
                bal.setAmount(share);
                balanceRepository.save(bal);
            }
        }
    }

    public List<ExpenseDto> getAllExpenseByGroupId(Long groupId){
        List<Expenses> expenses = expensesRepository.findAllByGroupId(groupId);
        return expenses.stream().map(expense -> {
            ExpenseDto expenseDto = new ExpenseDto();
            expenseDto.setNote(expense.getNote());
            expenseDto.setGroupId(expense.getId());
            expenseDto.setAmount(expense.getAmount());
            expenseDto.setCategory(expense.getCategory());
            expenseDto.setTitle(expense.getTitle());
            expenseDto.setPaidBy(expense.getPaidBy());
            expenseDto.setDate(expense.getCreatedAt());
            expenseDto.setSplitType(expense.getSplitType());

            expenseDto.setExpenseSpilt(expense.getExpenseSplits().stream()
                    .map(expenseSplit -> {
                        ExpenseSpiltDto expenseSpiltDto = new ExpenseSpiltDto();
                        expenseSpiltDto.setExpenseId(expenseSplit.getId());
                        expenseSpiltDto.setPercentage(expenseSplit.getPercentage());
                        expenseSpiltDto.setIsSettled(expenseSplit.getIsSettled());
                        expenseSpiltDto.setShareAmount(expenseSplit.getShareAmount());
                        expenseSpiltDto.setUserId(expenseSplit.getUserId());
                        return expenseSpiltDto;
                    }).collect(Collectors.toList()));
            return expenseDto;
        }).collect(Collectors.toList());
    }

    public List<ExpenseDto> getUserPaidExpenses(Long userId){
        List<Expenses> expenses = expensesRepository.findAllByPaidBy(userId);
        return expenses.stream().map(expense -> {
            ExpenseDto expenseDto = new ExpenseDto();
            expenseDto.setNote(expense.getNote());
            expenseDto.setGroupId(expense.getId());
            expenseDto.setAmount(expense.getAmount());
            expenseDto.setCategory(expense.getCategory());
            expenseDto.setTitle(expense.getTitle());
            expenseDto.setPaidBy(expense.getPaidBy());
            expenseDto.setDate(expense.getCreatedAt());
            expenseDto.setSplitType(expense.getSplitType());

            expenseDto.setExpenseSpilt(expense.getExpenseSplits().stream()
                    .map(expenseSplit -> {
                        ExpenseSpiltDto expenseSpiltDto = new ExpenseSpiltDto();
                        expenseSpiltDto.setExpenseId(expenseSplit.getId());
                        expenseSpiltDto.setPercentage(expenseSplit.getPercentage());
                        expenseSpiltDto.setIsSettled(expenseSplit.getIsSettled());
                        expenseSpiltDto.setShareAmount(expenseSplit.getShareAmount());
                        expenseSpiltDto.setUserId(expenseSplit.getUserId());
                        return expenseSpiltDto;
                    }).collect(Collectors.toList()));
            return expenseDto;
        }).collect(Collectors.toList());
    }

    public void GroupTotalBalance(Expenses expenses){
        GroupExpenseHistory groupExpenseHistory = new GroupExpenseHistory();
        groupExpenseHistory.setGroupName(expenses.getGroup().getName());
        groupExpenseHistory.setGroup(expenses.getGroup());
        groupExpenseHistory.setTotalExpense(groupExpenseHistory.getTotalExpense() + expenses.getAmount());
        groupExpenseHistory.setPerShareAmount(groupExpenseHistory.getPerShareAmount() + expenses.getShareAmount());
        groupExpenseHistoryRepository.save(groupExpenseHistory);
    }

}
