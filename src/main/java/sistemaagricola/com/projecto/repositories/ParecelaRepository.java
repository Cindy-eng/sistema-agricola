package sistemaagricola.com.projecto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sistemaagricola.com.projecto.models.Parcela;

public interface ParecelaRepository extends JpaRepository<Parcela, Long> {
    boolean existsByNome(String nome);
}
