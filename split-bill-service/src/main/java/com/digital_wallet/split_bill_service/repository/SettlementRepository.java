package com.digital_wallet.split_bill_service.repository;

import com.digital_wallet.split_bill_service.model.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {

}
