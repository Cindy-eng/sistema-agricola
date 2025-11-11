package sistemaagricola.com.projecto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sistemaagricola.com.projecto.models.Cultura;
import sistemaagricola.com.projecto.models.User;

import java.util.List;
import java.util.Optional;

public interface CulturaRepository extends JpaRepository<Cultura, Long> {
    Optional<Cultura> findByNomeAndUsuario(String nome, User usuario);
    List<Cultura> findByUsuario(User usuario);
    Optional<Cultura> findByIdAndUsuario(Long id, User usuario);
    boolean existsByIdAndUsuario(Long id, User usuario);
}
