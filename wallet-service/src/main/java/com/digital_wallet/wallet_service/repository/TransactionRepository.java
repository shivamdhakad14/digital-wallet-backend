package com.digital_wallet.wallet_service.repository;

import com.digital_wallet.wallet_service.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByWalletId(Long walletId);
}
