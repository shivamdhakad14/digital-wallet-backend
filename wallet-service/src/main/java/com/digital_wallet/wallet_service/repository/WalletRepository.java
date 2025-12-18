package com.digital_wallet.wallet_service.repository;

import com.digital_wallet.wallet_service.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserId(Long userId);
    Boolean existsByUserId(Long userId);
    Optional<Wallet> findByMobileNumber(String mobileNumber);
}
