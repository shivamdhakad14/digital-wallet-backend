package com.digital_wallet.split_bill_service.model;

import com.digital_wallet.split_bill_service.enums.Method;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "settlements")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id",nullable = false)
    private Group group;

    @Column(nullable = false)
    private Long fromUser;

    @Column(nullable = false)
    private Long toUser;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Method method;

    private String upiId;
    private String note;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
}
