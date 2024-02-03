package com.payMyBuddy.app.model;

import com.payMyBuddy.app.util.MoneyHolder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "bank")
public class Bank implements MoneyHolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "emitterBankId")
    @ToString.Exclude
    private List<Transaction> emitterBankListOperation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiverBankId")
    @ToString.Exclude
    private List<Transaction> receiverBankListOperation;

    @Column(name = "balance", nullable = false)
    private double balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String getCode() {
        return null;
    }
}
