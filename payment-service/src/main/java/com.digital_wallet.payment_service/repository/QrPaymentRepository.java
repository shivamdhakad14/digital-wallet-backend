package com.digital_wallet.payment_service.repository;

import com.digital_wallet.payment_service.model.QrPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QrPaymentRepository extends JpaRepository<QrPayment, Long> {

    List<QrPayment> findByWalletId(Long walletId);
}
