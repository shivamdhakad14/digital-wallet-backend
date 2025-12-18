package com.digital_wallet.split_bill_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "group_balances")
@Getter
@Setter
@ToString(exclude = "group")
@AllArgsConstructor
@NoArgsConstructor
public class GroupBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id",nullable = false)
    private Group group;

    @Column(nullable = false)
    private Long fromUser;
    private String fromUserName;

    @Column(nullable = false)
    private Long toUser;
    private String toUserName;

    @Column(nullable = false)
    private Double amount;

    private String status;
    private Boolean isReceived;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
