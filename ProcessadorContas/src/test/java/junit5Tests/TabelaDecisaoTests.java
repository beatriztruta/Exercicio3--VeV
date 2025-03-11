package junit5Tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TabelaDecisaoTests {

    @Test
    void testeCenarioDecisaoInvalido() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            decisaoService.aplicarDecisao("cenário inválido");
        });
        assertEquals("Cenário inválido para tomada de decisão", exception.getMessage());
    }

    @Test
    void testeDecisaoCaminhoAlternativo() {
        String resultado = decisaoService.aplicarDecisao("caminho alternativo");
        assertEquals("Alternativa aplicada com sucesso", resultado);
    }

    @Test
    void testeDecisaoNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            decisaoService.aplicarDecisao(null);
        });
    }
}
