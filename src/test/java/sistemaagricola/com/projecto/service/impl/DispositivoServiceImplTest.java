package sistemaagricola.com.projecto.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import sistemaagricola.com.projecto.exception.BusinessException;
import sistemaagricola.com.projecto.exception.NotFoundException;
import sistemaagricola.com.projecto.models.Dispositivo;
import sistemaagricola.com.projecto.models.Parcela;
import sistemaagricola.com.projecto.models.User;
import sistemaagricola.com.projecto.repositories.DispositivoRepository;
import sistemaagricola.com.projecto.repositories.ParecelaRepository;
import sistemaagricola.com.projecto.security.SecurityUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DispositivoServiceImplTest {

    @Mock
    private DispositivoRepository repository;

    @Mock
    private ParecelaRepository parcelaRepository;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private DispositivoServiceImpl dispositivoService;

    private User user;
    private Parcela parcela;
    private Dispositivo dispositivo;

    @BeforeEach
    void setUp() {
        user = sistemaagricola.com.projecto.util.TestDataBuilder.user()
                .id(1L)
                .build();

        parcela = sistemaagricola.com.projecto.util.TestDataBuilder.parcela()
                .id(1L)
                .usuario(user)
                .build();

        dispositivo = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .id(1L)
                .deviceKey("DEV-001")
                .parcela(parcela)
                .build();
    }

    @Test
    void should_criar_when_validDispositivo() {
        Dispositivo novoDispositivo = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .deviceKey("DEV-002")
                .parcela(parcela)
                .build();

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(parcelaRepository.findByIdAndUsuario(1L, user)).thenReturn(Optional.of(parcela));
        when(repository.existsByDeviceKey("DEV-002")).thenReturn(false);
        when(repository.save(any(Dispositivo.class))).thenAnswer(invocation -> {
            Dispositivo d = invocation.getArgument(0);
            d.setId(2L);
            return d;
        });

        Dispositivo result = dispositivoService.criar(novoDispositivo);

        assertThat(result).isNotNull();
        assertThat(result.getParcela()).isEqualTo(parcela);
        verify(securityUtil).getCurrentUser();
        verify(parcelaRepository).findByIdAndUsuario(1L, user);
        verify(repository).existsByDeviceKey("DEV-002");
        verify(repository).save(any(Dispositivo.class));
    }

    @Test
    void should_throwException_when_parcelaNotFound() {
        Dispositivo novoDispositivo = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .deviceKey("DEV-002")
                .parcela(parcela)
                .build();

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(parcelaRepository.findByIdAndUsuario(1L, user)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> {
            dispositivoService.criar(novoDispositivo);
        });

        verify(parcelaRepository).findByIdAndUsuario(1L, user);
        verify(repository, never()).save(any(Dispositivo.class));
    }

    @Test
    void should_throwException_when_parcelaIsNull() {
        Dispositivo novoDispositivo = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .deviceKey("DEV-002")
                .parcela(null)
                .build();

        when(securityUtil.getCurrentUser()).thenReturn(user);

        assertThrows(BusinessException.class, () -> {
            dispositivoService.criar(novoDispositivo);
        });

        verify(repository, never()).save(any(Dispositivo.class));
    }

    @Test
    void should_throwException_when_deviceKeyDuplicated() {
        Dispositivo novoDispositivo = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .deviceKey("DEV-001")
                .parcela(parcela)
                .build();

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(parcelaRepository.findByIdAndUsuario(1L, user)).thenReturn(Optional.of(parcela));
        when(repository.existsByDeviceKey("DEV-001")).thenReturn(true);

        assertThrows(BusinessException.class, () -> {
            dispositivoService.criar(novoDispositivo);
        });

        verify(repository).existsByDeviceKey("DEV-001");
        verify(repository, never()).save(any(Dispositivo.class));
    }

    @Test
    void should_obter_when_dispositivoExists() {
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.findById(1L)).thenReturn(Optional.of(dispositivo));
        when(parcelaRepository.existsByIdAndUsuario(1L, user)).thenReturn(true);

        Dispositivo result = dispositivoService.obter(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(repository).findById(1L);
        verify(parcelaRepository).existsByIdAndUsuario(1L, user);
    }

    @Test
    void should_throwException_when_dispositivoNotFound() {
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            dispositivoService.obter(1L);
        });

        verify(repository).findById(1L);
    }

    @Test
    void should_throwException_when_parcelaNotBelongsToUser() {
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.findById(1L)).thenReturn(Optional.of(dispositivo));
        when(parcelaRepository.existsByIdAndUsuario(1L, user)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            dispositivoService.obter(1L);
        });

        verify(parcelaRepository).existsByIdAndUsuario(1L, user);
    }

    @Test
    void should_listarPorParcela_when_parcelaExists() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Dispositivo> page = new PageImpl<>(Arrays.asList(dispositivo));

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(parcelaRepository.existsByIdAndUsuario(1L, user)).thenReturn(true);
        when(repository.findByParcelaId(1L, pageable)).thenReturn(page);

        Page<Dispositivo> result = dispositivoService.listarPorParcela(1L, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(parcelaRepository).existsByIdAndUsuario(1L, user);
        verify(repository).findByParcelaId(1L, pageable);
    }

    @Test
    void should_alterarEstado_when_dispositivoExists() {
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(repository.findById(1L)).thenReturn(Optional.of(dispositivo));
        when(parcelaRepository.existsByIdAndUsuario(1L, user)).thenReturn(true);
        when(repository.save(any(Dispositivo.class))).thenReturn(dispositivo);

        Dispositivo result = dispositivoService.alterarEstado(1L, Dispositivo.Estado.INACTIVO);

        assertThat(result).isNotNull();
        assertThat(result.getEstado()).isEqualTo(Dispositivo.Estado.INACTIVO);
        verify(repository).save(dispositivo);
    }

    @Test
    void should_listarTodos_when_parcelasExist() {
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(parcelaRepository.findByUsuario(user)).thenReturn(Arrays.asList(parcela));
        when(repository.findAll()).thenReturn(Arrays.asList(dispositivo));

        List<Dispositivo> result = dispositivoService.listarTodos();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(parcelaRepository).findByUsuario(user);
    }

    @Test
    void should_returnEmptyList_when_noParcelas() {
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(parcelaRepository.findByUsuario(user)).thenReturn(Collections.emptyList());

        List<Dispositivo> result = dispositivoService.listarTodos();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(repository, never()).findAll();
    }

    @Test
    void should_obterPorDeviceKey_when_deviceKeyExists() {
        when(repository.findByDeviceKey("DEV-001")).thenReturn(Optional.of(dispositivo));

        Optional<Dispositivo> result = dispositivoService.obterPorDeviceKey("DEV-001");

        assertThat(result).isPresent();
        assertThat(result.get().getDeviceKey()).isEqualTo("DEV-001");
        verify(repository).findByDeviceKey("DEV-001");
    }
}

