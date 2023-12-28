package com.kiranaRegister.transactions.dto;

import java.time.LocalDateTime;

public class TransactionRequest {
    private Double amount;
    private String currency;
    private String transactionType;

    private LocalDateTime transactionDate;

    public Double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
