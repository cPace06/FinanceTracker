
package com.financetracker.test;

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

public class TransactionServiceTest {

    private TransactionRepository repository;
    private TransactionService service;
    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;

    @BeforeEach
    void setUp() {

        repository = new TransactionRepository();
        service = new TransactionService(repository);

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
    void addTransactionShouldStoreTransaction() {

        // Act
        service.addTransaction(transaction1);

        // Assert
        assertEquals(1, repository.getAllTransactions().size());
        assertSame(transaction1, repository.getTransactionByID(1));
    }

    @Test
    void getAllTransactionsShouldReturnStoredTransactions() {

        // Arrange
        service.addTransaction(transaction1);
        service.addTransaction(transaction2);

        // Act
        List<Transaction> transactions = service.getAllTransactions();

        // Assert
        assertEquals(2, transactions.size());
        assertTrue(transactions.contains(transaction1));
        assertTrue(transactions.contains(transaction2));
    }

    @Test
    void removeTransactionShouldRemoveExistingTransaction() {

        // Arrange
        service.addTransaction(transaction1);
        service.addTransaction(transaction2);

        // Act
        boolean result = service.removeTransaction(1);

        // Assert
        assertTrue(result);
        assertNull(repository.getTransactionByID(1));
        assertEquals(1, service.getAllTransactions().size());
    }

    @Test
    void removeTransactionShouldReturnFalseWhenNotFound() {

        // Arrange
        service.addTransaction(transaction1);

        // Act
        boolean result = service.removeTransaction(99);

        // Assert
        assertFalse(result);
        assertEquals(1, service.getAllTransactions().size());
    }

    @Test
    void duplicateTransactionIDShouldBeRejected() {

        // Arrange
        service.addTransaction(transaction1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                service.addTransaction(transaction1)
        );

        assertEquals(1, service.getAllTransactions().size());
    }

    @Test
    void nullTransactionShouldBeRejected() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                service.addTransaction(null)
        );

        assertTrue(service.getAllTransactions().isEmpty());
    }

    @Test
    void nullRepositoryShouldBeRejected() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new TransactionService(null)
        );
    }

    @Test
    void calculateTotalIncomeShouldReturnCorrectTotal() {

        // Arrange
        service.addTransaction(transaction1);
        service.addTransaction(transaction2);
        service.addTransaction(transaction3);

        // Act
        BigDecimal total = service.calculateTotalIncome();

        // Assert
        assertEquals(0, new BigDecimal("500.00").compareTo(total));
    }

    @Test
    void calculateTotalExpensesShouldReturnCorrectTotal() {

        // Arrange
        service.addTransaction(transaction1);
        service.addTransaction(transaction2);
        service.addTransaction(transaction3);

        // Act
        BigDecimal total = service.calculateTotalExpenses();

        // Assert
        assertEquals(0, new BigDecimal("75.00").compareTo(total));
    }

    @Test
    void calculateNetBalanceShouldReturnCorrectBalance() {

        // Arrange
        service.addTransaction(transaction1);
        service.addTransaction(transaction2);
        service.addTransaction(transaction3);

        // Act
        BigDecimal balance = service.calculateNetBalance();

        // Assert
        assertEquals(0, new BigDecimal("425.00").compareTo(balance));
    }

    @Test
    void emptyRepositoryShouldReturnZeroForAllCalculations() {

        // Act
        BigDecimal income = service.calculateTotalIncome();
        BigDecimal expenses = service.calculateTotalExpenses();
        BigDecimal balance = service.calculateNetBalance();

        // Assert
        assertEquals(0, BigDecimal.ZERO.compareTo(income));
        assertEquals(0, BigDecimal.ZERO.compareTo(expenses));
        assertEquals(0, BigDecimal.ZERO.compareTo(balance));
    }

    @Test
    void incomeOnlyShouldReturnPositiveBalance() {

        // Arrange
        service.addTransaction(transaction2);

        // Act
        BigDecimal balance = service.calculateNetBalance();

        // Assert
        assertEquals(0, new BigDecimal("500.00").compareTo(balance));
    }

    @Test
    void expensesOnlyShouldReturnNegativeBalance() {

        // Arrange
        service.addTransaction(transaction1);
        service.addTransaction(transaction3);

        // Act
        BigDecimal balance = service.calculateNetBalance();

        // Assert
        assertEquals(0, new BigDecimal("-75.00").compareTo(balance));
    }

    @Test
    void removingTransactionShouldUpdateCalculations() {

        // Arrange
        service.addTransaction(transaction1);
        service.addTransaction(transaction2);
        service.addTransaction(transaction3);

        // Act
        service.removeTransaction(1);

        // Assert
        assertEquals(0, new BigDecimal("25.00")
                .compareTo(service.calculateTotalExpenses()));

        assertEquals(0, new BigDecimal("475.00")
                .compareTo(service.calculateNetBalance()));
    }

    @Test
    void calculationsShouldHandleDecimalPrecision() {

        // Arrange
        Transaction income = new Transaction(
                4,
                new BigDecimal("0.10"),
                "Other",
                "Small income",
                LocalDate.of(2026, 10, 31),
                TransactionType.INCOME
        );

        Transaction additionalIncome = new Transaction(
                5,
                new BigDecimal("0.20"),
                "Other",
                "Additional income",
                LocalDate.of(2026, 10, 31),
                TransactionType.INCOME
        );

        service.addTransaction(income);
        service.addTransaction(additionalIncome);

        // Act
        BigDecimal total = service.calculateTotalIncome();

        // Assert
        assertEquals(0, new BigDecimal("0.30").compareTo(total));
    }
}

