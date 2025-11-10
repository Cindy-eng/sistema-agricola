// src/main/java/com/agro/iot/repository/RegraAlertaRepository.java
package sistemaagricola.com.projecto.repositories;

import  sistemaagricola.com.projecto.models.RegraAlerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegraAlertaRepository extends JpaRepository<RegraAlerta, Long> {
    List<RegraAlerta> findByParcelaIdAndActivoTrue(Long parcelaId);
    List<RegraAlerta> findByActivoTrue();
}
