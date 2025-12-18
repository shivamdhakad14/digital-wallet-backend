package com.digital_wallet.payment_service.controller;

import com.digital_wallet.payment_service.dto.*;
import com.digital_wallet.payment_service.service.PaymentService;
import com.digital_wallet.payment_service.service.QrCodeManageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private QrCodeManageService qrCodeManageService;

    @PostMapping("/qr-payment")
    public ResponseEntity<QrPaymentResponseDto> qrPayment(@RequestBody QrPaymentDto qrPaymentDto) throws JsonProcessingException {
        System.out.println(qrPaymentDto);
        return ResponseEntity.ok(paymentService.qrPayment(qrPaymentDto));
    }

    @PostMapping("/generate-qr")
    public ResponseEntity<ResponseEntity<byte[]>> generateQr(@RequestBody QrCodeDto qrCodeDto){
        return ResponseEntity.ok(qrCodeManageService.qrCodeGenerate(qrCodeDto));
    }

    @PostMapping("/add-money")
    public ResponseEntity<TransactionDto> addMoneyToWallet(@RequestBody AddMoneyRequestDto addMoneyRequestDto){
        return ResponseEntity.ok(paymentService.addMoneyToWallet(addMoneyRequestDto));
    }
}
