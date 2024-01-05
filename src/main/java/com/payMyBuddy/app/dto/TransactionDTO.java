package com.payMyBuddy.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class TransactionDTO {
        private String name;

        private double amount;

        private String description;

        public TransactionDTO(String moneyHolderName, double amount, String description) {
            this.name = moneyHolderName;
            this.amount = amount;
            this.description = description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TransactionDTO that = (TransactionDTO) o;
            return amount == that.amount &&
                    Objects.equals(name, that.name) &&
                    Objects.equals(description, that.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, amount, description);
        }

        @Override
        public String toString() {
            return "TransferRequest{" +
                    "moneyHolderName='" + name + '\'' +
                    ", amount=" + amount +
                    ", description='" + description + '\'' +
                    '}';
        }
}
