// model/Parcela.java
package sistemaagricola.com.projecto.models;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity @Table(name="parcela", indexes = {
    @Index(name="idx_parcela_usuario", columnList="usuario_id")
})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Getter
@Setter
public class Parcela {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false,length=120)
    private String nome;
    private Double lat;
    private Double lon;
    @ManyToOne(optional=false)
    @JoinColumn(name="usuario_id", nullable=false)
    @JsonIgnore
    private User usuario;
}
