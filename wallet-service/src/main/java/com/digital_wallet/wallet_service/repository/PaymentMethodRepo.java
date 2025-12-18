package com.digital_wallet.wallet_service.repository;

import com.digital_wallet.wallet_service.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepo extends JpaRepository<PaymentMethod, Long> {

    List<PaymentMethod> findAllByUserId(Long userId);
}
