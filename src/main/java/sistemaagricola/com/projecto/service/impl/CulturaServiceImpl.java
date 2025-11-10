// service/impl/CulturaServiceImpl.java
package sistemaagricola.com.projecto.service.impl;
import sistemaagricola.com.projecto.models.Cultura;
import sistemaagricola.com.projecto.repositories.CulturaRepository;
import sistemaagricola.com.projecto.service.CulturaService;
import sistemaagricola.com.projecto.exception.*;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @Transactional
public class CulturaServiceImpl implements CulturaService {
  private final CulturaRepository repo;
  public CulturaServiceImpl(CulturaRepository repo){ this.repo=repo; }
  public Cultura criar(Cultura c){ return repo.save(c); }
  @Transactional(readOnly=true)
  public Cultura obter(Long id){ return repo.findById(id).orElseThrow(() -> new NotFoundException("Cultura não encontrada")); }
  @Transactional(readOnly=true)
  public List<Cultura> listar(){ return repo.findAll(); }
  public Cultura actualizar(Long id, Cultura c){
    var atual = obter(id); atual.setNome(c.getNome()); return repo.save(atual);
  }
  public void remover(Long id){ repo.deleteById(id); }
}
