package sistemaagricola.com.projecto.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import sistemaagricola.com.projecto.models.Manutencao;
import sistemaagricola.com.projecto.service.ManutencaoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/manutencoes")
@Tag(name = "Manutenções", description = "Gerenciamento de manutenções de dispositivos")
@SecurityRequirement(name = "bearer-jwt")
public class ManutencaoController {
  private final ManutencaoService svc;
  public ManutencaoController(ManutencaoService svc){ this.svc=svc; }

  @Operation(summary = "Abrir manutenção", description = "Abre uma nova manutenção para um dispositivo")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Manutenção aberta com sucesso"),
          @ApiResponse(responseCode = "400", description = "Dados inválidos"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @PostMapping 
  public Manutencao abrir(@RequestBody Manutencao m){ return svc.abrir(m); }
  
  @Operation(summary = "Fechar manutenção", description = "Fecha uma manutenção existente")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Manutenção fechada com sucesso"),
          @ApiResponse(responseCode = "404", description = "Manutenção não encontrada"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @PatchMapping("/{id}/fechar") 
  public Manutencao fechar(@Parameter(description = "ID da manutenção", example = "1") @PathVariable Long id){ return svc.fechar(id); }
  
  @Operation(summary = "Listar manutenções por dispositivo e estado", description = "Retorna manutenções de um dispositivo filtradas por estado")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Lista de manutenções"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @GetMapping("/por-dispositivo/{dispositivoId}")
  public List<Manutencao> listar(
          @Parameter(description = "ID do dispositivo", example = "1") @PathVariable Long dispositivoId, 
          @Parameter(description = "Estado da manutenção (ABERTA ou FECHADA)", example = "ABERTA") @RequestParam String estado){
    return svc.listarPorDispositivoEEstado(dispositivoId, estado);
  }
}
