// model/RegraAlerta.java
package sistemaagricola.com.projecto.models;
import jakarta.persistence.*; import lombok.*;

@Entity @Table(name="regra_alerta")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Getter
@Setter
public class RegraAlerta {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) private Parcela parcela; // ou Cultura, conforme uso
    @Column(nullable=false,length=160) private String expressao; // ex: "tipo==HUM_SOLO && valor<18 por 120m"
    @Column(nullable=false,length=10) private String severidade; // INFO, AVISO, CRITICO
    @Column(nullable=false,length=20) private String destino; // APP, SMS, EMAIL
    @Column(nullable=false) private Boolean activo;
}
