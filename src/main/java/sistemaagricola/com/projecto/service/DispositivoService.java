package sistemaagricola.com.projecto.service;

import sistemaagricola.com.projecto.models.Dispositivo;
import org.springframework.data.domain.*;
import java.util.*;

public interface DispositivoService {
    Dispositivo criar(Dispositivo d);
    Optional<Dispositivo> obterPorDeviceKey(String deviceKey);
    Dispositivo obter(Long id);
    Page<Dispositivo> listarPorParcela(Long parcelaId, Pageable pg);
    Dispositivo alterarEstado(Long id, Dispositivo.Estado estado);
    List<Dispositivo> listarTodos();
}
