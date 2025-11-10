package sistemaagricola.com.projecto.service.impl;

// service/impl/EventoIotServiceImpl.java
import sistemaagricola.com.projecto.models.EventoIot;
import sistemaagricola.com.projecto.repositories.EventoIotRepository;
import sistemaagricola.com.projecto.service.EventoIotService;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.time.Instant; import java.util.List;

@Service @Transactional
public class EventoIotServiceImpl implements EventoIotService {
    private final EventoIotRepository repo;
    public EventoIotServiceImpl(EventoIotRepository repo){ this.repo=repo; }
    public EventoIot registar(EventoIot e){ return repo.save(e); }
    @Transactional(readOnly=true)
    public List<EventoIot> listarPorDispositivoEPeriodo(Long dispositivoId, Instant ini, Instant fim){
        return repo.findByDispositivoAndPeriodo(dispositivoId, ini, fim);
    }
    public long purgarAntes(Long dispositivoId, Instant ts){ return repo.deleteByDispositivoIdAndTsBefore(dispositivoId, ts); }
}
