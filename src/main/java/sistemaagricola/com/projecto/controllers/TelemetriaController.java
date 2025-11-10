// src/main/java/sistemaagricola/com/projecto/controllers/TelemetriaController.java
package sistemaagricola.com.projecto.controllers;

import sistemaagricola.com.projecto.dto.IngestaoTelemetriaDTO;
import sistemaagricola.com.projecto.models.Telemetria;
import sistemaagricola.com.projecto.repositories.TelemetriaRepository;
import sistemaagricola.com.projecto.service.TelemetriaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/telemetria")
public class TelemetriaController {
  private final TelemetriaService svc;
  private final TelemetriaRepository repo;

  public TelemetriaController(TelemetriaService svc, TelemetriaRepository repo){
    this.svc = svc;
    this.repo = repo;
  }

  @PostMapping("/ingestao")
  public Telemetria ingerir(@RequestBody @Valid IngestaoTelemetriaDTO dto){
    return svc.ingerir(dto);
  }

  @GetMapping("/janela")
  public List<Telemetria> janela(@RequestParam Long sensorId,
                                 @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant ini,
                                 @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant fim){
    return svc.janela(sensorId, ini, fim);
  }

  @GetMapping("/ultima")
  public Optional<Telemetria> ultima(@RequestParam Long sensorId){
    return svc.ultima(sensorId);
  }

  @DeleteMapping("/purgar")
  public long purgar(@RequestParam Long sensorId,
                     @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant antesDe){
    return svc.purgarAntes(sensorId, antesDe);
  }

  @GetMapping("/agregar")
  public List<TelemetriaRepository.TelemetriaAgg> agregar(@RequestParam Long sensorId,
                                                        @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant ini,
                                                        @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant fim,
                                                        @RequestParam(defaultValue="hour") String granularidade){
    return repo.aggregateWindow(sensorId, ini, fim, granularidade);
  }
}
