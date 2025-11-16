package sistemaagricola.com.projecto.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import sistemaagricola.com.projecto.service.ParcelaService;
import org.springframework.web.bind.annotation.*;
import sistemaagricola.com.projecto.models.Parcela;
import java.util.List;

@RestController
@RequestMapping("/api/parcelas")
@Tag(name = "Parcelas", description = "Gerenciamento de parcelas agrícolas")
@SecurityRequirement(name = "bearer-jwt")
public class ParcelaController {
    private final ParcelaService svc;
    public ParcelaController(ParcelaService svc){ this.svc=svc; }

    @Operation(summary = "Criar nova parcela", description = "Cria uma nova parcela agrícola associada ao usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcela criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "422", description = "Parcela já existe")
    })
    @PostMapping 
    public Parcela criar(@RequestBody @Valid Parcela p){ return svc.criar(p); }
    
    @Operation(summary = "Obter parcela por ID", description = "Retorna uma parcela específica do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcela encontrada"),
            @ApiResponse(responseCode = "404", description = "Parcela não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @GetMapping("/{id}") 
    public Parcela obter(@Parameter(description = "ID da parcela") @PathVariable Long id){ return svc.obter(id); }
    
    @Operation(summary = "Listar parcelas", description = "Retorna todas as parcelas do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de parcelas"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @GetMapping 
    public List<Parcela> listar(){ return svc.listar(); }
    
    @Operation(summary = "Atualizar parcela", description = "Atualiza uma parcela existente do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcela atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Parcela não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @PutMapping("/{id}") 
    public Parcela actualizar(@Parameter(description = "ID da parcela") @PathVariable Long id, @RequestBody @Valid Parcela p){ return svc.actualizar(id,p); }
    
    @Operation(summary = "Remover parcela", description = "Remove uma parcela do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcela removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Parcela não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @DeleteMapping("/{id}") 
    public void remover(@Parameter(description = "ID da parcela") @PathVariable Long id){ svc.remover(id); }
}
