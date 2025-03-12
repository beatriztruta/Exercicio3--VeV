package com.example.Ingressos.junit5Tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import com.exemplo.service.SistemadeIngressos;
import com.exemplo.model.Show;
import com.exemplo.model.Ingresso;
import com.exemplo.model.TipoIngresso;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EquivalenciaTest {
    private SistemadeIngressos sistema;

    @BeforeEach
    void setUp() {
        sistema = new SistemadeIngressos(1, 500, 10.00, 0.0);
    }

    @Test
    void testDistribuicaoIngressoVIP() {
        long countVIP = sistema.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.VIP)
                .count();
        assertEquals(100, countVIP);
    }

    @Test
    void testDistribuicaoIngressoMeiaEntrada() {
        long countMeiaEntrada = sistema.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.MEIA_ENTRADA)
                .count();
        assertEquals(50, countMeiaEntrada);
    }

    @Test
    void testDistribuicaoIngressoNormal() {
        long countNormal = sistema.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.NORMAL)
                .count();
        assertEquals(350, countNormal);
    }

    @Test
    void testTentativaVendaAcimaCapacidade() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            if (sistema.getIngressos().size() >= 501) {
                throw new IllegalStateException("Erro: ingressos esgotados");
            }
        });

        assertEquals("Erro: ingressos esgotados", exception.getMessage());
    }

    @Test
    public void testCalculoReceitaLiquidaLucro() {
        Show show = new Show("Concerto", "Artista", 2000.0, 1000.0, false);
        show.adicionarLote(1, 500, 50.0, 0.0);
        show.venderTodosIngressos();

        assertEquals("LUCRO", show.calcularStatusFinanceiro(), "O status financeiro deve ser LUCRO");
    }

    @Test
    public void testCalculoReceitaLiquidaEstavel() {
        Show show = new Show("Concerto", "Artista", 2000.0, 1300.0, false);
        show.adicionarLote(1, 500, 50.0, 0.0);
        show.venderTodosIngressos();

        assertEquals("ESTÁVEL", show.calcularStatusFinanceiro(), "O status financeiro deve ser ESTÁVEL");
    }

    @Test
    public void testCalculoReceitaLiquidaPrejuizo() {
        Show show = new Show("Concerto", "Artista", 3000.0, 1000.0, false);
        show.adicionarLote(1, 500, 50.0, 0.0);
        show.venderTodosIngressos();

        assertEquals("PREJUÍZO", show.calcularStatusFinanceiro(), "O status financeiro deve ser PREJUÍZO");
    }

    // Novos testes cobrindo possiveis faltas observadas


    @Test
    public void testCriarSistemaComDescontoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SistemadeIngressos(1, 50, 50.0, 0.3);
        });
    }

    @Test
    public void testVenderParcialmenteIngressos() {
        SistemadeIngressos sistema = new SistemadeIngressos(1, 10, 100.0, 0.05);
        List<Ingresso> ingressos = sistema.getIngressos();

        ingressos.get(0).marcarComoVendido();
        ingressos.get(1).marcarComoVendido();

        assertEquals(2, ingressos.stream().filter(Ingresso::isVendido).count());
    }

    @Test
    public void testTentarVenderIngressoJaVendido() {
        Ingresso ingresso = new Ingresso(1, TipoIngresso.NORMAL, 100.0);
        ingresso.marcarComoVendido();

        assertThrows(IllegalStateException.class, ingresso::marcarComoVendido);
    }
}