package com.payMyBuddy.app.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emitter_user_id", updatable = false)
    private User emitterUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_user_id", updatable = false)
    private User receiverUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverBankId", updatable = false)
    private Bank receiverBankId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emitterBankId", updatable = false)
    private Bank emitterBankId;

    @CreationTimestamp
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime at;

    @Column(name ="description")
    private String description;

    @Column(name = "amount", nullable = false, updatable = false)
    private double amount;

}
