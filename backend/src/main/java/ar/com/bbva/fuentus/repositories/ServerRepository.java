package ar.com.bbva.fuentus.repositories;

import ar.com.bbva.fuentus.dto.ServerAppsRowDTO;
import ar.com.bbva.fuentus.dto.ServerInfoDTO;
import ar.com.bbva.fuentus.dto.ServerNameDTO;
import ar.com.bbva.fuentus.entities.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {

    @Query("SELECT new ar.com.bbva.fuentus.dto.ServerInfoDTO(" +
            "a_srv.id, " +
            "s.id, " +
            "s.name, " +
            "a.name) " +
            "FROM App a " +
            "JOIN AppServer a_srv ON a_srv.appId = a.id " +
            "JOIN Server s ON s.id = a_srv.serverId " +
            "WHERE a.uuaa LIKE CONCAT(:uuaa, '%')")
    List<ServerInfoDTO> findServersByUuaa(@Param("uuaa") String uuaa);

    @Query("SELECT new ar.com.bbva.fuentus.dto.ServerInfoDTO(" +
            "a_srv.id, " +
            "s.id, " +
            "s.name, " +
            "a.name) " +
            "FROM App a " +
            "JOIN AppServer a_srv ON a_srv.appId = a.id " +
            "JOIN Server s ON s.id = a_srv.serverId " +
            "WHERE a.name = :name")
    List<ServerInfoDTO> findServersByName(@Param("name") String name);

    @Query("SELECT new ar.com.bbva.fuentus.dto.ServerAppsRowDTO(" +
            "s.id, " +
            "s.name, " +
            "a.id, " +
            "a.name) " +
            "FROM App a " +
            "JOIN AppServer a_srv ON a_srv.appId = a.id " +
            "JOIN Server s ON s.id = a_srv.serverId " +
            "WHERE (:serverName IS NULL OR UPPER(s.name) = UPPER(:serverName)) " +
            "AND (:appNameFilter IS NULL OR UPPER(a.name) LIKE CONCAT('%', UPPER(:appNameFilter), '%')) " +
            "ORDER BY s.name ASC, a.name ASC")
    List<ServerAppsRowDTO> findServersWithApps(@Param("serverName") String serverName,
                                               @Param("appNameFilter") String appNameFilter);

    @Query("SELECT new ar.com.bbva.fuentus.dto.ServerNameDTO(s.id, s.name) " +
            "FROM Server s " +
            "ORDER BY UPPER(s.name) ASC")
    List<ServerNameDTO> findAllServerNames();
}
