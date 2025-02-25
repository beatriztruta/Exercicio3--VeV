package test.java.functionalTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.processadorcontas.Application;

@SpringBootTest(classes = Application.class)
class ProcessadorContasApplicationTests {

    @Test
    public void contextLoads() {
        // Teste para verificar se o contexto Spring Boot carrega corretamente.
    }
}
