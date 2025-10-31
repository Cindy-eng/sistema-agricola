// model/Telemetria.java
package sistemaagricola.com.projecto.models;
import jakarta.persistence.*; import lombok.*;


import java.time.Instant;
@Entity @Table(name="telemetria", indexes = {
        @Index(name="idx_tel_sensor_ts", columnList="sensor_id,ts")
})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Telemetria {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) private Sensor sensor;
    @Column(nullable=false) private Instant ts;
    @Column(nullable=false) private Double valor;
    @Column(length=16) private String qualidade; // OK, SUSPEITA, INVALIDA
}
