package com.payMyBuddy.app.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BankDTO {
    private String name;
    private String address;
    private double balance;
    private Long userId;

}