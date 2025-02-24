package TestesFuncionais;

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
}

