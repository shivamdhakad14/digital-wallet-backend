package com.digital_wallet.transaction_service.model;

import com.digital_wallet.transaction_service.enums.Direction;
import com.digital_wallet.transaction_service.enums.MethodType;
import com.digital_wallet.transaction_service.enums.PaymentType;
import com.digital_wallet.transaction_service.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table( name = "user_transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromUser;
    private String toUser;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long walletId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(nullable = false)
    private Direction direction;

    @Column(nullable = false)
    private PaymentType paymentType;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private MethodType methodType;

    @Column(nullable = false)
    private Double amount;
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
}
