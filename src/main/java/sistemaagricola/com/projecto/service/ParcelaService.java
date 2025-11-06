// service/ParcelaService.java
package com.agro.iot.service;
import com.agro.iot.model.Parcela; import java.util.List;
public interface ParcelaService {
  Parcela criar(Parcela p);
  Parcela obter(Long id);
  List<Parcela> listar();
  Parcela actualizar(Long id, Parcela p);
  void remover(Long id);
}
