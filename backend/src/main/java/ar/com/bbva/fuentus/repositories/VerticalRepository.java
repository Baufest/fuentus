package ar.com.bbva.fuentus.repositories;

import ar.com.bbva.fuentus.entities.Vertical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerticalRepository extends JpaRepository<Vertical, Long> {
    
    /**
     * Busca todas las verticales con sus orgN2fabrica distintos de Nucleus
     */
    @Query("SELECT DISTINCT v FROM Vertical v " +
           "LEFT JOIN FETCH v.verticalNucleusList vn " +
           "LEFT JOIN FETCH vn.nucleus n")
    List<Vertical> findAllWithNucleus();
    
    /**
     * Obtiene todos los nombres distintos de verticales
     */
    @Query("SELECT DISTINCT v.name FROM Vertical v WHERE v.name IS NOT NULL ORDER BY v.name")
    List<String> findDistinctVerticalNames();
}