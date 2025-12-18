package com.digital_wallet.payment_service.model;

import com.digital_wallet.payment_service.enums.MethodType;
import com.digital_wallet.payment_service.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "add_money_requests")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMoneyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private MethodType methodType;

    @Column(nullable = false)
    private Long methodId;

    @Column(nullable = false)
    private String status = "PENDING";

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
}
