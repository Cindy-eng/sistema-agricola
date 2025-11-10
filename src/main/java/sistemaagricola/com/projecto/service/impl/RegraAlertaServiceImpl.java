package sistemaagricola.com.projecto.service.impl;

import sistemaagricola.com.projecto.models.RegraAlerta;
import sistemaagricola.com.projecto.repositories.RegraAlertaRepository;
import sistemaagricola.com.projecto.service.RegraAlertaService;
import sistemaagricola.com.projecto.exception.NotFoundException;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @Transactional
public class RegraAlertaServiceImpl implements RegraAlertaService {
    private final RegraAlertaRepository repo;
    public RegraAlertaServiceImpl(RegraAlertaRepository repo){ this.repo=repo; }
    public RegraAlerta criar(RegraAlerta r){ return repo.save(r); }
    public RegraAlerta activar(Long id, boolean activo){
        var r = repo.findById(id).orElseThrow(() -> new NotFoundException("Regra não encontrada"));
        r.setActivo(activo); return repo.save(r);
    }
    @Transactional(readOnly=true)
    public List<RegraAlerta> listarActivasPorParcela(Long parcelaId){ return repo.findByParcelaIdAndActivoTrue(parcelaId); }
    public void remover(Long id){ repo.deleteById(id); }
}
