package sistemaagricola.com.projecto.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import sistemaagricola.com.projecto.dto.SensorCreateDTO;
import sistemaagricola.com.projecto.models.Sensor;
import sistemaagricola.com.projecto.repositories.DispositivoRepository;
import sistemaagricola.com.projecto.service.SensorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sensores")
@Tag(name = "Sensores", description = "Gerenciamento de sensores")
@SecurityRequirement(name = "bearer-jwt")
public class SensorController {
  private final SensorService svc;
  private final DispositivoRepository dispRepo;

  public SensorController(SensorService svc, DispositivoRepository dispRepo){
    this.svc = svc;
    this.dispRepo = dispRepo;
  }

  @Operation(summary = "Criar sensor", description = "Cria um novo sensor associado a um dispositivo")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Sensor criado com sucesso"),
          @ApiResponse(responseCode = "400", description = "Dados inválidos"),
          @ApiResponse(responseCode = "401", description = "Não autenticado"),
          @ApiResponse(responseCode = "422", description = "Sensor duplicado no dispositivo")
  })
  @PostMapping
  public Sensor criar(@RequestBody @Valid SensorCreateDTO dto){
    var disp = dispRepo.findById(dto.dispositivoId()).orElseThrow();
    return svc.criar(Sensor.builder()
        .dispositivo(disp)
        .tipo(dto.tipo())
        .unidade(dto.unidade())
        .build());
  }
  
  @Operation(summary = "Obter sensor por ID", description = "Retorna um sensor específico")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Sensor encontrado"),
          @ApiResponse(responseCode = "404", description = "Sensor não encontrado"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @GetMapping("/{id}") 
  public Sensor obter(@Parameter(description = "ID do sensor") @PathVariable Long id){ return svc.obter(id); }
  
  @Operation(summary = "Listar sensores por dispositivo", description = "Retorna todos os sensores de um dispositivo")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Lista de sensores"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @GetMapping("/por-dispositivo/{dispositivoId}") 
  public List<Sensor> listarPorDispositivo(@Parameter(description = "ID do dispositivo") @PathVariable Long dispositivoId){
    return svc.listarPorDispositivo(dispositivoId);
  }
  
  @Operation(summary = "Remover sensor", description = "Remove um sensor")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Sensor removido com sucesso"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @DeleteMapping("/{id}") 
  public void remover(@Parameter(description = "ID do sensor") @PathVariable Long id){ svc.remover(id); }
}
