package sistemaagricola.com.projecto.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sistemaagricola.com.projecto.repositories.TelemetriaRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelemetriaQueryServiceImplTest {

    @Mock
    private TelemetriaRepository repository;

    @InjectMocks
    private TelemetriaQueryServiceImpl queryService;

    private TelemetriaRepository.TelemetriaAgg aggregate;

    @BeforeEach
    void setUp() {
        aggregate = mock(TelemetriaRepository.TelemetriaAgg.class);
        when(aggregate.getBucket()).thenReturn(Instant.now());
        when(aggregate.getAvg()).thenReturn(25.5);
        when(aggregate.getMin()).thenReturn(20.0);
        when(aggregate.getMax()).thenReturn(30.0);
        when(aggregate.getCnt()).thenReturn(100L);
    }

    @Test
    void should_agregar_when_validParameters() {
        Instant ini = Instant.now().minusSeconds(3600);
        Instant fim = Instant.now();
        String granularidade = "hour";

        when(repository.aggregateWindow(1L, ini, fim, granularidade))
                .thenReturn(Arrays.asList(aggregate));

        List<TelemetriaRepository.TelemetriaAgg> result = queryService.agregar(1L, ini, fim, granularidade);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(repository).aggregateWindow(1L, ini, fim, granularidade);
    }
}

