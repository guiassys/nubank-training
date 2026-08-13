package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WalletServiceLevel2Test {

    private IWalletService walletService; // Mantemos IWalletService para acesso a create, deposit, etc.

    @BeforeEach
    void setUp() {
        walletService = new WalletService();
    }

    @Test
    void shouldReturnAccountWithMajorTotalSpender() {
        // Arrange
        walletService.create("A");
        walletService.create("B");
        walletService.create("C");
        walletService.deposit("A", 100);
        walletService.deposit("B", 100);

        walletService.transfer("A", "C", 10);
        walletService.transfer("B", "C", 20);

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