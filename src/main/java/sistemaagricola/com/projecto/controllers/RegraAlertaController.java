package sistemaagricola.com.projecto.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import sistemaagricola.com.projecto.models.RegraAlerta;
import sistemaagricola.com.projecto.dto.CreateRegraDTO;
import sistemaagricola.com.projecto.service.RegraAlertaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/regras")
@Tag(name = "Regras de Alerta", description = "Gerenciamento de regras de alerta")
@SecurityRequirement(name = "bearer-jwt")
public class RegraAlertaController {
  private final RegraAlertaService svc;
  public RegraAlertaController(RegraAlertaService svc){ this.svc=svc; }

  @Operation(summary = "Criar regra de alerta", description = "Cria uma nova regra de alerta para uma parcela")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Regra criada com sucesso"),
          @ApiResponse(responseCode = "400", description = "Dados inválidos"),
          @ApiResponse(responseCode = "401", description = "Não autenticado"),
          @ApiResponse(responseCode = "404", description = "Parcela não encontrada")
  })
  @PostMapping 
  public RegraAlerta criar(@RequestBody @Valid CreateRegraDTO dto){ 
    return svc.criar(dto); 
  }
  
  @Operation(summary = "Ativar/Desativar regra", description = "Ativa ou desativa uma regra de alerta")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Regra atualizada com sucesso"),
          @ApiResponse(responseCode = "404", description = "Regra não encontrada"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @PatchMapping("/{id}/activo/{activo}") 
  public RegraAlerta activar(
          @Parameter(description = "ID da regra", example = "1") @PathVariable Long id, 
          @Parameter(description = "Estado ativo (true/false)", example = "true") @PathVariable boolean activo){ 
    return svc.activar(id, activo); 
  }
  
  @Operation(summary = "Listar regras ativas por parcela", description = "Retorna todas as regras ativas de uma parcela")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Lista de regras ativas"),
          @ApiResponse(responseCode = "404", description = "Parcela não encontrada"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @GetMapping("/activas/por-parcela/{parcelaId}") 
  public List<RegraAlerta> listarActivas(@Parameter(description = "ID da parcela", example = "1") @PathVariable Long parcelaId){ 
    return svc.listarActivasPorParcela(parcelaId); 
  }
  
  @Operation(summary = "Remover regra", description = "Remove uma regra de alerta")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Regra removida com sucesso"),
          @ApiResponse(responseCode = "404", description = "Regra não encontrada"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @DeleteMapping("/{id}") 
  public void remover(@Parameter(description = "ID da regra", example = "1") @PathVariable Long id){ 
    svc.remover(id); 
  }
}
