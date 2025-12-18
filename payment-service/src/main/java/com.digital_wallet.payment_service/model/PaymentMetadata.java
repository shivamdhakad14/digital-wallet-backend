package com.digital_wallet.payment_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "payment_metadata")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private String qrCode;
    private String upiId;
    private String phoneNumber;
    private String splitGroupId;
    private String note;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
}
