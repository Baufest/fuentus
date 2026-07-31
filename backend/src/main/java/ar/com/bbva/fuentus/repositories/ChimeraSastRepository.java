package ar.com.bbva.fuentus.repositories;

import ar.com.bbva.fuentus.entities.ChimeraSast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ChimeraSastRepository extends JpaRepository<ChimeraSast, String> {

    /**
     * Buscar por project_id y name (combinación única para identificar un registro)
     */
    @Query("SELECT cs FROM ChimeraSast cs WHERE cs.projectId = :projectId AND cs.name = :name")
    Optional<ChimeraSast> findByProjectIdAndName(@Param("projectId") String projectId, @Param("name") String name);
    
    /**
     * Buscar por UUAA
     */
    @Query("SELECT cs FROM ChimeraSast cs WHERE cs.uuaa = :uuaa")
    List<ChimeraSast> findByUuaa(@Param("uuaa") String uuaa);
}