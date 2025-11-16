package sistemaagricola.com.projecto.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sistemaagricola.com.projecto.dto.CreateRegraDTO;
import sistemaagricola.com.projecto.exception.NotFoundException;
import sistemaagricola.com.projecto.models.Parcela;
import sistemaagricola.com.projecto.models.RegraAlerta;
import sistemaagricola.com.projecto.models.User;
import sistemaagricola.com.projecto.repositories.ParecelaRepository;
import sistemaagricola.com.projecto.repositories.RegraAlertaRepository;
import sistemaagricola.com.projecto.security.SecurityUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegraAlertaServiceImplTest {

    @Mock
    private RegraAlertaRepository repository;

    @Mock
    private ParecelaRepository parcelaRepository;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private RegraAlertaServiceImpl regraAlertaService;

    private User user;
    private Parcela parcela;
    private RegraAlerta regraAlerta;

    @BeforeEach
    void setUp() {
        user = sistemaagricola.com.projecto.util.TestDataBuilder.user()
                .id(1L)
                .build();

        parcela = sistemaagricola.com.projecto.util.TestDataBuilder.parcela()
                .id(1L)
                .usuario(user)
                .build();

        regraAlerta = sistemaagricola.com.projecto.util.TestDataBuilder.regraAlerta()
                .id(1L)
                .parcela(parcela)
                .build();
    }

    @Test
    void should_criar_when_validDTO() {
        CreateRegraDTO dto = new CreateRegraDTO(
                1L,
                "tipo==HUM_SOLO && valor<18 por 120m",
                "AVISO",
                "APP",
                true
        );

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(parcelaRepository.findByIdAndUsuario(1L, user)).thenReturn(Optional.of(parcela));
        when(repository.save(any(RegraAlerta.class))).thenReturn(regraAlerta);

        RegraAlerta result = regraAlertaService.criar(dto);

        assertThat(result).isNotNull();
        verify(securityUtil).getCurrentUser();
        verify(parcelaRepository).findByIdAndUsuario(1L, user);
        verify(repository).save(any(RegraAlerta.class));
    }

    @Test
    void should_throwException_when_parcelaNotFound() {
        CreateRegraDTO dto = new CreateRegraDTO(
                999L,
                "tipo==HUM_SOLO && valor<18 por 120m",
                "AVISO",
                "APP",
                true
        );

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(parcelaRepository.findByIdAndUsuario(999L, user)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            regraAlertaService.criar(dto);
        });

        verify(parcelaRepository).findByIdAndUsuario(999L, user);
        verify(repository, never()).save(any(RegraAlerta.class));
    }

    @Test
    void should_activar_when_regraExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(regraAlerta));
        when(repository.save(any(RegraAlerta.class))).thenReturn(regraAlerta);

        RegraAlerta result = regraAlertaService.activar(1L, false);

        assertThat(result).isNotNull();
        assertThat(result.getActivo()).isFalse();
        verify(repository).findById(1L);
        verify(repository).save(regraAlerta);
    }

    @Test
    void should_throwException_when_activarRegraNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            regraAlertaService.activar(999L, true);
        });

        verify(repository).findById(999L);
        verify(repository, never()).save(any(RegraAlerta.class));
    }

    @Test
    void should_listarActivasPorParcela_when_parcelaExists() {
        RegraAlerta regra2 = sistemaagricola.com.projecto.util.TestDataBuilder.regraAlerta()
                .id(2L)
                .parcela(parcela)
                .build();

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(parcelaRepository.findByIdAndUsuario(1L, user)).thenReturn(Optional.of(parcela));
        when(repository.findByParcelaIdAndActivoTrue(1L)).thenReturn(Arrays.asList(regraAlerta, regra2));

        List<RegraAlerta> result = regraAlertaService.listarActivasPorParcela(1L);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(parcelaRepository).findByIdAndUsuario(1L, user);
        verify(repository).findByParcelaIdAndActivoTrue(1L);
    }

    @Test
    void should_throwException_when_listarActivasParcelaNotFound() {
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(parcelaRepository.findByIdAndUsuario(999L, user)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            regraAlertaService.listarActivasPorParcela(999L);
        });

        verify(parcelaRepository).findByIdAndUsuario(999L, user);
        verify(repository, never()).findByParcelaIdAndActivoTrue(anyLong());
    }

    @Test
    void should_remover_when_regraExists() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        regraAlertaService.remover(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void should_throwException_when_removerRegraNotFound() {
        when(repository.existsById(999L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            regraAlertaService.remover(999L);
        });

        verify(repository).existsById(999L);
        verify(repository, never()).deleteById(anyLong());
    }
}

