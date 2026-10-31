package com.financetracker.service;

import com.financetracker.model.Transaction;
import com.financetracker.model.TransactionType;
import com.financetracker.persistence.TransactionRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }

        this.repository = repository;
    }

    public void addTransaction(Transaction transaction) {
        repository.addTransaction(transaction);
    }

    public List<Transaction> getAllTransactions() {
       return repository.getAllTransactions();
    }

    public boolean removeTransaction(int transactionID) {
        return repository.removeTransaction(transactionID);
    }

    public BigDecimal calculateTotalIncome() {
        BigDecimal total = BigDecimal.ZERO;

        for (Transaction transaction : repository.getAllTransactions()) {
            if (transaction.getType() == TransactionType.INCOME) {
                total = total.add(transaction.getAmount());
            }
        }

        return total;
    }

    public BigDecimal calculateTotalExpenses() {
        BigDecimal total = BigDecimal.ZERO;

        for (Transaction transaction : repository.getAllTransactions()) {
            if (transaction.getType() == TransactionType.EXPENSE) {
                total = total.add(transaction.getAmount());
            }
        }

        return total;
    }

    public BigDecimal calculateNetBalance() {
        return calculateTotalIncome().subtract(calculateTotalExpenses());
    }
}