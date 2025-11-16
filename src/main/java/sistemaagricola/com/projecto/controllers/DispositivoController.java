package sistemaagricola.com.projecto.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import sistemaagricola.com.projecto.service.DispositivoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import sistemaagricola.com.projecto.models.Dispositivo;
import java.util.List;

@RestController
@RequestMapping("/api/dispositivos")
@Tag(name = "Dispositivos", description = "Gerenciamento de dispositivos IoT")
@SecurityRequirement(name = "bearer-jwt")
public class DispositivoController {
  private final DispositivoService svc;
  public DispositivoController(DispositivoService svc){ this.svc=svc; }

  @Operation(summary = "Criar dispositivo", description = "Cria um novo dispositivo IoT associado a uma parcela")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Dispositivo criado com sucesso"),
          @ApiResponse(responseCode = "400", description = "Dados inválidos"),
          @ApiResponse(responseCode = "401", description = "Não autenticado"),
          @ApiResponse(responseCode = "422", description = "deviceKey duplicada ou parcela não encontrada")
  })
  @PostMapping 
  public Dispositivo criar(@RequestBody Dispositivo d){ return svc.criar(d); }
  
  @Operation(summary = "Obter dispositivo por ID", description = "Retorna um dispositivo específico")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Dispositivo encontrado"),
          @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @GetMapping("/{id}") 
  public Dispositivo obter(@Parameter(description = "ID do dispositivo") @PathVariable Long id){ return svc.obter(id); }
  
  @Operation(summary = "Listar todos os dispositivos", description = "Retorna todos os dispositivos do usuário autenticado")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Lista de dispositivos"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @GetMapping 
  public List<Dispositivo> listarTodos(){ return svc.listarTodos(); }

  @Operation(summary = "Listar dispositivos por parcela", description = "Retorna dispositivos de uma parcela específica com paginação")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Página de dispositivos"),
          @ApiResponse(responseCode = "404", description = "Parcela não encontrada"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @GetMapping("/por-parcela/{parcelaId}")
  public Page<Dispositivo> listarPorParcela(
          @Parameter(description = "ID da parcela") @PathVariable Long parcelaId,
          @Parameter(description = "Número da página (0-indexed)", example = "0") @RequestParam(defaultValue="0") int page,
          @Parameter(description = "Tamanho da página", example = "20") @RequestParam(defaultValue="20") int size){
    return svc.listarPorParcela(parcelaId, PageRequest.of(page,size, Sort.by("id").descending()));
  }

  @Operation(summary = "Alterar estado do dispositivo", description = "Altera o estado de um dispositivo (ACTIVO/INACTIVO)")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Estado alterado com sucesso"),
          @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado"),
          @ApiResponse(responseCode = "401", description = "Não autenticado")
  })
  @PatchMapping("/{id}/estado/{estado}")
  public Dispositivo alterarEstado(
          @Parameter(description = "ID do dispositivo") @PathVariable Long id, 
          @Parameter(description = "Novo estado (ACTIVO ou INACTIVO)", example = "ACTIVO") @PathVariable Dispositivo.Estado estado){
    return svc.alterarEstado(id, estado);
  }
}
