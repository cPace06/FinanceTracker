
package com.financetracker.test;

import com.financetracker.controller.TransactionController;
import com.financetracker.model.Transaction;
import com.financetracker.model.TransactionType;
import com.financetracker.persistence.TransactionRepository;
import com.financetracker.service.TransactionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionControllerTest {

    private TransactionRepository repository;
    private TransactionService service;
    private TransactionController controller;

    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;

    @BeforeEach
    void setUp() {

        repository = new TransactionRepository();
        service = new TransactionService(repository);
        controller = new TransactionController(service);

        // Expense: Gas
        transaction1 = new Transaction(
                1,
                new BigDecimal("50.00"),
                "Gas",
                "Filled up my car",
                LocalDate.of(2026, 10, 31),
                TransactionType.EXPENSE
        );

        // Income: Paycheck
        transaction2 = new Transaction(
                2,
                new BigDecimal("500.00"),
                "Salary",
                "Paycheck",
                LocalDate.of(2026, 10, 31),
                TransactionType.INCOME
        );

        // Expense: Food
        transaction3 = new Transaction(
                3,
                new BigDecimal("25.00"),
                "Food",
                "Lunch",
                LocalDate.of(2026, 10, 31),
                TransactionType.EXPENSE
        );
    }

    @Test
    void nullServiceShouldBeRejected() {

        assertThrows(IllegalArgumentException.class, () ->
                new TransactionController(null)
        );
    }

    @Test
    void addTransactionShouldStoreTransaction() {

        // Act
        controller.addTransaction(transaction1);

        // Assert
        assertEquals(1, controller.getAllTransactions().size());
        assertSame(transaction1, repository.getTransactionByID(1));
    }

    @Test
    void getAllTransactionsShouldReturnStoredTransactions() {

        // Arrange
        controller.addTransaction(transaction1);
        controller.addTransaction(transaction2);

        // Act
        List<Transaction> transactions = controller.getAllTransactions();

        // Assert
        assertEquals(2, transactions.size());
        assertTrue(transactions.contains(transaction1));
        assertTrue(transactions.contains(transaction2));
    }

    @Test
    void getAllTransactionsShouldReturnEmptyList() {

        // Act
        List<Transaction> transactions = controller.getAllTransactions();

        // Assert
        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
    }

    @Test
    void duplicateTransactionIDShouldBeRejected() {

        // Arrange
        controller.addTransaction(transaction1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.addTransaction(transaction1)
        );

        assertEquals(1, controller.getAllTransactions().size());
    }

    @Test
    void nullTransactionShouldBeRejected() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.addTransaction(null)
        );

        assertTrue(controller.getAllTransactions().isEmpty());
    }

    @Test
    void removeTransactionShouldRemoveExistingTransaction() {

        // Arrange
        controller.addTransaction(transaction1);
        controller.addTransaction(transaction2);

        // Act
        boolean result = controller.removeTransaction(1);

        // Assert
        assertTrue(result);
        assertNull(repository.getTransactionByID(1));
        assertEquals(1, controller.getAllTransactions().size());
        assertSame(transaction2, repository.getTransactionByID(2));
    }

    @Test
    void removeTransactionShouldReturnFalseWhenNotFound() {

        // Arrange
        controller.addTransaction(transaction1);

        // Act
        boolean result = controller.removeTransaction(99);

        // Assert
        assertFalse(result);
        assertEquals(1, controller.getAllTransactions().size());
    }

    @Test
    void getTotalIncomeShouldReturnCorrectAmount() {

        // Arrange
        controller.addTransaction(transaction1);
        controller.addTransaction(transaction2);
        controller.addTransaction(transaction3);

        // Act
        BigDecimal income = controller.getTotalIncome();

        // Assert
        assertEquals(0, new BigDecimal("500.00").compareTo(income));
    }

    @Test
    void getTotalExpensesShouldReturnCorrectAmount() {

        // Arrange
        controller.addTransaction(transaction1);
        controller.addTransaction(transaction2);
        controller.addTransaction(transaction3);

        // Act
        BigDecimal expenses = controller.getTotalExpenses();

        // Assert
        assertEquals(0, new BigDecimal("75.00").compareTo(expenses));
    }

    @Test
    void getNetBalanceShouldReturnCorrectBalance() {

        // Arrange
        controller.addTransaction(transaction1);
        controller.addTransaction(transaction2);
        controller.addTransaction(transaction3);

        // Act
        BigDecimal balance = controller.getNetBalance();

        // Assert
        assertEquals(0, new BigDecimal("425.00").compareTo(balance));
    }

    @Test
    void emptyRepositoryShouldReturnZeroForAllCalculations() {

        // Assert
        assertEquals(0, BigDecimal.ZERO.compareTo(controller.getTotalIncome()));
        assertEquals(0, BigDecimal.ZERO.compareTo(controller.getTotalExpenses()));
        assertEquals(0, BigDecimal.ZERO.compareTo(controller.getNetBalance()));
    }

    @Test
    void expensesOnlyShouldReturnNegativeBalance() {

        // Arrange
        controller.addTransaction(transaction1);
        controller.addTransaction(transaction3);

        // Act
        BigDecimal balance = controller.getNetBalance();

        // Assert
        assertEquals(0, new BigDecimal("-75.00").compareTo(balance));
    }

    @Test
    void removingTransactionShouldUpdateBalance() {

        // Arrange
        controller.addTransaction(transaction1);
        controller.addTransaction(transaction2);
        controller.addTransaction(transaction3);

        // Act
        controller.removeTransaction(1);

        // Assert
        assertEquals(0, new BigDecimal("25.00")
                .compareTo(controller.getTotalExpenses()));

        assertEquals(0, new BigDecimal("475.00")
                .compareTo(controller.getNetBalance()));
    }
}
