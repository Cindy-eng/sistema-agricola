package sistemaagricola.com.projecto.service;

import sistemaagricola.com.projecto.models.RegraAlerta;
import sistemaagricola.com.projecto.dto.CreateRegraDTO;
import java.util.List;

public interface RegraAlertaService {
    RegraAlerta criar(CreateRegraDTO dto);
    RegraAlerta activar(Long id, boolean activo);
    List<RegraAlerta> listarActivasPorParcela(Long parcelaId);
    void remover(Long id);
}