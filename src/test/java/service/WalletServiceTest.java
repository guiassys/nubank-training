package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Implementa testes unitários para a classe WalletService. Considere que a Classe WalletService é responsável por
 * controlar o fluxo de vários objeto Account.
 */
class WalletServiceTest {

    private WalletService walletService;

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

    @Test
    void shouldReturnAccountWithMajorTotalSpender(){
        // Arrange
        walletService.create("A");
        walletService.create("B");
        walletService.create("C");
        walletService.deposit("A", 100);
        walletService.deposit("B", 100);

        boolean transfer1 = walletService.transfer("A", "C", 10);
        boolean transfer2 = walletService.transfer("B", "C", 20);

        // Act
        List<String> result = walletService.topSpenders(1);

        // Assert
        assertEquals(1, result.size());
        assertEquals("B(20)", result.getFirst());
    }

    @Test
    void shouldBreakTieUsingAlphabeticalOrder() {
        // Arrange: A, B e C gastam exatamente o mesmo valor (100)
        walletService.create("C");
        walletService.create("A");
        walletService.create("B");

        walletService.deposit("A", 200);
        walletService.deposit("B", 200);
        walletService.deposit("C", 200);

        walletService.payment("A", 100, 1000L);
        walletService.payment("B", 100, 1001L);
        walletService.payment("C", 100, 1002L);

        // Act: Pede o Top 2
        List<String> top2 = walletService.topSpenders(2);

        // Assert: Em empate de 100, A e B devem preceder C alfabeticamente
        assertEquals(2, top2.size());
        assertEquals("A(100)", top2.get(0));
        assertEquals("B(100)", top2.get(1));
    }

    @Test
    void shouldIgnoreAccountsWithZeroSpentAndHandleKGreaterThanAccounts() {
        // Arrange: A tem gasto, B e C apenas depósitos
        walletService.create("A");
        walletService.create("B");
        walletService.create("C");

        walletService.deposit("A", 100);
        walletService.deposit("B", 100);
        walletService.payment("A", 50, 1000L);

        // Act: Pede K = 5 (maior do que o total de contas com gasto)
        List<String> result = walletService.topSpenders(5);

        // Assert: Retorna apenas A, pois B e C possuem totalSpent = 0
        assertEquals(1, result.size());
        assertEquals("A(50)", result.getFirst());
    }

}