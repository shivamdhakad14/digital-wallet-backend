package com.digital_wallet.wallet_service.controller;

import com.digital_wallet.wallet_service.dto.*;
import com.digital_wallet.wallet_service.service.PaymentMethodService;
import com.digital_wallet.wallet_service.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;
    @Autowired
    private PaymentMethodService paymentMethodService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createWallet(@RequestBody WalletRequestDto walletRequestDto){
        Map<String, String> response = new HashMap<>();
        try{
            String wallet = walletService.createWallet(walletRequestDto);
            response.put("status", "success");
            response.put("message", wallet);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "failed");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<WalletDto> getWalletByUserId(@PathVariable Long userId){
        System.out.println("wallet found by userId : " + userId);
        return ResponseEntity.ok(walletService.getWalletByUserId(userId));
    }

    @GetMapping("/mobile/{mobileNum}")
    public ResponseEntity<WalletDto> getWalletByMobileNumber(@PathVariable String mobileNum){
        return ResponseEntity.ok(walletService.getWalletByMobile(mobileNum));
    }

    @GetMapping("/payment-method/{id}")
    public ResponseEntity<PaymentMethodDto> getPaymentMethodById(@PathVariable Long id){
        return ResponseEntity.ok(walletService.getPaymentMethodById(id));
    }

    @PostMapping("/debit")
    public ResponseEntity<ResponseDto> debit(@RequestBody DebitDto req) {
        return ResponseEntity.ok(walletService.debit(req));
    }

    @PostMapping("/credit")
    public ResponseEntity<ResponseDto> credit(@RequestBody CreditDto req) {
        return ResponseEntity.ok( walletService.credit(req));
    }

    @GetMapping("/payment-methods/{userId}")
    public ResponseEntity<List<PaymentMethodDto>> getAllPaymentMethod(@PathVariable Long userId){
        return ResponseEntity.ok(paymentMethodService.getAllPaymentMethod(userId));
    }

    @PostMapping("/verify-pin")
    public ResponseEntity<Boolean> verifyWalletPin(@RequestParam Long walletId, @RequestParam String pin){
        return ResponseEntity.ok(walletService.verifyWalletPin(walletId, pin));
    }
}
