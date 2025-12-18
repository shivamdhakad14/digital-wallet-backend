package com.digital_wallet.wallet_service.model;

import com.digital_wallet.wallet_service.enums.PaymentMethodType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "payment_methods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethodType methodType;

    @Column(unique = true)
    private Long accountId;

    private String provider;
    private String detailMasked;
    private Boolean isDefault = false;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
}
