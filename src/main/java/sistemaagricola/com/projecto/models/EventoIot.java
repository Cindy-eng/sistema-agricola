// model/EventoIot.java
package sistemaagricola.com.projecto.models;
import jakarta.persistence.*; import lombok.*;


import java.time.Instant;
@Entity @Table(name="evento_iot", indexes = {
        @Index(name="idx_evt_dispositivo_ts", columnList="dispositivo_id,ts")
})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EventoIot {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) private Dispositivo dispositivo;
    @Column(nullable=false,length=40) private String tipo; // ONLINE, OFFLINE, PRAGA_DETECTADA
    @Column(nullable=false) private Instant ts;
    @Lob private String payload; // JSON curto
}
