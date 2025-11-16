package sistemaagricola.com.projecto.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Telemetria", description = "Ingestão e consulta de dados de telemetria")
public class TelemetriaController {
  private final TelemetriaService svc;
  private final TelemetriaRepository repo;

  public TelemetriaController(TelemetriaService svc, TelemetriaRepository repo){
    this.svc = svc;
    this.repo = repo;
  }

  @Operation(summary = "Ingerir telemetria", description = "Endpoint público para dispositivos IoT enviarem dados de telemetria")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Telemetria ingerida com sucesso"),
          @ApiResponse(responseCode = "400", description = "Dados inválidos"),
          @ApiResponse(responseCode = "404", description = "Dispositivo ou sensor não encontrado"),
          @ApiResponse(responseCode = "422", description = "Sensor não pertence ao dispositivo")
  })
  @PostMapping("/ingestao")
  public Telemetria ingerir(@RequestBody @Valid IngestaoTelemetriaDTO dto){
    return svc.ingerir(dto);
  }

  @Operation(summary = "Consultar janela de telemetria", description = "Retorna telemetria de um sensor em um período específico")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Lista de telemetria"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @SecurityRequirement(name = "bearer-jwt")
  @GetMapping("/janela")
  public List<Telemetria> janela(
          @Parameter(description = "ID do sensor", example = "1") @RequestParam Long sensorId,
          @Parameter(description = "Data/hora inicial (ISO 8601)", example = "2024-01-15T00:00:00Z") @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant ini,
          @Parameter(description = "Data/hora final (ISO 8601)", example = "2024-01-15T23:59:59Z") @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant fim){
    return svc.janela(sensorId, ini, fim);
  }

  @Operation(summary = "Obter última leitura", description = "Retorna a última leitura de telemetria de um sensor")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Última telemetria (pode ser vazio)"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @SecurityRequirement(name = "bearer-jwt")
  @GetMapping("/ultima")
  public Optional<Telemetria> ultima(@Parameter(description = "ID do sensor", example = "1") @RequestParam Long sensorId){
    return svc.ultima(sensorId);
  }

  @Operation(summary = "Purgar telemetria antiga", description = "Remove telemetria anterior a uma data específica")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Quantidade de registros removidos"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @SecurityRequirement(name = "bearer-jwt")
  @DeleteMapping("/purgar")
  public long purgar(
          @Parameter(description = "ID do sensor", example = "1") @RequestParam Long sensorId,
          @Parameter(description = "Data limite (ISO 8601)", example = "2024-01-01T00:00:00Z") @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant antesDe){
    return svc.purgarAntes(sensorId, antesDe);
  }

  @Operation(summary = "Agregar telemetria", description = "Retorna telemetria agregada por período (média, min, max, contagem)")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Lista de agregações"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @SecurityRequirement(name = "bearer-jwt")
  @GetMapping("/agregar")
  public List<TelemetriaRepository.TelemetriaAgg> agregar(
          @Parameter(description = "ID do sensor", example = "1") @RequestParam Long sensorId,
          @Parameter(description = "Data/hora inicial (ISO 8601)", example = "2024-01-15T00:00:00Z") @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant ini,
          @Parameter(description = "Data/hora final (ISO 8601)", example = "2024-01-15T23:59:59Z") @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant fim,
          @Parameter(description = "Granularidade (hour, day, week, month)", example = "hour") @RequestParam(defaultValue="hour") String granularidade){
    return repo.aggregateWindow(sensorId, ini, fim, granularidade);
  }
}
