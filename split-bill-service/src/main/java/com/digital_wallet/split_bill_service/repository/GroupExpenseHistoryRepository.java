package com.digital_wallet.split_bill_service.repository;

import com.digital_wallet.split_bill_service.model.GroupExpenseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupExpenseHistoryRepository extends JpaRepository<GroupExpenseHistory, Long> {

    Optional<GroupExpenseHistory> findByGroupId(Long groupId);
}
