package ar.com.bbva.fuentus.repositories;

import ar.com.bbva.fuentus.entities.ChimeraSca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChimeraScaRepository extends JpaRepository<ChimeraSca, Long> {

    /**
     * Find ChimeraSca by projectId and name
     */
    @Query("SELECT c FROM ChimeraSca c WHERE c.projectId = :projectId AND c.name = :name")
    Optional<ChimeraSca> findByProjectIdAndName(@Param("projectId") String projectId, @Param("name") String name);

    /**
     * Find ChimeraSca by appId
     */
    List<ChimeraSca> findByAppId(Long appId);

    /**
     * Find ChimeraSca by uuaa
     */
    List<ChimeraSca> findByUuaa(String uuaa);

    /**
     * Find ChimeraSca by projectId
     */
    List<ChimeraSca> findByProjectId(String projectId);
}