// src/main/java/sistemaagricola/com/projecto/controllers/ManutencaoController.java
package sistemaagricola.com.projecto.controllers;

import sistemaagricola.com.projecto.models.Manutencao;
import sistemaagricola.com.projecto.service.ManutencaoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/manutencoes")
public class ManutencaoController {
  private final ManutencaoService svc;
  public ManutencaoController(ManutencaoService svc){ this.svc=svc; }

  @PostMapping public Manutencao abrir(@RequestBody Manutencao m){ return svc.abrir(m); }
  @PatchMapping("/{id}/fechar") public Manutencao fechar(@PathVariable Long id){ return svc.fechar(id); }
  @GetMapping("/por-dispositivo/{dispositivoId}")
  public List<Manutencao> listar(@PathVariable Long dispositivoId, @RequestParam String estado){
    return svc.listarPorDispositivoEEstado(dispositivoId, estado);
  }
}
