package com.financetracker.controller;

import com.financetracker.model.Transaction;
import com.financetracker.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;

public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }

        this.service = service;
    }

    public void addTransaction(Transaction transaction) {
        service.addTransaction(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return service.getAllTransactions();
    }

    public boolean removeTransaction(int transactionID) {
        return service.removeTransaction(transactionID);
    }

    public BigDecimal getTotalIncome() {
        return service.calculateTotalIncome();
    }

    public BigDecimal getTotalExpenses() {
        return service.calculateTotalExpenses();
    }

    public BigDecimal getNetBalance() {
        return service.calculateNetBalance();
    }
}

