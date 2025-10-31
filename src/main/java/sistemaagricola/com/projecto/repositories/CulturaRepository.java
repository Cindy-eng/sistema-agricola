package sistemaagricola.com.projecto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sistemaagricola.com.projecto.models.Cultura;

import java.util.Optional;

public interface CulturaRepository extends JpaRepository<Cultura, Long> {
    Optional<Cultura> findByNome(String nome);
}
