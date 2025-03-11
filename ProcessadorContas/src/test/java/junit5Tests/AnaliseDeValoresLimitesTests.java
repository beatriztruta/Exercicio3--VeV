package junit5Tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnaliseDeValoresLimitesTests {

    @Test
    void testeValorLimiteSuperior() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.analiseLimiteConta(Double.MAX_VALUE);
        });
    }

    @Test
    void testeValorLimiteInferior() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.analiseLimiteConta(Double.MIN_VALUE);
        });
    }

    @Test
    void testeAnaliseValorNegativo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.analiseLimiteConta(-1);
        });
        assertEquals("Valor deve ser positivo", exception.getMessage());
    }
}
