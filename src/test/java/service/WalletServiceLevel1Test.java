package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletServiceLevel1Test {

    private IWalletService walletService;

    @BeforeEach
    void setUp() {
        walletService = new WalletService();
    }

    @Test
    void shouldCreateAccount() {
        // Act
        boolean result = walletService.create("A");

        // Assert
        assertTrue(result);
        assertEquals(0, walletService.balance("A"));
    }

    @Test
    void shouldNotCreateAccountWithSameId() {
        // Arrange
        walletService.create("A");

        // Act
        boolean result = walletService.create("A");

        // Assert
        assertFalse(result);
        assertEquals(0, walletService.balance("A"));
    }

    @Test
    void shouldDepositAmountIntoExistingAccount() {
        // Arrange
        walletService.create("A");

        // Act
        int result = walletService.deposit("A", 100);

        // Assert
        assertEquals(100, result);
        assertEquals(100, walletService.balance("A"));
    }

    @Test
    void shouldReturnMinusOneWhenDepositingIntoInexistentAccount() {
        // Act
        int result = walletService.deposit("X", 100);

        // Assert
        assertEquals(-1, result);
        assertEquals(-1, walletService.balance("X"));
    }

    @Test
    void shouldThrowExceptionWhenDepositingNegativeAmount() {
        // Arrange
        walletService.create("A");

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> walletService.deposit("A", -100)
        );

        // Assert
        assertEquals(
                "Amount must be greater than zero.",
                exception.getMessage()
        );

        assertEquals(0, walletService.balance("A"));
    }

    @Test
    void shouldTransferWhenBalanceIsSufficient() {
        // Arrange
        walletService.create("A");
        walletService.create("B");
        walletService.deposit("A", 100);

        // Act
        boolean result = walletService.transfer("A", "B", 50);

        // Assert
        assertTrue(result);
        assertEquals(50, walletService.balance("A"));
        assertEquals(50, walletService.balance("B"));
    }

    @Test
    void shouldNotTransferWhenBalanceIsInsufficient() {
        // Arrange
        walletService.create("A");
        walletService.create("B");
        walletService.deposit("A", 100);

        // Act
        boolean result = walletService.transfer("A", "B", 150);

        // Assert
        assertFalse(result);

        // Estado não deve ser alterado
        assertEquals(100, walletService.balance("A"));
        assertEquals(0, walletService.balance("B"));
    }

    @Test
    void shouldNotTransferToSameAccount() {
        // Arrange
        walletService.create("A");
        walletService.deposit("A", 100);

        // Act
        boolean result = walletService.transfer("A", "A", 10);

        // Assert
        assertFalse(result);

        // Estado não deve ser alterado
        assertEquals(100, walletService.balance("A"));
    }

    @Test
    void shouldNotTransferWhenDestinationAccountDoesNotExist() {
        // Arrange
        walletService.create("A");
        walletService.deposit("A", 100);

        // Act
        boolean result = walletService.transfer("A", "X", 10);

        // Assert
        assertFalse(result);

        // Estado não deve ser alterado
        assertEquals(100, walletService.balance("A"));
        assertEquals(-1, walletService.balance("X"));
    }

    @Test
    void shouldNotTransferWhenSourceAccountDoesNotExist() {
        // Arrange
        walletService.create("B");

        // Act
        boolean result = walletService.transfer("X", "B", 10);

        // Assert
        assertFalse(result);

        assertEquals(-1, walletService.balance("X"));
        assertEquals(0, walletService.balance("B"));
    }

    @Test
    void shouldNotTransferWhenBothAccountsDoNotExist() {
        // Act
        boolean result = walletService.transfer("X", "Y", 10);

        // Assert
        assertFalse(result);

        assertEquals(-1, walletService.balance("X"));
        assertEquals(-1, walletService.balance("Y"));
    }

    @Test
    void shouldNotTransferZeroAmount() {
        // Arrange
        walletService.create("A");
        walletService.create("B");
        walletService.deposit("A", 100);

        // Act
        boolean result = walletService.transfer("A", "B", 0);

        // Assert
        assertFalse(result);

        // Estado não deve ser alterado
        assertEquals(100, walletService.balance("A"));
        assertEquals(0, walletService.balance("B"));
    }

    @Test
    void shouldNotTransferNegativeAmount() {
        // Arrange
        walletService.create("A");
        walletService.create("B");
        walletService.deposit("A", 100);

        // Act
        boolean result = walletService.transfer("A", "B", -10);

        // Assert
        assertFalse(result);

        // Estado não deve ser alterado
        assertEquals(100, walletService.balance("A"));
        assertEquals(0, walletService.balance("B"));
    }

    @Test
    void shouldReturnBalanceOfExistingAccount() {
        // Arrange
        walletService.create("A");
        walletService.deposit("A", 100);

        // Act
        int result = walletService.balance("A");

        // Assert
        assertEquals(100, result);
    }

    @Test
    void shouldReturnMinusOneWhenAccountDoesNotExist() {
        // Act
        int result = walletService.balance("X");

        // Assert
        assertEquals(-1, result);
    }
}