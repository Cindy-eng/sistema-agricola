package sistemaagricola.com.projecto.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sistemaagricola.com.projecto.models.Dispositivo;

import java.util.Optional;

public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {
    Optional<Dispositivo> findByDeviceKey(String deviceKey);
    boolean existsByDeviceKey(String deviceKey);
    Page<Dispositivo> findByParcelaId(Long parcelaId, Pageable pageable);
}
