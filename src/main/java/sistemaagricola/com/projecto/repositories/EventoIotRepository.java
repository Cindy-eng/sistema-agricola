package sistemaagricola.com.projecto.repositories;


import sistemaagricola.com.projecto.models.EventoIot;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface EventoIotRepository extends JpaRepository<EventoIot, Long> {

    @Query("select e from EventoIot e " +
            "where e.dispositivo.id = :dispId and e.ts between :ini and :fim " +
            "order by e.ts asc")
    List<EventoIot> findByDispositivoAndPeriodo(@Param("dispId") Long dispositivoId,
                                                @Param("ini") Instant ini,
                                                @Param("fim") Instant fim);

    long deleteByDispositivoIdAndTsBefore(Long dispositivoId, Instant ts);
}
