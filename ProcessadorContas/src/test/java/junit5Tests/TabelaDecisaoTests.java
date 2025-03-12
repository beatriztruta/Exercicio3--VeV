
package junit5Tests;

import com.example.processadorcontas.model.*;
import com.example.processadorcontas.service.ProcessadorContasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TabelaDecisaoTests {
    private ProcessadorContasService service;

    @BeforeEach
    void setUp() {
        service = new ProcessadorContasService();
    }

    @Test
    void decisaoCartaoCreditoDataInferior15Dias() {
        Conta conta = new Conta();
        conta.setTipoPagamento(TipoPagamento.CARTAO_CREDITO);
        conta.setValorPago(new BigDecimal("100"));
        conta.setDataConta(LocalDate.now().minusDays(10)); // menos de 15 dias
        conta.setDataPagamento(LocalDate.now());

        Fatura fatura = new Fatura();
        fatura.setValorTotal(new BigDecimal("100"));
        fatura.setData(LocalDate.now());

        Fatura resultado = service.processarFatura(fatura, List.of(conta));
        assertEquals("PENDENTE", resultado.getStatus());
    }

    @Test
    void pagamentoPorTransferenciaDataFutura() {
        Conta conta = new Conta();
        conta.setTipoPagamento(TipoPagamento.TRANSFERENCIA_BANCARIA);
        conta.setValorPago(new BigDecimal("100"));
        conta.setDataConta(LocalDate.now().plusDays(1)); // data futura

        Fatura fatura = new Fatura();
        fatura.setValorTotal(new BigDecimal("100"));
        fatura.setData(LocalDate.now());

        Fatura resultado = service.processarFatura(fatura, List.of(conta));
        assertEquals("PENDENTE", resultado.getStatus());
    }
}
