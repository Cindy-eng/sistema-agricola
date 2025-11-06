// service/impl/ParcelaServiceImpl.java
package com.agro.iot.service.impl;
import com.agro.iot.model.Parcela;
import com.agro.iot.repository.ParcelaRepository;
import com.agro.iot.service.ParcelaService;
import com.agro.iot.exception.*;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @Transactional
public class ParcelaServiceImpl implements ParcelaService {
  private final ParcelaRepository repo;
  public ParcelaServiceImpl(ParcelaRepository repo){ this.repo=repo; }

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
