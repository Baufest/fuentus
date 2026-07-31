package ar.com.bbva.fuentus.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "apps_servers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppServer {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    Long id;

    @Column(name = "app_id")
    Long appId;

    @Column(name = "server_id")
    Long serverId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppServer appServer = (AppServer) o;
        return Objects.equals(id, appServer.id) &&
               Objects.equals(appId, appServer.appId) &&
               Objects.equals(serverId, appServer.serverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appId, serverId);
    }

    @Override
    public String toString() {
        return "AppServer{" +
               "id=" + id +
               ", appId=" + appId +
               ", serverId=" + serverId +
               '}';
    }
}
