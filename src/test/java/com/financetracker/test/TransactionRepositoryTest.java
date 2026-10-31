package com.financetracker.test;

import com.financetracker.model.Transaction;
import com.financetracker.model.TransactionType;
import com.financetracker.persistence.TransactionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionRepositoryTest {

    private TransactionRepository repository;
    private Transaction transaction1;
    private Transaction transaction2;

    @BeforeEach
    void setUp() {

        // Create a fresh repository before each test
        repository = new TransactionRepository();

        // Sample expense transaction
        transaction1 = new Transaction(
                1,
                new BigDecimal("50.00"),
                "Gas",
                "Filled up my car",
                LocalDate.of(2026, 10, 31),
                TransactionType.EXPENSE
        );

        // Sample income transaction
        transaction2 = new Transaction(
                2,
                new BigDecimal("500.00"),
                "Salary",
                "Paycheck",
                LocalDate.of(2026, 10, 31),
                TransactionType.INCOME
        );
    }

    @Test
    void addTransactionShouldStoreTransaction() {

        // Act
        repository.addTransaction(transaction1);

        // Assert
        assertEquals(1, repository.getAllTransactions().size());
        assertEquals(transaction1, repository.getTransactionByID(1));
    }

    @Test
    void getAllTransactionsShouldReturnAllTransactions() {

        // Act
        repository.addTransaction(transaction1);
        repository.addTransaction(transaction2);

        // Assert
        assertEquals(2, repository.getAllTransactions().size());
        assertTrue(repository.getAllTransactions().contains(transaction1));
        assertTrue(repository.getAllTransactions().contains(transaction2));
    }

    @Test
    void getTransactionByIDShouldReturnCorrectTransaction() {

        // Act
        repository.addTransaction(transaction1);
        repository.addTransaction(transaction2);

        // Assert
        assertEquals(transaction2, repository.getTransactionByID(2));
    }

    @Test
    void getTransactionByIDShouldReturnNullWhenNotFound() {

        // Arrange
        repository.addTransaction(transaction1);
        repository.addTransaction(transaction2);

        // Act
        Transaction result = repository.getTransactionByID(99);

        // Assert
        assertNull(result);
    }

    @Test
    void duplicateTransactionIDShouldBeRejected() {

        // Arrange
        repository.addTransaction(transaction1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repository.addTransaction(transaction1)
        );

        // Verify original transaction remains unchanged
        assertEquals(1, repository.getAllTransactions().size());
        assertSame(transaction1, repository.getTransactionByID(1));
    }

    @Test
    void nullTransactionShouldBeRejected() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repository.addTransaction(null)
        );

        assertTrue(repository.getAllTransactions().isEmpty());
    }

    @Test
    void removeTransactionShouldRemoveExistingTransaction() {

        // Arrange
        repository.addTransaction(transaction1);
        repository.addTransaction(transaction2);

        // Act
        boolean result = repository.removeTransaction(1);

        // Assert
        assertTrue(result);
        assertNull(repository.getTransactionByID(1));
        assertEquals(1, repository.getAllTransactions().size());
        assertSame(transaction2, repository.getTransactionByID(2));
    }

    @Test
    void removeTransactionShouldReturnFalseWhenNotFound() {

        // Arrange
        repository.addTransaction(transaction1);

        // Act
        boolean result = repository.removeTransaction(99);

        // Assert
        assertFalse(result);
        assertEquals(1, repository.getAllTransactions().size());
        assertSame(transaction1, repository.getTransactionByID(1));
    }

    @Test
    void getAllTransactionsShouldProtectInternalList() {

        // Arrange
        repository.addTransaction(transaction1);
        repository.addTransaction(transaction2);

        // Act
        List<Transaction> returnedList = repository.getAllTransactions();
        returnedList.clear();

        // Assert
        assertTrue(returnedList.isEmpty());
        assertEquals(2, repository.getAllTransactions().size());
        assertNotNull(repository.getTransactionByID(1));
        assertNotNull(repository.getTransactionByID(2));
    }
}
