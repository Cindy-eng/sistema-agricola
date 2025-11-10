package sistemaagricola.com.projecto.service;

import sistemaagricola.com.projecto.models.EventoIot;
import java.time.Instant; import java.util.List;

public interface EventoIotService {
    EventoIot registar(EventoIot e);
    List<EventoIot> listarPorDispositivoEPeriodo(Long dispositivoId, Instant ini, Instant fim);
    long purgarAntes(Long dispositivoId, Instant tsLimite);
}
