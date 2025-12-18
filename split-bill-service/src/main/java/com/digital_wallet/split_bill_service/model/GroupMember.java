package com.digital_wallet.split_bill_service.model;

import com.digital_wallet.split_bill_service.enums.JoinMethod;
import com.digital_wallet.split_bill_service.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "group_members")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id",nullable = false)
    @JsonIgnore
    private Group group;

    @Column(nullable = false)
    private Long userId;

    private String memberName;

    @Column(nullable = false)
    private Boolean isGroupAdmin = false;

    private JoinMethod joinMethod = JoinMethod.MANUAL;
    private Status status = Status.ACTIVE;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
    private Timestamp deletedAt;
}
