// model/Manutencao.java
package sistemaagricola.com.projecto.models;
import jakarta.persistence.*; import lombok.*;


import java.time.Instant;
@Entity @Table(name="manutencao")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Manutencao {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) private Dispositivo dispositivo;
    @Column(nullable=false,length=32) private String tipo; // CALIBRACAO, TROCA_BATERIA
    @Column(nullable=false,length=16) private String estado; // ABERTA, FECHADA
    private Instant tsAbertura; private Instant tsFecho;
}
