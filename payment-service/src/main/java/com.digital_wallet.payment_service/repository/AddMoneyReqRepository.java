package com.digital_wallet.payment_service.repository;

import com.digital_wallet.payment_service.model.AddMoneyRequest;
import com.digital_wallet.payment_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddMoneyReqRepository extends JpaRepository<AddMoneyRequest, Long> {


}
