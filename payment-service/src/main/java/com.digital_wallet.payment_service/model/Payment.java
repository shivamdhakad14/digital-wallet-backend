package com.digital_wallet.payment_service.model;

import com.digital_wallet.payment_service.enums.MethodType;
import com.digital_wallet.payment_service.enums.PaymentType;
import com.digital_wallet.payment_service.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String txnId;

    @Column(nullable = false)
    private Long payerId;

    @Column(nullable = false)
    private Long payeeId;

    private Long payerWalletId;
    private Long payeeWalletId;

    @Column(nullable = false)
    private Double amount;

    private String currency = "INR";

    @Column(nullable = false)
    private PaymentType paymentType;

    @Column(nullable = false)
    private MethodType methodType;

    private Long methodId;
    private Status status = Status.INITIATED;
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private PaymentMetadata paymentMetadata;
}
