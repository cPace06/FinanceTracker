package com.financetracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private final int transactionID;
    private final BigDecimal amount;
    private final String category;
    private final String description;
    private final LocalDate date;
    private TransactionType type;

    public Transaction(int transactionID, BigDecimal amount,
                       String category, String description,
                       LocalDate date, TransactionType type) {

        // Validate transaction ID
        if (transactionID <= 0) {
            throw new IllegalArgumentException("Transaction ID must be greater than 0");
        }

        // Validate amount
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than 0");
        }

        // Validate category
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }

        // Validate date
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        // Validate transaction type
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }

        // Initialize fields
        this.transactionID = transactionID;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format(
                "Transaction ID: %d%n" +
                        "Amount: $%.2f%n" +
                        "Category: %s%n" +
                        "Description: %s%n" +
                        "Date: %s%n" +
                        "Type: %s",
                transactionID,
                amount,
                category,
                description,
                date,
                type
        );
    }

}
