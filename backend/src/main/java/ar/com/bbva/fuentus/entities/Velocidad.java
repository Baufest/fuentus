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
@Table(name = "velocidad")
public class Velocidad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nucleus_id", referencedColumnName = "Id_Fullservice")
    private Nucleus nucleus;

    @Column(name = "lt")
    private Integer lt;

    @Column(name = "ct")
    private Integer ct;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Velocidad velocidad = (Velocidad) o;
        return Objects.equals(id, velocidad.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Velocidad{" +
                "id=" + id +
                ", lt=" + lt +
                ", ct=" + ct +
                ", date=" + date +
                '}';
    }
}
