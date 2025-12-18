package com.digital_wallet.split_bill_service.controller;

import com.digital_wallet.split_bill_service.dto.ApiResponse;
import com.digital_wallet.split_bill_service.dto.ExpenseDto;
import com.digital_wallet.split_bill_service.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/split-bill/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addExpense(@RequestBody ExpenseDto expenseDto) {
        try {
            expenseService.addExpense(expenseDto);
            ApiResponse response = new ApiResponse(
                    true,
                    "Expense added successfully",
                    null
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (RuntimeException e) {

            ApiResponse errorResponse = new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            );

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<List<ExpenseDto>> getAllGroupUserExistByGroupId(@PathVariable Long groupId){
        return ResponseEntity.ok(expenseService.getAllExpenseByGroupId(groupId));
    }
}
