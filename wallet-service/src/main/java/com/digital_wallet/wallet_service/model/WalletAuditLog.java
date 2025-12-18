package com.digital_wallet.wallet_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "wallet_audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(nullable = false)
    private String action;

    private Double oldValue;
    private Double newValue;
    private String performedBy;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp performedAt;
}
