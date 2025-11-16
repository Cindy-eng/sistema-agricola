package sistemaagricola.com.projecto.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sistemaagricola.com.projecto.exception.BusinessException;
import sistemaagricola.com.projecto.exception.NotFoundException;
import sistemaagricola.com.projecto.models.Dispositivo;
import sistemaagricola.com.projecto.models.Sensor;
import sistemaagricola.com.projecto.repositories.SensorRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorServiceImplTest {

    @Mock
    private SensorRepository repository;

    @InjectMocks
    private SensorServiceImpl sensorService;

    private Dispositivo dispositivo;
    private Sensor sensor;

    @BeforeEach
    void setUp() {
        dispositivo = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .id(1L)
                .build();

        sensor = sistemaagricola.com.projecto.util.TestDataBuilder.sensor()
                .id(1L)
                .tipo("HUM_SOLO")
                .unidade("%")
                .dispositivo(dispositivo)
                .build();
    }

    @Test
    void should_criar_when_validSensor() {
        Sensor novoSensor = sistemaagricola.com.projecto.util.TestDataBuilder.sensor()
                .tipo("TEMP_AR")
                .unidade("°C")
                .dispositivo(dispositivo)
                .build();

        when(repository.existsByDispositivoIdAndTipo(1L, "TEMP_AR")).thenReturn(false);
        when(repository.save(any(Sensor.class))).thenAnswer(invocation -> {
            Sensor s = invocation.getArgument(0);
            s.setId(2L);
            return s;
        });

        Sensor result = sensorService.criar(novoSensor);

        assertThat(result).isNotNull();
        verify(repository).existsByDispositivoIdAndTipo(1L, "TEMP_AR");
        verify(repository).save(any(Sensor.class));
    }

    @Test
    void should_throwException_when_sensorDuplicated() {
        Sensor novoSensor = sistemaagricola.com.projecto.util.TestDataBuilder.sensor()
                .tipo("HUM_SOLO")
                .dispositivo(dispositivo)
                .build();

        when(repository.existsByDispositivoIdAndTipo(1L, "HUM_SOLO")).thenReturn(true);

        assertThrows(BusinessException.class, () -> {
            sensorService.criar(novoSensor);
        });

        verify(repository).existsByDispositivoIdAndTipo(1L, "HUM_SOLO");
        verify(repository, never()).save(any(Sensor.class));
    }

    @Test
    void should_obter_when_sensorExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(sensor));

        Sensor result = sensorService.obter(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTipo()).isEqualTo("HUM_SOLO");
        verify(repository).findById(1L);
    }

    @Test
    void should_throwException_when_sensorNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            sensorService.obter(1L);
        });

        verify(repository).findById(1L);
    }

    @Test
    void should_listarPorDispositivo_when_sensorsExist() {
        Sensor sensor2 = sistemaagricola.com.projecto.util.TestDataBuilder.sensor()
                .id(2L)
                .tipo("TEMP_AR")
                .dispositivo(dispositivo)
                .build();

        when(repository.findByDispositivoId(1L)).thenReturn(Arrays.asList(sensor, sensor2));

        List<Sensor> result = sensorService.listarPorDispositivo(1L);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(repository).findByDispositivoId(1L);
    }

    @Test
    void should_remover_when_sensorExists() {
        doNothing().when(repository).deleteById(1L);

        sensorService.remover(1L);

        verify(repository).deleteById(1L);
    }
}

