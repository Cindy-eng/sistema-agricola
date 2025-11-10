// src/main/java/sistemaagricola/com/projecto/controllers/RegraAlertaController.java
package sistemaagricola.com.projecto.controllers;

import sistemaagricola.com.projecto.models.RegraAlerta;
import sistemaagricola.com.projecto.service.RegraAlertaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/regras")
public class RegraAlertaController {
  private final RegraAlertaService svc;
  public RegraAlertaController(RegraAlertaService svc){ this.svc=svc; }

  @PostMapping public RegraAlerta criar(@RequestBody RegraAlerta r){ return svc.criar(r); }
  @PatchMapping("/{id}/activo/{activo}") public RegraAlerta activar(@PathVariable Long id, @PathVariable boolean activo){ return svc.activar(id, activo); }
  @GetMapping("/activas/por-parcela/{parcelaId}") public List<RegraAlerta> listarActivas(@PathVariable Long parcelaId){ return svc.listarActivasPorParcela(parcelaId); }
  @DeleteMapping("/{id}") public void remover(@PathVariable Long id){ svc.remover(id); }
}
