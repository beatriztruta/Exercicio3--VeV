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

public class TabelaDecisaoTests {

    private final ProcessadorContasService service = new ProcessadorContasService();

    // Decisão: Boleto pago em dia – deve ser considerado.
    @Test
    public void testDecisaoBoletoEmDia() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.of(2023, 7, 10));
        fatura.setValorTotal(new BigDecimal("100.00"));
        fatura.setNomeCliente("Teste");

        Conta conta = new Conta();
        conta.setCodigoConta("TD1");
        conta.setDataConta(LocalDate.of(2023, 7, 10));
        conta.setDataPagamento(LocalDate.of(2023, 7, 10));
        conta.setValorPago(new BigDecimal("100.00"));
        conta.setTipoPagamento(TipoPagamento.BOLETO);

        Fatura resultado = service.processarFatura(fatura, Collections.singletonList(conta));
        Assertions.assertEquals("PAGA", resultado.getStatus());
    }

    // Decisão: Boleto pago com atraso – aplica acréscimo de 10%.
    @Test
    public void testDecisaoBoletoAtrasado() {
        Fatura fatura = new Fatura();
        // Valor ajustado: 500 + 10% = 550
        fatura.setData(LocalDate.of(2023, 7, 20));
        fatura.setValorTotal(new BigDecimal("550.00"));
        fatura.setNomeCliente("Teste");

        Conta conta = new Conta();
        conta.setCodigoConta("TD2");
        conta.setDataConta(LocalDate.of(2023, 7, 10));
        conta.setDataPagamento(LocalDate.of(2023, 7, 15)); // Atraso
        conta.setValorPago(new BigDecimal("500.00"));
        conta.setTipoPagamento(TipoPagamento.BOLETO);

        Fatura resultado = service.processarFatura(fatura, Collections.singletonList(conta));
        Assertions.assertEquals("PAGA", resultado.getStatus());
    }

    // Decisão: Cartão de crédito com data válida (≥15 dias) – deve ser incluído.
    @Test
    public void testDecisaoCartaoCreditoValido() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.of(2023, 8, 20));
        fatura.setValorTotal(new BigDecimal("250.00"));
        fatura.setNomeCliente("Teste");

        Conta conta = new Conta();
        conta.setCodigoConta("TD3");
        conta.setDataConta(LocalDate.of(2023, 8, 5));
        conta.setDataPagamento(LocalDate.of(2023, 8, 5));
        conta.setValorPago(new BigDecimal("250.00"));
        conta.setTipoPagamento(TipoPagamento.CARTAO_CREDITO);

        Fatura resultado = service.processarFatura(fatura, Collections.singletonList(conta));
        Assertions.assertEquals("PAGA", resultado.getStatus());
    }

    // Decisão: Cartão de crédito com data inválida (<15 dias) – pagamento ignorado.
    @Test
    public void testDecisaoCartaoCreditoInvalido() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.of(2023, 8, 20));
        fatura.setValorTotal(new BigDecimal("250.00"));
        fatura.setNomeCliente("Teste");

        Conta conta = new Conta();
        conta.setCodigoConta("TD4");
        conta.setDataConta(LocalDate.of(2023, 8, 7)); // Apenas 13 dias de diferença
        conta.setDataPagamento(LocalDate.of(2023, 8, 7));
        conta.setValorPago(new BigDecimal("250.00"));
        conta.setTipoPagamento(TipoPagamento.CARTAO_CREDITO);

        Fatura resultado = service.processarFatura(fatura, Collections.singletonList(conta));
        Assertions.assertEquals("PENDENTE", resultado.getStatus());
    }

    // Decisão: Transferência bancária com data válida – deve ser incluído.
    @Test
    public void testDecisaoTransferenciaValida() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.of(2023, 9, 15));
        fatura.setValorTotal(new BigDecimal("300.00"));
        fatura.setNomeCliente("Teste");

        Conta conta = new Conta();
        conta.setCodigoConta("TD5");
        conta.setDataConta(LocalDate.of(2023, 9, 15));
        conta.setDataPagamento(LocalDate.of(2023, 9, 15));
        conta.setValorPago(new BigDecimal("300.00"));
        conta.setTipoPagamento(TipoPagamento.TRANSFERENCIA_BANCARIA);

        Fatura resultado = service.processarFatura(fatura, Collections.singletonList(conta));
        Assertions.assertEquals("PAGA", resultado.getStatus());
    }

    // Decisão: Transferência bancária com data inválida (posterior) – pagamento
    // ignorado.
    @Test
    public void testDecisaoTransferenciaInvalida() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.of(2023, 9, 15));
        fatura.setValorTotal(new BigDecimal("300.00"));
        fatura.setNomeCliente("Teste");

        Conta conta = new Conta();
        conta.setCodigoConta("TD6");
        conta.setDataConta(LocalDate.of(2023, 9, 16)); // Posterior à fatura
        conta.setDataPagamento(LocalDate.of(2023, 9, 16));
        conta.setValorPago(new BigDecimal("300.00"));
        conta.setTipoPagamento(TipoPagamento.TRANSFERENCIA_BANCARIA);

        Fatura resultado = service.processarFatura(fatura, Collections.singletonList(conta));
        Assertions.assertEquals("PENDENTE", resultado.getStatus());
    }
}
