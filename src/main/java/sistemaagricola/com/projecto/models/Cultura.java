package sistemaagricola.com.projecto.models;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="cultura", indexes = {
    @Index(name="idx_cultura_usuario", columnList="usuario_id")
})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Getter
@Setter
public class Cultura {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false,length=120)
    private String nome;
    @ManyToOne(optional=false)
    @JoinColumn(name="usuario_id", nullable=false)
    private User usuario;
}
