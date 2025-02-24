package model;

import util.TipoIngresso;

public class Ingresso {
    private final int id;
    private final TipoIngresso tipo;
    private boolean vendido;
    private final double preco;

    public Ingresso(int id, TipoIngresso tipo, double preco) {
        this.id = id;
        this.tipo = tipo;
        this.preco = preco;
        this.vendido = false;
    }

    public int getId() {
        return id;
    }

    public TipoIngresso getTipo() {
        return tipo;
    }

    public boolean isVendido() {
        return vendido;
    }

    public void marcarComoVendido() {
        this.vendido = true;
    }

    public double getPreco() {
        return preco;
    }
}
