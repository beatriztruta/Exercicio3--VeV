package test.java.functionalTests;

import com.example.processadorcontas.model.Conta;
import com.example.processadorcontas.model.Fatura;
import com.example.processadorcontas.model.TipoPagamento;
import com.example.processadorcontas.service.ProcessadorContasService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

public class AnaliseDeValoresLimitesTests {

    private final ProcessadorContasService service = new ProcessadorContasService();

    // Teste: Boleto com valor mínimo (0,01) e pagamento em dia.
    @Test
    public void testBoletoValorMinimo() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.of(2023, 2, 20));
        fatura.setValorTotal(new BigDecimal("0.01"));
        fatura.setNomeCliente("Teste");

        Conta conta = new Conta();
        conta.setCodigoConta("B01");
        conta.setDataConta(LocalDate.of(2023, 2, 20));
        conta.setDataPagamento(LocalDate.of(2023, 2, 20));
        conta.setValorPago(new BigDecimal("0.01"));
        conta.setTipoPagamento(TipoPagamento.BOLETO);

        Fatura resultado = service.processarFatura(fatura, Collections.singletonList(conta));
        Assertions.assertEquals("PAGA", resultado.getStatus());
    }

    // Teste: Boleto com valor abaixo do mínimo (0,00) – deve lançar exceção.
    @Test
    public void testBoletoValorAbaixoMinimo() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.of(2023, 2, 20));
        fatura.setValorTotal(new BigDecimal("10.00"));
        fatura.setNomeCliente("Teste");

        Conta conta = new Conta();
        conta.setCodigoConta("B02");
        conta.setDataConta(LocalDate.of(2023, 2, 20));
        conta.setDataPagamento(LocalDate.of(2023, 2, 20));
        conta.setValorPago(new BigDecimal("0.00")); // Valor inválido
        conta.setTipoPagamento(TipoPagamento.BOLETO);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.processarFatura(fatura, Collections.singletonList(conta));
        });
    }

    // Teste: Boleto com valor máximo válido (5000,00).
    @Test
    public void testBoletoValorMaximo() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.of(2023, 3, 1));
        fatura.setValorTotal(new BigDecimal("5000.00"));
        fatura.setNomeCliente("Teste");

        Conta conta = new Conta();
        conta.setCodigoConta("B03");
        conta.setDataConta(LocalDate.of(2023, 3, 1));
        conta.setDataPagamento(LocalDate.of(2023, 3, 1));
        conta.setValorPago(new BigDecimal("5000.00"));
        conta.setTipoPagamento(TipoPagamento.BOLETO);

        Fatura resultado = service.processarFatura(fatura, Collections.singletonList(conta));
        Assertions.assertEquals("PAGA", resultado.getStatus());
    }

    // Teste: Boleto com valor acima do máximo (5000,01) – deve lançar exceção.
    @Test
    public void testBoletoValorAcimaMaximo() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.of(2023, 3, 1));
        fatura.setValorTotal(new BigDecimal("6000.00"));
        fatura.setNomeCliente("Teste");

        Conta conta = new Conta();
        conta.setCodigoConta("B04");
        conta.setDataConta(LocalDate.of(2023, 3, 1));
        conta.setDataPagamento(LocalDate.of(2023, 3, 1));
        conta.setValorPago(new BigDecimal("5000.01")); // Valor acima do máximo permitido
        conta.setTipoPagamento(TipoPagamento.BOLETO);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.processarFatura(fatura, Collections.singletonList(conta));
        });
    }

    // Teste: Boleto com pagamento atrasado – aplicar acréscimo de 10%.
    @Test
    public void testBoletoPagamentoAtrasado() {
        Fatura fatura = new Fatura();
        // Valor ajustado: 1000 * 1.10 = 1100,00
        fatura.setData(LocalDate.of(2023, 4, 20));
        fatura.setValorTotal(new BigDecimal("1100.00"));
        fatura.setNomeCliente("Teste");

        Conta conta = new Conta();
        conta.setCodigoConta("B05");
        // Data da conta anterior à data de pagamento (indica atraso)
        conta.setDataConta(LocalDate.of(2023, 4, 10));
        conta.setDataPagamento(LocalDate.of(2023, 4, 15));
        conta.setValorPago(new BigDecimal("1000.00"));
        conta.setTipoPagamento(TipoPagamento.BOLETO);

        Fatura resultado = service.processarFatura(fatura, Collections.singletonList(conta));
        Assertions.assertEquals("PAGA", resultado.getStatus());
    }
}
