// model/Sensor.java
package sistemaagricola.com.projecto.models;
import jakarta.persistence.*; import lombok.*;

@Entity @Table(name="sensor", indexes = {
        @Index(name="idx_sensor_dispositivo", columnList="dispositivo_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sensor {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional=false)
    private Dispositivo dispositivo;
    @Column(nullable=false,length=40)
    private String tipo;
    @Column(nullable=false,length=16)
    private String unidade;
    @Column(length=64) private String calibVigente;
}
