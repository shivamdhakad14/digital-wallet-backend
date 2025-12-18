package com.digital_wallet.split_bill_service.repository;

import com.digital_wallet.split_bill_service.model.ExpenseSplit;
import com.digital_wallet.split_bill_service.model.GroupBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupBalanceRepository extends JpaRepository<GroupBalance, Long> {

    List<GroupBalance> findByFromUser(Long fromUser);

    List<GroupBalance> findByToUser(Long toUser);

    List<GroupBalance> findByGroup_Id(Long groupId);

    Optional<GroupBalance> findByGroup_IdAndFromUserAndToUser(Long groupId, Long fromUser, Long toUser);
}
