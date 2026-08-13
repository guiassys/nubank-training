package service;

import exceptions.AccountAlreadyExistsException;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientBalanceException;
import exceptions.InvalidAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletServiceLevel1Test {

    private IWalletService walletService; // Mantemos IWalletService aqui por simplicidade, pois os testes misturam admin e financeiro

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

        // Act & Assert
        assertThrows(
                AccountAlreadyExistsException.class,
                () -> walletService.create("A")
        );

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
        // Act & Assert
        assertThrows(
                AccountNotFoundException.class,
                () -> walletService.deposit("X", 100)
        );
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
    void shouldNotTransferWhenBalanceIsSufficient() {
        // Arrange
        walletService.create("A");
        walletService.create("B");
        walletService.deposit("A", 100);

        // Act & Assert
        assertThrows(
                InsufficientBalanceException.class,
                () -> walletService.transfer("A", "B", 150)
        );

        // Estado não deve ser alterado
        assertEquals(100, walletService.balance("A"));
        assertEquals(0, walletService.balance("B"));
    }

    @Test
    void shouldNotTransferToSameAccount() {
        // Arrange
        walletService.create("A");
        walletService.deposit("A", 100);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> walletService.transfer("A", "A", 10)
        );

        // Estado não deve ser alterado
        assertEquals(100, walletService.balance("A"));
    }

    @Test
    void shouldNotTransferWhenDestinationAccountDoesNotExist() {
        // Arrange
        walletService.create("A");
        walletService.deposit("A", 100);

        // Act & Assert
        assertThrows(
                AccountNotFoundException.class,
                () -> walletService.transfer("A", "X", 10)
        );

        // Estado não deve ser alterado
        assertEquals(100, walletService.balance("A"));
    }

    @Test
    void shouldNotTransferWhenSourceAccountDoesNotExist() {
        // Arrange
        walletService.create("B");

        // Act & Assert
        assertThrows(
                AccountNotFoundException.class,
                () -> walletService.transfer("X", "B", 10)
        );

        assertEquals(0, walletService.balance("B"));
    }

    @Test
    void shouldNotTransferWhenBothAccountsDoNotExist() {
        // Act & Assert
        assertThrows(
                AccountNotFoundException.class,
                () -> walletService.transfer("X", "Y", 10)
        );
    }

    @Test
    void shouldNotTransferZeroAmount() {
        // Arrange
        walletService.create("A");
        walletService.create("B");
        walletService.deposit("A", 100);

        // Act & Assert
        assertThrows(
                InvalidAmountException.class,
                () -> walletService.transfer("A", "B", 0)
        );

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

        // Act & Assert
        assertThrows(
                InvalidAmountException.class,
                () -> walletService.transfer("A", "B", -10)
        );

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
        // Act & Assert
        assertThrows(
                AccountNotFoundException.class,
                () -> walletService.balance("X")
        );
    }
}