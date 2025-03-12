package Junit5Tests;

import model.Ingresso;
import model.Show;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.SistemadeIngressos;
import util.TipoIngresso;

import java.util.List;

import static org.junit.Assert.*;

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
    public void testCriarShowSemIngressos() {
        Show show = new Show("Show Teste", "Artista X", 1000.0, 2000.0, false);
        assertEquals(0, show.getLotes().size());
    }