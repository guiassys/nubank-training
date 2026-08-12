package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletServiceLevel3Test {

    private IWalletService walletService;

    @BeforeEach
    void setUp() {
        walletService = new WalletService();
        walletService.create("A");
        walletService.create("B");
        walletService.deposit("A", 1000);
        walletService.deposit("B", 1000);
    }

    @Test
    @DisplayName("Deve realizar pagamento com cashback e agendar o crédito para 24h depois")
    void shouldProcessPaymentWithCashbackAndCreditAfter24Hours() {
        long initialTimestamp = 1000000L;
        String txId = walletService.paymentWithCashback("A", 200, initialTimestamp, 10);
        assertNotNull(txId);

        // Saldo imediato em t = 1.000.000 ms: 800
        assertEquals(800, walletService.balance("A", initialTimestamp));

        // Saldo 1 ms antes de dar 24h: 800 (ainda não maturou)
        long beforeMaturity = initialTimestamp + 86400000L - 1L;
        assertEquals(800, walletService.balance("A", beforeMaturity));

        // Saldo em exatas 24h: 820 (cashback creditado)
        long maturityTimestamp = initialTimestamp + 86400000L;
        assertEquals(820, walletService.balance("A", maturityTimestamp));
    }

    @Test
    @DisplayName("Deve estornar (refund) um pagamento e devolver o saldo e totalSpent da conta")
    void shouldRefundPaymentAndRestoreBalanceAndTotalSpent() {
        // t = 1.000 ms -> Pagamento simples de 300
        walletService.payment("A", 300, 1000L);
        assertEquals(700, walletService.balance("A"));
        assertEquals("A(300)", walletService.topSpenders(1).getFirst());

        // Precisamos do ID para o refund, então faremos via paymentWithCashback
        String txId = walletService.paymentWithCashback("A", 200, 2000L, 0);
        assertEquals(500, walletService.balance("A"));
        assertEquals("A(500)", walletService.topSpenders(1).getFirst());

        // Reembolsa a transação de 200
        boolean refunded = walletService.refund("A", txId, 3000L);
        assertTrue(refunded);

        // Saldo restaurado para 700 e totalSpent volta para 300
        assertEquals(700, walletService.balance("A"));
        assertEquals("A(300)", walletService.topSpenders(1).getFirst());

        // Não deve permitir reembolso duplicado da mesma transação
        assertFalse(walletService.refund("A", txId, 4000L));
    }

    @Test
    @DisplayName("Deve cancelar cashback pendente se o pagamento for estornado antes das 24h")
    void shouldCancelPendingCashbackOnRefund() {
        // t = 1.000.000 ms -> Pagamento de 200 com 10% de cashback (20)
        String txId = walletService.paymentWithCashback("A", 200, 1000000L, 10);
        assertEquals(800, walletService.balance("A"));

        // Reembolsa em t = 2.000.000 ms (antes da maturação de 24h)
        assertTrue(walletService.refund("A", txId, 2000000L));
        assertEquals(1000, walletService.balance("A"));

        // Passa das 24 horas (t = 87.400.000 ms) -> O cashback cancelado NÃO deve ser creditado!
        assertEquals(1000, walletService.balance("A"));
    }

    @Test
    @DisplayName("Deve calcular o valor total gasto dentro da janela deslizante (SPENT_IN_WINDOW)")
    void shouldCalculateSpentInSlidingWindowCorrectly() {
        // t = 100.000 ms -> Gasta 100
        walletService.payment("A", 100, 100000L);
        // t = 200.000 ms -> Gasta 250
        walletService.payment("A", 250, 200000L);
        // t = 500.000 ms -> Gasta 300
        walletService.payment("A", 300, 500000L);

        // Consulta em t = 500.000 ms com janela de 350.000 ms -> Intervalo [150.000, 500.000]
        // Entram as transações de t=200.000 (250) e t=500.000 (300). Total: 550
        int spentWindow = walletService.spentInWindow("A", 350000L, 500000L);
        assertEquals(550, spentWindow);

        // Consulta com janela menor (100.000 ms em t = 500.000) -> Intervalo [400.000, 500.000]
        // Entra apenas a transação de t=500.000 (300)
        assertEquals(300, walletService.spentInWindow("A", 100000L, 500000L));
    }
}
