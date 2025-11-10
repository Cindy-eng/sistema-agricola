package sistemaagricola.com.projecto.repositories;

import sistemaagricola.com.projecto.models.Manutencao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {
    List<Manutencao> findByDispositivoIdAndEstado(Long dispositivoId, String estado);
}
