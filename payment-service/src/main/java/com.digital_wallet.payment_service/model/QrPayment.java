package com.digital_wallet.payment_service.model;

import com.digital_wallet.payment_service.enums.QrStatus;
import com.digital_wallet.payment_service.enums.QrType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.w3c.dom.Text;

import java.sql.Timestamp;

@Entity
@Table(name = "qr_payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long walletId;

    @Column(nullable = false)
    private QrType qrType = QrType.STATIC;

    @Column(nullable = false, length = 1500)
    private String encodedData;

    private String upiId;
    private Double amount;

    private String currency = "INR";
    private String notes;

    private Boolean isScanned = false;

    @Column(nullable = false)
    private QrStatus qrStatus = QrStatus.ACTIVE;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
