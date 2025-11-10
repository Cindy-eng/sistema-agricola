// service/impl/ParcelaServiceImpl.java
package sistemaagricola.com.projecto.service.impl;
import sistemaagricola.com.projecto.models.Parcela;
import sistemaagricola.com.projecto.repositories.ParecelaRepository;
import sistemaagricola.com.projecto.service.ParcelaService;
import sistemaagricola.com.projecto.exception.*;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @Transactional
public class ParcelaServiceImpl implements ParcelaService {
  private final ParecelaRepository repo;
  public ParcelaServiceImpl(ParecelaRepository repo){ this.repo=repo; }

  public Parcela criar(Parcela p){
    if(repo.existsByNome(p.getNome())) throw new BusinessException("Parcela já existe");
    return repo.save(p);
  }
  @Transactional(readOnly=true)
  public Parcela obter(Long id){ return repo.findById(id).orElseThrow(() -> new NotFoundException("Parcela não encontrada")); }
  @Transactional(readOnly=true)
  public List<Parcela> listar(){ return repo.findAll(); }
  public Parcela actualizar(Long id, Parcela p){
    var atual = obter(id);
    atual.setNome(p.getNome()); atual.setLat(p.getLat()); atual.setLon(p.getLon());
    return repo.save(atual);
  }
  public void remover(Long id){ repo.deleteById(id); }
}
