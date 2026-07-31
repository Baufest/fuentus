package ar.com.bbva.fuentus.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "verticales")
public class Vertical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String refVertical;
    @OneToMany(mappedBy = "vertical", fetch = FetchType.LAZY)
    private List<VerticalNucleus> verticalNucleusList;

}
