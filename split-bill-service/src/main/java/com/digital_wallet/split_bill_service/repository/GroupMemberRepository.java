package com.digital_wallet.split_bill_service.repository;

import com.digital_wallet.split_bill_service.model.Group;
import com.digital_wallet.split_bill_service.model.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

//    Boolean existsByUserIdAndGroupId(Long userId, Long groupId);
      List<GroupMember> findAllByGroupId(Long groupId);
//    List<GroupMember> findAllByUserIdAndDeletedAtISNull(Long userId);

}
