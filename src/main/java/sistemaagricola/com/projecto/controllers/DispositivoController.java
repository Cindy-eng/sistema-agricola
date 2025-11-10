// src/main/java/com/agro/iot/controller/DispositivoController.java
package sistemaagricola.com.projecto.controllers;

import sistemaagricola.com.projecto.service.DispositivoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import sistemaagricola.com.projecto.models.Dispositivo;
import java.util.List;

@RestController
@RequestMapping("/api/dispositivos")
public class DispositivoController {
  private final DispositivoService svc;
  public DispositivoController(DispositivoService svc){ this.svc=svc; }

  @PostMapping public Dispositivo criar(@RequestBody Dispositivo d){ return svc.criar(d); }
  @GetMapping("/{id}") public Dispositivo obter(@PathVariable Long id){ return svc.obter(id); }
  @GetMapping public List<Dispositivo> listarTodos(){ return svc.listarTodos(); }

  @GetMapping("/por-parcela/{parcelaId}")
  public Page<Dispositivo> listarPorParcela(@PathVariable Long parcelaId,
                                            @RequestParam(defaultValue="0") int page,
                                            @RequestParam(defaultValue="20") int size){
    return svc.listarPorParcela(parcelaId, PageRequest.of(page,size, Sort.by("id").descending()));
  }

  @PatchMapping("/{id}/estado/{estado}")
  public Dispositivo alterarEstado(@PathVariable Long id, @PathVariable Dispositivo.Estado estado){
    return svc.alterarEstado(id, estado);
  }
}
