package model;

import service.SistemadeIngressos;
import java.util.ArrayList;
import java.util.List;

public class Show {
    private final String nome;
    private final String artista;
    private final double cache;
    private final double despesasInfraestrutura;
    private final boolean dataEspecial;
    private List<SistemadeIngressos> lotes;

    public Show(String nome, String artista, double cache, double despesasInfraestrutura, boolean dataEspecial) {
        this.nome = nome;
        this.artista = artista;
        this.cache = cache;
        this.despesasInfraestrutura = despesasInfraestrutura;
        this.dataEspecial = dataEspecial;
        this.lotes = new ArrayList<>();
    }

    public String getArtista() {
        return artista;
    }

    public double getCache() {
        return cache;
    }

    public double getDespesasInfraestrutura() {
        return despesasInfraestrutura;
    }

    public boolean isDataEspecial() {
        return dataEspecial;
    }

    public void adicionarLote(int id, int quantidade, double precoBase, double desconto) {
        lotes.add(new SistemadeIngressos(id, quantidade, precoBase, desconto));
    }

    public List<SistemadeIngressos> getLotes() {
        return lotes;
    }

    public void venderTodosIngressos() {
        lotes.forEach(lote -> lote.getIngressos().forEach(Ingresso::marcarComoVendido));
    }

    public double calcularReceitaLiquida() {
        double receitaBruta = lotes.stream().mapToDouble(SistemadeIngressos::calcularReceita).sum();
        double despesasTotais = cache + despesasInfraestrutura * (dataEspecial ? 1.15 : 1);
        return receitaBruta - despesasTotais;
    }

    public String calcularStatusFinanceiro() {
        double receitaLiquida = calcularReceitaLiquida();
        if (receitaLiquida > 0) {
            return "LUCRO";
        } else if (receitaLiquida == 0) {
            return "ESTÁVEL";
        } else {
            return "PREJUÍZO";
        }
    }
}
