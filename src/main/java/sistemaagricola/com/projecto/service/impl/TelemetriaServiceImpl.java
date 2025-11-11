package sistemaagricola.com.projecto.service.impl;

import sistemaagricola.com.projecto.dto.IngestaoTelemetriaDTO;
import sistemaagricola.com.projecto.models.Telemetria;
import sistemaagricola.com.projecto.repositories.TelemetriaRepository;
import sistemaagricola.com.projecto.repositories.SensorRepository;
import sistemaagricola.com.projecto.repositories.DispositivoRepository;
import sistemaagricola.com.projecto.service.TelemetriaService;
import sistemaagricola.com.projecto.exception.NotFoundException;
import sistemaagricola.com.projecto.exception.BusinessException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TelemetriaServiceImpl implements TelemetriaService {
    private final TelemetriaRepository telemetriaRepo;
    private final SensorRepository sensorRepo;
    private final DispositivoRepository dispositivoRepo;

    public TelemetriaServiceImpl(TelemetriaRepository telemetriaRepo, 
                                 SensorRepository sensorRepo,
                                 DispositivoRepository dispositivoRepo) {
        this.telemetriaRepo = telemetriaRepo;
        this.sensorRepo = sensorRepo;
        this.dispositivoRepo = dispositivoRepo;
    }

    @Override
    public Telemetria ingerir(IngestaoTelemetriaDTO dto) {
        // Validar que o dispositivo existe
        var dispositivo = dispositivoRepo.findByDeviceKey(dto.deviceKey())
                .orElseThrow(() -> new NotFoundException("Dispositivo não encontrado com deviceKey: " + dto.deviceKey()));

        // Validar que o sensor existe e pertence ao dispositivo
        var sensor = sensorRepo.findById(dto.sensorId())
                .orElseThrow(() -> new NotFoundException("Sensor não encontrado: " + dto.sensorId()));

        if (!sensor.getDispositivo().getId().equals(dispositivo.getId())) {
            throw new BusinessException("Sensor não pertence ao dispositivo informado");
        }

        // Criar e salvar a telemetria
        var telemetria = Telemetria.builder()
                .sensor(sensor)
                .ts(dto.ts())
                .valor(dto.valor())
                .qualidade("OK") // Por padrão, qualidade OK
                .build();

        return telemetriaRepo.save(telemetria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Telemetria> janela(Long sensorId, Instant ini, Instant fim) {
        return telemetriaRepo.findWindow(sensorId, ini, fim);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Telemetria> ultima(Long sensorId) {
        return telemetriaRepo.findTopBySensorIdOrderByTsDesc(sensorId);
    }

    @Override
    public long purgarAntes(Long sensorId, Instant tsLimite) {
        return telemetriaRepo.deleteBySensorIdAndTsBefore(sensorId, tsLimite);
    }
}

