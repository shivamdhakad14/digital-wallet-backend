package com.digital_wallet.payment_service.repository;

import com.digital_wallet.payment_service.model.Payment;
import com.digital_wallet.payment_service.model.PaymentMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMetadataRepository extends JpaRepository<PaymentMetadata, Long> {


}
