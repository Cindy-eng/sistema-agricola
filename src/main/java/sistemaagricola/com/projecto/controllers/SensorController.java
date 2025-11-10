// src/main/java/sistemaagricola/com/projecto/controllers/SensorController.java
package sistemaagricola.com.projecto.controllers;

import sistemaagricola.com.projecto.dto.SensorCreateDTO;
import sistemaagricola.com.projecto.models.Sensor;
import sistemaagricola.com.projecto.repositories.DispositivoRepository;
import sistemaagricola.com.projecto.service.SensorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sensores")
public class SensorController {
  private final SensorService svc;
  private final DispositivoRepository dispRepo;

  public SensorController(SensorService svc, DispositivoRepository dispRepo){
    this.svc = svc;
    this.dispRepo = dispRepo;
  }

  @PostMapping
  public Sensor criar(@RequestBody @Valid SensorCreateDTO dto){
    var disp = dispRepo.findById(dto.dispositivoId()).orElseThrow();
    return svc.criar(Sensor.builder()
        .dispositivo(disp)
        .tipo(dto.tipo())
        .unidade(dto.unidade())
        .build());
  }
  @GetMapping("/{id}") public Sensor obter(@PathVariable Long id){ return svc.obter(id); }
  @GetMapping("/por-dispositivo/{dispositivoId}") public List<Sensor> listarPorDispositivo(@PathVariable Long dispositivoId){
    return svc.listarPorDispositivo(dispositivoId);
  }
  @DeleteMapping("/{id}") public void remover(@PathVariable Long id){ svc.remover(id); }
}
