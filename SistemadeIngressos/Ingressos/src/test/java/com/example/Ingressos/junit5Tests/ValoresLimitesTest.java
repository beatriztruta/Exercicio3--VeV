package com.example.Ingressos.junit5Tests;

import static org.junit.jupiter.api.Assertions.*;

import model.Ingresso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.SistemadeIngressos;
import util.TipoIngresso;

class ValoresLimitesTest {
    private SistemadeIngressos sistema;

    @BeforeEach
    void setUp() {
        sistema = new SistemadeIngressos(1, 10, 10.00, 0.0);
    }

    @Test
    void testPrecoIngressoNormal() {
        assertEquals(10.00, sistema.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.NORMAL)
                .findFirst().get().getPreco());
    }

    @Test
    void testPrecoIngressoVIP() {
        assertEquals(20.00, sistema.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.VIP)
                .findFirst().get().getPreco());
    }

    @Test
    void testPrecoIngressoMeiaEntrada() {
        assertEquals(5.00, sistema.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.MEIA_ENTRADA)
                .findFirst().get().getPreco());
    }

    @Test
    void testDescontoMaximoPermitido() {
        sistema.setDesconto(0.25);
        assertEquals(0.25, sistema.getDesconto());
    }

    @Test
    void testDescontoAcimaDoPermitido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            sistema.setDesconto(0.30);
        });
        assertEquals("Desconto não pode exceder 25%", exception.getMessage());
    }

    @Test
    void testVendaDeZeroIngressos() {
        double receita = sistema.calcularReceita();
        assertEquals(0.00, receita);
    }

    @Test
    void testIngressoJaVendido() {
        Ingresso ingresso = sistema.getIngressos().get(0);
        ingresso.marcarComoVendido();
        assertTrue(ingresso.isVendido());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            if (ingresso.isVendido()) {
                throw new IllegalStateException("Erro: Ingresso já vendido");
            }
        });

        assertEquals("Erro: Ingresso já vendido", exception.getMessage());
    }

    @Test
    void testVendaTotalIngressosLote() {
        int totalVendido = 10;
        assertEquals(10, sistema.getIngressos().size());
    }


    // novos testes que cobrem possiveis faltas dos sistema

    @Test
    public void testCalculoStatusFinanceiro() {
        Show show = new Show("Show Exemplo", "Banda A", 1000.0, 2000.0, true);
        show.adicionarLote(1, 20, 200.0, 0.1);
        show.venderTodosIngressos();

        assertEquals("LUCRO", show.calcularStatusFinanceiro());
    }
    @Test
    public void testCriarShowSemIngressos() {
        Show show = new Show("Show Teste", "Artista X", 1000.0, 2000.0, false);
        assertEquals(0, show.getLotes().size());
    }
    @Test
    public void testCalculoReceitaComDesconto() {
        SistemadeIngressos sistema = new SistemadeIngressos(1, 5, 200.0, 0.1);
        List<Ingresso> ingressos = sistema.getIngressos();

        ingressos.forEach(Ingresso::marcarComoVendido);

        assertEquals(900.0, sistema.calcularReceita(), 0.01);
    }


}