package sistemaagricola.com.projecto.service;

import sistemaagricola.com.projecto.models.Manutencao;
import java.util.List;

public interface ManutencaoService {
    Manutencao abrir(Manutencao m);
    Manutencao fechar(Long id);
    List<Manutencao> listarPorDispositivoEEstado(Long dispositivoId, String estado);
}
