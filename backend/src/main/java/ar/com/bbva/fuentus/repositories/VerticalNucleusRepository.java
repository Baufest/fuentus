package ar.com.bbva.fuentus.repositories;

import ar.com.bbva.fuentus.entities.Vertical;
import ar.com.bbva.fuentus.entities.VerticalNucleus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerticalNucleusRepository extends JpaRepository<VerticalNucleus, Long> {
    
    /**
     * Elimina todas las relaciones VerticalNucleus para una vertical específica
     */
    @Modifying
    @Query("DELETE FROM VerticalNucleus vn WHERE vn.vertical.id = :verticalId")
    void deleteByVerticalId(@Param("verticalId") Long verticalId);
    
    /**
     * Busca todas las relaciones por vertical
     */
    List<VerticalNucleus> findByVertical(Vertical vertical);
}