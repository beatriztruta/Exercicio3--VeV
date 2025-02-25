import com.example.processadorcontas.service.ProcessadorContasService;


import com.example.processadorcontas.dto.ProcessamentoRequest;
import com.example.processadorcontas.model.Fatura;
import com.example.processadorcontas.service.ProcessadorContasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contas")
public class ProcessadorContasController {

    @Autowired
    private ProcessadorContasService processadorContasService;

    @PostMapping("/processar")
    public ResponseEntity<Fatura> processarFatura(@RequestBody ProcessamentoRequest request) {
        Fatura faturaProcessada = processadorContasService.processarFatura(request.getFatura(), request.getContas());
        return ResponseEntity.ok(faturaProcessada);
    }
}
