package sistemaagricola.com.projecto.service;

import sistemaagricola.com.projecto.dto.IngestaoTelemetriaDTO;
import sistemaagricola.com.projecto.models.Telemetria;
import java.time.Instant; import java.util.List; import java.util.Optional;

public interface TelemetriaService {
    Telemetria ingerir(IngestaoTelemetriaDTO dto);
    List<Telemetria> janela(Long sensorId, Instant ini, Instant fim);
    Optional<Telemetria> ultima(Long sensorId);
    long purgarAntes(Long sensorId, Instant tsLimite);
}
