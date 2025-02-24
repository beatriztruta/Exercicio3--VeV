import model.Show;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Ingresso;
import service.SistemadeIngressos;
import util.TipoIngresso;

import static org.junit.jupiter.api.Assertions.*;

class SistemadeIngressosTest {

    private Show show;

    @BeforeEach
    void setUp() {
        show = new Show("Show Exemplo", "Artista Exemplo", 1000.0, 2000.0, true);
        show.adicionarLote(1, 500, 10.0, 0.15);
    }

    @Test
    void testCriacaoShow() {
        assertEquals("Artista Exemplo", show.getArtista());
        assertEquals(1000.0, show.getCache());
        assertEquals(2000.0, show.getDespesasInfraestrutura());
        assertTrue(show.isDataEspecial());
    }

    @Test
    void testDistribuicaoIngressos() {
        SistemadeIngressos lote = show.getLotes().get(0);
        int totalIngressos = lote.getIngressos().size();

        long vipCount = lote.getIngressos().stream().filter(i -> i.getTipo() == TipoIngresso.VIP).count();
        long meiaEntradaCount = lote.getIngressos().stream().filter(i -> i.getTipo() == TipoIngresso.MEIA_ENTRADA).count();
        long normalCount = lote.getIngressos().stream().filter(i -> i.getTipo() == TipoIngresso.NORMAL).count();

        assertEquals(500, totalIngressos);
        assertEquals(100, vipCount);
        assertEquals(50, meiaEntradaCount);
        assertEquals(350, normalCount);
    }

    @Test
    void testVendaIngressos() {
        SistemadeIngressos lote = show.getLotes().get(0);
        Ingresso ingresso = lote.getIngressos().get(0);

        assertFalse(ingresso.isVendido());
        ingresso.marcarComoVendido();
        assertTrue(ingresso.isVendido());
    }

    @Test
    void testStatusFinanceiro() {
        show.venderTodosIngressos();
        String statusFinanceiro = show.calcularStatusFinanceiro();
        assertEquals("LUCRO", statusFinanceiro);
    }
}