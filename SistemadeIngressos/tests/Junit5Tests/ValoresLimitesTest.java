package Junit5Tests;

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
    void testVendaTotalIngressosLote() {
        int totalVendido = 10;
        assertEquals(10, sistema.getIngressos().size());
    }

    // novos testes que cobrem possiveis faltas dos sistema

    @Test
    public void testTentarVenderIngressoJaVendido() {
        Ingresso ingresso = new Ingresso(1, TipoIngresso.NORMAL, 100.0);
        ingresso.marcarComoVendido();

        assertThrows(IllegalStateException.class, ingresso::marcarComoVendido);
    }

    @Test
    public void testCalculoReceitaComDesconto() {
        SistemadeIngressos sistema = new SistemadeIngressos(1, 5, 200.0, 0.1);
        List<Ingresso> ingressos = sistema.getIngressos();

        ingressos.forEach(Ingresso::marcarComoVendido);

        assertEquals(900.0, sistema.calcularReceita(), 0.01); // 5 ingressos com 10% desconto
    }

    
}