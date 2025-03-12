
package junit5Tests;

import com.example.processadorcontas.model.*;
import com.example.processadorcontas.service.ProcessadorContasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PagamentoTests {
    private ProcessadorContasService service;

    @BeforeEach
    void setUp() {
        service = new ProcessadorContasService();
    }

    @Test
    void pagamentoBoletoComAtraso() {
        Conta conta = new Conta();
        conta.setTipoPagamento(TipoPagamento.BOLETO);
        conta.setValorPago(new BigDecimal("1000"));
        conta.setDataConta(LocalDate.now().minusDays(5));
        conta.setDataPagamento(LocalDate.now()); // atraso gera acréscimo de 10%

        Fatura fatura = new Fatura();
        fatura.setValorTotal(new BigDecimal("1100")); // valor após acréscimo de 10%
        fatura.setData(LocalDate.now());

        Fatura resultado = service.processarFatura(fatura, List.of(conta));
        assertEquals("PAGA", resultado.getStatus());
        assertEquals(0, new BigDecimal("1100.00").compareTo(resultado.getPagamentos().get(0).getValor()));
    }

    @Test
    void pagamentoCartaoCreditoDentroDoPrazoValido() {
        Conta conta = new Conta();
        conta.setTipoPagamento(TipoPagamento.CARTAO_CREDITO);
        conta.setValorPago(new BigDecimal("500"));
        conta.setDataConta(LocalDate.now().minusDays(16)); // período válido (15+ dias)
        conta.setDataPagamento(LocalDate.now());

        Fatura fatura = new Fatura();
        fatura.setValorTotal(new BigDecimal("500"));
        fatura.setData(LocalDate.now());

        Fatura resultado = service.processarFatura(fatura, List.of(conta));
        assertEquals("PAGA", resultado.getStatus());
        assertFalse(resultado.getPagamentos().isEmpty());
    }

    @Test
    void pagamentoTransferenciaDataInvalida() {
        Conta conta = new Conta();
        conta.setTipoPagamento(TipoPagamento.TRANSFERENCIA_BANCARIA);
        conta.setValorPago(new BigDecimal("250"));
        conta.setDataConta(LocalDate.now().plusDays(2)); // data futura inválida
        conta.setDataPagamento(LocalDate.now());

        Fatura fatura = new Fatura();
        fatura.setValorTotal(new BigDecimal("250"));
        fatura.setData(LocalDate.now());

        Fatura resultado = service.processarFatura(fatura, List.of(conta));
        assertEquals("PENDENTE", resultado.getStatus());
        assertTrue(resultado.getPagamentos().isEmpty());
    }
}
