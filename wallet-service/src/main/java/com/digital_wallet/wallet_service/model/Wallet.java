package com.digital_wallet.wallet_service.model;

import com.digital_wallet.wallet_service.enums.WalletStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "wallets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String walletName;

    @Column(nullable = false)
    private Double balance = 0.00;

    @Column(nullable = false)
    private String currency = "INR";

    @Column(nullable = false)
    private String pin;

    private Boolean isPinSet = false;

    @Enumerated(EnumType.STRING)
    private WalletStatus status = WalletStatus.ACTIVE;

    private Integer failedAttempt = 0;

    private String mobileNumber;

    private String upiId;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WalletAuditLog> walletAuditLogs;
}

