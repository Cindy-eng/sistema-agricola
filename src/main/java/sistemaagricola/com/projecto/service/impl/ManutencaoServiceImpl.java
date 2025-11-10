// service/impl/ManutencaoServiceImpl.java
package sistemaagricola.com.projecto.service.impl;

import sistemaagricola.com.projecto.models.Manutencao;
import sistemaagricola.com.projecto.repositories.ManutencaoRepository;
import sistemaagricola.com.projecto.service.ManutencaoService;
import sistemaagricola.com.projecto.exception.NotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class ManutencaoServiceImpl implements ManutencaoService {
    private final ManutencaoRepository repo;

    public ManutencaoServiceImpl(ManutencaoRepository repo) {
        this.repo = repo;
    }

    @Override
    public Manutencao abrir(Manutencao m) {
        m.setEstado("ABERTA");
        m.setTsAbertura(Instant.now());
        return repo.save(m);
    }

    @Override
    public Manutencao fechar(Long id) {
        Manutencao m = repo.findById(id).orElseThrow(() -> new NotFoundException("Manutenção não encontrada"));
        m.setEstado("FECHADA");
        m.setTsFecho(Instant.now());
        return repo.save(m);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Manutencao> listarPorDispositivoEEstado(Long dispositivoId, String estado) {
        return repo.findByDispositivoIdAndEstado(dispositivoId, estado);
    }
}
