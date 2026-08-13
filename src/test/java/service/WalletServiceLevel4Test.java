package service;

import exceptions.AccountLockedException;
import exceptions.DailyLimitExceededException;
import model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class WalletServiceLevel4Test {

    private IWalletService walletService; // Mantemos IWalletService para acesso a create, deposit, etc.

    @BeforeEach
    void setUp() {
        walletService = new WalletService();
        walletService.create("A");
        walletService.create("B");
        walletService.deposit("A", 10000); // Saldo alto para testes de concorrência
        walletService.deposit("B", 10000);
    }

    @Test
    @DisplayName("Nível 4 - Concorrência: Deve manter a consistência do saldo sob transferências concorrentes")
    void shouldMaintainBalanceConsistencyUnderConcurrentTransfers() throws InterruptedException {
        int initialBalanceA = walletService.balance("A");
        int initialBalanceB = walletService.balance("B");
        int transferAmount = 1;
        int numThreads = 100;
        int numTransfersPerThread = 10;

        // Desbloqueia ou garante que o limite de fraude não trave a conta durante o estresse de concorrência
        // Para cada thread, executamos transfer de forma segura
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // 100 threads transferem de A para B
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < numTransfersPerThread; j++) {
                    try {
                        walletService.transfer("A", "B", transferAmount);
                    } catch (Exception ignored) {
                        // Se for bloqueado por fraude no teste estressado, desbloqueia para continuar a transferência
                        walletService.unblock("A");
                        try {
                            walletService.transfer("A", "B", transferAmount);
                        } catch (Exception innerIgnored) {}
                    }
                }
            });
        }

        // 100 threads transferem de B para A
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < numTransfersPerThread; j++) {
                    try {
                        walletService.transfer("B", "A", transferAmount);
                    } catch (Exception ignored) {
                        walletService.unblock("B");
                        try {
                            walletService.transfer("B", "A", transferAmount);
                        } catch (Exception innerIgnored) {}
                    }
                }
            });
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS), "Os testes de concorrência excederam o tempo limite.");

        // Garante que o somatório total do sistema permaneça conservado (20.000)
        int totalBalance = walletService.balance("A") + walletService.balance("B");
        assertEquals(initialBalanceA + initialBalanceB, totalBalance);
    }

    @Test
    @DisplayName("Nível 4 - Fraude: Deve bloquear a conta após 3 transações de débito em menos de 2 minutos")
    void shouldLockAccountAfter3DebitTransactionsInUnder2Minutes() {
        long baseTimestamp = 1000000L;

        // 3 transações dentro da janela de 2 minutos
        assertTrue(walletService.payment("A", 10, baseTimestamp + 10000));
        assertTrue(walletService.payment("A", 10, baseTimestamp + 60000));
        assertTrue(walletService.payment("A", 10, baseTimestamp + 110000));

        // A 4ª transação deve falhar (lançar exceção ou retornar false) e a conta deve ser bloqueada
        try {
            boolean result = walletService.payment("A", 10, baseTimestamp + 115000);
            assertFalse(result);
        } catch (AccountLockedException ignored) {
            // Comportamento esperado com exceções ativas
        }

        Account accountA = walletService.getAccountById("A");
        assertTrue(accountA.isLocked());

        // Depósitos ainda devem funcionar. Saldo: 10000 - 30 (pagamentos) + 100 (depósito) = 10070
        assertEquals(10070, walletService.deposit("A", 100));
    }

    @Test
    @DisplayName("Nível 4 - Fraude: Deve desbloquear a conta com o comando UNBLOCK")
    void shouldUnlockAccountWithUnblockCommand() {
        long baseTimestamp = 2000000L;
        // Força o bloqueio da conta com 3 débitos
        walletService.payment("A", 1, baseTimestamp + 1000);
        walletService.payment("A", 1, baseTimestamp + 2000);
        walletService.payment("A", 1, baseTimestamp + 3000);

        try {
            walletService.payment("A", 1, baseTimestamp + 4000); // Força o bloqueio
        } catch (AccountLockedException ignored) {}

        assertTrue(walletService.getAccountById("A").isLocked());

        // Tenta desbloquear
        assertTrue(walletService.unblock("A"));
        assertFalse(walletService.getAccountById("A").isLocked());

        // A conta agora deve aceitar um novo pagamento. Saldo: 10000 - 3 (pagamentos) - 10 (novo pagamento) = 9987
        assertTrue(walletService.payment("A", 10, baseTimestamp + 5000));
        assertEquals(9987, walletService.balance("A"));
    }

    @Test
    @DisplayName("Nível 4 - Limite Diário: Deve respeitar o limite de gasto diário")
    void shouldRespectDailySpendingLimit() {
        long day1 = 500000L;
        long day2 = day1 + 86400000L;

        // Define o limite diário para 100
        assertTrue(walletService.setDailyLimit("A", 100));

        // Gasta 80 no dia 1 - OK
        assertTrue(walletService.payment("A", 80, day1 + 1000));
        assertEquals(9920, walletService.balance("A"));

        // Tenta gastar mais 30 no dia 1 - Deve falhar ou lançar DailyLimitExceededException (80 + 30 > 100)
        try {
            boolean result = walletService.payment("A", 30, day1 + 2000);
            assertFalse(result);
        } catch (DailyLimitExceededException ignored) {}

        assertEquals(9920, walletService.balance("A")); // Saldo não deve mudar

        // Tenta gastar 20 no dia 1 - OK (80 + 20 <= 100)
        assertTrue(walletService.payment("A", 20, day1 + 3000));
        assertEquals(9900, walletService.balance("A"));

        // Tenta gastar mais 1 no dia 1 - Deve falhar (100 + 1 > 100)
        try {
            boolean result = walletService.payment("A", 1, day1 + 4000);
            assertFalse(result);
        } catch (DailyLimitExceededException ignored) {}

        // Gasta 50 no dia 2 - OK, pois o limite é por dia
        assertTrue(walletService.payment("A", 50, day2 + 1000));
        assertEquals(9850, walletService.balance("A"));
    }
}