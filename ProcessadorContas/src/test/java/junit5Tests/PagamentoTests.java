package junit5Tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PagamentoTests {

    @Test
    void testePagamentoSemSaldo() {
        Conta conta = new Conta();
        conta.setSaldo(0);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            pagamentoService.realizarPagamento(conta, 100);
        });
        assertEquals("Saldo insuficiente", exception.getMessage());
    }

    @Test
    void testePagamentoValorExatoSaldo() {
        Conta conta = new Conta();
        conta.setSaldo(100);
        assertDoesNotThrow(() -> {
            pagamentoService.realizarPagamento(conta, 100);
        });
        assertEquals(0, conta.getSaldo());
    }

    @Test
    void testePagamentoContaInativa() {
        Conta conta = new Conta();
        conta.setAtiva(false);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            pagamentoService.realizarPagamento(conta, 50);
        });
        assertEquals("Conta inativa", exception.getMessage());
    }
}
