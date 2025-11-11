// service/impl/ParcelaServiceImpl.java
package sistemaagricola.com.projecto.service.impl;
import sistemaagricola.com.projecto.models.Parcela;
import sistemaagricola.com.projecto.models.User;
import sistemaagricola.com.projecto.repositories.ParecelaRepository;
import sistemaagricola.com.projecto.service.ParcelaService;
import sistemaagricola.com.projecto.exception.*;
import sistemaagricola.com.projecto.security.SecurityUtil;
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @Transactional
public class ParcelaServiceImpl implements ParcelaService {
  private final ParecelaRepository repo;
  private final SecurityUtil securityUtil;
  
  public ParcelaServiceImpl(ParecelaRepository repo, SecurityUtil securityUtil){ 
    this.repo=repo; 
    this.securityUtil = securityUtil;
  }

  public Parcela criar(Parcela p){
    User usuario = securityUtil.getCurrentUser();
    if(repo.existsByNomeAndUsuario(p.getNome(), usuario)) 
      throw new BusinessException("Parcela já existe");
    p.setUsuario(usuario);
    return repo.save(p);
  }
  
  @Transactional(readOnly=true)
  public Parcela obter(Long id){ 
    User usuario = securityUtil.getCurrentUser();
    return repo.findByIdAndUsuario(id, usuario)
        .orElseThrow(() -> new NotFoundException("Parcela não encontrada")); 
  }
  
  @Transactional(readOnly=true)
  public List<Parcela> listar(){ 
    User usuario = securityUtil.getCurrentUser();
    return repo.findByUsuario(usuario); 
  }
  
  public Parcela actualizar(Long id, Parcela p){
    User usuario = securityUtil.getCurrentUser();
    var atual = repo.findByIdAndUsuario(id, usuario)
        .orElseThrow(() -> new NotFoundException("Parcela não encontrada"));
    atual.setNome(p.getNome()); 
    atual.setLat(p.getLat()); 
    atual.setLon(p.getLon());
    return repo.save(atual);
  }
  
  public void remover(Long id){ 
    User usuario = securityUtil.getCurrentUser();
    if (!repo.existsByIdAndUsuario(id, usuario)) {
      throw new NotFoundException("Parcela não encontrada");
    }
    repo.deleteById(id); 
  }
}
