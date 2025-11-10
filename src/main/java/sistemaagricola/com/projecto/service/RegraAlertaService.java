package sistemaagricola.com.projecto.service;

import sistemaagricola.com.projecto.models.RegraAlerta; import java.util.List;

public interface RegraAlertaService {
    RegraAlerta criar(RegraAlerta r);
    RegraAlerta activar(Long id, boolean activo);
    List<RegraAlerta> listarActivasPorParcela(Long parcelaId);
    void remover(Long id);
}