package  sistemaagricola.com.projecto.repositories;

import sistemaagricola.com.projecto.models.Telemetria;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TelemetriaRepository extends JpaRepository<Telemetria, Long>, JpaSpecificationExecutor<Telemetria> {

    @Query("select t from Telemetria t " +
           "where t.sensor.id = :sensorId and t.ts between :ini and :fim " +
           "order by t.ts asc")
    List<Telemetria> findWindow(@Param("sensorId") Long sensorId,
                                @Param("ini") Instant ini,
                                @Param("fim") Instant fim);

    Optional<Telemetria> findTopBySensorIdOrderByTsDesc(Long sensorId);

    long deleteBySensorIdAndTsBefore(Long sensorId, Instant ts);


    interface TelemetriaPoint {
        Instant getTs();
        Double getValor();
    }

    @Query("select t.ts as ts, t.valor as valor from Telemetria t " +
           "where t.sensor.id = :sensorId and t.ts >= :desde order by t.ts asc")
    List<TelemetriaPoint> streamDesde(@Param("sensorId") Long sensorId,
                                      @Param("desde") Instant desde);

    
    interface TelemetriaAgg {
        Instant getBucket();
        Double getAvg();
        Double getMin();
        Double getMax();
        Long getCnt();
    }

    @Query(value = """
        select date_trunc(:granularidade, ts) as bucket,
               avg(valor) as avg,
               min(valor) as min,
               max(valor) as max,
               count(*)   as cnt
        from telemetria
        where sensor_id = :sensorId and ts between :ini and :fim
        group by 1
        order by 1 asc
        """, nativeQuery = true)
    List<TelemetriaAgg> aggregateWindow(@Param("sensorId") Long sensorId,
                                        @Param("ini") Instant ini,
                                        @Param("fim") Instant fim,
                                        @Param("granularidade") String granularidade);

    
    @Query("select t from Telemetria t where t.sensor.id = :sensorId order by t.ts desc")
    Slice<Telemetria> sliceBySensor(@Param("sensorId") Long sensorId, Pageable pageable);
}
