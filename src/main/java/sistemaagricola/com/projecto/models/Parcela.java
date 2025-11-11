// model/Parcela.java
package sistemaagricola.com.projecto.models;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="parcela")
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
}
