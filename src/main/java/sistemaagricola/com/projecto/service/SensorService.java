package sistemaagricola.com.projecto.service;
import sistemaagricola.com.projecto.models.Sensor;
import java.util.List;

public interface SensorService {
  Sensor criar(Sensor s);
  Sensor obter(Long id);
  List<Sensor> listarPorDispositivo(Long dispositivoId);
  void remover(Long id);
}