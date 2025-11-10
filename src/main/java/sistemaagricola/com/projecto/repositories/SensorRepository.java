package sistemaagricola.com.projecto.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import sistemaagricola.com.projecto.models.Sensor;
import sistemaagricola.com.projecto.models.Dispositivo;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor,Long> {
    boolean existsByDispositivoIdAndTipo(Long dispositivoId, String tipo); // Alteração aqui
    List<Sensor> findByDispositivoId(Long dispositivoId);

    List<Sensor> findByDispositivoDeviceKey(String deviceKey);
}
