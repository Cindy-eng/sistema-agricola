package sistemaagricola.com.projecto.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import sistemaagricola.com.projecto.models.Sensor;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor,Long> {
    List<Sensor> findByDispositivoId(Long dispositivoId);

    boolean existsByDispositivoIdAndTipo(Long dispositivoId, String tipo);
}
