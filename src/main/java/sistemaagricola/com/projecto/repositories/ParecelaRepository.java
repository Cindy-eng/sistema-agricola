package sistemaagricola.com.projecto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sistemaagricola.com.projecto.models.Parcela;
import sistemaagricola.com.projecto.models.User;

import java.util.List;
import java.util.Optional;

public interface ParecelaRepository extends JpaRepository<Parcela, Long> {
    boolean existsByNomeAndUsuario(String nome, User usuario);
    List<Parcela> findByUsuario(User usuario);
    Optional<Parcela> findByIdAndUsuario(Long id, User usuario);
    boolean existsByIdAndUsuario(Long id, User usuario);
}
