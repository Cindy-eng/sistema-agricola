package sistemaagricola.com.projecto.service;

import sistemaagricola.com.projecto.models.Dispositivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DispositivoService {
    Dispositivo criar(Dispositivo d);
    Optional<Dispositivo> obterPorDeviceKey(String deviceKey);
    Dispositivo obter(Long id);
    Page<Dispositivo> listarPorParcela(Long parcelaId, Pageable pg);
    Dispositivo alterarEstado(Long id, Dispositivo.Estado estado);
    List<Dispositivo> listarTodos();
}
