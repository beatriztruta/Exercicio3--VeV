package com.example.processadorcontas.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Pagamento {
    private BigDecimal valor;
    private LocalDate dataPagamento;
    private TipoPagamento tipoPagamento;

    // Getters e Setters

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }
}
