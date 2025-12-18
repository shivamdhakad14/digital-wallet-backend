package com.digital_wallet.wallet_service.model;

import com.digital_wallet.wallet_service.enums.TransactionStatus;
import com.digital_wallet.wallet_service.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    private TransactionType txnType;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus txnStatus = TransactionStatus.PENDING;

    private String refrenceId;
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
}
