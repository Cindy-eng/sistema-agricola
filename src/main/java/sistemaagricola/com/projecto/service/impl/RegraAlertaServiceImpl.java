package sistemaagricola.com.projecto.service.impl;

import sistemaagricola.com.projecto.models.RegraAlerta;
import sistemaagricola.com.projecto.models.Parcela;
import sistemaagricola.com.projecto.dto.CreateRegraDTO;
import sistemaagricola.com.projecto.repositories.RegraAlertaRepository;
import sistemaagricola.com.projecto.repositories.ParecelaRepository;
import sistemaagricola.com.projecto.service.RegraAlertaService;
import sistemaagricola.com.projecto.exception.NotFoundException;
import sistemaagricola.com.projecto.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class RegraAlertaServiceImpl implements RegraAlertaService {
    private final RegraAlertaRepository repo;
    private final ParecelaRepository parcelaRepo;
    private final SecurityUtil securityUtil;
    
    public RegraAlertaServiceImpl(
            RegraAlertaRepository repo, 
            ParecelaRepository parcelaRepo,
            SecurityUtil securityUtil) {
        this.repo = repo;
        this.parcelaRepo = parcelaRepo;
        this.securityUtil = securityUtil;
    }
    
    public RegraAlerta criar(CreateRegraDTO dto) {
        // Carregar e validar que a parcela pertence ao usuário atual
        var usuario = securityUtil.getCurrentUser();
        Parcela parcela = parcelaRepo.findByIdAndUsuario(dto.parcelaId(), usuario)
                .orElseThrow(() -> new NotFoundException("Parcela não encontrada ou não pertence ao usuário"));
        
        // Criar a regra
        RegraAlerta regra = RegraAlerta.builder()
                .parcela(parcela)
                .expressao(dto.expressao())
                .severidade(dto.severidade())
                .destino(dto.destino())
                .activo(dto.activo())
                .build();
        
        return repo.save(regra);
    }
    
    public RegraAlerta activar(Long id, boolean activo) {
        var r = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Regra não encontrada"));
        r.setActivo(activo);
        return repo.save(r);
    }
    
    @Transactional(readOnly = true)
    public List<RegraAlerta> listarActivasPorParcela(Long parcelaId) {
        // Validar que a parcela pertence ao usuário atual
        var usuario = securityUtil.getCurrentUser();
        parcelaRepo.findByIdAndUsuario(parcelaId, usuario)
                .orElseThrow(() -> new NotFoundException("Parcela não encontrada ou não pertence ao usuário"));
        
        return repo.findByParcelaIdAndActivoTrue(parcelaId);
    }
    
    public void remover(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Regra não encontrada");
        }
        repo.deleteById(id);
    }
}
