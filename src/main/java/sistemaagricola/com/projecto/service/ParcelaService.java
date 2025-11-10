// service/ParcelaService.java
package sistemaagricola.com.projecto.service;

import sistemaagricola.com.projecto.models.Parcela; import java.util.List;
public interface ParcelaService {
  Parcela criar(Parcela p);
  Parcela obter(Long id);
  List<Parcela> listar();
  Parcela actualizar(Long id, Parcela p);
  void remover(Long id);
}
