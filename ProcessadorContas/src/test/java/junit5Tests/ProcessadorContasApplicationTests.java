
package junit5Tests;

import com.example.processadorcontas.model.*;
import com.example.processadorcontas.service.ProcessadorContasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProcessadorContasApplicationTests {
    private ProcessadorContasService service;

    @BeforeEach
    void setUp() {
        service = new ProcessadorContasService();
    }

    @Test
    void testProcessarFaturaPagamentoValidoBoleto() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.now());
        fatura.setValorTotal(new BigDecimal("200"));

        Conta conta = new Conta();
        conta.setTipoPagamento(TipoPagamento.BOLETO);
        conta.setValorPago(new BigDecimal("200"));
        conta.setDataConta(LocalDate.now());
        conta.setDataPagamento(LocalDate.now());

        Fatura resultado = service.processarFatura(fatura, List.of(conta));
        assertEquals("PAGA", resultado.getStatus());
    }

    @Test
    void testProcessarFaturaValorBoletoInvalido() {
        Fatura fatura = new Fatura();
        fatura.setValorTotal(new BigDecimal("6000"));
        fatura.setData(LocalDate.now());

        Conta conta = new Conta();
        conta.setTipoPagamento(TipoPagamento.BOLETO);
        conta.setDataConta(LocalDate.now().minusDays(10));
        conta.setDataPagamento(LocalDate.now());
        conta.setValorPago(new BigDecimal("6000"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.processarFatura(fatura, List.of(conta));
        });
        assertEquals("Valor do boleto fora do limite permitido", exception.getMessage());
    }

    @Test
    void testProcessarFaturaCartaoCreditoPeriodoInsuficiente() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.now());
        fatura.setValorTotal(new BigDecimal("300"));

        Conta conta = new Conta();
        conta.setTipoPagamento(TipoPagamento.CARTAO_CREDITO);
        conta.setDataConta(LocalDate.now().minusDays(10)); // menos de 15 dias exigidos
        conta.setDataPagamento(LocalDate.now());
        conta.setValorPago(new BigDecimal("300"));

        Fatura resultado = service.processarFatura(fatura, List.of(conta));
        assertEquals("PENDENTE", resultado.getStatus());
    }

    @Test
    void testProcessarFaturaTransferenciaDataInvalida() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.now());
        fatura.setValorTotal(new BigDecimal("150"));

        Conta conta = new Conta();
        conta.setTipoPagamento(TipoPagamento.TRANSFERENCIA_BANCARIA);
        conta.setDataConta(LocalDate.now().plusDays(2)); // Data após a fatura
        conta.setDataPagamento(LocalDate.now());
        conta.setValorPago(new BigDecimal("150"));

        Fatura resultado = service.processarFatura(fatura, List.of(conta));
        assertEquals("PENDENTE", resultado.getStatus());
        assertTrue(resultado.getPagamentos().isEmpty());
    }
}
