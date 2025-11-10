package sistemaagricola.com.projecto.service.impl;

import sistemaagricola.com.projecto.models.Sensor;
import sistemaagricola.com.projecto.repositories.SensorRepository;
import sistemaagricola.com.projecto.service.SensorService;
import sistemaagricola.com.projecto.exception.BusinessException;
import sistemaagricola.com.projecto.exception.NotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @Transactional
public class SensorServiceImpl implements SensorService {
  private final SensorRepository repo;
  public SensorServiceImpl(SensorRepository repo){ this.repo=repo; }
  public Sensor criar(Sensor s){
    if(repo.existsByDispositivoIdAndTipo(s.getDispositivo().getId(), s.getTipo()))
      throw new BusinessException("Sensor duplicado no dispositivo");
    return repo.save(s);
  }
  @Transactional(readOnly=true)
  public Sensor obter(Long id){ return repo.findById(id).orElseThrow(() -> new NotFoundException("Sensor não encontrado")); }
  @Transactional(readOnly=true)
  public List<Sensor> listarPorDispositivo(Long dispositivoId){ return repo.findByDispositivoId(dispositivoId); }
  public void remover(Long id){ repo.deleteById(id); }
}
