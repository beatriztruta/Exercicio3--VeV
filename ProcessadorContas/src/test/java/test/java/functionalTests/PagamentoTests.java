package test.java.functionalTests;

import com.example.processadorcontas.model.Conta;
import com.example.processadorcontas.model.Fatura;
import com.example.processadorcontas.model.TipoPagamento;
import com.example.processadorcontas.service.ProcessadorContasService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class PagamentoTests {

    private final ProcessadorContasService service = new ProcessadorContasService();

    // Teste: Múltiplos pagamentos válidos (Boleto, Cartão de Crédito e
    // Transferência) que, somados, pagam a fatura.
    @Test
    public void testProcessamentoMultiploPagamentos() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.of(2023, 10, 20));
        fatura.setValorTotal(new BigDecimal("1500.00"));
        fatura.setNomeCliente("Teste");

        // Boleto (pagamento em dia)
        Conta conta1 = new Conta();
        conta1.setCodigoConta("P1");
        conta1.setDataConta(LocalDate.of(2023, 10, 20));
        conta1.setDataPagamento(LocalDate.of(2023, 10, 20));
        conta1.setValorPago(new BigDecimal("500.00"));
        conta1.setTipoPagamento(TipoPagamento.BOLETO);

        // Cartão de crédito com data válida (15 dias de diferença)
        Conta conta2 = new Conta();
        conta2.setCodigoConta("P2");
        conta2.setDataConta(LocalDate.of(2023, 10, 5));
        conta2.setDataPagamento(LocalDate.of(2023, 10, 5));
        conta2.setValorPago(new BigDecimal("400.00"));
        conta2.setTipoPagamento(TipoPagamento.CARTAO_CREDITO);

        // Transferência bancária com data válida (igual à data da fatura)
        Conta conta3 = new Conta();
        conta3.setCodigoConta("P3");
        conta3.setDataConta(LocalDate.of(2023, 10, 20));
        conta3.setDataPagamento(LocalDate.of(2023, 10, 20));
        conta3.setValorPago(new BigDecimal("600.00"));
        conta3.setTipoPagamento(TipoPagamento.TRANSFERENCIA_BANCARIA);

        Fatura resultado = service.processarFatura(fatura, Arrays.asList(conta1, conta2, conta3));
        Assertions.assertEquals("PAGA", resultado.getStatus());
    }

    // Teste: Múltiplos pagamentos onde um pagamento inválido (cartão de crédito com
    // data inválida)
    // não é incluído, resultando em fatura PENDENTE.
    @Test
    public void testPagamentoMultiploComPagamentoInvalido() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.of(2023, 10, 20));
        fatura.setValorTotal(new BigDecimal("1500.00"));
        fatura.setNomeCliente("Teste");

        // Boleto válido
        Conta conta1 = new Conta();
        conta1.setCodigoConta("P1");
        conta1.setDataConta(LocalDate.of(2023, 10, 20));
        conta1.setDataPagamento(LocalDate.of(2023, 10, 20));
        conta1.setValorPago(new BigDecimal("500.00"));
        conta1.setTipoPagamento(TipoPagamento.BOLETO);

        // Cartão de crédito inválido (diferença de data menor que 15 dias)
        Conta conta2 = new Conta();
        conta2.setCodigoConta("P2");
        conta2.setDataConta(LocalDate.of(2023, 10, 10)); // Apenas 10 dias de diferença
        conta2.setDataPagamento(LocalDate.of(2023, 10, 10));
        conta2.setValorPago(new BigDecimal("400.00"));
        conta2.setTipoPagamento(TipoPagamento.CARTAO_CREDITO);

        // Transferência válida
        Conta conta3 = new Conta();
        conta3.setCodigoConta("P3");
        conta3.setDataConta(LocalDate.of(2023, 10, 20));
        conta3.setDataPagamento(LocalDate.of(2023, 10, 20));
        conta3.setValorPago(new BigDecimal("600.00"));
        conta3.setTipoPagamento(TipoPagamento.TRANSFERENCIA_BANCARIA);

        // Somente boleto (500) e transferência (600) são incluídos; soma = 1100 < 1500.
        Fatura resultado = service.processarFatura(fatura, Arrays.asList(conta1, conta2, conta3));
        Assertions.assertEquals("PENDENTE", resultado.getStatus());
    }
}
