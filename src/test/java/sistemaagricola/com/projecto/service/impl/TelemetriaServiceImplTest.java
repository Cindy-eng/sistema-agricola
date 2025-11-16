package sistemaagricola.com.projecto.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sistemaagricola.com.projecto.dto.IngestaoTelemetriaDTO;
import sistemaagricola.com.projecto.exception.BusinessException;
import sistemaagricola.com.projecto.exception.NotFoundException;
import sistemaagricola.com.projecto.models.Dispositivo;
import sistemaagricola.com.projecto.models.Sensor;
import sistemaagricola.com.projecto.models.Telemetria;
import sistemaagricola.com.projecto.repositories.DispositivoRepository;
import sistemaagricola.com.projecto.repositories.SensorRepository;
import sistemaagricola.com.projecto.repositories.TelemetriaRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelemetriaServiceImplTest {

    @Mock
    private TelemetriaRepository telemetriaRepository;

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private DispositivoRepository dispositivoRepository;

    @InjectMocks
    private TelemetriaServiceImpl telemetriaService;

    private Dispositivo dispositivo;
    private Sensor sensor;
    private Telemetria telemetria;

    @BeforeEach
    void setUp() {
        dispositivo = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .id(1L)
                .deviceKey("DEV-001")
                .build();

        sensor = sistemaagricola.com.projecto.util.TestDataBuilder.sensor()
                .id(1L)
                .dispositivo(dispositivo)
                .build();

        telemetria = sistemaagricola.com.projecto.util.TestDataBuilder.telemetria()
                .id(1L)
                .sensor(sensor)
                .ts(Instant.now())
                .valor(25.5)
                .build();
    }

    @Test
    void should_ingerir_when_validDTO() {
        IngestaoTelemetriaDTO dto = new IngestaoTelemetriaDTO(
                "DEV-001",
                1L,
                Instant.now(),
                25.5
        );

        when(dispositivoRepository.findByDeviceKey("DEV-001")).thenReturn(Optional.of(dispositivo));
        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensor));
        when(telemetriaRepository.save(any(Telemetria.class))).thenReturn(telemetria);

        Telemetria result = telemetriaService.ingerir(dto);

        assertThat(result).isNotNull();
        assertThat(result.getSensor()).isEqualTo(sensor);
        assertThat(result.getValor()).isEqualTo(25.5);
        verify(dispositivoRepository).findByDeviceKey("DEV-001");
        verify(sensorRepository).findById(1L);
        verify(telemetriaRepository).save(any(Telemetria.class));
    }

    @Test
    void should_throwException_when_dispositivoNotFound() {
        IngestaoTelemetriaDTO dto = new IngestaoTelemetriaDTO(
                "DEV-999",
                1L,
                Instant.now(),
                25.5
        );

        when(dispositivoRepository.findByDeviceKey("DEV-999")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            telemetriaService.ingerir(dto);
        });

        verify(dispositivoRepository).findByDeviceKey("DEV-999");
        verify(sensorRepository, never()).findById(anyLong());
    }

    @Test
    void should_throwException_when_sensorNotFound() {
        IngestaoTelemetriaDTO dto = new IngestaoTelemetriaDTO(
                "DEV-001",
                999L,
                Instant.now(),
                25.5
        );

        when(dispositivoRepository.findByDeviceKey("DEV-001")).thenReturn(Optional.of(dispositivo));
        when(sensorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            telemetriaService.ingerir(dto);
        });

        verify(sensorRepository).findById(999L);
    }

    @Test
    void should_throwException_when_sensorNotBelongsToDispositivo() {
        Dispositivo outroDispositivo = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .id(2L)
                .deviceKey("DEV-002")
                .build();

        Sensor sensorOutroDispositivo = sistemaagricola.com.projecto.util.TestDataBuilder.sensor()
                .id(2L)
                .dispositivo(outroDispositivo)
                .build();

        IngestaoTelemetriaDTO dto = new IngestaoTelemetriaDTO(
                "DEV-001",
                2L,
                Instant.now(),
                25.5
        );

        when(dispositivoRepository.findByDeviceKey("DEV-001")).thenReturn(Optional.of(dispositivo));
        when(sensorRepository.findById(2L)).thenReturn(Optional.of(sensorOutroDispositivo));

        assertThrows(BusinessException.class, () -> {
            telemetriaService.ingerir(dto);
        });
    }

    @Test
    void should_janela_when_validParameters() {
        Instant ini = Instant.now().minusSeconds(3600);
        Instant fim = Instant.now();

        when(telemetriaRepository.findWindow(1L, ini, fim))
                .thenReturn(Arrays.asList(telemetria));

        List<Telemetria> result = telemetriaService.janela(1L, ini, fim);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(telemetriaRepository).findWindow(1L, ini, fim);
    }

    @Test
    void should_ultima_when_sensorExists() {
        when(telemetriaRepository.findTopBySensorIdOrderByTsDesc(1L))
                .thenReturn(Optional.of(telemetria));

        Optional<Telemetria> result = telemetriaService.ultima(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(telemetriaRepository).findTopBySensorIdOrderByTsDesc(1L);
    }

    @Test
    void should_purgarAntes_when_validParameters() {
        Instant tsLimite = Instant.now().minusSeconds(86400);

        when(telemetriaRepository.deleteBySensorIdAndTsBefore(1L, tsLimite))
                .thenReturn(5L);

        long result = telemetriaService.purgarAntes(1L, tsLimite);

        assertThat(result).isEqualTo(5L);
        verify(telemetriaRepository).deleteBySensorIdAndTsBefore(1L, tsLimite);
    }
}

