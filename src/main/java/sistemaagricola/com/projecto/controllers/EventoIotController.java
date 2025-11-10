// src/main/java/sistemaagricola/com/projecto/controllers/EventoIotController.java
package sistemaagricola.com.projecto.controllers;

import sistemaagricola.com.projecto.models.EventoIot;
import sistemaagricola.com.projecto.service.EventoIotService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoIotController {
  private final EventoIotService svc;
  public EventoIotController(EventoIotService svc){ this.svc=svc; }

  @PostMapping public EventoIot registar(@RequestBody EventoIot e){ return svc.registar(e); }

  @GetMapping("/por-dispositivo/{dispositivoId}")
  public List<EventoIot> listar(@PathVariable Long dispositivoId,
                                @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant ini,
                                @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant fim){
    return svc.listarPorDispositivoEPeriodo(dispositivoId, ini, fim);
  }

  @DeleteMapping("/purgar/{dispositivoId}")
  public long purgar(@PathVariable Long dispositivoId,
                     @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant antesDe){
    return svc.purgarAntes(dispositivoId, antesDe);
  }
}
