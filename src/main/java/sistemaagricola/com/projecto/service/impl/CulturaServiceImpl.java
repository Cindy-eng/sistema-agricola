// service/impl/CulturaServiceImpl.java
package sistemaagricola.com.projecto.service.impl;
import sistemaagricola.com.projecto.models.Cultura;
import sistemaagricola.com.projecto.models.User;
import sistemaagricola.com.projecto.repositories.CulturaRepository;
import sistemaagricola.com.projecto.service.CulturaService;
import sistemaagricola.com.projecto.exception.*;
import sistemaagricola.com.projecto.security.SecurityUtil;
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @Transactional
public class CulturaServiceImpl implements CulturaService {
  private final CulturaRepository repo;
  private final SecurityUtil securityUtil;
  
  public CulturaServiceImpl(CulturaRepository repo, SecurityUtil securityUtil){ 
    this.repo=repo; 
    this.securityUtil = securityUtil;
  }
  
  public Cultura criar(Cultura c){ 
    User usuario = securityUtil.getCurrentUser();
    c.setUsuario(usuario);
    return repo.save(c); 
  }
  
  @Transactional(readOnly=true)
  public Cultura obter(Long id){ 
    User usuario = securityUtil.getCurrentUser();
    return repo.findByIdAndUsuario(id, usuario)
        .orElseThrow(() -> new NotFoundException("Cultura não encontrada")); 
  }
  
  @Transactional(readOnly=true)
  public List<Cultura> listar(){ 
    User usuario = securityUtil.getCurrentUser();
    return repo.findByUsuario(usuario); 
  }
  
  public Cultura actualizar(Long id, Cultura c){
    User usuario = securityUtil.getCurrentUser();
    var atual = repo.findByIdAndUsuario(id, usuario)
        .orElseThrow(() -> new NotFoundException("Cultura não encontrada"));
    atual.setNome(c.getNome()); 
    return repo.save(atual);
  }
  
  public void remover(Long id){ 
    User usuario = securityUtil.getCurrentUser();
    if (!repo.existsByIdAndUsuario(id, usuario)) {
      throw new NotFoundException("Cultura não encontrada");
    }
    repo.deleteById(id); 
  }
}
