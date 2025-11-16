package sistemaagricola.com.projecto.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sistemaagricola.com.projecto.exception.NotFoundException;
import sistemaagricola.com.projecto.models.Dispositivo;
import sistemaagricola.com.projecto.models.Manutencao;
import sistemaagricola.com.projecto.repositories.ManutencaoRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManutencaoServiceImplTest {

    @Mock
    private ManutencaoRepository repository;

    @InjectMocks
    private ManutencaoServiceImpl manutencaoService;

    private Dispositivo dispositivo;
    private Manutencao manutencao;

    @BeforeEach
    void setUp() {
        dispositivo = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .id(1L)
                .build();

        manutencao = sistemaagricola.com.projecto.util.TestDataBuilder.manutencao()
                .id(1L)
                .dispositivo(dispositivo)
                .tipo("CALIBRACAO")
                .estado("ABERTA")
                .build();
    }

    @Test
    void should_abrir_when_validManutencao() {
        Manutencao novaManutencao = sistemaagricola.com.projecto.util.TestDataBuilder.manutencao()
                .tipo("TROCA_BATERIA")
                .dispositivo(dispositivo)
                .build();

        when(repository.save(any(Manutencao.class))).thenAnswer(invocation -> {
            Manutencao m = invocation.getArgument(0);
            m.setId(2L);
            m.setEstado("ABERTA");
            m.setTsAbertura(Instant.now());
            return m;
        });

        Manutencao result = manutencaoService.abrir(novaManutencao);

        assertThat(result).isNotNull();
        assertThat(result.getEstado()).isEqualTo("ABERTA");
        assertThat(result.getTsAbertura()).isNotNull();
        verify(repository).save(any(Manutencao.class));
    }

    @Test
    void should_fechar_when_manutencaoExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(manutencao));
        when(repository.save(any(Manutencao.class))).thenReturn(manutencao);

        Manutencao result = manutencaoService.fechar(1L);

        assertThat(result).isNotNull();
        assertThat(result.getEstado()).isEqualTo("FECHADA");
        assertThat(result.getTsFecho()).isNotNull();
        verify(repository).findById(1L);
        verify(repository).save(manutencao);
    }

    @Test
    void should_throwException_when_fecharManutencaoNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            manutencaoService.fechar(999L);
        });

        verify(repository).findById(999L);
        verify(repository, never()).save(any(Manutencao.class));
    }

    @Test
    void should_listarPorDispositivoEEstado_when_validParameters() {
        Manutencao manutencao2 = sistemaagricola.com.projecto.util.TestDataBuilder.manutencao()
                .id(2L)
                .dispositivo(dispositivo)
                .estado("ABERTA")
                .build();

        when(repository.findByDispositivoIdAndEstado(1L, "ABERTA"))
                .thenReturn(Arrays.asList(manutencao, manutencao2));

        List<Manutencao> result = manutencaoService.listarPorDispositivoEEstado(1L, "ABERTA");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(repository).findByDispositivoIdAndEstado(1L, "ABERTA");
    }
}

