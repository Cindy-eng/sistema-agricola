package sistemaagricola.com.projecto.controllers;



import sistemaagricola.com.projecto.models.Cultura;
import sistemaagricola.com.projecto.service.CulturaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
        import java.util.List;

@RestController
@RequestMapping("/api/culturas")
public class CulturaController {
    private final CulturaService svc;
    public CulturaController(CulturaService svc){ this.svc=svc; }

    @PostMapping public Cultura criar(@RequestBody @Valid Cultura c){ return svc.criar(c); }
    @GetMapping("/{id}") public Cultura obter(@PathVariable Long id){ return svc.obter(id); }
    @GetMapping public List<Cultura> listar(){ return svc.listar(); }
    @PutMapping("/{id}") public Cultura actualizar(@PathVariable Long id, @RequestBody @Valid Cultura c){ return svc.actualizar(id,c); }
    @DeleteMapping("/{id}") public void remover(@PathVariable Long id){ svc.remover(id); }
}
