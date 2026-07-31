package ar.com.bbva.fuentus.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "vertical_nucleus_services")
public class VerticalNucleus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Vertical vertical;
    @ManyToOne
    private Nucleus nucleus;
}
