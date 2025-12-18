package com.digital_wallet.split_bill_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "group_expense_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupExpenseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false)
    private Double totalExpense;

    @Column(nullable = false)
    private Double perShareAmount;

    private Boolean isSettled= false;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
    private Timestamp deletedAt;
}
