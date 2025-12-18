package com.digital_wallet.transaction_service.model;

import com.digital_wallet.transaction_service.enums.PaymentType;
import com.digital_wallet.transaction_service.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table( name = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromUser;
    private String toUser;

    @Column(nullable = false)
    private Long payerWalletId;

    @Column(nullable = false)
    private Long payeeWalletId;

    @Column(nullable = false)
    private Double amount;
    private String currency = "INR";

    @Column(nullable = false)
    private PaymentType type;

    @Column(nullable = false)
    private Status status = Status.PENDING;

    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp initiatedAt;
    private String failureReason;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTransaction> userTransactionList;
}
