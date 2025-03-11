package junit5Tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParticaoEquivalenciaTests {

    @Test
    void testeParticaoInvalida() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            equivalenciaService.validarParticao(null);
        });
        assertEquals("Partição inválida", exception.getMessage());
    }

    @Test
    void testeValorDentroParticao() {
        boolean resultado = equivalenciaService.validarParticao(50);
        assertTrue(resultado, "O valor deveria estar dentro da partição válida");
    }

    @Test
    void testeValorForaParticao() {
        boolean resultado = equivalenciaService.validarParticao(150);
        assertFalse(resultado, "O valor deveria estar fora da partição válida");
    }
}
