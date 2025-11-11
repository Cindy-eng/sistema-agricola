package sistemaagricola.com.projecto.models;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name="dispositivo", indexes = {
        @Index(name="idx_dispositivo_parcela", columnList="parcela_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Dispositivo {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true, length=64)
    private String deviceKey;
    @Column(nullable=false,length=60)
    private String tipo;
    @Column(length=60)
    private String modelo;
    @Column(length=32)
    private String firmware;
    @ManyToOne(optional=false)
    private Parcela parcela;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=16)
    private Estado estado;
    public enum Estado { ACTIVO, INACTIVO }
}
