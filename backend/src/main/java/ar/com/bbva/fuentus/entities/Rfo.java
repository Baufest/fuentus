package ar.com.bbva.fuentus.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rfo")
public class Rfo implements Serializable {

    @Id
    @Column(name = "rfo_id")
    private Long rfoId;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "estado_rfo", length = 50)
    private String estadoRfo;

    @Column(name = "fecha_puesta_produccion")
    private String fechaPuestaProduccion;

    // Relación OneToOne con Nucleus
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nucleus_id", referencedColumnName = "Id_Fullservice")
    private Nucleus nucleus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rfo rfo = (Rfo) o;
        return Objects.equals(rfoId, rfo.rfoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rfoId);
    }

    @Override
    public String toString() {
        return "Rfo{" +
               "rfoId=" + rfoId +
               ", email='" + email + '\'' +
               ", estadoRfo='" + estadoRfo + '\'' +
               ", fechaPuestaProduccion=" + fechaPuestaProduccion +
               ", nucleus=" + (nucleus != null ? nucleus.getId() : null) +
               '}';
    }
}