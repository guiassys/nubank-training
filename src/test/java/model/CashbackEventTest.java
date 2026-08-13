package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CashbackEventTest {

    @Test
    void shouldBeCancelled() {
        // Arrange
        CashbackEvent event = new CashbackEvent("tx1", "A", 10, 1000L);

        // Act
        event.cancel();

        // Assert
        assertTrue(event.isCancelled());
    }

    @Test
    void shouldCompareToOtherEvent() {
        // Arrange
        CashbackEvent event1 = new CashbackEvent("tx1", "A", 10, 1000L);
        CashbackEvent event2 = new CashbackEvent("tx2", "B", 20, 2000L);
        CashbackEvent event3 = new CashbackEvent("tx3", "C", 30, 1000L);

        // Assert
        assertTrue(event1.compareTo(event2) < 0); // event1 deve vir antes de event2
        assertTrue(event2.compareTo(event1) > 0); // event2 deve vir depois de event1
        assertEquals(0, event1.compareTo(event3)); // event1 e event3 são equivalentes na ordem
    }
}
