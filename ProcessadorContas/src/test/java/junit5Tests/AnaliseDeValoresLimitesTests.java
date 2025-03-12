
package junit5Tests;

import com.example.processadorcontas.model.*;
import com.example.processadorcontas.service.ProcessadorContasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AnaliseDeValoresLimitesTests {
    private ProcessadorContasService service;

    @BeforeEach
    void setUp() {
        service = new ProcessadorContasService();
    }

    @Test
    void limiteInferiorBoleto() {
        Conta conta = new Conta();
        conta.setTipoPagamento(TipoPagamento.BOLETO);
        conta.setValorPago(new BigDecimal("0.00")); // valor inferior permitido
        conta.setDataConta(LocalDate.now());
        conta.setDataPagamento(LocalDate.now());

        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.now());

        assertThrows(IllegalArgumentException.class, () -> {
            service.processarFatura(fatura, List.of(conta));
        });
    }

    @Test
    void limiteSuperiorBoleto() {
        Conta conta = new Conta();
        conta.setTipoPagamento(TipoPagamento.BOLETO);
        conta.setValorPago(new BigDecimal("5000.01")); // valor superior permitido
        conta.setDataConta(LocalDate.now());
        conta.setDataPagamento(LocalDate.now());

        assertThrows(IllegalArgumentException.class, () -> {
            service.processarFatura(new Fatura(), List.of(conta));
        });
    }
}
