package main.java.com.example.processadorcontas.service;

import com.example.processadorcontas.model.Conta;
import com.example.processadorcontas.model.Fatura;
import com.example.processadorcontas.model.Pagamento;
import com.example.processadorcontas.model.TipoPagamento;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProcessadorContasService {

    public Fatura processarFatura(Fatura fatura, List<Conta> contas) {
        BigDecimal somaPagamentos = BigDecimal.ZERO;
        List<Pagamento> pagamentosGerados = new ArrayList<>();

        for (Conta conta : contas) {
            Pagamento pagamento = new Pagamento();
            pagamento.setTipoPagamento(conta.getTipoPagamento());
            pagamento.setDataPagamento(conta.getDataPagamento());
            BigDecimal valor = conta.getValorPago();

            if (conta.getTipoPagamento() == TipoPagamento.BOLETO) {
                // Validação: valor mínimo R$ 0,01 e máximo R$ 5.000,00
                if (valor.compareTo(new BigDecimal("0.01")) < 0 || valor.compareTo(new BigDecimal("5000.00")) > 0) {
                    throw new IllegalArgumentException("Valor do boleto fora do limite permitido");
                }
                // Se o boleto foi pago com atraso, adiciona 10%
                if (conta.getDataPagamento().isAfter(conta.getDataConta())) {
                    valor = valor.multiply(new BigDecimal("1.10"));
                }
            } else if (conta.getTipoPagamento() == TipoPagamento.CARTAO_CREDITO) {
                // Somente inclui se a data da conta for de pelo menos 15 dias anterior à data
                // da fatura
                long dias = ChronoUnit.DAYS.between(conta.getDataConta(), fatura.getData());
                if (dias < 15) {
                    continue; // Não inclui este pagamento
                }
            } else if (conta.getTipoPagamento() == TipoPagamento.TRANSFERENCIA_BANCARIA) {
                // Só inclui se a data da conta for igual ou anterior à data da fatura
                if (conta.getDataConta().isAfter(fatura.getData())) {
                    continue;
                }
            }

            pagamento.setValor(valor);
            pagamentosGerados.add(pagamento);
            somaPagamentos = somaPagamentos.add(valor);
        }

        // Define o status da fatura conforme a soma dos pagamentos válidos
        if (somaPagamentos.compareTo(fatura.getValorTotal()) >= 0) {
            fatura.setStatus("PAGA");
        } else {
            fatura.setStatus("PENDENTE");
        }
        fatura.setPagamentos(pagamentosGerados);
        return fatura;
    }
}
