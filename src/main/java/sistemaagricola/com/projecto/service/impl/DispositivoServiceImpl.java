package sistemaagricola.com.projecto.service.impl;

<<<<<<< HEAD
// service/impl/DispositivoServiceImpl.java
=======
>>>>>>> origin/master
import sistemaagricola.com.projecto.models.Dispositivo;
import sistemaagricola.com.projecto.repositories.DispositivoRepository;
import sistemaagricola.com.projecto.service.DispositivoService;
import sistemaagricola.com.projecto.exception.*;
<<<<<<< HEAD
=======

>>>>>>> origin/master
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.*; import java.util.*;

@Service @Transactional
public class DispositivoServiceImpl implements DispositivoService {
<<<<<<< HEAD
    private final DispositivoRepository repo;
    public DispositivoServiceImpl(DispositivoRepository repo){ this.repo=repo; }
    public Dispositivo criar(Dispositivo d){
        if(repo.existsByDeviceKey(d.getDeviceKey())) throw new BusinessException("deviceKey duplicada");
        return repo.save(d);
    }
    @Transactional(readOnly=true)
    public Optional<Dispositivo> obterPorDeviceKey(String key){ return repo.findByDeviceKey(key); }
    @Transactional(readOnly=true)
    public Dispositivo obter(Long id){ return repo.findById(id).orElseThrow(() -> new NotFoundException("Dispositivo não encontrado")); }
    @Transactional(readOnly=true)
    public Page<Dispositivo> listarPorParcela(Long parcelaId, Pageable pg){ return repo.findByParcelaId(parcelaId, pg); }
    public Dispositivo alterarEstado(Long id, Dispositivo.Estado estado){
        var d = obter(id); d.setEstado(estado); return repo.save(d);
    }
    @Transactional(readOnly=true)
    public List<Dispositivo> listarTodos(){ return repo.findAll(); }
}
=======
  private final DispositivoRepository repo;
  public DispositivoServiceImpl(DispositivoRepository repo){ this.repo=repo; }
  public Dispositivo criar(Dispositivo d){
    if(repo.existsByDeviceKey(d.getDeviceKey())) throw new BusinessException("deviceKey duplicada");
    return repo.save(d);
  }
  @Transactional(readOnly=true)
  public Optional<Dispositivo> obterPorDeviceKey(String key){ return repo.findByDeviceKey(key); }
  @Transactional(readOnly=true)
  public Dispositivo obter(Long id){ return repo.findById(id).orElseThrow(() -> new NotFoundException("Dispositivo não encontrado")); }
  @Transactional(readOnly=true)
  public Page<Dispositivo> listarPorParcela(Long parcelaId, Pageable pg){ return repo.findByParcelaId(parcelaId, pg); }
  public Dispositivo alterarEstado(Long id, Dispositivo.Estado estado){
    var d = obter(id); d.setEstado(estado); return repo.save(d);
  }
  @Transactional(readOnly=true)
  public List<Dispositivo> listarTodos(){ return repo.findAll(); }
}
>>>>>>> origin/master
