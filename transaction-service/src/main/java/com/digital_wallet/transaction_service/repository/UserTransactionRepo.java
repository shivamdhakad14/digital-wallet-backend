package com.digital_wallet.transaction_service.repository;

import com.digital_wallet.transaction_service.model.Transaction;
import com.digital_wallet.transaction_service.model.UserTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTransactionRepo extends JpaRepository<UserTransaction, Long> {
    Page<UserTransaction> findAllByUserId(Long userId, Pageable pageable);
}
