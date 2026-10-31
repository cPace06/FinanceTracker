package com.financetracker.test;

import com.financetracker.model.Transaction;
import com.financetracker.model.TransactionType;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void validTransactionShouldBeCreated() {
        Transaction transaction = new Transaction(
                1,
                new BigDecimal("50.00"),
                "Gas",
                "Filled up my car",
                LocalDate.of(2026, 10, 31),
                TransactionType.EXPENSE
        );

        assertEquals(1, transaction.getTransactionID());
        assertEquals(new BigDecimal("50.00"), transaction.getAmount());
        assertEquals("Gas", transaction.getCategory());
        assertEquals("Filled up my car", transaction.getDescription());
        assertEquals(LocalDate.of(2026, 10, 31), transaction.getDate());
        assertEquals(TransactionType.EXPENSE, transaction.getType());
    }

    @Test
    void zeroAmountShouldBeRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(1, BigDecimal.ZERO, "Gas",
                        "Gas purchase", LocalDate.now(),
                        TransactionType.EXPENSE)
        );
    }

    @Test
    void negativeAmountShouldBeRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(1, new BigDecimal("-25.00"), "Gas",
                        "Gas purchase", LocalDate.now(),
                        TransactionType.EXPENSE)
        );
    }

    @Test
    void nullAmountShouldBeRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(1, null, "Gas",
                        "Gas purchase", LocalDate.now(),
                        TransactionType.EXPENSE)
        );
    }

    @Test
    void blankCategoryShouldBeRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(1, new BigDecimal("25.00"), "   ",
                        "Gas purchase", LocalDate.now(),
                        TransactionType.EXPENSE)
        );
    }

    @Test
    void nullCategoryShouldBeRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(1, new BigDecimal("25.00"), null,
                        "Gas purchase", LocalDate.now(),
                        TransactionType.EXPENSE)
        );
    }

    @Test
    void nullDateShouldBeRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(1, new BigDecimal("25.00"), "Gas",
                        "Gas purchase", null,
                        TransactionType.EXPENSE)
        );
    }

    @Test
    void nullTypeShouldBeRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(1, new BigDecimal("25.00"), "Gas",
                        "Gas purchase", LocalDate.now(),
                        null)
        );
    }

    @Test
    void zeroTransactionIDShouldBeRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(0, new BigDecimal("25.00"), "Gas",
                        "Gas purchase", LocalDate.now(),
                        TransactionType.EXPENSE)
        );
    }

    @Test
    void negativeTransactionIDShouldBeRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(-1, new BigDecimal("25.00"), "Gas",
                        "Gas purchase", LocalDate.now(),
                        TransactionType.EXPENSE)
        );
    }

    @Test
    void incomeTransactionShouldBeCreated() {
        Transaction transaction = new Transaction(
                2,
                new BigDecimal("500.00"),
                "Salary",
                "Paycheck",
                LocalDate.now(),
                TransactionType.INCOME
        );

        assertEquals(TransactionType.INCOME, transaction.getType());
        assertEquals(new BigDecimal("500.00"), transaction.getAmount());
    }

    @Test
    void toStringShouldContainTransactionDetails() {
        Transaction transaction = new Transaction(
                1,
                new BigDecimal("50.00"),
                "Gas",
                "Filled up my car",
                LocalDate.of(2026, 10, 31),
                TransactionType.EXPENSE
        );

        String result = transaction.toString();

        assertTrue(result.contains("50.00"));
        assertTrue(result.contains("Gas"));
        assertTrue(result.contains("Filled up my car"));
        assertTrue(result.contains("2026-10-31"));
        assertTrue(result.contains("EXPENSE"));
    }
}
