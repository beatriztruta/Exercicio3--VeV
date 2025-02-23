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

public class ParticaoEquivalenciaTests {

    private final ProcessadorContasService service = new ProcessadorContasService();

    // Teste: Cartão de crédito com data válida (diferença de 15 dias ou mais)
    @Test
    public void testCartaoCreditoDataValida() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.of(2023, 5, 20));
        fatura.setValorTotal(new BigDecimal("200.00"));
        fatura.setNomeCliente("Teste");

        Conta conta = new Conta();
        conta.setCodigoConta("EQ1");
        // Data da conta 15 dias antes da fatura
        conta.setDataConta(LocalDate.of(2023, 5, 5));
        conta.setDataPagamento(LocalDate.of(2023, 5, 5));
        conta.setValorPago(new BigDecimal("200.00"));
        conta.setTipoPagamento(TipoPagamento.CARTAO_CREDITO);

        Fatura resultado = service.processarFatura(fatura, Collections.singletonList(conta));
        Assertions.assertEquals("PAGA", resultado.getStatus());
    }

    // Teste: Cartão de crédito com data inválida (diferença menor que 15 dias)
    @Test
    public void testCartaoCreditoDataInvalida() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.of(2023, 5, 20));
        fatura.setValorTotal(new BigDecimal("200.00"));
        fatura.setNomeCliente("Teste");

        Conta conta = new Conta();
        conta.setCodigoConta("EQ2");
        // Data da conta com apenas 14 dias de diferença
        conta.setDataConta(LocalDate.of(2023, 5, 7));
        conta.setDataPagamento(LocalDate.of(2023, 5, 7));
        conta.setValorPago(new BigDecimal("200.00"));
        conta.setTipoPagamento(TipoPagamento.CARTAO_CREDITO);

        Fatura resultado = service.processarFatura(fatura, Collections.singletonList(conta));
        // Pagamento não incluído; fatura permanece PENDENTE
        Assertions.assertEquals("PENDENTE", resultado.getStatus());
    }

    // Teste: Transferência bancária com data válida (data da conta igual ou
    // anterior à fatura)
    @Test
    public void testTransferenciaDataValida() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.of(2023, 6, 15));
        fatura.setValorTotal(new BigDecimal("300.00"));
        fatura.setNomeCliente("Teste");

        Conta conta = new Conta();
        conta.setCodigoConta("EQ3");
        // Data da conta igual à data da fatura
        conta.setDataConta(LocalDate.of(2023, 6, 15));
        conta.setDataPagamento(LocalDate.of(2023, 6, 15));
        conta.setValorPago(new BigDecimal("300.00"));
        conta.setTipoPagamento(TipoPagamento.TRANSFERENCIA_BANCARIA);

        Fatura resultado = service.processarFatura(fatura, Collections.singletonList(conta));
        Assertions.assertEquals("PAGA", resultado.getStatus());
    }

    // Teste: Transferência bancária com data inválida (data da conta posterior à
    // fatura)
    @Test
    public void testTransferenciaDataInvalida() {
        Fatura fatura = new Fatura();
        fatura.setData(LocalDate.of(2023, 6, 15));
        fatura.setValorTotal(new BigDecimal("300.00"));
        fatura.setNomeCliente("Teste");

        Conta conta = new Conta();
        conta.setCodigoConta("EQ4");
        // Data da conta posterior à data da fatura
        conta.setDataConta(LocalDate.of(2023, 6, 16));
        conta.setDataPagamento(LocalDate.of(2023, 6, 16));
        conta.setValorPago(new BigDecimal("300.00"));
        conta.setTipoPagamento(TipoPagamento.TRANSFERENCIA_BANCARIA);

        Fatura resultado = service.processarFatura(fatura, Collections.singletonList(conta));
        Assertions.assertEquals("PENDENTE", resultado.getStatus());
    }
}
