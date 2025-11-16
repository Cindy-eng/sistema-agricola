package sistemaagricola.com.projecto.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sistemaagricola.com.projecto.models.Dispositivo;
import sistemaagricola.com.projecto.models.EventoIot;
import sistemaagricola.com.projecto.repositories.EventoIotRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoIotServiceImplTest {

    @Mock
    private EventoIotRepository repository;

    @InjectMocks
    private EventoIotServiceImpl eventoIotService;

    private Dispositivo dispositivo;
    private EventoIot eventoIot;

    @BeforeEach
    void setUp() {
        dispositivo = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .id(1L)
                .build();

        eventoIot = sistemaagricola.com.projecto.util.TestDataBuilder.eventoIot()
                .id(1L)
                .dispositivo(dispositivo)
                .tipo("ONLINE")
                .ts(Instant.now())
                .build();
    }

    @Test
    void should_registar_when_validEvento() {
        EventoIot novoEvento = sistemaagricola.com.projecto.util.TestDataBuilder.eventoIot()
                .tipo("OFFLINE")
                .dispositivo(dispositivo)
                .build();

        when(repository.save(any(EventoIot.class))).thenAnswer(invocation -> {
            EventoIot e = invocation.getArgument(0);
            e.setId(2L);
            return e;
        });

        EventoIot result = eventoIotService.registar(novoEvento);

        assertThat(result).isNotNull();
        verify(repository).save(any(EventoIot.class));
    }

    @Test
    void should_listarPorDispositivoEPeriodo_when_validParameters() {
        Instant ini = Instant.now().minusSeconds(3600);
        Instant fim = Instant.now();

        EventoIot evento2 = sistemaagricola.com.projecto.util.TestDataBuilder.eventoIot()
                .id(2L)
                .dispositivo(dispositivo)
                .tipo("PRAGA_DETECTADA")
                .build();

        when(repository.findByDispositivoAndPeriodo(1L, ini, fim))
                .thenReturn(Arrays.asList(eventoIot, evento2));

        List<EventoIot> result = eventoIotService.listarPorDispositivoEPeriodo(1L, ini, fim);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(repository).findByDispositivoAndPeriodo(1L, ini, fim);
    }

    @Test
    void should_purgarAntes_when_validParameters() {
        Instant ts = Instant.now().minusSeconds(86400);

        when(repository.deleteByDispositivoIdAndTsBefore(1L, ts))
                .thenReturn(10L);

        long result = eventoIotService.purgarAntes(1L, ts);

        assertThat(result).isEqualTo(10L);
        verify(repository).deleteByDispositivoIdAndTsBefore(1L, ts);
    }
}

