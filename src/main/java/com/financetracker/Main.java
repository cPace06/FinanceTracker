package com.financetracker;

import com.financetracker.model.Transaction;
import com.financetracker.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        Transaction t1 = new Transaction(
                1,
                new BigDecimal("50.00"),
                "Gas",
                "Filled up my car",
                LocalDate.now(),
                TransactionType.EXPENSE
        );

        System.out.println(t1);
    }
}
