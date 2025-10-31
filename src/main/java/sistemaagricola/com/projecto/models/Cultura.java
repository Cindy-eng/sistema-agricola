package sistemaagricola.com.projecto.models;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="cultura")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Cultura {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false,length=120)
    private String nome;
}
