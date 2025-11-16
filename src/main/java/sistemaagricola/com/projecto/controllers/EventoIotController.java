package sistemaagricola.com.projecto.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import sistemaagricola.com.projecto.models.EventoIot;
import sistemaagricola.com.projecto.service.EventoIotService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@Tag(name = "Eventos IoT", description = "Gerenciamento de eventos de dispositivos IoT")
@SecurityRequirement(name = "bearer-jwt")
public class EventoIotController {
  private final EventoIotService svc;
  public EventoIotController(EventoIotService svc){ this.svc=svc; }

  @Operation(summary = "Registrar evento IoT", description = "Registra um novo evento de dispositivo IoT")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Evento registrado com sucesso"),
          @ApiResponse(responseCode = "400", description = "Dados inválidos"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @PostMapping 
  public EventoIot registar(@RequestBody EventoIot e){ return svc.registar(e); }

  @Operation(summary = "Listar eventos por dispositivo e período", description = "Retorna eventos de um dispositivo em um período específico")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Lista de eventos"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @GetMapping("/por-dispositivo/{dispositivoId}")
  public List<EventoIot> listar(
          @Parameter(description = "ID do dispositivo", example = "1") @PathVariable Long dispositivoId,
          @Parameter(description = "Data/hora inicial (ISO 8601)", example = "2024-01-15T00:00:00Z") @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant ini,
          @Parameter(description = "Data/hora final (ISO 8601)", example = "2024-01-15T23:59:59Z") @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant fim){
    return svc.listarPorDispositivoEPeriodo(dispositivoId, ini, fim);
  }

  @Operation(summary = "Purgar eventos antigos", description = "Remove eventos anteriores a uma data específica")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Quantidade de eventos removidos"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @DeleteMapping("/purgar/{dispositivoId}")
  public long purgar(
          @Parameter(description = "ID do dispositivo", example = "1") @PathVariable Long dispositivoId,
          @Parameter(description = "Data limite (ISO 8601)", example = "2024-01-01T00:00:00Z") @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant antesDe){
    return svc.purgarAntes(dispositivoId, antesDe);
  }
}
