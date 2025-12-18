package com.digital_wallet.split_bill_service.repository;

import com.digital_wallet.split_bill_service.model.ExpenseSplit;
import com.digital_wallet.split_bill_service.model.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, Long> {

}
