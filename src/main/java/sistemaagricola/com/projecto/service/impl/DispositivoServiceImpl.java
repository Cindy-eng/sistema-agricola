package sistemaagricola.com.projecto.service.impl;

import sistemaagricola.com.projecto.models.Dispositivo;
import sistemaagricola.com.projecto.models.Parcela;
import sistemaagricola.com.projecto.models.User;
import sistemaagricola.com.projecto.repositories.DispositivoRepository;
import sistemaagricola.com.projecto.repositories.ParecelaRepository;
import sistemaagricola.com.projecto.service.DispositivoService;
import sistemaagricola.com.projecto.exception.*;
import sistemaagricola.com.projecto.security.SecurityUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.*;
import java.util.*;

@Service
@Transactional
public class DispositivoServiceImpl implements DispositivoService {
    private final DispositivoRepository repo;
    private final ParecelaRepository parcelaRepo;
    private final SecurityUtil securityUtil;

    public DispositivoServiceImpl(DispositivoRepository repo, 
                                  ParecelaRepository parcelaRepo,
                                  SecurityUtil securityUtil) {
        this.repo = repo;
        this.parcelaRepo = parcelaRepo;
        this.securityUtil = securityUtil;
    }

    public Dispositivo criar(Dispositivo d) {
        User usuario = securityUtil.getCurrentUser();
        
        // Validar que a parcela pertence ao usuário
        if (d.getParcela() != null && d.getParcela().getId() != null) {
            Parcela parcela = parcelaRepo.findByIdAndUsuario(d.getParcela().getId(), usuario)
                    .orElseThrow(() -> new BusinessException("Parcela não encontrada ou não pertence ao usuário"));
            d.setParcela(parcela);
        } else {
            throw new BusinessException("Parcela é obrigatória");
        }
        
        if (repo.existsByDeviceKey(d.getDeviceKey()))
            throw new BusinessException("deviceKey duplicada");
        return repo.save(d);
    }

    @Transactional(readOnly = true)
    public Optional<Dispositivo> obterPorDeviceKey(String key) {
        return repo.findByDeviceKey(key);
    }

    @Transactional(readOnly = true)
    public Dispositivo obter(Long id) {
        User usuario = securityUtil.getCurrentUser();
        Dispositivo dispositivo = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Dispositivo não encontrado"));
        
        // Validar que a parcela do dispositivo pertence ao usuário
        if (!parcelaRepo.existsByIdAndUsuario(dispositivo.getParcela().getId(), usuario)) {
            throw new NotFoundException("Dispositivo não encontrado");
        }
        
        return dispositivo;
    }

    @Transactional(readOnly = true)
    public Page<Dispositivo> listarPorParcela(Long parcelaId, Pageable pg) {
        User usuario = securityUtil.getCurrentUser();
        
        // Validar que a parcela pertence ao usuário
        if (!parcelaRepo.existsByIdAndUsuario(parcelaId, usuario)) {
            throw new NotFoundException("Parcela não encontrada");
        }
        
        return repo.findByParcelaId(parcelaId, pg);
    }

    public Dispositivo alterarEstado(Long id, Dispositivo.Estado estado) {
        var d = obter(id); // Já valida se pertence ao usuário
        d.setEstado(estado);
        return repo.save(d);
    }

    @Transactional(readOnly = true)
    public List<Dispositivo> listarTodos() {
        User usuario = securityUtil.getCurrentUser();
        List<Parcela> parcelas = parcelaRepo.findByUsuario(usuario);
        
        if (parcelas.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<Long> parcelaIds = parcelas.stream()
                .map(Parcela::getId)
                .toList();
        
        return repo.findAll().stream()
                .filter(d -> parcelaIds.contains(d.getParcela().getId()))
                .toList();
    }
}
