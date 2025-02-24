package TestesFuncionais;

import model.Show;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TabelaDeDecisaoTest {
    @Test
    public void testRegraNegocio1_Lucro() {
        Show show = new Show("Show Especial", "Artista X", 1000.0, 2000.0, true);
        show.adicionarLote(1, 500, 10.0, 0.0); // Receita TOTAL = 5000

        show.venderTodosIngressos();

        assertEquals(1700.0, show.calcularReceitaLiquida(), 0.01, "Receita líquida incorreta para Regra 1");
        assertEquals("LUCRO", show.calcularStatusFinanceiro(), "Status financeiro deveria ser LUCRO");
    }

    @Test
    public void testRegraNegocio2_Estavel() {
        Show show = new Show("Show Normal", "Artista Y", 1000.0, 2000.0, false);
        show.adicionarLote(1, 500, 6.6, 0.0); // Receita TOTAL = 3300

        show.venderTodosIngressos();

        assertEquals(300.0, show.calcularReceitaLiquida(), 0.01, "Receita líquida incorreta para Regra 2");
        assertEquals("ESTÁVEL", show.calcularStatusFinanceiro(), "Status financeiro deveria ser ESTÁVEL");
    }

    @Test
    public void testRegraNegocio3_Prejuizo() {
        Show show = new Show("Show com custo alto", "Artista Z", 1000.0, 2000.0, true);
        show.adicionarLote(1, 500, 5.0, 0.0); // Receita TOTAL = 2500

        show.venderTodosIngressos();

        assertEquals(-800.0, show.calcularReceitaLiquida(), 0.01, "Receita líquida incorreta para Regra 3");
        assertEquals("PREJUÍZO", show.calcularStatusFinanceiro(), "Status financeiro deveria ser PREJUÍZO");
    }
}
