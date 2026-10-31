
package com.financetracker.persistence;

import com.financetracker.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    private final ArrayList<Transaction> transactions;

    public TransactionRepository() {
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {

        // Validate transaction
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }

        // Prevent duplicate transaction IDs
        if (getTransactionByID(transaction.getTransactionID()) != null) {
            throw new IllegalArgumentException("Transaction ID already exists");
        }

        transactions.add(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    public Transaction getTransactionByID(int transactionID) {

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionID() == transactionID) {
                return transaction;
            }
        }

        return null;
    }

    public boolean removeTransaction(int transactionID) {
        return transactions.removeIf(
                transaction -> transaction.getTransactionID() == transactionID
        );
    }
}

