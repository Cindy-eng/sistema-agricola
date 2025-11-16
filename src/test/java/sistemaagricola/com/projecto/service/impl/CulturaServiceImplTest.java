package sistemaagricola.com.projecto.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sistemaagricola.com.projecto.exception.NotFoundException;
import sistemaagricola.com.projecto.models.Cultura;
import sistemaagricola.com.projecto.models.User;
import sistemaagricola.com.projecto.repositories.CulturaRepository;
import sistemaagricola.com.projecto.security.SecurityUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CulturaServiceImplTest {

    @Mock
    private CulturaRepository repository;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private CulturaServiceImpl culturaService;

    private User user;
    private Cultura cultura;

    @BeforeEach
    void setUp() {
        user = sistemaagricola.com.projecto.util.TestDataBuilder.user()
                .id(1L)
                .build();

        cultura = sistemaagricola.com.projecto.util.TestDataBuilder.cultura()
                .id(1L)
                .nome("Milho")
                .usuario(user)
                .build();
    }

    @Test
    void should_criar_when_validCultura() {
        Cultura novaCultura = sistemaagricola.com.projecto.util.TestDataBuilder.cultura()
                .nome("Trigo")
                .build();

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.save(any(Cultura.class))).thenAnswer(invocation -> {
            Cultura c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });

        Cultura result = culturaService.criar(novaCultura);

        assertThat(result).isNotNull();
        assertThat(result.getUsuario()).isEqualTo(user);
        verify(securityUtil).getCurrentUser();
        verify(repository).save(any(Cultura.class));
    }

    @Test
    void should_obter_when_culturaExists() {
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.findByIdAndUsuario(1L, user)).thenReturn(Optional.of(cultura));

        Cultura result = culturaService.obter(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNome()).isEqualTo("Milho");
        verify(repository).findByIdAndUsuario(1L, user);
    }

    @Test
    void should_throwException_when_culturaNotFound() {
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.findByIdAndUsuario(1L, user)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            culturaService.obter(1L);
        });

        verify(repository).findByIdAndUsuario(1L, user);
    }

    @Test
    void should_listar_when_culturasExist() {
        Cultura cultura2 = sistemaagricola.com.projecto.util.TestDataBuilder.cultura()
                .id(2L)
                .nome("Soja")
                .usuario(user)
                .build();

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.findByUsuario(user)).thenReturn(Arrays.asList(cultura, cultura2));

        List<Cultura> result = culturaService.listar();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(repository).findByUsuario(user);
    }

    @Test
    void should_actualizar_when_culturaExists() {
        Cultura culturaAtualizada = sistemaagricola.com.projecto.util.TestDataBuilder.cultura()
                .nome("Milho Atualizado")
                .build();

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.findByIdAndUsuario(1L, user)).thenReturn(Optional.of(cultura));
        when(repository.save(any(Cultura.class))).thenReturn(cultura);

        Cultura result = culturaService.actualizar(1L, culturaAtualizada);

        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("Milho Atualizado");
        verify(repository).findByIdAndUsuario(1L, user);
        verify(repository).save(cultura);
    }

    @Test
    void should_throwException_when_actualizarCulturaNotFound() {
        Cultura culturaAtualizada = sistemaagricola.com.projecto.util.TestDataBuilder.cultura()
                .nome("Milho Atualizado")
                .build();

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.findByIdAndUsuario(1L, user)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            culturaService.actualizar(1L, culturaAtualizada);
        });

        verify(repository).findByIdAndUsuario(1L, user);
        verify(repository, never()).save(any(Cultura.class));
    }

    @Test
    void should_remover_when_culturaExists() {
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.existsByIdAndUsuario(1L, user)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        culturaService.remover(1L);

        verify(repository).existsByIdAndUsuario(1L, user);
        verify(repository).deleteById(1L);
    }

    @Test
    void should_throwException_when_removerCulturaNotFound() {
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.existsByIdAndUsuario(1L, user)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            culturaService.remover(1L);
        });

        verify(repository).existsByIdAndUsuario(1L, user);
        verify(repository, never()).deleteById(anyLong());
    }
}

