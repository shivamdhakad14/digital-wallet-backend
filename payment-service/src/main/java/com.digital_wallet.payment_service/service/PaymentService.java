package com.digital_wallet.payment_service.service;

import com.digital_wallet.payment_service.client.*;
import com.digital_wallet.payment_service.dto.*;
import com.digital_wallet.payment_service.enums.MethodType;
import com.digital_wallet.payment_service.enums.PaymentType;
import com.digital_wallet.payment_service.enums.Status;
import com.digital_wallet.payment_service.enums.notificationEnums.NotificationChannel;
import com.digital_wallet.payment_service.enums.notificationEnums.NotificationPriority;
import com.digital_wallet.payment_service.enums.notificationEnums.NotificationStatus;
import com.digital_wallet.payment_service.enums.notificationEnums.NotificationType;
import com.digital_wallet.payment_service.model.AddMoneyRequest;
import com.digital_wallet.payment_service.model.Payment;
import com.digital_wallet.payment_service.producer.NotificationProducer;
import com.digital_wallet.payment_service.producer.TransactionProducer;
import com.digital_wallet.payment_service.repository.AddMoneyReqRepository;
import com.digital_wallet.payment_service.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private WalletClient walletClient;
    @Autowired
    private UserClient userClient;
    @Autowired
    private BankClient bankClient;
    @Autowired
    private AddMoneyReqRepository addMoneyReqRepository;
    @Autowired
    private QrCodeManageService qrCodeManageService;
    @Autowired
    private TransactionProducer transactionProducer;
    @Autowired
    private NotificationProducer notificationProducer;
    @Autowired
    private TransactionClient transactionClient;
    @Autowired
    private NotificationClient notificationClient;

    @Transactional
    public TransactionDto addMoneyToWallet(AddMoneyRequestDto addMoneyRequestDto) {
        try {

            BankAccountDto bankAccount = bankClient.getBankAccount(addMoneyRequestDto.getPaymentMethodId());

            BankResponse bankResponse = bankClient.debitFromBank(
                    bankAccount.getId(),
                    addMoneyRequestDto.getAmount()
            );

            if (!"SUCCESS".equals(bankResponse.getStatus())) {
                throw new RuntimeException("Bank debit failed: " + bankResponse.getMessage());
            }

            CreditDto creditDto = new CreditDto();
            creditDto.setUserId(addMoneyRequestDto.getUserId());
            creditDto.setAmount(addMoneyRequestDto.getAmount());
            walletClient.credit(creditDto);

            AddMoneyRequest addMoneyRequest = new AddMoneyRequest();
            addMoneyRequest.setAmount(addMoneyRequestDto.getAmount());
            addMoneyRequest.setStatus("SUCCESS");
            addMoneyRequest.setMethodId(bankAccount.getId());
            addMoneyRequest.setMethodType(MethodType.BANK);
            addMoneyRequest.setUserId(addMoneyRequestDto.getUserId());
            AddMoneyRequest save = addMoneyReqRepository.save(addMoneyRequest);

            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setTransactionId("TXN"+UUID.randomUUID());
            transactionDto.setDate(save.getCreatedAt());
            transactionDto.setStatus(Status.SUCCESS);
            transactionDto.setMethodType(MethodType.BANK);
            return transactionDto;
        } catch (Exception e) {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setTransactionId("TXN"+UUID.randomUUID());
            transactionDto.setDate(new Timestamp(System.currentTimeMillis()));
            transactionDto.setStatus(Status.FAILED);
            transactionDto.setMethodType(MethodType.BANK);
            return transactionDto;
        }
    }

    @Transactional
    public QrPaymentResponseDto qrPayment(QrPaymentDto qrPaymentDto) throws JsonProcessingException {
        String payerWalletName = "";
        String payeeWalletName = "";
        try {
            UserDto payer = userClient.getUserById(qrPaymentDto.getPayerId());
            UserDto payee = userClient.getUserById(qrPaymentDto.getPayeeId());

            if (payer == null || payee == null) {
                throw new RuntimeException("Invalid payer or payee user ID");
            }

            WalletDto payerWallet = walletClient.getWalletByUserId(qrPaymentDto.getPayerId());
            WalletDto payeeWallet = walletClient.getWalletByUserId(qrPaymentDto.getPayeeId());

            payerWalletName = payerWallet.getWalletName();
            payeeWalletName = payeeWallet.getWalletName();

            if (payerWallet == null || payeeWallet == null) {
                throw new RuntimeException("Invalid payer or payee wallet ID");
            }

            if (payerWallet.getId().equals(payeeWallet.getId())) {
                throw new RuntimeException("Payer and payee wallets cannot be the same");
            }

            if (payerWallet.getBalance() < qrPaymentDto.getAmount()) {
                throw new RuntimeException("Insufficient wallet balance");
            }

            if (qrPaymentDto.getAmount() <= 0) {
                throw new RuntimeException("Invalid transaction amount");
            }

            DebitDto debitDto = new DebitDto();
            debitDto.setUserId(payer.getId());
            debitDto.setAmount(qrPaymentDto.getAmount());
            debitDto.setPin(qrPaymentDto.getPin());
            debitDto.setPaymentMethodId(qrPaymentDto.getMethodId());

            ResponseDto debitResponse = walletClient.debit(debitDto);
            if (!"SUCCESS".equalsIgnoreCase(debitResponse.getStatus())) {
                throw new RuntimeException("Debit failed: " + debitResponse.getMessage());
            }

            CreditDto creditDto = new CreditDto();
            creditDto.setUserId(payee.getId());
            creditDto.setAmount(qrPaymentDto.getAmount());
            ResponseDto creditResponse = walletClient.credit(creditDto);

            if (!"SUCCESS".equalsIgnoreCase(creditResponse.getStatus())) {
                CreditDto refundDto = new CreditDto();
                refundDto.setUserId(payer.getId());
                refundDto.setAmount(qrPaymentDto.getAmount());
                walletClient.credit(refundDto);
                throw new RuntimeException("Payment failed during credit. Amount refunded to payer.");
            }

            Payment payment = createPayment(payer.getId(), payee.getId(), payerWallet.getId(), payeeWallet.getId(),
                    qrPaymentDto.getAmount(), PaymentType.QR, qrPaymentDto.getMethodId(), MethodType.WALLET, Status.SUCCESS);

            createTransaction(qrPaymentDto, payerWalletName, payeeWalletName, payment, Status.SUCCESS, "Not Failed");
            createNotification(payer.getId(), "Payment Successful", "You paid ₹"+qrPaymentDto.getAmount()+ " to " + payeeWalletName,
                   NotificationType.TRANSACTION, NotificationStatus.UNREAD, NotificationPriority.MEDIUM, NotificationChannel.IN_APP);
            createNotification(payee.getId(), "Money Received", payerWalletName +" sent you ₹" + qrPaymentDto.getAmount(),
                    NotificationType.TRANSACTION, NotificationStatus.UNREAD, NotificationPriority.MEDIUM, NotificationChannel.IN_APP);

            QrPaymentResponseDto response = new QrPaymentResponseDto();
            response.setTxnId(payment.getTxnId());
            response.setPaymentTime(payment.getCreatedAt());
            response.setPaymentMethodType(payment.getMethodType());
            response.setStatus(payment.getStatus());
            return response;

        } catch (Exception e) {

            Payment payment = createPayment(qrPaymentDto.getPayerId(), qrPaymentDto.getPayeeId(),
                    qrPaymentDto.getPayerWalletId(), qrPaymentDto.getPayeeWalletId(), qrPaymentDto.getAmount()
                    , PaymentType.QR, qrPaymentDto.getMethodId(), MethodType.WALLET, Status.FAILED);

            createTransaction(qrPaymentDto, payerWalletName, payeeWalletName, payment, Status.FAILED, e.getMessage());
            createNotification(qrPaymentDto.getPayerId(), "Payment Failed", "You paid ₹"+qrPaymentDto.getAmount()+ " to " + payeeWalletName + " Failed",
                    NotificationType.TRANSACTION, NotificationStatus.UNREAD, NotificationPriority.HIGH, NotificationChannel.IN_APP);

            QrPaymentResponseDto response = new QrPaymentResponseDto();
            response.setTxnId(payment.getTxnId());
            response.setPaymentTime(payment.getCreatedAt());
            response.setPaymentMethodType(payment.getMethodType());
            response.setStatus(payment.getStatus());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    private Payment createPayment(Long payer, Long payee, Long payerWallet, Long payeeWallet,
                                  Double amount, PaymentType paymentType, Long methodId, MethodType methodType,
                                  Status status) {

        Payment txn = new Payment();
        txn.setPayerId(payer);
        txn.setPayeeId(payee);
        txn.setPayerWalletId(payerWallet);
        txn.setPayeeWalletId(payeeWallet);
        txn.setAmount(amount);
        txn.setStatus(status);
        txn.setCurrency("INR");
        txn.setPaymentType(paymentType);
        txn.setMethodId(methodId);
        txn.setMethodType(methodType);
        txn.setTxnId("TNX" + UUID.randomUUID());

        return paymentRepository.save(txn);
    }

    public void createTransaction(QrPaymentDto qrPaymentDto, String payerWalletName, String payeeWalletName,
                                  Payment payment, Status status, String failure){
        TransactionReqDto transactionReqDto = new TransactionReqDto();
        transactionReqDto.setFrom(payerWalletName);
        transactionReqDto.setTo(payeeWalletName);
        transactionReqDto.setAmount(qrPaymentDto.getAmount());
        transactionReqDto.setCurrency("INR");
        transactionReqDto.setDescription(qrPaymentDto.getDescription());
        transactionReqDto.setType(PaymentType.QR);
        transactionReqDto.setMethodType(MethodType.WALLET);
        transactionReqDto.setPayeeId(payment.getPayeeId());
        transactionReqDto.setPayerId(payment.getPayerId());
        transactionReqDto.setPayeeWalletId(payment.getPayeeWalletId());
        transactionReqDto.setPayerWalletId(payment.getPayerWalletId());
        transactionReqDto.setStatus(status);
        transactionReqDto.setFailureReason(failure);
        transactionProducer.sendTransaction(transactionReqDto);
//        transactionClient.createTransaction(transactionReqDto);
    }

    public void createNotification(Long userId, String tittle, String message, NotificationType type,
                    NotificationStatus status, NotificationPriority priority, NotificationChannel channel) throws JsonProcessingException {
        NotificationReqDto notificationReqDto = new NotificationReqDto();
        notificationReqDto.setUserId(userId);
        notificationReqDto.setTittle(tittle);
        notificationReqDto.setMessage(message);
        notificationReqDto.setChannel(channel);
        notificationReqDto.setPriority(priority);
        notificationReqDto.setType(type);
        notificationReqDto.setStatus(status);
        notificationProducer.sendPaymentEvent(notificationReqDto);
//        notificationClient.createNotification(notificationReqDto);
    }


}
