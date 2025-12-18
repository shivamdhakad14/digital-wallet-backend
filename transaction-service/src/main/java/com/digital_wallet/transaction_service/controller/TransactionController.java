package com.digital_wallet.transaction_service.controller;

import com.digital_wallet.transaction_service.dto.TransactionDto;
import com.digital_wallet.transaction_service.dto.TransactionReqDto;
import com.digital_wallet.transaction_service.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create")
    public void createTransaction(@RequestBody TransactionReqDto transactionReqDto){
        transactionService.createTransaction(transactionReqDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<TransactionDto>> getAllTransaction(@PathVariable Long userId){
        System.out.println("getAllTransaction called");
        return ResponseEntity.ok(transactionService.getAllTransactionByUserId(userId));
    }
}
