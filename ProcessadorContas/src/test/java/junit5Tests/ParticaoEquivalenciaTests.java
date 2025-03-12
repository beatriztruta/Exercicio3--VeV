
package junit5Tests;

import com.example.processadorcontas.model.*;
import com.example.processadorcontas.service.ProcessadorContasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ParticaoEquivalenciaTests {
    private ProcessadorContasService service;

    @BeforeEach
    void setUp() {
        service = new ProcessadorContasService();
    }

    @Test
    void valorValidoDentroParticao() {
        Conta conta = new Conta();
        conta.setTipoPagamento(TipoPagamento.BOLETO);
        conta.setValorPago(new BigDecimal("2500"));
        conta.setDataConta(LocalDate.now());
        conta.setDataPagamento(LocalDate.now());

        Fatura fatura = new Fatura();
        fatura.setValorTotal(new BigDecimal("2500"));
        fatura.setData(LocalDate.now());

        Fatura resultado = service.processarFatura(fatura, List.of(conta));
        assertEquals("PAGA", resultado.getStatus());
    }

    @Test
    void valorForaDaParticaoBoleto() {
        Conta conta = new Conta();
        conta.setTipoPagamento(TipoPagamento.BOLETO);
        conta.setValorPago(new BigDecimal("6000"));
        conta.setDataConta(LocalDate.now());
        conta.setDataPagamento(LocalDate.now());

        Fatura fatura = new Fatura();
        fatura.setValorTotal(new BigDecimal("6000"));
        fatura.setData(LocalDate.now());

        assertThrows(IllegalArgumentException.class, () -> {
            service.processarFatura(fatura, List.of(conta));
        });
    }
}
