package junit5Tests;

import com.example.processadorcontas.service.ProcessadorContasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProcessadorContasApplicationTests {

    private ProcessadorContasService service;

    @BeforeEach
    void setUp() {
        service = new ProcessadorContasService();
    }

    @Test
    void testProcessamentoValido() {
        assertDoesNotThrow(() -> {
            service.processarConta("conta_valida", 100);
        });
    }

    @Test
    void testProcessamentoContaInexistente() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.processarConta(null);
        });

        assertEquals("Conta não pode ser nula", exception.getMessage());
    }

    @Test
    void testPagamentoValorNegativo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.realizarPagamento(new Pagamento(-500, TipoPagamento.DEBITO));
        });

        assertEquals("Valor do pagamento deve ser positivo", exception.getMessage());
    }

    @Test
    void testProcessarFaturaPaga() {
        Fatura fatura = new Fatura();
        fatura.setPaga(true);

        boolean resultado = service.processarFatura(fatura);

        assertFalse(resultado, "Uma fatura já paga não deve ser processada novamente.");
    }

    @Test
    void testProcessarPagamentoComValorZero() {
        Pagamento pagamento = new Pagamento();
        pagamento.setValor(0);

        assertThrows(IllegalArgumentException.class, () -> {
            service.processarPagamento(pagamento);
        });
    }

    @Test
    void testProcessarPagamentoComTipoInvalido() {
        Pagamento pagamento = new Pagamento();
        pagamento.setTipoPagamento(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.processarPagamento(pagamento);
        });

        assertEquals("Tipo de pagamento inválido", exception.getMessage());
    }

    @Test
    void testProcessarContaComValorAlto() {
        assertDoesNotThrow(() -> {
            service.processarConta("conta_valida", 1000000);
        });
    }

    @Test
    void testProcessarPagamentoDuplicado() {
        Pagamento pagamento = new Pagamento(200, TipoPagamento.CREDITO);

        assertDoesNotThrow(() -> service.processarPagamento(pagamento));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            service.processarPagamento(pagamento);
        });

        assertEquals("Pagamento já processado", exception.getMessage());
    }

    @Test
    void testProcessarFaturaComValorZero() {
        Fatura fatura = new Fatura();
        fatura.setValor(0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.processarFatura(fatura);
        });

        assertEquals("Valor da fatura deve ser maior que zero", exception.getMessage());
    }

    @Test
    void testProcessarContaSuspensa() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.processarConta("conta_suspensa", 500);
        });

        assertEquals("Conta está suspensa", exception.getMessage());
    }

}
