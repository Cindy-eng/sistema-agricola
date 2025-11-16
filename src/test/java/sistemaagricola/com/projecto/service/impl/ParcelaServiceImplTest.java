package sistemaagricola.com.projecto.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sistemaagricola.com.projecto.exception.BusinessException;
import sistemaagricola.com.projecto.exception.NotFoundException;
import sistemaagricola.com.projecto.models.Parcela;
import sistemaagricola.com.projecto.models.User;
import sistemaagricola.com.projecto.repositories.ParecelaRepository;
import sistemaagricola.com.projecto.security.SecurityUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParcelaServiceImplTest {

    @Mock
    private ParecelaRepository repository;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private ParcelaServiceImpl parcelaService;

    private User user;
    private Parcela parcela;

    @BeforeEach
    void setUp() {
        user = sistemaagricola.com.projecto.util.TestDataBuilder.user()
                .id(1L)
                .build();

        parcela = sistemaagricola.com.projecto.util.TestDataBuilder.parcela()
                .id(1L)
                .nome("Parcela Norte")
                .lat(-23.5505)
                .lon(-46.6333)
                .usuario(user)
                .build();
    }

    @Test
    void should_criar_when_validParcela() {
        Parcela novaParcela = sistemaagricola.com.projecto.util.TestDataBuilder.parcela()
                .nome("Parcela Sul")
                .lat(-23.5600)
                .lon(-46.6400)
                .build();

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.existsByNomeAndUsuario("Parcela Sul", user)).thenReturn(false);
        when(repository.save(any(Parcela.class))).thenAnswer(invocation -> {
            Parcela p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        Parcela result = parcelaService.criar(novaParcela);

        assertThat(result).isNotNull();
        assertThat(result.getUsuario()).isEqualTo(user);
        verify(securityUtil).getCurrentUser();
        verify(repository).existsByNomeAndUsuario("Parcela Sul", user);
        verify(repository).save(any(Parcela.class));
    }

    @Test
    void should_throwException_when_parcelaAlreadyExists() {
        Parcela novaParcela = sistemaagricola.com.projecto.util.TestDataBuilder.parcela()
                .nome("Parcela Norte")
                .build();

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.existsByNomeAndUsuario("Parcela Norte", user)).thenReturn(true);

        assertThrows(BusinessException.class, () -> {
            parcelaService.criar(novaParcela);
        });

        verify(repository).existsByNomeAndUsuario("Parcela Norte", user);
        verify(repository, never()).save(any(Parcela.class));
    }

    @Test
    void should_obter_when_parcelaExists() {
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.findByIdAndUsuario(1L, user)).thenReturn(Optional.of(parcela));

        Parcela result = parcelaService.obter(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNome()).isEqualTo("Parcela Norte");
        verify(repository).findByIdAndUsuario(1L, user);
    }

    @Test
    void should_throwException_when_parcelaNotFound() {
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.findByIdAndUsuario(1L, user)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            parcelaService.obter(1L);
        });

        verify(repository).findByIdAndUsuario(1L, user);
    }

    @Test
    void should_listar_when_parcelasExist() {
        Parcela parcela2 = sistemaagricola.com.projecto.util.TestDataBuilder.parcela()
                .id(2L)
                .nome("Parcela Sul")
                .usuario(user)
                .build();

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.findByUsuario(user)).thenReturn(Arrays.asList(parcela, parcela2));

        List<Parcela> result = parcelaService.listar();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(repository).findByUsuario(user);
    }

    @Test
    void should_actualizar_when_parcelaExists() {
        Parcela parcelaAtualizada = sistemaagricola.com.projecto.util.TestDataBuilder.parcela()
                .nome("Parcela Norte Atualizada")
                .lat(-23.5555)
                .lon(-46.6444)
                .build();

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.findByIdAndUsuario(1L, user)).thenReturn(Optional.of(parcela));
        when(repository.save(any(Parcela.class))).thenReturn(parcela);

        Parcela result = parcelaService.actualizar(1L, parcelaAtualizada);

        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("Parcela Norte Atualizada");
        assertThat(result.getLat()).isEqualTo(-23.5555);
        assertThat(result.getLon()).isEqualTo(-46.6444);
        verify(repository).findByIdAndUsuario(1L, user);
        verify(repository).save(parcela);
    }

    @Test
    void should_remover_when_parcelaExists() {
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.existsByIdAndUsuario(1L, user)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        parcelaService.remover(1L);

        verify(repository).existsByIdAndUsuario(1L, user);
        verify(repository).deleteById(1L);
    }

    @Test
    void should_throwException_when_removerParcelaNotFound() {
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.existsByIdAndUsuario(1L, user)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            parcelaService.remover(1L);
        });

        verify(repository).existsByIdAndUsuario(1L, user);
        verify(repository, never()).deleteById(anyLong());
    }
}

