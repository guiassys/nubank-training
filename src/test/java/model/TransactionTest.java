package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void shouldCreateTransactionWithCorrectInitialState() {
        // Arrange & Act
        Transaction transaction = new Transaction("tx1", "A", 100, 1000L, 10);

        // Assert
        assertEquals("tx1", transaction.getId());
        assertEquals("A", transaction.getAccountId());
        assertEquals(100, transaction.getAmount());
        assertEquals(1000L, transaction.getTimestamp());
        assertEquals(10, transaction.getCashbackAmount());
        assertFalse(transaction.isRefunded());
    }

    @Test
    void shouldBeRefunded() {
        // Arrange
        Transaction transaction = new Transaction("tx1", "A", 100, 1000L, 10);

        // Act
        transaction.setRefunded(true);

        // Assert
        assertTrue(transaction.isRefunded());
    }
}
