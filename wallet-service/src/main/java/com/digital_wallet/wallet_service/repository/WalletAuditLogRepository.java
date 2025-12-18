package com.digital_wallet.wallet_service.repository;

import com.digital_wallet.wallet_service.model.Transaction;
import com.digital_wallet.wallet_service.model.WalletAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletAuditLogRepository extends JpaRepository<WalletAuditLog, Long> {

    List<WalletAuditLog> findAllByWalletId(Long walletId);
}
