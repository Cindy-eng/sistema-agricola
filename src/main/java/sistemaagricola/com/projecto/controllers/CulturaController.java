package sistemaagricola.com.projecto.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import sistemaagricola.com.projecto.models.Cultura;
import sistemaagricola.com.projecto.service.CulturaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/culturas")
@Tag(name = "Culturas", description = "Gerenciamento de culturas")
@SecurityRequirement(name = "bearer-jwt")
public class CulturaController {
    private final CulturaService svc;
    public CulturaController(CulturaService svc){ this.svc=svc; }

    @Operation(summary = "Criar nova cultura", description = "Cria uma nova cultura associada ao usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cultura criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @PostMapping 
    public Cultura criar(@RequestBody @Valid Cultura c){ return svc.criar(c); }
    
    @Operation(summary = "Obter cultura por ID", description = "Retorna uma cultura específica do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cultura encontrada"),
            @ApiResponse(responseCode = "404", description = "Cultura não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @GetMapping("/{id}") 
    public Cultura obter(@Parameter(description = "ID da cultura") @PathVariable Long id){ return svc.obter(id); }
    
    @Operation(summary = "Listar culturas", description = "Retorna todas as culturas do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de culturas"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @GetMapping 
    public List<Cultura> listar(){ return svc.listar(); }
    
    @Operation(summary = "Atualizar cultura", description = "Atualiza uma cultura existente do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cultura atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cultura não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @PutMapping("/{id}") 
    public Cultura actualizar(@Parameter(description = "ID da cultura") @PathVariable Long id, @RequestBody @Valid Cultura c){ return svc.actualizar(id,c); }
    
    @Operation(summary = "Remover cultura", description = "Remove uma cultura do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cultura removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cultura não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @DeleteMapping("/{id}") 
    public void remover(@Parameter(description = "ID da cultura") @PathVariable Long id){ svc.remover(id); }
}
