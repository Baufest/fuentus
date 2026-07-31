package ar.com.bbva.fuentus.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productividad")
public class Productividad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nucleus_id", referencedColumnName = "Id_Fullservice")
    private Nucleus nucleus;

    @Column(name = "features")
    private Integer features;

    @Column(name = "ftes_directos")
    private Double ftesDirectos;

    @Column(name = "ftes_indirectos")
    private Double ftesIndirectos;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Productividad that = (Productividad) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Productividad{" +
                "id=" + id +
                ", features=" + features +
                ", ftesDirectos=" + ftesDirectos +
                ", ftesIndirectos=" + ftesIndirectos +
                ", fecha=" + fecha +
                '}';
    }
}
