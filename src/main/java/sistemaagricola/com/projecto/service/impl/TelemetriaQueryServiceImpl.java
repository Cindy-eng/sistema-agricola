// service/impl/TelemetriaQueryServiceImpl.java
package sistemaagricola.com.projecto.service.impl;

import sistemaagricola.com.projecto.repositories.TelemetriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TelemetriaQueryServiceImpl {
    private final TelemetriaRepository repo;

    public TelemetriaQueryServiceImpl(TelemetriaRepository repo) {
        this.repo = repo;
    }

    public List<TelemetriaRepository.TelemetriaAgg> agregar(Long sensorId, Instant ini, Instant fim, String granularidade) {
        return repo.aggregateWindow(sensorId, ini, fim, granularidade);
    }
}
