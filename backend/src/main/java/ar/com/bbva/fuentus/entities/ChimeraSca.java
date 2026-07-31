package ar.com.bbva.fuentus.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChimeraSca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "app_id")
    private Long appId;

    private String name;

    private String uuaa;

    private Long low;

    private Long medium;

    private Long high;

    private Long critical;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChimeraSca that = (ChimeraSca) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(projectId, that.projectId) &&
               Objects.equals(appId, that.appId) &&
               Objects.equals(name, that.name) &&
               Objects.equals(uuaa, that.uuaa) &&
               Objects.equals(low, that.low) &&
               Objects.equals(medium, that.medium) &&
               Objects.equals(high, that.high) &&
               Objects.equals(critical, that.critical);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectId, appId, name, uuaa, low, medium, high, critical);
    }

    @Override
    public String toString() {
        return "ChimeraSca{" +
               "id=" + id +
               ", projectId='" + projectId + '\'' +
               ", appId=" + appId +
               ", name='" + name + '\'' +
               ", uuaa='" + uuaa + '\'' +
               ", low=" + low +
               ", medium=" + medium +
               ", high=" + high +
               ", critical=" + critical +
               '}';
    }
}
