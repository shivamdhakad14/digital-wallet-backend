package com.digital_wallet.transaction_service.service;

import com.digital_wallet.transaction_service.dto.TransactionDto;
import com.digital_wallet.transaction_service.dto.TransactionReqDto;
import com.digital_wallet.transaction_service.enums.Direction;
import com.digital_wallet.transaction_service.enums.MethodType;
import com.digital_wallet.transaction_service.enums.Status;
import com.digital_wallet.transaction_service.model.Transaction;
import com.digital_wallet.transaction_service.model.UserTransaction;
import com.digital_wallet.transaction_service.repository.TransactionRepo;
import com.digital_wallet.transaction_service.repository.UserTransactionRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private UserTransactionRepo userTransactionRepo;

    @Transactional
    public void createTransaction(TransactionReqDto dto) {
        if (dto.getPayerId().equals(dto.getPayeeId())) {
            throw new IllegalArgumentException("Payer and payee cannot be the same user");
        }

        if (dto.getPayerWalletId().equals(dto.getPayeeWalletId())) {
            throw new IllegalArgumentException("PayerWalletId and PayeeWalletId cannot be the same");
        }

        Transaction transaction = new Transaction();
        transaction.setFromUser(dto.getFrom());
        transaction.setToUser(dto.getTo());
        transaction.setDescription(dto.getDescription());
        transaction.setAmount(dto.getAmount());
        transaction.setCurrency(dto.getCurrency());
        transaction.setType(dto.getType());
        transaction.setStatus(dto.getStatus());
        transaction.setPayeeWalletId(dto.getPayeeWalletId());
        transaction.setPayerWalletId(dto.getPayerWalletId());

//        if (dto.getStatus() == Status.FAILED && dto.getFailureReason() != null) {
//            transaction.setFailureReason(dto.getFailureReason());
//        }

        Transaction savedTransaction = transactionRepo.save(transaction);

        createUserTransaction(savedTransaction, dto.getFrom(), dto.getTo(), dto.getPayerId(),
                dto.getPayerWalletId(), Direction.DEBIT, dto.getMethodType()
        );

        if (dto.getStatus() == Status.SUCCESS) {
            createUserTransaction(savedTransaction, dto.getFrom(), dto.getTo(), dto.getPayeeId(),
                    dto.getPayeeWalletId(), Direction.CREDIT, dto.getMethodType()
            );
        }
    }

    private void createUserTransaction(Transaction transaction, String from, String to, Long userId,
                                       Long walletId, Direction direction, MethodType methodType) {

        UserTransaction userTx = new UserTransaction();
        userTx.setTransaction(transaction);
        userTx.setFromUser(from);
        userTx.setToUser(to);
        userTx.setDescription(transaction.getDescription());
        userTx.setAmount(transaction.getAmount());
        userTx.setStatus(transaction.getStatus());
        userTx.setDirection(direction);
        userTx.setUserId(userId);
        userTx.setPaymentType(transaction.getType());
        userTx.setMethodType(methodType);
        userTx.setWalletId(walletId);

        userTransactionRepo.save(userTx);
    }

    public List<TransactionDto> getAllTransactionByUserId(Long userId){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<UserTransaction> transactions = userTransactionRepo.findAllByUserId(userId, pageable);
        return transactions.stream().map(transaction -> {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setId(transaction.getId());
            transactionDto.setAmount(transaction.getAmount());
            transactionDto.setStatus(transaction.getStatus());
            LocalDateTime dateTime = transaction.getCreatedAt().toLocalDateTime();
            String date = dateTime.toLocalDate().toString();
            String time = dateTime.toLocalTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            transactionDto.setDate(date);
            transactionDto.setTime(time);
            transactionDto.setDirection(transaction.getDirection());
            if (transaction.getDirection().equals(Direction.CREDIT)){
                transactionDto.setFrom(transaction.getFromUser());
            }
            if (transaction.getDirection().equals(Direction.DEBIT)){
                transactionDto.setTo(transaction.getToUser());
            }
            transactionDto.setMethod(transaction.getMethodType());
            transactionDto.setType(transaction.getPaymentType());

            return transactionDto;
        }).collect(Collectors.toList());
    }


}
