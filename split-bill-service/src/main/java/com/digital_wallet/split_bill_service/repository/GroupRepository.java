package com.digital_wallet.split_bill_service.repository;

import com.digital_wallet.split_bill_service.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findAllByGroupMembers_UserIdAndDeletedAtIsNull(Long userId);
}
