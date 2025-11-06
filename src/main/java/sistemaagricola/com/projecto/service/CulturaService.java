// service/CulturaService.java
package com.agro.iot.service;
import com.agro.iot.model.Cultura; import java.util.List;
public interface CulturaService {
  Cultura criar(Cultura c);
  Cultura obter(Long id);
  List<Cultura> listar();
  Cultura actualizar(Long id, Cultura c);
  void remover(Long id);
}
