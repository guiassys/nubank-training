package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Implementa testes unitários para a classe Account. Considere que a Classe Account é responsável por alterar o seu
 * próprio estado.
 */
class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("A");
    }

    @Test
    void shouldCreateAccountWithZeroBalance() {
        // Act
        int balance = account.getBalance();

        // Assert
        assertEquals(0, balance);
    }

    @Test
    void shouldDepositAmount() {
        // Arrange
        int amount = 100;

        // Act
        account.deposit(amount);

        // Assert
        assertEquals(100, account.getBalance());
    }

    @Test
    void shouldNotDepositZero() {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> account.deposit(0)
        );

        // Assert
        assertEquals(
                "Amount must be greater than zero.",
                exception.getMessage()
        );

        assertEquals(0, account.getBalance());
    }

    @Test
    void shouldNotDepositNegativeAmount() {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> account.deposit(-100)
        );

        // Assert
        assertEquals(
                "Amount must be greater than zero.",
                exception.getMessage()
        );

        assertEquals(0, account.getBalance());
    }

    @Test
    void shouldWithdrawAmount() {
        // Arrange
        account.deposit(100);

        // Act
        boolean result = account.withdraw(50);

        // Assert
        assertTrue(result);
        assertEquals(50, account.getBalance());
    }

    @Test
    void shouldNotWithdrawWhenBalanceIsInsufficient() {
        // Arrange
        account.deposit(100);

        // Act
        boolean result = account.withdraw(150);

        // Assert
        assertFalse(result);
        assertEquals(100, account.getBalance());
    }

    @Test
    void shouldNotWithdrawZero() {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> account.withdraw(0)
        );

        // Assert
        assertEquals(
                "Amount must be greater than zero.",
                exception.getMessage()
        );

        assertEquals(0, account.getBalance());
    }

    @Test
    void shouldNotWithdrawNegativeAmount() {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> account.withdraw(-50)
        );

        // Assert
        assertEquals(
                "Amount must be greater than zero.",
                exception.getMessage()
        );

        assertEquals(0, account.getBalance());
    }


    @Test
    void shouldNotDepositNegativeAmount2() {
        // Act
        IllegalArgumentException illegalArgumentException = assertThrows(
                IllegalArgumentException.class,
                () -> account.deposit(-100)
        );

        // Assert
        assertEquals("Amount must be greater than zero.", illegalArgumentException.getMessage());

    }

}