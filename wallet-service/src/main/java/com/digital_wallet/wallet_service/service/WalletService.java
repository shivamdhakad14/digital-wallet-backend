package com.digital_wallet.wallet_service.service;

import com.digital_wallet.wallet_service.client.BankClient;
import com.digital_wallet.wallet_service.client.UserClient;
import com.digital_wallet.wallet_service.dto.*;
import com.digital_wallet.wallet_service.enums.PaymentMethodType;
import com.digital_wallet.wallet_service.enums.TransactionStatus;
import com.digital_wallet.wallet_service.enums.TransactionType;
import com.digital_wallet.wallet_service.enums.WalletStatus;
import com.digital_wallet.wallet_service.model.PaymentMethod;
import com.digital_wallet.wallet_service.model.Transaction;
import com.digital_wallet.wallet_service.model.Wallet;
import com.digital_wallet.wallet_service.repository.PaymentMethodRepo;
import com.digital_wallet.wallet_service.repository.TransactionRepository;
import com.digital_wallet.wallet_service.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserClient userClient;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BankClient bankClient;
    @Autowired
    private PaymentMethodRepo paymentMethodRepo;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private PaymentMethodService paymentMethodService;

    @Transactional
    public String createWallet(WalletRequestDto walletRequestDto) {
        if (walletRepository.existsByUserId(walletRequestDto.getUserId())) {
            throw new RuntimeException("Wallet already exists for this user");
        }
        UserDto user = userClient.getUserById(walletRequestDto.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + walletRequestDto.getUserId());
        }
        String pin = walletRequestDto.getPin();
        if (pin == null || pin.length() != 6) {
            throw new RuntimeException("Please enter a 6-digit wallet PIN");
        }

        Wallet wallet = new Wallet();
        wallet.setUserId(user.getId());
        wallet.setWalletName(walletRequestDto.getWalletName());
        wallet.setPin(passwordEncoder.encode(pin));
        wallet.setIsPinSet(true);
        wallet.setMobileNumber(user.getMobileNumber());
        wallet.setUpiId(user.getMobileNumber()+"@payflow");
        wallet.setBalance(0.00);
        wallet.setCurrency("INR");
        wallet.setStatus(WalletStatus.ACTIVE);
        wallet.setFailedAttempt(0);
        Wallet save = walletRepository.save(wallet);

        userClient.markWalletCreated(save.getUserId());

        PaymentMethodDto paymentMethodDto = new PaymentMethodDto();
        paymentMethodDto.setUserId(save.getUserId());
        paymentMethodDto.setMethodType(PaymentMethodType.WALLET);
        paymentMethodDto.setDetailMasked(save.getUpiId());
        paymentMethodDto.setAccountId(save.getId());
        paymentMethodDto.setIsDefault(true);

        paymentMethodService.createPaymentMethod(paymentMethodDto);
        return "Wallet created successfully for : " + user.getName();
    }

    public WalletDto getWalletByUserId(Long userId){
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("wallet not found"));
        WalletDto walletDto = new WalletDto();
        walletDto.setId(wallet.getId());
        walletDto.setBalance(wallet.getBalance());
        walletDto.setCurrency(wallet.getCurrency());
        walletDto.setWalletName(wallet.getWalletName());
        walletDto.setStatus(wallet.getStatus());
        walletDto.setUpiId(wallet.getUpiId());
        walletDto.setIsPinSet(wallet.getIsPinSet());
        return walletDto;
    }

    public WalletDto getWalletByMobile(String mobileNUmber){
        Wallet wallet = walletRepository.findByMobileNumber(mobileNUmber)
                .orElseThrow(() -> new RuntimeException("wallet not found"));
        WalletDto walletDto = new WalletDto();
        walletDto.setId(wallet.getId());
        walletDto.setBalance(wallet.getBalance());
        walletDto.setCurrency(wallet.getCurrency());
        walletDto.setWalletName(wallet.getWalletName());
        walletDto.setStatus(wallet.getStatus());
        walletDto.setUpiId(wallet.getUpiId());
        walletDto.setMobileNumber(wallet.getMobileNumber());
        walletDto.setIsPinSet(wallet.getIsPinSet());
        return walletDto;
    }

    @Transactional
    public String changeWalletPin(Long walletId, String oldPin, String newPin) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (!Boolean.TRUE.equals(wallet.getIsPinSet())) {
            throw new RuntimeException("PIN is not set for this wallet. Please create a new one first.");
        }

        if (!passwordEncoder.matches(oldPin, wallet.getPin())) {
            throw new RuntimeException("Old PIN does not match");
        }

        wallet.setPin(passwordEncoder.encode(newPin));
        wallet.setIsPinSet(true);
        wallet.setFailedAttempt(0);
        walletRepository.save(wallet);
        return "Wallet PIN changed successfully";
    }

    public Boolean verifyWalletPin(Long walletId, String pin){
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        return passwordEncoder.matches(pin, wallet.getPin());
    }

    @Transactional
    public ResponseDto credit(CreditDto creditDto){
        Wallet wallet = walletRepository.findByUserId(creditDto.getUserId())
                .orElseThrow(() -> new RuntimeException("wallet not found"));

        ResponseDto responseDto = new ResponseDto();
        if (!wallet.getStatus().equals(WalletStatus.ACTIVE)){
            responseDto.setStatus("FAILED");
            responseDto.setMessage("Wallet is not active");
            return responseDto;
        }

        wallet.setBalance(wallet.getBalance() + creditDto.getAmount());
        walletRepository.save(wallet);

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(creditDto.getAmount());
        transaction.setTxnStatus(TransactionStatus.SUCCESS);
        transaction.setTxnType(TransactionType.CREDIT);
        transaction.setRefrenceId("Txn"+ UUID.randomUUID());
        transactionRepository.save(transaction);

        responseDto.setStatus("SUCCESS");
        responseDto.setMessage("Amount Credit successfully");
        return responseDto;
    }

    @Transactional
    public ResponseDto debit(DebitDto debitDto){
        PaymentMethod paymentMethod = paymentMethodRepo.findById(debitDto.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("payment method not found"));
        Wallet wallet = walletRepository.findByUserId(paymentMethod.getUserId())
                .orElseThrow(() -> new RuntimeException("wallet not found : " + paymentMethod.getUserId()));
        try {
            ResponseDto responseDto = new ResponseDto();
            if (!wallet.getStatus().equals(WalletStatus.ACTIVE)) {
                responseDto.setStatus("FAILED");
                responseDto.setMessage("Wallet is not active");
                return responseDto;
            }

            if (!passwordEncoder.matches(debitDto.getPin(), wallet.getPin())) {
                responseDto.setStatus("FAILED");
                responseDto.setMessage("Invalid PIN");
                return responseDto;
            }

            if (wallet.getBalance() < debitDto.getAmount()) {
                responseDto.setStatus("FAILED");
                responseDto.setMessage("Insufficient balance");
                return responseDto;
            }

            wallet.setBalance(wallet.getBalance() - debitDto.getAmount());
            walletRepository.save(wallet);

            Transaction transaction = new Transaction();
            transaction.setWallet(wallet);
            transaction.setAmount(debitDto.getAmount());
            transaction.setTxnStatus(TransactionStatus.SUCCESS);
            transaction.setTxnType(TransactionType.DEBIT);
            transaction.setRefrenceId("Txn" + UUID.randomUUID());
            transactionRepository.save(transaction);

            responseDto.setStatus("SUCCESS");
            responseDto.setMessage("Amount debit successfully");
            return responseDto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PaymentMethodDto getPaymentMethodById(Long id){
        PaymentMethod paymentMethod = paymentMethodRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("method not found"));

            PaymentMethodDto paymentMethodDto = new PaymentMethodDto();
            paymentMethodDto.setId(paymentMethod.getId());
            paymentMethodDto.setMethodType(paymentMethod.getMethodType());
            paymentMethodDto.setAccountId(paymentMethod.getAccountId());
            paymentMethodDto.setDetailMasked(paymentMethod.getDetailMasked());
            paymentMethodDto.setIsDefault(paymentMethod.getIsDefault());
            return paymentMethodDto;
    }

    public List<PaymentMethodDto> getAllPaymentMethod(Long userId){
        List<PaymentMethod> paymentMethods = paymentMethodRepo.findAllByUserId(userId);
        return paymentMethods.stream().map(paymentMethod -> {
            PaymentMethodDto paymentMethodDto = new PaymentMethodDto();
            paymentMethodDto.setId(paymentMethod.getId());
            paymentMethodDto.setMethodType(paymentMethod.getMethodType());
            paymentMethodDto.setAccountId(paymentMethod.getAccountId());
            paymentMethodDto.setDetailMasked(paymentMethod.getDetailMasked());
            paymentMethodDto.setIsDefault(paymentMethod.getIsDefault());
            return paymentMethodDto;
        }).collect(Collectors.toList());
    }


}
