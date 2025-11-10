package sistemaagricola.com.projecto.controllers;

import jakarta.validation.Valid;
import sistemaagricola.com.projecto.service.ParcelaService;

import org.springframework.web.bind.annotation.*;
import sistemaagricola.com.projecto.models.Parcela;

import java.util.List;

@RestController
@RequestMapping("/api/parcelas")
public class ParcelaController {
    private final ParcelaService svc;
    public ParcelaController(ParcelaService svc){ this.svc=svc; }

    @PostMapping public Parcela criar(@RequestBody @Valid Parcela p){ return svc.criar(p); }
    @GetMapping("/{id}") public Parcela obter(@PathVariable Long id){ return svc.obter(id); }
    @GetMapping public List<Parcela> listar(){ return svc.listar(); }
    @PutMapping("/{id}") public Parcela actualizar(@PathVariable Long id, @RequestBody @Valid Parcela p){ return svc.actualizar(id,p); }
    @DeleteMapping("/{id}") public void remover(@PathVariable Long id){ svc.remover(id); }
}
