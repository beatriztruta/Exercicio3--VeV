package com.example.processadorcontas.dto;

import com.example.processadorcontas.model.Conta;
import com.example.processadorcontas.model.Fatura;
import java.util.List;

public class ProcessamentoRequest {
    private Fatura fatura;
    private List<Conta> contas;

    // Getters e Setters

    public Fatura getFatura() {
        return fatura;
    }

    public void setFatura(Fatura fatura) {
        this.fatura = fatura;
    }

    public List<Conta> getContas() {
        return contas;
    }

    public void setContas(List<Conta> contas) {
        this.contas = contas;
    }
}
