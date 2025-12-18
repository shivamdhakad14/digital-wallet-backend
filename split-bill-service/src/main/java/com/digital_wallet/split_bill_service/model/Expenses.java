package com.digital_wallet.split_bill_service.model;

import com.digital_wallet.split_bill_service.enums.SplitType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "expenses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false)
    private Long paidBy;
    private String paidByName;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double shareAmount;

    private String currency = "INR";
    private String category;
    private String title;

    @Column(nullable = false)
    private SplitType splitType;

    @Column(nullable = false)
    private String note;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    @OneToMany(mappedBy = "expenses",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseSplit> expenseSplits;
}
