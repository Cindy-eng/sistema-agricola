// service/CulturaService.java
package sistemaagricola.com.projecto.service;
import sistemaagricola.com.projecto.models.Cultura; import java.util.List;
public interface CulturaService {
  Cultura criar(Cultura c);
  Cultura obter(Long id);
  List<Cultura> listar();
  Cultura actualizar(Long id, Cultura c);
  void remover(Long id);
}
