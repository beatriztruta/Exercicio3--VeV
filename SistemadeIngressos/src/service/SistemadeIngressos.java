package service;

import model.Show;
import model.Ingresso;
import util.TipoIngresso;

import java.util.ArrayList;
import java.util.List;

public class SistemadeIngressos {
    private final int id;
    private final List<Ingresso> ingressos;
    private double desconto;

    public SistemadeIngressos(int id, int quantidade, double precoBase, double desconto) {
        if (desconto > 0.25) {
            throw new IllegalArgumentException("Desconto não pode exceder 25%");
        }

        this.id = id;
        this.desconto = desconto;
        this.ingressos = new ArrayList<>();
        int vipCount = (int) (quantidade * 0.2);
        int meiaEntradaCount = (int) (quantidade * 0.1);
        int normalCount = quantidade - vipCount - meiaEntradaCount;

        for (int i = 0; i < vipCount; i++) {
            ingressos.add(new Ingresso(i + 1, TipoIngresso.VIP, precoBase * 2));
        }
        for (int i = vipCount; i < vipCount + meiaEntradaCount; i++) {
            ingressos.add(new Ingresso(i + 1, TipoIngresso.MEIA_ENTRADA, precoBase / 2));
        }
        for (int i = vipCount + meiaEntradaCount; i < quantidade; i++) {
            ingressos.add(new Ingresso(i + 1, TipoIngresso.NORMAL, precoBase));
        }
    }

    public int getId() {
        return id;
    }

    public List<Ingresso> getIngressos() {
        return ingressos;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        if (desconto > 0.25) {
            throw new IllegalArgumentException("Desconto não pode exceder 25%");
        }
        this.desconto = desconto;
    }

    public double calcularReceita() {
        return ingressos.stream()
                .filter(Ingresso::isVendido)
                .mapToDouble(ingresso -> ingresso.getPreco() * (1 - desconto))
                .sum();
    }
}
